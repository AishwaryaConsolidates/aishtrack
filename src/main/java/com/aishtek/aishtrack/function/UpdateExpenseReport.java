package com.aishtek.aishtrack.function;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.aishtek.aishtrack.beans.Customer;
import com.aishtek.aishtrack.dao.ExpenseDAO;
import com.aishtek.aishtrack.dao.ExpenseReportDAO;
import com.aishtek.aishtrack.dao.ServiceReportDAO;
import com.aishtek.aishtrack.dao.TechnicianDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class UpdateExpenseReport extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output = new ServerlessOutput();
    try (Connection connection = getConnection()) {
      try {
        Response response = getParams(serverlessInput.getBody());
        setTechnicianId(response, connection);

        Date advanceAmountDate =
            new SimpleDateFormat("dd/MM/yyyy").parse(response.advanceAmountDate);
        if (Util.isNullOrEmpty(response.id)) {
          int expenseReportId =
              createExpenseReport(connection, Util.getInt(response.serviceReportId),
                  Util.getInt(response.customerId), Util.getInt(response.technicianId),
                  new BigDecimal(response.advanceAmount),
                  new BigDecimal(response.carryForwardAmount), response.location,
                  advanceAmountDate);

          createExpenses(connection, expenseReportId, response.expenseDate, response.expenseType,
              response.expenseNote, response.expenseAmount);

          output = createSuccessOutput("" + expenseReportId);
        } else {
          updateExpenseReport(connection, Util.getInt(response.id),
              new BigDecimal(response.advanceAmount), new BigDecimal(response.carryForwardAmount),
              response.location, advanceAmountDate);

          createExpenses(connection, Util.getInt(response.id), response.expenseDate,
              response.expenseType, response.expenseNote, response.expenseAmount);

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

  public int createExpenseReport(Connection connection, int serviceReportId, int customerId,
      int technicianId, BigDecimal advanceAmount, BigDecimal carryForwardAmount, String location,
      Date advanceAmountDate)
      throws SQLException {
    if (serviceReportId > 0) {
      Customer customer = ServiceReportDAO.getCustomerFor(connection, serviceReportId);
      customerId = customer.getId();
    }
    return ExpenseReportDAO.create(connection, serviceReportId, customerId, technicianId,
        advanceAmount, carryForwardAmount, location, advanceAmountDate);
  }

  public void updateExpenseReport(Connection connection, int expenseReportId,
      BigDecimal advanceAmount, BigDecimal carryForwardAmount, String location,
      Date advanceAmountDate)
      throws SQLException {
    ExpenseReportDAO.update(connection, expenseReportId, advanceAmount, carryForwardAmount,
        location, advanceAmountDate);
  }

  public Response getParams(String jsonString) {
    return (new Gson()).fromJson(jsonString, Response.class);
  }

  public void createExpenses(Connection connection, int expenseReportId, String expenseDate,
      String expenseType, String note, String amount) throws SQLException {

    ArrayList<String> expenseDates = getStringList(expenseDate);
    ArrayList<String> expenseTypes = getStringList(expenseType);
    ArrayList<String> notes = getStringList(note);
    ArrayList<String> amounts = getStringList(amount);

    ExpenseDAO.deleteFor(connection, expenseReportId);

    for (int i = 0; i < expenseTypes.size(); i++) {
      if (!Util.isNullOrEmpty(amounts.get(i))) {
        ExpenseDAO.create(connection, expenseReportId, expenseDate(expenseDates.get(i)),
            expenseTypes.get(i), notes.get(i), new BigDecimal(amounts.get(i)));
      }
    }
  }

  private void setTechnicianId(Response response, Connection connection) throws SQLException {
    if (Util.isNullOrEmpty(response.technicianId)
        && !Util.isNullOrEmpty(response.technicianEmail)) {
      response.technicianId =
          "" + TechnicianDAO.getTechnicianIdFor(connection, response.technicianEmail);
    }
  }

  private Date expenseDate(String dateString) {
    try {
      return new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
    } catch (ParseException e) {
      return new Date();
    }
  }

  class Response {
    public String id;
    public String serviceReportId;
    public String technicianId;
    public String technicianEmail;
    public String customerId;
    public String advanceAmount;
    public String advanceAmountDate;
    public String carryForwardAmount;
    public String location;

    public String expenseDate;
    public String expenseType;
    public String expenseNote;
    public String expenseAmount;

  }
}