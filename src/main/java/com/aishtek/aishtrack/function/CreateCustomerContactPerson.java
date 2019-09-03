package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import com.aishtek.aishtrack.beans.Person;
import com.aishtek.aishtrack.dao.CustomerDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class CreateCustomerContactPerson extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;
    try (Connection connection = getConnection()) {
      try {
        Response response = getParams(serverlessInput.getBody());
        createContactPerson(connection, response.customerId, response.firstName, response.lastName,
            response.email, response.phone, response.designation);

        ArrayList<HashMap<String, String>> contactPersons =
            CustomerDAO.getContactPersons(connection, response.customerId);
        output = createSuccessOutput(contactPersons);
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

  public void createContactPerson(Connection connection, int customerId, String firstName,
      String lastName, String email, String phone, String designation)
      throws SQLException {
    Person person = new Person(firstName, lastName, designation, phone, email);
    CustomerDAO.createContactPerson(connection, customerId, person);
  }

  public Response getParams(String jsonString) {
    return (new Gson()).fromJson(jsonString, Response.class);
  }

  class Response {
    public Integer customerId;
    public String designation;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
  }
}