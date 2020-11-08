package com.aishtek.aishtrack.services;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.math.BigDecimal;
import org.junit.Test;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class AmountToWordsConverterTest extends BaseIntegrationTest {

  @Test
  public void itConvertsBigDecimalAmountToWords() throws IOException {
    BigDecimal amount = new BigDecimal("1.12");
    assertEquals(AmountToWordsConverter.convertToRupees(amount), "Rupees One and Twelve Paisa only");

    amount = new BigDecimal("12.12");
    assertEquals(AmountToWordsConverter.convertToRupees(amount), "Rupees Twelve and Twelve Paisa only");

    amount = new BigDecimal("123.12");
    assertEquals(AmountToWordsConverter.convertToRupees(amount),
        "Rupees One Hundred Twenty Three and Twelve Paisa only");

    amount = new BigDecimal("1234.12");
    assertEquals(AmountToWordsConverter.convertToRupees(amount),
        "Rupees One Thousand Two Hundred Thirty Four and Twelve Paisa only");

    amount = new BigDecimal("12345.12");
    assertEquals(AmountToWordsConverter.convertToRupees(amount),
        "Rupees Twelve Thousand Three Hundred Forty Five and Twelve Paisa only");

    amount = new BigDecimal("123456.12");
    assertEquals(AmountToWordsConverter.convertToRupees(amount),
        "Rupees One Lakh Twenty Three Thousand Four Hundred Fifty Six and Twelve Paisa only");

    amount = new BigDecimal("1234567.12");
    assertEquals(AmountToWordsConverter.convertToRupees(amount),
        "Rupees Twelve Lakh Thirty Four Thousand Five Hundred Sixty Seven and Twelve Paisa only");

    amount = new BigDecimal("12345678.12");
    assertEquals(AmountToWordsConverter.convertToRupees(amount),
        "Rupees One Crore Twenty Three Lakh Forty Five Thousand Six Hundred Seventy Eight and Twelve Paisa only");

    amount = new BigDecimal("123456789.12");
    assertEquals(AmountToWordsConverter.convertToRupees(amount),
        "Rupees Twelve Crore Thirty Four Lakh Fifty Six Thousand Seven Hundred Eighty Nine and Twelve Paisa only");

    amount = new BigDecimal("1234567890.12");
    assertEquals(AmountToWordsConverter.convertToRupees(amount),
        "Rupees One Hundred Twenty Three Crore Forty Five Lakh Sixty Seven Thousand Eight Hundred Ninety and Twelve Paisa only");
  }
}
