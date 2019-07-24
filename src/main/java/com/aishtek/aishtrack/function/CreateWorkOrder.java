package com.aishtek.aishtrack.function;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.models.Address;
import com.aishtek.aishtrack.models.Customer;
import com.aishtek.aishtrack.models.Person;
import com.aishtek.aishtrack.models.WorkOrder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * Lambda function that triggered by the API Gateway event "POST /". It reads all the query parameters as the metadata for this
 * article and stores them to a DynamoDB table. It reads the payload as the content of the article and stores it to a S3 bucket.
 */
public class CreateWorkOrder extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output = new ServerlessOutput();

    try {
      Person person = Person.createIt("first_name", "Asterix", "last_name", "Gaul", "designation",
          "Troubleshooter", "email", "asterix@aishtek.tst", "phone", "9999999999");
      Address address =
          Address.createIt("street", "Gaul Street", "city", "Gaul", "pincode", "55555");
      Customer.createIt("name", "The Cafe", "nick_name", "Cafe", "contact_person_id",
          person.getPersonId(), "address_id", address.getAddressId());
      Map<String, String> params = getParams(serverlessInput.getBody());
      WorkOrder workOrder = com.aishtek.aishtrack.use_cases.CreateWorkOrder.process(
          Integer.parseInt(params.get("customerId")), (String) params.get("type"),
          (String) params.get("notes"));

      output.setStatusCode(200);
      output.setBody(workOrder.toJson(true));
    } catch (Exception e) {
      output.setStatusCode(500);
      StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
      output.setBody(sw.toString());
    }
    return output;
  }
}