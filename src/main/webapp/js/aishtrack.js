var cognitoUser;
var userEmail;
var userRole;

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
        url : apiURLBase + "/getCustomers",
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
        url : apiURLBase + "/getTechnicians",
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
        url : apiURLBase + "/getCategories",
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
        url : apiURLBase + "/getEquipments?categoryId=" + categoryId,
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
        url : apiURLBase + "/getCustomerPersons?customerId=" + customerId,
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
        url : apiURLBase + "/getWorkOrders?workOrderId=" + workOrderId,
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
	$("#deleteWorkOrderId").val(workOrder.id);
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
			ClientId : clientId,
			AppWebDomain : appWebDomain,
			UserPoolId : userPoolId,
			TokenScopesArray : ['phone', 'email', 'profile','openid', 'aws.cognito.signin.user.admin'], // e.g.['phone', 'email', 'profile','openid', 'aws.cognito.signin.user.admin'],
			RedirectUriSignIn : htmlURLBase + '/index.html',
			RedirectUriSignOut : htmlURLBase + '/login.html',
			AdvancedSecurityDataCollectionFlag : 'false', // e.g. true
         	//IdentityProvider : '<TODO: add identity provider you want to specify>', // e.g. 'Facebook', 
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
			window.location.href = htmlURLBase + "/login.html";
		}
	};
	// The default response_type is "token", uncomment the next line will make it be "code".
	// auth.useCodeGrantFlow();
	return auth;
}

async function verifySignin(onlyAllowForRole=null) {
		var data = { 
            UserPoolId : userPoolId,
			ClientId : clientId
	    };
	    var userPool = new AmazonCognitoIdentity.CognitoUserPool(data);
	    cognitoUser = userPool.getCurrentUser();
	    
	    if (cognitoUser != null) {
	        cognitoUser.getSession(function(err, session) {
	            if (err) {
	            	alert("Your session has expired, please log in again.");
	    			window.location.href = htmlURLBase + "/login.html";
	            }
	            console.log('session validity: ' + session.isValid());
	        });
	        
	        await retrieveattribute();
	        if(onlyAllowForRole != null) {
            	if(!userRole.includes(onlyAllowForRole)) {
          		  alert("You do not have access to this page. Contact Administrator.");
          		  window.location.href = htmlURLBase + "/index.html";
                }
            }
            return [userRole, userEmail];
	    } else {
	    	alert("You need to log into access the application");
			window.location.href = htmlURLBase + "/login.html";
	    }
}

function retrieveattribute() {
    return new Promise(function(res) {
        cognitoUser.getUserAttributes(function(err, result) {
            if (err) {
                alert(err);
                return;
            }
            for (i = 0; i < result.length; i++) {
                if (result[i].getName() == 'email') {
                    userEmail = result[i].getValue();
                    console.log(userEmail);
                }
                if (result[i].getName() == 'profile') {
                    userRole = result[i].getValue();
                    console.log(userRole);
                }
            }
            res([userRole, userEmail]);
        });
    })
}

function getSignedIdUser() {
	var data = { 
        UserPoolId : userPoolId,
		ClientId : clientId
    };
    var userPool = new AmazonCognitoIdentity.CognitoUserPool(data);
    cognitoUser = userPool.getCurrentUser();
    
    if (cognitoUser != null) {
        	cognitoUser.getUserAttributes(function(err, result) {
                if (err) {
                	console.log(err);
                    return "error";
                }
                for (i = 0; i < result.length; i++) {
                	if(result[i].getName() == 'email') {
                	  return result[i].getValue();
                	}
                }
            });
    } else {
    	return "error";
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

function checkLogin() {
	if(localStorage.getItem("userEmail")){
		return true;
	} else {
		return false;
	}
}

function checkForAdministrator() {
	if(getUserRole() === "administrator"){
		return true;
	} else {
		window.location.href = htmlURLBase + "/technicianIndex.html";
	}
}

function checkForTechinician() {
	if(getUserRole() === "technician"){
		return true;
	} else {
		window.location.href = htmlURLBase + "/index.html";
	}
}

function getUserRole() {
	if(localStorage.getItem("userRole")){
		return localStorage.getItem("userRole");
	} else {
		window.location.href = htmlURLBase + "/login.html";
	}
}

function getUserEmail() {
	if(localStorage.getItem("userEmail")){
		return localStorage.getItem("userEmail");
	} else {
		window.location.href = htmlURLBase + "/login.html";
	}
}

function signout() {
	localStorage.removeItem("userRole");
	localStorage.removeItem("userEmail");
	window.location.href = htmlURLBase + "/login.html";
}

function navigateTo(page) {
	window.location.href = htmlURLBase + page;
}