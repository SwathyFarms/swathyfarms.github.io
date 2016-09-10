//Modules' Stuff
var jsonfile = require('jsonfile');
//QRInventory Stuff
var curSet = new Array();
var curFile = "";
var curState = "push";
//Express Stuff
var express = require('express');
var app = express();

app.get('/', function (req, res) {
	res.send('Hola, Working !!!');
});

app.all('/dataSet/getContents',function(req,res){
	res.send(JSON.stringify(curSet));
});
app.all('/dataSet/clearContents',function(req,res){
	curSet = [];
	res.send(JSON.stringify(curSet));
});

app.all('/dataSet/newValue/:value1',function(req,res){
	var reqVal1 = req.params["value1"];
	if(curState == "push")
		if(!curSet.includes(reqVal1))
			curSet.push(reqVal1);
	if(curState == "pop")
		if(curSet.includes(reqVal1))
			curSet.pop(reqVal1);
	res.send(JSON.stringify(curSet));
});

app.all('/dataSet/setMode/:value1',function(req,res){
	var reqVal1 = req.params["value1"];
	if(reqVal1 == "push")
		curState = "push";
	if(reqVal1 == "pop")
		curState = "pop";
	res.send(JSON.stringify(curState));
});
app.all('/dataSet/getMode',function(req,res){
	res.send(JSON.stringify(curState));
});

app.all('/dataSet/setName/:value1',function(req,res){
	var reqVal1 = req.params["value1"];
	curFile = reqVal1;
	res.send(JSON.stringify(curFile));
});
app.all('/dataSet/getName',function(req,res){
	var reqVal1 = req.params["value1"];
	res.send(JSON.stringify(curFile));
});
app.all('/dataSet/save',function(req,res){
	jsonfile.writeFileSync("dataSet/" + curFile, curSet);
	res.send(JSON.stringify(curSet));
});
app.all('/dataSet/load',function(req,res){
	curSet = jsonfile.readFileSync(curFile);
	res.send(JSON.stringify("dataSet/" + curSet));	
});

app.listen(3000, function () {
  console.log('Example app listening on port 3000!');
});