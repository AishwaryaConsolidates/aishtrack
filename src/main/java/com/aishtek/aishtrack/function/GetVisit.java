package com.aishtek.aishtrack.function;

import java.sql.Connection;
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

public class GetVisit extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;

    try (Connection connection = getConnection()) {
      try {

        int visitId = Util.getInt(serverlessInput.getQueryStringParameters().get("visitId"));

        // get visit
        Visit visit = VisitDAO.findById(connection, visitId);
        visit.setVisitDateString(Util.formatDate(visit.getVisitDate()));
        visit.setRecommendedSpareParts(RecommendedSparePartDAO.findByVisitId(connection, visitId));
        visit.setReplacedSpareParts(ReplacedSparePartDAO.findByVisitId(connection, visitId));

        output = createSuccessOutput();
        output.setBody(new Gson().toJson(visit));
      } catch (Exception e) {
        connection.rollback();
        output = createFailureOutput(e);
      }
    } catch (Exception e) {
      output = createFailureOutput(e);
    }
    return output;
  }
}