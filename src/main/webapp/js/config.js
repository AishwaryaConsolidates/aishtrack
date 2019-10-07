//staging
var htmlURLBase = "https://aishtek.s3.amazonaws.com/aishtrack";
var apiURLBase = "https://4ompw72vyb.execute-api.ap-south-1.amazonaws.com/Prod";
var userPoolId = 'ap-south-1_5Zq6Ucr1p';
var clientId = 'u0tjn44hhggiavte32291paft';
var appWebDomain = 'aishtracktest.auth.ap-south-1.amazoncognito.com';

//production
//var htmlURLBase = "https://aishtrack.s3.amazonaws.com";
//var apiURLBase = "https://1s5jtgvpjg.execute-api.ap-south-1.amazonaws.com/Prod";
//var userPoolId = 'ap-south-1_kfadN9z8w';
//var clientId = 'k3sr2j779uu03nmnejbl8tk8r';
//var appWebDomain = 'aishtrack.auth.ap-south-1.amazoncognito.com';

var installationDetails = {
		"Gas": ["Gas Pressure", "Gas Regulator", "Gas Pressure Gauge", "Gas Pipe Leak Test", "Burner Flame Color"],
		"Steam": ["Type Of Steam", "Steam Pressure", "Steam Regulator", "Steam Pressure Gauge", "Steam Pipe Leak Test", "Condensation Water Drain"],
		"Refrigeration": ["Type Of Ref Gas", "Compressor Details", "Chiller/Freezer", "Condensing Fan", "Dryer-Filter", "Cooling Fan"],
		"Plumbing": ["Type Of Water", "Hot/Cold Water", "Water Pressure", "Water Regulator", "Water Pressure Gauge", "Water Pipe Leak Test", "Water Drain Connection"],
		"Electrical": ["Voltage" ,"AMPS", "Hz", "Phase", "Heating Element", "Motor", "Programing Circuit Board"]
};
