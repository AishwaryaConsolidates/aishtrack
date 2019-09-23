var installationDetails = {
		"Gas": ["Gas Pressure", "Gas Regulator", "Gas Pressure Gauge", "Gas Pipe Leak Test", "Burner Flame Color"],
		"Steam": ["Type Of Steam", "Steam Pressure", "Steam Regulator", "Steam Pressure Gauge", "Steam Pipe Leak Test", "Condensation Water Drain"],
		"Refrigeration": ["Type Of Ref Gas", "Compressor Details", "Chiller/Freezer", "Condensing Fan", "Dryer-Filter", "Cooling Fan"],
		"Plumbing": ["Type Of Water", "Hot/Cold Water", "Water Pressure", "Water Regulator", "Water Pressure Gauge", "Water Pipe Leak Test", "Water Drain Connection"],
		"Electrical": ["Voltage" ,"AMPS", "Hz", "Phase", "Heating Element", "Motor", "Programing Circuit Board"]
};
//var installationHeadings = ["Gas", "Steam", "Refrigeration", "Plumbing", "Electrical"]

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
    return $.ajax({
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
    return $.ajax({
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
    return $.ajax({
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

function getEquipments(categoryId, nextFunction=null) {
    return $.ajax({
        url : "https://4ompw72vyb.execute-api.ap-south-1.amazonaws.com/Prod/getEquipments?categoryId=" + categoryId,
        type : "GET",
        dataType: "json",
        crossDomain: true,
		success: function(data) {
			createDropdown(JSON.stringify(data), "equipmentId");
			functionToRun = window[nextFunction];
	    	if (typeof functionToRun === "function") functionToRun();
		},
		error: function(data) {
			log("Error fetching equipments");
		}
	});
}

function getCustomerPersons(customerId) {
    return $.ajax({
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
			$.when(getEquipments(workOrder.categoryId), getCustomerPersons(workOrder.customerId)).done(function() {
				fillWorkOrderForm(workOrder);
        	});
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

//Initialize a cognito auth object.
function initCognitoSDK() {
	var authData = {
			ClientId : '33i51jtcqrisallfq09ddfoc1e', // Your client id here
			AppWebDomain : 'aishtek.auth.ap-south-1.amazoncognito.com',
			TokenScopesArray : ['email', 'openid', 'phone'], // e.g.['phone', 'email', 'profile','openid', 'aws.cognito.signin.user.admin'],
			RedirectUriSignIn : 'https://aishtek.s3.amazonaws.com/aishtrack/index.html',
			RedirectUriSignOut : 'https://aishtek.s3.amazonaws.com/aishtrack/products.html',
/*         	IdentityProvider : '<TODO: add identity provider you want to specify>', // e.g. 'Facebook', */
			UserPoolId : 'ap-south-1_jiWBJIz70', // Your user pool id here
			AdvancedSecurityDataCollectionFlag : 'false', // e.g. true
		    //Storage: 'LocalStorage' // OPTIONAL e.g. new CookieStorage(), to use the specified storage provided
		};
	var auth = new AmazonCognitoIdentity.CognitoAuth(authData);
	// You can also set state parameter 
	// auth.setState(<state parameter>);  
	auth.userhandler = {
		onSuccess: function(result) {
			console.log("Sign in success");
			return true;
		},
		onFailure: function(err) {
			alert("You need to log into access the application");
			window.location.href = "https://aishtek.s3.amazonaws.com/aishtrack/login.html";
		}
	};
	// The default response_type is "token", uncomment the next line will make it be "code".
	// auth.useCodeGrantFlow();
	return auth;
}

function verifySignin() {
		var data = { 
			UserPoolId : 'ap-south-1_jiWBJIz70',
	        ClientId : '33i51jtcqrisallfq09ddfoc1e'
	    };
	    var userPool = new AmazonCognitoIdentity.CognitoUserPool(data);
	    var cognitoUser = userPool.getCurrentUser();
	    
	    if (cognitoUser != null) {
	        cognitoUser.getSession(function(err, session) {
	            if (err) {
	            	alert("Your session has expired, please log in again.");
	    			window.location.href = "https://aishtek.s3.amazonaws.com/aishtrack/login.html";
	            }
	            console.log('session validity: ' + session.isValid());
	        });
	    } else {
	    	alert("You need to log into access the application");
			window.location.href = "https://aishtek.s3.amazonaws.com/aishtrack/login.html";
	    }
}

function showJSON(obj) {
    var result = "";
	for (var p in obj) {
	    if( obj.hasOwnProperty(p) ) {
	    	if (obj[p]) {
	            result += p + ": " + obj[p] + "<br/>";
	        }
	    }
	  }              
    return result;
}