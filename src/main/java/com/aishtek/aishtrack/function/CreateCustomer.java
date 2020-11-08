package com.aishtek.aishtrack.function;

import java.sql.Connection;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.services.CustomerService;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class CreateCustomer extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;
    try (Connection connection = getConnection()) {
      try {
        Response response = getParams(serverlessInput.getBody());

        int customerId =
            customerService().createCustomer(connection, response.name, response.nickName,
            response.gstIN, response.street, response.area, response.city, response.state, response.pincode,
            response.designation, response.firstName, response.lastName, response.email, response.phone,
                response.mobile, response.alternatePhone);

        output = createSuccessOutput("" + customerId);
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

  private Response getParams(String jsonString) {
    return (new Gson()).fromJson(jsonString, Response.class);
  }

  private CustomerService customerService() {
    return new CustomerService();
  }
  class Response {
    public String name;
    private String nickName;
    public String gstIN;

    private String street;
    private String area;
    private String city;
    private String state;
    private String pincode;

    public String designation;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String mobile;
    private String alternatePhone;
  }
}