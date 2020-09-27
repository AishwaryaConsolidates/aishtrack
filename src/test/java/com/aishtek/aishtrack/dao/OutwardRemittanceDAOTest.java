package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.junit.Test;
import com.aishtek.aishtrack.beans.OutwardRemittance;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class OutwardRemittanceDAOTest extends BaseIntegrationTest {

  @Test
  public void createFindByIdTest() throws Exception {
    try (Connection connection = getConnection()) {
      int fromBankAccountId = createBankAccount(connection);
      int fromBankAddressId = createTestAddress(connection);
      int fromAddressId = createTestAddress(connection);
      int supplierId = createSupplier(connection, "domestic");
      int supplierAddressId = createTestAddress(connection);
      int supplierBankAddressId = createTestAddress(connection);
      int supplierBankAccountId = createBankAccount(connection);
      BigDecimal amount = new BigDecimal(200);
      String goods = "goods";
      String goodsClassificationNo = "goodsClassificationNo";
      String countryOfOrigin = "countryOfOrigin";
      String currency = "currency";
      String purpose = "purpose";
      String otherInfo = "otherInfo";
      String signaturePlace = "signaturePlace";
      Date signatureDate = new Date();

      OutwardRemittance outwardRemittance = new OutwardRemittance(0, fromBankAccountId, fromBankAddressId, fromAddressId,
              supplierId, supplierAddressId, supplierBankAccountId, supplierBankAddressId, amount,
              goods, goodsClassificationNo, countryOfOrigin, currency, purpose, otherInfo,
              signaturePlace, signatureDate, 0);

      int outwardRemittanceId = OutwardRemittanceDAO.create(connection, outwardRemittance);
      
      OutwardRemittance createdOutwardRemittance =
          OutwardRemittanceDAO.findById(connection, outwardRemittanceId);


      assertEquals(createdOutwardRemittance.getId(), outwardRemittanceId);
      assertEquals(createdOutwardRemittance.getAmount().toString(), "200.00");
      assertEquals(createdOutwardRemittance.getFromBankAccountId(), fromBankAccountId);
      assertEquals(createdOutwardRemittance.getFromBankAddressId(), fromBankAddressId);
      assertEquals(createdOutwardRemittance.getFromAddressId(), fromAddressId);
      assertEquals(createdOutwardRemittance.getSupplierId(), supplierId);
      assertEquals(createdOutwardRemittance.getSupplierAddressId(), supplierAddressId);
      assertEquals(createdOutwardRemittance.getSupplierBankAccountId(), supplierBankAccountId);
      assertEquals(createdOutwardRemittance.getSupplierBankAddressId(), supplierBankAddressId);
      assertEquals(createdOutwardRemittance.getAmount().toString(), "200.00");

      assertEquals(createdOutwardRemittance.getGoods(), goods);
      assertEquals(createdOutwardRemittance.getGoodsClassificationNo(), goodsClassificationNo);
      assertEquals(createdOutwardRemittance.getCountryOfOrigin(), countryOfOrigin);
      assertEquals(createdOutwardRemittance.getCurrency(), currency);
      assertEquals(createdOutwardRemittance.getPurpose(), purpose);
      assertEquals(createdOutwardRemittance.getOtherInfo(), otherInfo);
      assertEquals(createdOutwardRemittance.getSignaturePlace(), signaturePlace);
      assertEquals(formatter.format(createdOutwardRemittance.getSignatureDate()),
          formatter.format(signatureDate));

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void updateTest() throws Exception {
    try (Connection connection = getConnection()) {

      int outwardRemittanceId = createOutwardRemittance(connection, 0);

      OutwardRemittance createdOutwardRemittance =
          OutwardRemittanceDAO.findById(connection, outwardRemittanceId);

      int fromBankAccountId = createBankAccount(connection);
      int fromBankAddressId = createTestAddress(connection);
      int fromAddressId = createTestAddress(connection);
      int supplierId = createSupplier(connection, "domestic");
      int supplierAddressId = createTestAddress(connection);
      int supplierBankAddressId = createTestAddress(connection);
      int supplierBankAccountId = createBankAccount(connection);
      BigDecimal amount2 = new BigDecimal(500);
      String goods = "goods2";
      String goodsClassificationNo = "goodsClassificationNo2";
      String countryOfOrigin = "countryOfOrigin2";
      String currency = "currency2";
      String purpose = "purpose2";
      String otherInfo = "otherInfo2";
      String signaturePlace = "signaturePlace2";

      createdOutwardRemittance.setAmount(amount2);
      createdOutwardRemittance.setFromBankAccountId(fromBankAccountId);
      createdOutwardRemittance.setFromAddressId(fromAddressId);
      createdOutwardRemittance.setFromBankAddressId(fromBankAddressId);
      createdOutwardRemittance.setSupplierId(supplierId);
      createdOutwardRemittance.setSupplierAddressId(supplierAddressId);
      createdOutwardRemittance.setSupplierBankAccountId(supplierBankAccountId);
      createdOutwardRemittance.setSupplierBankAddressId(supplierBankAddressId);
      createdOutwardRemittance.setGoods(goods);
      createdOutwardRemittance.setGoodsClassificationNo(goodsClassificationNo);
      createdOutwardRemittance.setCountryOfOrigin(countryOfOrigin);
      createdOutwardRemittance.setCurrency(currency);
      createdOutwardRemittance.setPurpose(purpose);
      createdOutwardRemittance.setOtherInfo(otherInfo);
      createdOutwardRemittance.setSignaturePlace(signaturePlace);
      createdOutwardRemittance.setSignatureDate(yesterday());
      OutwardRemittanceDAO.update(connection, createdOutwardRemittance);

      OutwardRemittance udatedOutwardRemittance =
          OutwardRemittanceDAO.findById(connection, outwardRemittanceId);

      assertEquals(udatedOutwardRemittance.getId(), outwardRemittanceId);
      assertEquals(udatedOutwardRemittance.getAmount().toString(), "500.00");
      assertEquals(udatedOutwardRemittance.getFromBankAccountId(), fromBankAccountId);
      assertEquals(udatedOutwardRemittance.getFromBankAddressId(), fromBankAddressId);
      assertEquals(udatedOutwardRemittance.getFromAddressId(), fromAddressId);
      assertEquals(udatedOutwardRemittance.getSupplierId(), supplierId);
      assertEquals(udatedOutwardRemittance.getSupplierAddressId(), supplierAddressId);
      assertEquals(udatedOutwardRemittance.getSupplierBankAccountId(), supplierBankAccountId);
      assertEquals(udatedOutwardRemittance.getSupplierBankAddressId(), supplierBankAddressId);

      assertEquals(udatedOutwardRemittance.getGoods(), goods);
      assertEquals(udatedOutwardRemittance.getGoodsClassificationNo(), goodsClassificationNo);
      assertEquals(udatedOutwardRemittance.getCountryOfOrigin(), countryOfOrigin);
      assertEquals(udatedOutwardRemittance.getCurrency(), currency);
      assertEquals(udatedOutwardRemittance.getPurpose(), purpose);
      assertEquals(udatedOutwardRemittance.getOtherInfo(), otherInfo);
      assertEquals(udatedOutwardRemittance.getSignaturePlace(), signaturePlace);
      assertEquals(formatter.format(udatedOutwardRemittance.getSignatureDate()),
          formatter.format(yesterday()));

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void findForPrintTest() throws Exception {
    try (Connection connection = getConnection()) {
      int fromBankAccountId = createBankAccount(connection);
      int fromBankAddressId = createTestAddress(connection);
      int fromAddressId = createTestAddress(connection);
      int supplierId = createSupplier(connection, "domestic");
      int supplierAddressId = createTestAddress(connection);
      int supplierBankAddressId = createTestAddress(connection);
      int supplierBankAccountId = createBankAccount(connection);
      BigDecimal amount = new BigDecimal(200);
      String goods = "goods";
      String goodsClassificationNo = "goodsClassificationNo";
      String countryOfOrigin = "countryOfOrigin";
      String currency = "currency";
      String purpose = "purpose";
      String otherInfo = "otherInfo";
      String signaturePlace = "signaturePlace";
      Date signatureDate = new Date();

      OutwardRemittance outwardRemittance = new OutwardRemittance(0, fromBankAccountId,
          fromBankAddressId, fromAddressId, supplierId, supplierAddressId, supplierBankAccountId,
          supplierBankAddressId, amount, goods, goodsClassificationNo, countryOfOrigin, currency,
          purpose, otherInfo, signaturePlace, signatureDate, 0);

      int outwardRemittanceId = OutwardRemittanceDAO.create(connection, outwardRemittance);

      HashMap<String, String> createdOutwardRemittance =
          OutwardRemittanceDAO.findForPrint(connection, outwardRemittanceId);


      assertEquals(createdOutwardRemittance.get("id"), "" + outwardRemittanceId);
      assertEquals(createdOutwardRemittance.get("amount"), "200.00");
      assertEquals(createdOutwardRemittance.get("goods"), goods);
      assertEquals(createdOutwardRemittance.get("goodsClassificationNo"), goodsClassificationNo);
      assertEquals(createdOutwardRemittance.get("countryOfOrigin"), countryOfOrigin);
      assertEquals(createdOutwardRemittance.get("currency"), currency);
      assertEquals(createdOutwardRemittance.get("purpose"), purpose);
      assertEquals(createdOutwardRemittance.get("otherInfo"), otherInfo);
      assertEquals(createdOutwardRemittance.get("signaturePlace"), signaturePlace);
      assertEquals(createdOutwardRemittance.get("signatureDate"), formatter.format(signatureDate));

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void searchTest() throws Exception {
    try (Connection connection = getConnection()) {
      int supplierId = createSupplier(connection, "domestic");

      int outwardRemittanceId = createOutwardRemittance(connection, supplierId);

      ArrayList<HashMap<String, String>> results =
          OutwardRemittanceDAO.searchFor(connection, supplierId, yesterday(), tomorrow());

      assertEquals(results.size(), 1);
      assertEquals(results.get(0).get("id"), "" + outwardRemittanceId);

      // other supplier
      results = OutwardRemittanceDAO.searchFor(connection, supplierId + 1, yesterday(), tomorrow());
      assertEquals(results.size(), 0);

      // start date
      results = OutwardRemittanceDAO.searchFor(connection, supplierId, tomorrow(), tomorrow());
      assertEquals(results.size(), 0);

      // other supplier
      results = OutwardRemittanceDAO.searchFor(connection, supplierId, yesterday(), yesterday());
      assertEquals(results.size(), 0);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }
}
