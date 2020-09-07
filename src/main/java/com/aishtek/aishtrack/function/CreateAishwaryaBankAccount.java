package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.util.Date;
import com.aishtek.aishtrack.beans.Address;
import com.aishtek.aishtrack.dao.AddressDAO;
import com.aishtek.aishtrack.dao.BankAccountDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class CreateAishwaryaBankAccount extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;
    try (Connection connection = getConnection()) {
      try {
        Date startDate = dateFor("31/08/2020");
        Date endDate = dateFor("31/08/2030");
        // First Bank Account
        Address address1 = new Address("Nr.429/31, 4th Block, Ram Krishna Building",
            "30th Cross, Jayanagar West", "Bangalore", "KA", "560011");
        
        int addressId1 = AddressDAO.create(connection, address1);

        int bankAccountId1 = BankAccountDAO.create(connection, "Union Bank of India",
            "Jayanagar Branch", "UBIN0537942", "379405110000104", "", "", addressId1);
            
        BankAccountDAO.createAishwaryaBankAccount(connection, bankAccountId1, startDate, endDate);

        // Second Bank Account
        Address address2 = new Address("Nr.429/31, 4th Block, Ram Krishna Building",
            "30th Cross, Jayanagar West", "Bangalore", "KA", "560011");

        int addressId2 = AddressDAO.create(connection, address2);

        int bankAccountId2 = BankAccountDAO.create(connection, "Union Bank of India",
            "Jayanagar Branch", "UBIN0537942", "353101010036208", "", "", addressId2);

        BankAccountDAO.createAishwaryaBankAccount(connection, bankAccountId2, startDate, endDate);

        // Third Bank Account
        Address address3 = new Address("P.B.NO.1127, 15TH CROSS, SOUTH END CIRCLE",
            "JAYANAGAR 2ND BLOCK", "Bangalore", "KA", "560011");

        int addressId3 = AddressDAO.create(connection, address3);

        int bankAccountId3 = BankAccountDAO.create(connection, "State Bank of India",
            "Jayanagar Branch", "SBIN0003286", "31637949546", "", "", addressId3);

        BankAccountDAO.createAishwaryaBankAccount(connection, bankAccountId3, startDate, endDate);
        output = createSuccessOutput("3 bank accounts created");
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

  class Response {

    private String name = "";
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
