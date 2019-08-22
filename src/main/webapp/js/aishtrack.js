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

function getCustomersFor(purpose, nextFunction=null) {
    $.ajax({
        url : "https://4ompw72vyb.execute-api.ap-south-1.amazonaws.com/Prod/getCustomers",
        type : "GET",
        dataType: "json",
        crossDomain: true,
		success: function(data) {
			if(purpose=="list") {
			    listCustomers(JSON.stringify(data));
		    } else if (purpose=="dropdown") {
		    	createDropdown(JSON.stringify(data), "customerId");
		    }
	    	functionToRun = window[nextFunction];
	    	if (typeof functionToRun === "function") functionToRun();
		},
		error: function(data) {
			log("Error fetching customers");
		}
	});
}

function listCustomers(json) {
	html = ""
	jsonArray = JSON.parse(json);
	$.each(jsonArray, function(index, jsonObject){
		html+= "<li class=\"tm-list-group-item\">"+jsonObject.name+"</li>"	
	});
	$("#customersList").html(html);
}

function createDropdown(json, fieldName) {
	dropdown = "#" + fieldName;
	var $customerSelect = $(dropdown);
	jsonArray = JSON.parse(json);
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