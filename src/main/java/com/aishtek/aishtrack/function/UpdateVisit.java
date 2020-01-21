package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.aishtek.aishtrack.beans.SparePart;
import com.aishtek.aishtrack.beans.Visit;
import com.aishtek.aishtrack.dao.RecommendedSparePartDAO;
import com.aishtek.aishtrack.dao.ReplacedSparePartDAO;
import com.aishtek.aishtrack.dao.VisitDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class UpdateVisit extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output = new ServerlessOutput();
    try (Connection connection = getConnection()) {
      try {
        Response response = getParams(serverlessInput.getBody());
        if (Util.isNullOrEmpty(response.id)) {
          Date visitDate = new SimpleDateFormat("dd/MM/yyyy").parse(response.visitDate);
          int visitId = createVisit(connection, response.serviceReportId, visitDate,
              response.complaint, response.findings, response.workDone, response.customerRemarks);

          createRecommendedSpareParts(connection, visitId, response.recommendedSparePartNumber,
              response.recommendedSparePartQuantity, response.recommendedSparePartDescription);

          createReplacedSpareParts(connection, visitId, response.replacedSparePartNumber,
              response.replacedSparePartQuantity, response.replacedSparePartDescription);

          output = createSuccessOutput("" + visitId);
        } else {
          Date visitDate = new SimpleDateFormat("dd/MM/yyyy").parse(response.visitDate);
          updateVisit(connection, Util.getInt(response.id), response.serviceReportId, visitDate,
              response.complaint, response.findings, response.workDone, response.customerRemarks);

          createRecommendedSpareParts(connection, Util.getInt(response.id),
              response.recommendedSparePartNumber, response.recommendedSparePartQuantity,
              response.recommendedSparePartDescription);

          createReplacedSpareParts(connection, Util.getInt(response.id),
              response.replacedSparePartNumber, response.replacedSparePartQuantity,
              response.replacedSparePartDescription);

          output = createSuccessOutput("");
        }
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

  public int createVisit(Connection connection, int serviceReportId, Date visitDate,
      String complaint, String findings, String workDone, String customerRemarks)
      throws SQLException {
    Visit visit =
        new Visit(0, serviceReportId, visitDate, complaint, findings, workDone, customerRemarks);

    return VisitDAO.create(connection, visit);
  }

  public void updateVisit(Connection connection, int id, int serviceReportId, Date visitDate,
      String complaint, String findings, String workDone, String customerRemarks)
      throws SQLException {
    Visit visit =
        new Visit(id, serviceReportId, visitDate, complaint, findings, workDone, customerRemarks);

    VisitDAO.update(connection, visit);
  }

  public Response getParams(String jsonString) {
    return (new Gson()).fromJson(jsonString, Response.class);
  }

  public void createRecommendedSpareParts(Connection connection, int visitId,
      String recommendedSparePartNumber, String recommendedSparePartQuantity,
      String recommendedSparePartDescription) throws SQLException {
    ArrayList<String> recommendedSparePartNumbers = getStringList(recommendedSparePartNumber);
    ArrayList<String> recommendedSparePartQuantitys = getStringList(recommendedSparePartQuantity);
    ArrayList<String> recommendedSparePartDescriptions =
        getStringList(recommendedSparePartDescription);

    RecommendedSparePartDAO.deleteFor(connection, visitId);
    String description;
    String quantity;
    for (int i = 0; i < recommendedSparePartNumbers.size(); i++) {
      if (!Util.isNullOrEmpty(recommendedSparePartNumbers.get(i))) {
        if (recommendedSparePartDescriptions.size() <= i) {
          description = recommendedSparePartDescriptions.get(i);
        } else {
          description = "";
        }
        if (recommendedSparePartQuantitys.size() <= i) {
          quantity = recommendedSparePartQuantitys.get(i);
        } else {
          quantity = "0";
        }
        RecommendedSparePartDAO.create(connection,
            new SparePart(visitId, recommendedSparePartNumbers.get(i), description,
                Util.getInt(quantity)));
      }
    }
  }

  public void createReplacedSpareParts(Connection connection, int visitId,
      String replacedSparePartNumber, String replacedSparePartQuantity,
      String replacedSparePartDescription) throws SQLException {
    ArrayList<String> replacedSparePartNumbers = getStringList(replacedSparePartNumber);
    ArrayList<String> replacedSparePartQuantitys = getStringList(replacedSparePartQuantity);
    ArrayList<String> replacedSparePartDescriptions = getStringList(replacedSparePartDescription);

    ReplacedSparePartDAO.deleteFor(connection, visitId);
    String description;
    String quantity;
    for (int i = 0; i < replacedSparePartNumbers.size(); i++) {
      if (!Util.isNullOrEmpty(replacedSparePartNumbers.get(i))) {

        if (replacedSparePartDescriptions.size() <= i) {
          description = replacedSparePartDescriptions.get(i);
        } else {
          description = "";
        }
        if (replacedSparePartQuantitys.size() <= i) {
          quantity = replacedSparePartQuantitys.get(i);
        } else {
          quantity = "0";
        }
        RecommendedSparePartDAO.create(connection, new SparePart(visitId,
            replacedSparePartNumbers.get(i), description, Util.getInt(quantity)));
      }
    }
  }

  class Response {
    public String id;
    public Integer serviceReportId;
    public String visitDate;
    public String complaint;
    public String findings;
    public String workDone;
    public String customerRemarks;

    public String recommendedSparePartDescription;
    public String recommendedSparePartNumber;
    public String recommendedSparePartQuantity;
    public String replacedSparePartDescription;
    public String replacedSparePartNumber;
    public String replacedSparePartQuantity;

  }
}