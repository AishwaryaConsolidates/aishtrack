package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.sql.SQLException;
import com.aishtek.aishtrack.beans.Customer;
import com.aishtek.aishtrack.beans.WorkOrder;
import com.aishtek.aishtrack.dao.CustomerDAO;
import com.aishtek.aishtrack.dao.WorkOrderDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.services.CustomerService;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class CreateWorkOrder extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;
    try (Connection connection = getConnection()) {
      try {
        Response response = getParams(serverlessInput.getBody());
        addContactPerson(connection, response);

        if (Util.isNullOrEmpty(response.id)) {
          Customer customer = CustomerDAO.findById(connection, response.customerId);
          int workOrderId =
              createWorkOrder(connection, response.customerId, response.contactPersonId,
                  response.type, response.notes, response.categoryId, response.equipmentId,
                  response.brand, response.model, response.serialNumber, response.partNumber,
                  customer.getAddressId());
            output = createSuccessOutput("" + workOrderId);
        } else {
          updateWorkOrder(connection, Util.getInt(response.id), response.customerId,
              response.type, response.notes, response.categoryId, response.equipmentId,
              response.brand, response.model, response.serialNumber, response.partNumber,
              response.contactPersonId);
          output = createSuccessOutput("");
        }
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

  public int createWorkOrder(Connection connection, int customerId, int contactPersonId,
      String type, String notes,
      int categoryId, int equipmentId, String brand, String model, String serialNumber,
      String partNumber, int addressId)
      throws SQLException {
    WorkOrder workOrder =
        new WorkOrder(customerId, contactPersonId, type, notes, categoryId, equipmentId, brand,
            model, serialNumber, partNumber, addressId);
    return WorkOrderDAO.create(connection, workOrder);
  }

  public void updateWorkOrder(Connection connection, int id, int customerId, String type,
      String notes, int categoryId, int equipmentId, String brand, String model,
      String serialNumber, String partNumber, int contactPersonId) throws SQLException {
    WorkOrder workOrder = WorkOrderDAO.findById(connection, id);
    workOrder.setCustomerId(customerId);
    workOrder.setType(type);
    workOrder.setNotes(notes);
    workOrder.setCategoryId(categoryId);
    workOrder.setEquipmentId(equipmentId);
    workOrder.setBrand(brand);
    workOrder.setModel(model);
    workOrder.setSerialNumber(serialNumber);
    workOrder.setPartNumber(partNumber);
    workOrder.setContactPersonId(contactPersonId);
    WorkOrderDAO.update(connection, workOrder);
  }

  public Response getParams(String jsonString) {
    return (new Gson()).fromJson(jsonString, Response.class);
  }

  public void addContactPerson(Connection connection, Response response) throws SQLException {
    if (!Util.isNullOrEmpty(response.firstName)) {
      CustomerService customerService = new CustomerService();
      response.contactPersonId = customerService.createContactPerson(connection,
          response.customerId, response.firstName, response.lastName, response.email,
          response.phone, response.designation, response.mobile, response.alternatePhone);
    }
  }

  class Response {
    public String id;
    public Integer customerId;
    public Integer contactPersonId;
    public String notes;
    public String type;
    public Integer categoryId;
    public Integer equipmentId;
    public String brand;
    public String model;
    public String serialNumber;
    public String partNumber;

    public String designation;
    public String firstName;
    public String lastName;
    public String email;
    public String phone;
    public String mobile;
    public String alternatePhone;
  }
}