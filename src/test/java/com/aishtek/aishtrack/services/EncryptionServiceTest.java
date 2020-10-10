package com.aishtek.aishtrack.services;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import org.junit.Test;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class EncryptionServiceTest extends BaseIntegrationTest {

  @Test
  public void itEncryptsAndDecryptsAString() throws IOException {
    String testString = "Sometimes You Gotta Run Before You Can Walk.";

    EncryptionService es = new EncryptionService();
    try {
      String encryptedString = es.encrypt(testString, 123456789);
      String decryptedString = es.decrypt(encryptedString, 123456789);
      assertEquals(testString, decryptedString);
    } catch (Exception e) {
      e.printStackTrace();
      assertEquals(true, false);
    }
  }

  @Test
  public void testAnEncryptedBankAccountNumber() throws IOException {
    String testString = "V818OhNmeXJ0C0CCPmPfWg==";

    EncryptionService es = new EncryptionService();
    try {
      String decryptedString = es.decrypt(testString, 31);
      assertEquals("379405110000104", decryptedString);
    } catch (Exception e) {
      e.printStackTrace();
      assertEquals(true, false);
    }
  }
}
