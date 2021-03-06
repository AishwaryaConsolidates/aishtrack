package com.aishtek.aishtrack.function;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import com.aishtek.aishtrack.beans.InlandPolicyDeclaration;
import com.aishtek.aishtrack.dao.InlandPolicyDeclarationDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class UpdateInlandPolicyDeclaration extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;
    try (Connection connection = getConnection()) {
      try {
        Response response = getParams(serverlessInput.getBody());
        InlandPolicyDeclaration inlandPolicyDeclaration =
            saveInlandDeclaration(connection, response);

        output = createSuccessOutput("" + inlandPolicyDeclaration.getId());
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

  private InlandPolicyDeclaration saveInlandDeclaration(Connection connection, Response response)
      throws ParseException, SQLException {

    int id = Util.isNullOrEmpty(response.id) ? 0 : Util.getInt(response.id);

    InlandPolicyDeclaration inlandPolicyDeclaration =
        new InlandPolicyDeclaration(id, response.inlandPolicyId, response.customerId,
            response.amount, response.description, response.quantity, response.toLocation,
        response.fromLocation, response.invoiceNumber, dateFor(response.invoiceDate),
        response.receiptNumber, dateFor(response.receiptDate), 0);

    InlandPolicyDeclarationDAO.save(connection, inlandPolicyDeclaration);
    return inlandPolicyDeclaration;
  }

  private Response getParams(String jsonString) {
    return (new Gson()).fromJson(jsonString, Response.class);
  }

  class Response {
    public String id;
    public Integer inlandPolicyId;
    public Integer customerId;
    public BigDecimal amount;
    public String description;
    public Integer quantity;
    public String toLocation;
    public String fromLocation;
    public String invoiceNumber;
    public String invoiceDate;
    public String receiptNumber;
    public String receiptDate;
  }
}