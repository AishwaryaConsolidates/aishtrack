package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.sql.SQLException;
import com.aishtek.aishtrack.beans.Address;
import com.aishtek.aishtrack.beans.Customer;
import com.aishtek.aishtrack.beans.Person;
import com.aishtek.aishtrack.dao.AddressDAO;
import com.aishtek.aishtrack.dao.CustomerDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
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

        Address address = new Address(response.street, response.area, response.city, response.state,
            response.pincode);
        int addressId = AddressDAO.create(connection, address);

        Customer customer =
            new Customer(0, response.name, response.nickName, addressId, 0, response.gstIN);
        int customerId = CustomerDAO.create(connection, customer);

        createContactPerson(connection, customerId, response.firstName, response.lastName,
            response.email, response.phone, response.designation, response.mobile,
            response.alternatePhone);

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

  public int createContactPerson(Connection connection, int customerId, String firstName,
      String lastName, String email, String phone, String designation, String mobile,
      String alternatePhone)
      throws SQLException {
    Person person =
        new Person(firstName, lastName, designation, phone, email, mobile, alternatePhone);
    return CustomerDAO.createContactPerson(connection, customerId, person);
  }

  private Response getParams(String jsonString) {
    return (new Gson()).fromJson(jsonString, Response.class);
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