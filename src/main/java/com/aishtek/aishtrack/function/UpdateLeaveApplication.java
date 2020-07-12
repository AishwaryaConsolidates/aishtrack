package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import com.aishtek.aishtrack.beans.LeaveApplication;
import com.aishtek.aishtrack.dao.LeaveApplicationDAO;
import com.aishtek.aishtrack.dao.TechnicianDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class UpdateLeaveApplication extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;
    try (Connection connection = getConnection()) {
      try {
        Response response = getParams(serverlessInput.getBody());
        setTechnicianId(response, connection);
        LeaveApplication leaveApplication = saveLeaveApplication(connection, response);

        output = createSuccessOutput("" + leaveApplication.getId());
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

  private LeaveApplication saveLeaveApplication(Connection connection, Response response)
      throws ParseException, SQLException {

    int id = Util.isNullOrEmpty(response.id) ? 0 : Util.getInt(response.id);

    LeaveApplication leaveApplication = new LeaveApplication(id, Util.getInt(response.technicianId),
        dateFor(response.fromDate), dateFor(response.toDate), response.reason, response.status,
        response.signature, dateFor(response.signatureDate), 0);

    LeaveApplicationDAO.save(connection, leaveApplication);
    return leaveApplication;
  }

  private Response getParams(String jsonString) {
    return (new Gson()).fromJson(jsonString, Response.class);
  }

  private void setTechnicianId(Response response, Connection connection) throws SQLException {
    if (Util.isNullOrEmpty(response.technicianId)
        && !Util.isNullOrEmpty(response.technicianEmail)) {
      response.technicianId =
          "" + TechnicianDAO.getTechnicianIdFor(connection, response.technicianEmail);
    }
  }
  class Response {
    public String id;
    public String technicianId;
    public String technicianEmail;
    public String signature;
    public String signatureDate;
    public String fromDate;
    public String toDate;
    public String reason;
    public String status;
  }
}