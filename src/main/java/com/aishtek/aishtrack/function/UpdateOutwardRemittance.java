package com.aishtek.aishtrack.function;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.aishtek.aishtrack.beans.Address;
import com.aishtek.aishtrack.beans.NameId;
import com.aishtek.aishtrack.beans.OutwardRemittance;
import com.aishtek.aishtrack.dao.BankAccountDAO;
import com.aishtek.aishtrack.dao.OutwardRemittanceDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.services.AishwaryaDetailsService;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class UpdateOutwardRemittance extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;
    try (Connection connection = getConnection()) {
      try {
        Response response = getParams(serverlessInput.getBody());
        OutwardRemittance outwardRemittance = saveOutwardRemittance(connection, response);

        output = createSuccessOutput("" + outwardRemittance.getId());
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

  private OutwardRemittance saveOutwardRemittance(Connection connection, Response response)
      throws ParseException, SQLException {

    int id = Util.isNullOrEmpty(response.id) ? 0 : Util.getInt(response.id);

    OutwardRemittance outwardRemittance = new OutwardRemittance(id, response.fromBankAccountId,
        getAishwaryaBankAddressId(connection, response.fromBankAccountId),
        getAishwaryaAddressId(connection), response.supplierId, response.supplierAddressId,
        response.supplierBankAccountId, response.supplierBankAddressId, response.amount,
        response.goods, response.goodsClassificationNo, response.countryOfOrigin, response.currency,
        response.purpose, response.otherInfo, response.signaturePlace,
        getSignatureDate(response.signatureDate), 0);

    OutwardRemittanceDAO.save(connection, outwardRemittance);
    return outwardRemittance;
  }

  private int getAishwaryaAddressId(Connection connection) throws SQLException {
    Address aishwaryaAddress = AishwaryaDetailsService.getCurrentAishwaryaAddress(connection);
    return aishwaryaAddress.getId();
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
    public Integer supplierAddressId;
    public Integer supplierBankAccountId;
    public Integer supplierBankAddressId;
    public BigDecimal amount;
    public String goods;
    public String goodsClassificationNo;
    public String countryOfOrigin;
    public String currency;
    public String purpose;
    public String otherInfo;
    public String signaturePlace;
    public String signatureDate;
  }
}