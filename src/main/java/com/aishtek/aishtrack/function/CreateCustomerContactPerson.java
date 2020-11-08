package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.services.CustomerService;
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

        CustomerService customerService = customerService();

        customerService.createContactPerson(connection, response.customerId, response.firstName,
            response.lastName, response.email, response.phone, response.designation,
            response.mobile, response.alternatePhone);

        ArrayList<HashMap<String, String>> contactPersons =
            customerService.getContactPersons(connection, response.customerId);

        output = createSuccessOutputForArrayHash(contactPersons);
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

  private CustomerService customerService() {
    return new CustomerService();
  }

  private Response getParams(String jsonString) {
    return (new Gson()).fromJson(jsonString, Response.class);
  }

  private ServerlessOutput createSuccessOutputForArrayHash(
      ArrayList<HashMap<String, String>> result) {
    ServerlessOutput output = createSuccessOutput();
    output.setBody(new Gson().toJson(result));
    return output;
  }

  class Response {
    public Integer customerId;
    public String designation;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String mobile;
    private String alternatePhone;
  }
}