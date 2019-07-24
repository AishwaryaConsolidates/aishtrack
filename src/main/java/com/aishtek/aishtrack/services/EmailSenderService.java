package com.aishtek.aishtrack.services;


import java.io.IOException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest; 

public class EmailSenderService {

  private static final String FROM_EMAIL_ADDRESS = "adarshadarsh@gmail.com";

  public static void sendEmail(String[] to, String subject, String htmlBody, String textBody)
      throws IOException {

    try {
      AmazonSimpleEmailService client =
          AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
      SendEmailRequest request = new SendEmailRequest()
          .withDestination(new Destination().withToAddresses(to))
          .withMessage(new Message()
              .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBody))
                  .withText(new Content().withCharset("UTF-8").withData(textBody)))
              .withSubject(new Content().withCharset("UTF-8").withData(subject)))
          .withSource(FROM_EMAIL_ADDRESS);
      client.sendEmail(request);
    } catch (Exception ex) {
      System.out.println("The email was not sent. Error message: " + ex.getMessage());
    }
  }
}