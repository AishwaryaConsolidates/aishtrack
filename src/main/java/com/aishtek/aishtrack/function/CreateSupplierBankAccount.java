package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import com.aishtek.aishtrack.beans.Address;
import com.aishtek.aishtrack.beans.NameId;
import com.aishtek.aishtrack.beans.Person;
import com.aishtek.aishtrack.dao.AddressDAO;
import com.aishtek.aishtrack.dao.BankAccountDAO;
import com.aishtek.aishtrack.dao.CustomerDAO;
import com.aishtek.aishtrack.dao.SupplierDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class CreateSupplierBankAccount extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;
    try (Connection connection = getConnection()) {
      try {
        Response response = getParams(serverlessInput.getBody());
        
        Address address = new Address(response.street, response.area, response.city, response.state, response.pincode);
        int addressId = AddressDAO.create(connection, address);

        int bankAccountId = BankAccountDAO.create(connection, response.name, response.branch,
            response.swiftCode, response.accountNumber, response.iban, response.otherDetails,
            addressId);
            
        SupplierDAO.createSupplierBankAccount(connection, response.supplierId, bankAccountId);

        ArrayList<NameId> bankAccounts =
            BankAccountDAO.forSupplier(connection, response.supplierId);
        output = createSuccessOutput();
        output.setBody(new Gson().toJson(bankAccounts));
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

  private ServerlessOutput createSuccessOutputForArrayHash(
      ArrayList<HashMap<String, String>> result) {
    ServerlessOutput output = createSuccessOutput();
    output.setBody(new Gson().toJson(result));
    return output;
  }

  class Response {
    public Integer supplierId;

    private String name;
    private String branch;
    private String swiftCode;
    private String accountNumber;
    private String iban;
    private String otherDetails;

    private String street;
    private String area;
    private String city;
    private String state;
    private String pincode;
  }
}