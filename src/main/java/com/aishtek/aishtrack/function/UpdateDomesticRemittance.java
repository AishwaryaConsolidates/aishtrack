package com.aishtek.aishtrack.function;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.aishtek.aishtrack.beans.DomesticRemittance;
import com.aishtek.aishtrack.beans.NameId;
import com.aishtek.aishtrack.dao.BankAccountDAO;
import com.aishtek.aishtrack.dao.DomesticRemittanceDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class UpdateDomesticRemittance extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;
    try (Connection connection = getConnection()) {
      try {
        Response response = getParams(serverlessInput.getBody());
        DomesticRemittance domesticRemittance = saveDomesticRemittance(connection, response);

        output = createSuccessOutput("" + domesticRemittance.getId());
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

  private DomesticRemittance saveDomesticRemittance(Connection connection, Response response)
      throws ParseException, SQLException {

    int id = Util.isNullOrEmpty(response.id) ? 0 : Util.getInt(response.id);

    DomesticRemittance domesticRemittance = new DomesticRemittance(id, response.fromBankAccountId,
        getAishwaryaBankAddressId(connection, response.fromBankAccountId),
        response.supplierId, response.supplierBankAccountId, response.amount, response.purpose,
        getSignatureDate(response.signatureDate), 0, response.chequeNumber,
        getSignatureDate(response.chequeDate));

    DomesticRemittanceDAO.save(connection, domesticRemittance);
    return domesticRemittance;
  }

  private int getAishwaryaBankAddressId(Connection connection, int bankAccountId)
      throws SQLException {
    ArrayList<NameId> fromBankAddress = BankAccountDAO.getAddressesFor(connection, bankAccountId);
    return fromBankAddress.get(0).getId();
  }

  private Date getSignatureDate(String signatureDate) throws ParseException {
    if (!Util.isNullOrEmpty(signatureDate)) {
      return new SimpleDateFormat("dd/MM/yyyy").parse(signatureDate);
    } else {
      return null;
    }
  }

  private Response getParams(String jsonString) {
    return (new Gson()).fromJson(jsonString, Response.class);
  }

  class Response {
    public String id;
    public Integer supplierId;
    public Integer fromBankAccountId;
    public Integer supplierBankAccountId;
    public BigDecimal amount;
    public String purpose;
    public String signatureDate;
    public String chequeNumber;
    public String chequeDate;
  }
}