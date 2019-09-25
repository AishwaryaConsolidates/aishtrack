var htmlURLBase = "https://aishtek.s3.amazonaws.com/aishtrack"; //https://aishtrack.s3.amazonaws.com
var apiURLBase = "https://4ompw72vyb.execute-api.ap-south-1.amazonaws.com/Prod"; //https://1s5jtgvpjg.execute-api.ap-south-1.amazonaws.com/Prod

var installationDetails = {
		"Gas": ["Gas Pressure", "Gas Regulator", "Gas Pressure Gauge", "Gas Pipe Leak Test", "Burner Flame Color"],
		"Steam": ["Type Of Steam", "Steam Pressure", "Steam Regulator", "Steam Pressure Gauge", "Steam Pipe Leak Test", "Condensation Water Drain"],
		"Refrigeration": ["Type Of Ref Gas", "Compressor Details", "Chiller/Freezer", "Condensing Fan", "Dryer-Filter", "Cooling Fan"],
		"Plumbing": ["Type Of Water", "Hot/Cold Water", "Water Pressure", "Water Regulator", "Water Pressure Gauge", "Water Pipe Leak Test", "Water Drain Connection"],
		"Electrical": ["Voltage" ,"AMPS", "Hz", "Phase", "Heating Element", "Motor", "Programing Circuit Board"]
};
