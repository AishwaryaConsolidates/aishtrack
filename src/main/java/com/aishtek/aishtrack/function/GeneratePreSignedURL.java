package com.aishtek.aishtrack.function;

import java.net.URL;
import java.sql.Connection;
import java.util.Date;
import com.aishtek.aishtrack.beans.File;
import com.aishtek.aishtrack.beans.VisitFile;
import com.aishtek.aishtrack.dao.ExpenseDAO;
import com.aishtek.aishtrack.dao.FileDAO;
import com.aishtek.aishtrack.dao.VisitFileDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.google.gson.Gson;

public class GeneratePreSignedURL extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;
    try (Connection connection = getConnection()) {
      try {
        Response response = getParams(serverlessInput.getBody());

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            .withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).build();

        // Set the pre-signed URL to expire after 10 minute.
        Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 10 * 60;
        expiration.setTime(expTimeMillis);

        // create file record
        String s3FileName =
            response.id + "_" + (new Date()).getTime() + "_" + response.uploadFileName;
        int fileId =
            FileDAO.create(connection,
                new File(response.fileName, getFileBaseUrl(response.type) + s3FileName));

        if (response.type.compareToIgnoreCase("visit") == 0) {
          VisitFileDAO.create(connection, new VisitFile(response.id, fileId));
        } else if (response.type.compareToIgnoreCase("expense") == 0) {
          ExpenseDAO.updateFile(connection, response.id, fileId);
        }

        // Generate the pre-signed URL.
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
            new GeneratePresignedUrlRequest(getBucket(response.type), s3FileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);
        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

        output = createSuccessOutput();
        output.setBody(new Gson().toJson(url.toString()));
        connection.commit();
      } catch (Exception e) {
        connection.rollback();
        output = createFailureOutput(e);
      }
    } catch (Exception e) {
      output = createFailureOutput(e);
    }
    return output;
  }

  public Response getParams(String jsonString) {
    return (new Gson()).fromJson(jsonString, Response.class);
  }

  public String getBucket(String type) {
    if (type != null && type.compareTo("expense") == 0) {
      return expensesBucketName;
    } else {
      return bucketName;
    }
  }

  public String getFileBaseUrl(String type) {
    if (type != null && type.compareTo("expense") == 0) {
      return expensesFileBaseURL;
    } else {
      return fileBaseURL;
    }
  }

  class Response {
    public String uploadFileName;
    public String fileName;
    public Integer id;
    public String type;
  }
}