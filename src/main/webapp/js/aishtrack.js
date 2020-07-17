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

function loadTopNav() {
	var elmnt = document.getElementById("top_nav");
	if(isTechnician()) {
		loadHTML(elmnt, htmlURLBase + "/partials/top_nav_technician.html");
	}

	if(isManager()) {
		loadHTML(elmnt, htmlURLBase + "/partials/top_nav_service_manager.html");
	}

	if(isAdministrator()) {
		loadHTML(elmnt, htmlURLBase + "/partials/top_nav_administrator.html")
	}
}

function loadFooter() {
	var elmnt = document.getElementById("footer");
	loadHTML(elmnt, htmlURLBase + "/partials/footer.html");
}

function loadHTML(elmnt, file) {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4) {
			if (this.status == 200) {
				elmnt.innerHTML = this.responseText;
			}
			if (this.status == 404) {
				elmnt.innerHTML = "Page not found.";
			}
		}
	}
	xhttp.open("GET", file, true);
	xhttp.send();
	return;
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

function getTechnicians(firstBlank = false) {
    return $.ajax({
        url : apiURLBase + "/getTechnicians",
        type : "GET",
        dataType: "json",
        crossDomain: true,
		success: function(data) {
			createDropdown(JSON.stringify(data), "technicianIds", firstBlank);
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
	if(isAdministrator()){
		return true;
	} else {
		window.location.href = htmlURLBase + "/technicianIndex.html";
	}
}

function isAdministrator() {
	if(getUserRole() === "administrator"){
		return true;
	} else {
		return false;
	}
}

function checkForTechinician() {
	if(isTechnician()){
		return true;
	} else {
		window.location.href = htmlURLBase + "/index.html";
	}
}

function isTechnician() {
	if(getUserRole() === "technician"){
		return true;
	} else {
		return false;
	}
}

function isManager() {
	if(getUserRole() === "manager"){
		return true;
	} else {
		return false;
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

function getToday() {
	var today = new Date();
	var dd = today.getDate();
    var mm = today.getMonth() + 1;
    var yyyy = today.getFullYear();

    if (dd < 10) {
        dd = '0' + dd;
    }
    if (mm < 10) {
        mm = '0' + mm;
    }
    return dd + '/' + mm + '/' + yyyy;
}

function getOverseasSuppliers() {
    return $.ajax({
        url : apiURLBase + "/getSuppliers?type=overseas",
        type : "GET",
        dataType: "json",
        crossDomain: true,
		success: function(data) {
			createDropdown(JSON.stringify(data), "supplierId", true);
		},
		error: function(data) {
			log("Error fetching suppliers");
		}
	});
}

function getDomesticSuppliers() {
    return $.ajax({
        url : apiURLBase + "/getSuppliers?type=domestic",
        type : "GET",
        dataType: "json",
        crossDomain: true,
		success: function(data) {
			createDropdown(JSON.stringify(data), "supplierId", true);
		},
		error: function(data) {
			log("Error fetching suppliers");
		}
	});
}

function getSupplierBankAccounts(supplierId) {
    return $.ajax({
        url : apiURLBase + "/getBankAccounts?supplierId="+supplierId,
        type : "GET",
        dataType: "json",
        crossDomain: true,
		success: function(data) {
			createDropdown(JSON.stringify(data), "supplierBankAccountId", false);
			getSupplierBankAccountAddresses($("#supplierBankAccountId option:first").val());
		},
		error: function(data) {
			log("Error fetching supplier bank accounts");
		}
	});
}

function getAishwaryaBankAccounts() {
    return $.ajax({
        url : apiURLBase + "/getBankAccounts?forAishwarya=true",
        type : "GET",
        dataType: "json",
        crossDomain: true,
		success: function(data) {
			createDropdown(JSON.stringify(data), "fromBankAccountId", false);
		},
		error: function(data) {
			log("Error fetching aishwarya bank accounts");
		}
	});
}

function getSupplierAddresses(supplierId) {
    return $.ajax({
        url : apiURLBase + "/getSupplierAddresses?supplierId="+supplierId,
        type : "GET",
        dataType: "json",
        crossDomain: true,
		success: function(data) {
			createDropdown(JSON.stringify(data), "supplierAddressId", false);
		},
		error: function(data) {
			log("Error fetching supplier addresses");
		}
	});
}

function getSupplierBankAccountAddresses(bankAccountId) {
    return $.ajax({
        url : apiURLBase + "/getBankAccountAddresses?bankAccountId=" + bankAccountId,
        type : "GET",
        dataType: "json",
        crossDomain: true,
		success: function(data) {
			createDropdown(JSON.stringify(data), "supplierBankAddressId", false);
		},
		error: function(data) {
			log("Error fetching bank account addresses");
		}
	});
}

function getMarinePolicies() {
    return $.ajax({
        url : apiURLBase + "/getMarinePolicies",
        type : "GET",
        dataType: "json",
        crossDomain: true,
		success: function(data) {
			createDropdown(JSON.stringify(data), "marinePolicyId", false);
		},
		error: function(data) {
			log("Error fetching marine policies");
		}
	});
}

function getInlandPolicies() {
    return $.ajax({
        url : apiURLBase + "/getInlandPolicies",
        type : "GET",
        dataType: "json",
        crossDomain: true,
		success: function(data) {
			createDropdown(JSON.stringify(data), "inlandPolicyId", false);
		},
		error: function(data) {
			log("Error fetching inland policies");
		}
	});
}
/* consolidate the upload file code fromm updateVisit and updateExpenseReport
 * it will need async and wait implementation punting on that for now

async function getPresignedUrl(id, uploadFileName, type, fileName) {
    var getUrlParams = {};
    getUrlParams["uploadFileName"] = uploadFileName;
    getUrlParams["type"] = type;
    getUrlParams["fileName"] = fileName;
    getUrlParams["id"] = id;
    $.ajax({
        url : apiURLBase + "/generatePreSignedURL",
        type : "POST",
        data: JSON.stringify(getUrlParams),
        dataType: "json",
        contentType: 'application/json',
        crossDomain: true,
		success: function(data) {
	        return data;
		},
		error: function(data) {
			alert("Error getting presigned url");
			return "";
		}
	});
}

async function uploadFileToS3(fileUploadURL, theFormFile) {
	$.ajax({
        url : fileUploadURL,
        type : "PUT",
        contentType: theFormFile.type,
        processData: false,
        data: theFormFile,
        crossDomain: true,
		success: function(data) {
			return true;
		},
		error: function(data) {
			alert("Error upload failed");
			return false;
		}
	});
}
*/