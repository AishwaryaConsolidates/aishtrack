package com.aishtek.aishtrack.services;

import java.io.IOException;
import org.junit.Ignore;
import org.junit.Test;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class EmailSenderServiceTest extends BaseIntegrationTest {

  @Test
  @Ignore
  public void itSendsAnActualEmail() throws IOException {
    String[] to = {"madadarsh@hotmail.com", "simpleboy007@yahoo.com"};
    EmailSenderService.sendEmail(to, "Test email from amazon aws ses", "This is a test email",
        "This is a test email");
    }
}
