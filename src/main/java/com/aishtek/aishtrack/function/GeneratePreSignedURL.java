package com.aishtek.aishtrack.function;

import java.net.URL;
import java.sql.Connection;
import java.util.Date;
import com.aishtek.aishtrack.beans.File;
import com.aishtek.aishtrack.beans.VisitFile;
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

  private static final String bucketName = "aishtrackfiles"; // aishtrackuploadedfiles
  private static final String fileBaseURL = "https://aishtrackfiles.s3.ap-south-1.amazonaws.com/"; // http://aishtrackuploadedfiles.s3-website.ap-south-1.amazonaws.com

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
            response.visitId + "_" + (new Date()).getTime() + "_" + response.uploadFileName;
        int fileId =
            FileDAO.create(connection, new File(response.fileName, fileBaseURL + s3FileName));

        VisitFileDAO.create(connection, new VisitFile(response.visitId, fileId));

        // Generate the pre-signed URL.
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
            new GeneratePresignedUrlRequest(bucketName, s3FileName).withMethod(HttpMethod.PUT)
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

  class Response {
    public String uploadFileName;
    public String fileName;
    public Integer visitId;
  }
}