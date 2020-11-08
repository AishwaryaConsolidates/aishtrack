package com.aishtek.aishtrack.beans;

import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Test;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class DeclarationsReportTest extends BaseIntegrationTest {

  @Test
  public void calculateAmountsTest() throws SQLException {
    DeclarationsReport declarationsReport = new DeclarationsReport();

    ArrayList<HashMap<String, String>> declarations = new ArrayList<HashMap<String, String>>();

    HashMap<String, String> declaration1 = new HashMap<String, String>();
    declaration1.put("amountDeclared", "100.00");
    declaration1.put("dutyAmount", "10.00");
    declarations.add(declaration1);
    HashMap<String, String> declaration2 = new HashMap<String, String>();
    declaration2.put("amountDeclared", "130.00");
    declaration2.put("dutyAmount", "20.00");
    declarations.add(declaration2);

    declarationsReport.setDeclarations(declarations);
    declarationsReport.setAmountInsured(new BigDecimal(1000));
    declarationsReport.setAmountUsed(new BigDecimal(200));
    declarationsReport.calculateAmounts();

    assertEquals(declarationsReport.getAmountDeclared().toBigInteger(),
        new BigDecimal(260).toBigInteger());
    assertEquals(declarationsReport.getOpeningBalance().toBigInteger(),
        new BigDecimal(800).toBigInteger());
    assertEquals(declarationsReport.getAmountBalance().toBigInteger(),
        new BigDecimal(540).toBigInteger());
  }
}
