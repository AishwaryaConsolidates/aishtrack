function includeHTML() {
	var z, i, elmnt, file, xhttp;
	/* Loop through a collection of all HTML elements: */
	z = document.getElementsByTagName("*");
	for (i = 0; i < z.length; i++) {
		elmnt = z[i];
		/* search for elements with a certain atrribute: */
		file = elmnt.getAttribute("w3-include-html");
		if (file) {
			/* Make an HTTP request using the attribute value as the file name: */
			xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
				if (this.readyState == 4) {
					if (this.status == 200) {
						elmnt.innerHTML = this.responseText;
					}
					if (this.status == 404) {
						elmnt.innerHTML = "Page not found.";
					}
					/* Remove the attribute, and call this function once more: */
					elmnt.removeAttribute("w3-include-html");
					includeHTML();
				}
			}
			xhttp.open("GET", file, true);
			xhttp.send();
			/* Exit the function: */
			return;
		}
	}
}

function getCustomers(nextFunction=null) {
    $.ajax({
        url : "https://4ompw72vyb.execute-api.ap-south-1.amazonaws.com/Prod/getCustomers",
        type : "GET",
        dataType: "json",
        crossDomain: true,
		success: function(data) {
			createDropdown(JSON.stringify(data), "customerId", true);
	    	functionToRun = window[nextFunction];
	    	if (typeof functionToRun === "function") functionToRun();
		},
		error: function(data) {
			log("Error fetching customers");
		}
	});
}

function createDropdown(json, fieldName, firstBlank = false) {
	dropdown = "#" + fieldName;
	var $customerSelect = $(dropdown);
	$customerSelect.empty();
	jsonArray = JSON.parse(json);
	if(firstBlank) {
		var $option = $("<option/>", {
			value : 0,
			text : "Select....."
		});
		$customerSelect.append($option);
	}
	$.each(jsonArray, function(index, jsonObject) {
		var $option = $("<option/>", {
			value : jsonObject.id,
			text : jsonObject.name
		});
		$customerSelect.append($option);
	});
}

function getJSONFormData(form){
    var unindexed_array = form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
    	if(indexed_array[n['name']]) {
    		indexed_array[n['name']] = indexed_array[n['name']] + "!@#" + n['value'];
    	} else {
          indexed_array[n['name']] = n['value'];
    	}
    });

    return JSON.stringify(indexed_array);
}

function getTechnicians() {
    $.ajax({
        url : "https://4ompw72vyb.execute-api.ap-south-1.amazonaws.com/Prod/getTechnicians",
        type : "GET",
        dataType: "json",
        crossDomain: true,
		success: function(data) {
			createDropdown(JSON.stringify(data), "technicianIds");
		},
		error: function(data) {
			log("Error fetching technicians");
		}
	});
}

function getCategories() {
    $.ajax({
        url : "https://4ompw72vyb.execute-api.ap-south-1.amazonaws.com/Prod/getCategories",
        type : "GET",
        dataType: "json",
        crossDomain: true,
		success: function(data) {
			createDropdown(JSON.stringify(data), "categoryId", true);
		},
		error: function(data) {
			log("Error fetching categories");
		}
	});
}

function getEquipments(categoryId) {
    $.ajax({
        url : "https://4ompw72vyb.execute-api.ap-south-1.amazonaws.com/Prod/getEquipments?categoryId=" + categoryId,
        type : "GET",
        dataType: "json",
        crossDomain: true,
		success: function(data) {
			createDropdown(JSON.stringify(data), "equipmentId");
		},
		error: function(data) {
			log("Error fetching equipments");
		}
	});
}

function getCustomerPersons(customerId) {
    $.ajax({
        url : "https://4ompw72vyb.execute-api.ap-south-1.amazonaws.com/Prod/getCustomerPersons?customerId=" + customerId,
        type : "GET",
        dataType: "json",
        crossDomain: true,
		success: function(data) {
			createDropdown(JSON.stringify(data), "contactPersonId");
		},
		error: function(data) {
			log("Error fetching customer persons");
		}
	});
}

function prefillWorkOrderData(workOrderId) {
    $.ajax({
        url : "https://4ompw72vyb.execute-api.ap-south-1.amazonaws.com/Prod/getWorkOrders?workOrderId=" + workOrderId,
        type : "GET",
        dataType: "json",
        crossDomain: true,
		success: function(data) {
			workOrder = JSON.parse(JSON.stringify(data));
			getEquipments(workOrder.categoryId);
			getCustomerPersons(workOrder.customerId);
			fillWorkOrderForm(workOrder);
		},
		error: function(data) {
			log("Error fetching customers");
		}
	});
  }

function fillWorkOrderForm(workOrder) {
	$("#workOrderId").val(workOrder.id);
	$("#customerId").val(workOrder.customerId);
	$("#type").val(workOrder.type);
	$("#notes").val(workOrder.notes);
	$("#contactPersonId").val(workOrder.contactPersonId);
	$("#categoryId").val(workOrder.categoryId);
	$("#equipmentId").val(workOrder.equipmentId);
	$("#brand").val(workOrder.brand);
	$("#model").val(workOrder.model);
	$("#serialNumber").val(workOrder.serialNumber);
	$("#partNumber").val(workOrder.partNumber);
	
}