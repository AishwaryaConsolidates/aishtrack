package com.aishtek.aishtrack.utils;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Test;

public class UtilTest {

  @Test
  public void isNullOrEmptyTest() throws IOException {
    assertEquals(Util.isNullOrEmpty(""), true);
    String str = null;
    assertEquals(Util.isNullOrEmpty(str), true);
    String[] strs = {};
    assertEquals(Util.isNullOrEmpty(strs), true);
    String[] strsNull = null;
    assertEquals(Util.isNullOrEmpty(strsNull), true);

    assertEquals(Util.isNullOrEmpty("qwerty"), false);
    String[] stars = {"qwerty", "asdfgh", "zxcv"};
    assertEquals(Util.isNullOrEmpty(stars), false);
  }

  @Test
  public void getIntTest() throws IOException {
    assertEquals(Util.getInt("0"), 0);
    assertEquals(Util.getInt("12345"), 12345);
    assertEquals(Util.getInt("some string"), 0);
  }

  @Test
  public void formatDateTest() throws IOException, ParseException {
    String sDate1 = "15/08/1947";
    Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
    assertEquals(Util.formatDate(date1), "15/08/1947");
  }
}
