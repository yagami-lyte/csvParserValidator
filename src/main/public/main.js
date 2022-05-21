var payload=[]
var result = []
var fields = []
var fieldCount = 0
var configName = []


function checkIfConfigNameAlreadyExit(file_name){
    if(configName.indexOf(file_name) !== -1)return 1
    return 0
}

function validateConfigName(){
    var file_name = document.getElementById("fileName").value
    var getCheckBox = document.getElementById("configCheckBox").checked
    if((file_name == "" ||  checkIfConfigNameAlreadyExit(file_name)) && getCheckBox){
        document.getElementById("config_name_validation").style.display = 'block';
        return 0
    }
    return 1
}

function csvReader() {
    localStorage.setItem("csv" , document.getElementById("csv_id").value.split("\\")[2])
    var csv = document.getElementById("csv_id").files[0];
    const reader = new FileReader();
    reader.onload = async function (event) {
        csv = event.target.result
        console.log(document.getElementById("csv_id").value.split("\\")[2])
        var lines = csv.toString().split("\n");
        var headers = lines[0].split(",");
        showColFields(headers);
        fields.push(headers)
        for (var i = 1; i < lines.length-1; i++) {
            var obj = {};
            var currentLine = lines[i].split(",");
            for (var j = 0; j < headers.length; j++) {
                obj[headers[j]] = currentLine[j].replaceAll('"', '');
            }
            result.push(obj);
        }
    };
    reader.readAsText(csv);
}


async function getConfigFilesName(){
    var resp = await fetch('get-config-files', {
        method: 'GET',
    })

    if (resp.status === 200) {
        var jsonData = await resp.json();
        console.log(jsonData)
        setConfigInDropDown(jsonData)
    }
}

function setConfigInDropDown(object){
    console.log(object)
    for(var i in object){
        console.log(object[i])
        console.log(object[i] != "")
        if(object[i] != ""){
            for( j in object[i]){
            let fileNameDropDown = document.getElementById("listOfFileNames");
                var fileNameDropdownOption = document.createElement("option");
                fileNameDropdownOption.value = object[i][j];
                fileNameDropdownOption.text = object[i][j];
                fileNameDropDown.appendChild(fileNameDropdownOption);
                configName.push(object[i][j]);
            }
        }
    }
}

async  function getConfigResponse(){
    let configName = document.getElementById("listOfFileNames").value
    if(configName !== ""){
        var resp = await fetch('get-config-response', {
            method: 'POST',
            body: JSON.stringify([{ "configName" : configName}])
        })

        if (resp.status === 200) {
            var jsonData = await resp.json();
            console.log(jsonData)
            setValuesInConfig(jsonData)
        }
    }
}

function setValuesInConfig(object){
    console.log(object)
    for(var i in object){
        console.log(object[i])
        console.log(object[i] != "")
        if(object[i] != ""){
            for( j in object[i]){
                changeDefaultValuesOfConfig(object[i][j])
            }
        }
    }
}

function changeDefaultValuesOfConfig(object){
    for (var fields in object) {
        console.log(fields)
        console.log(`type${fields}`)
        var valueOfTypeId = document.getElementById(`type${fields}`.replaceAll('"', ''))
        if(valueOfTypeId !== null){
            setDefaultValues(object,fields)
        }
    }
}

function setDefaultValues(object,fields){
    document.getElementById(`type${fields}`.replaceAll('"', '')).value = object[fields]["type"];
    document.getElementById(`fixed-len${fields}`.replaceAll('"', '')).value = object[fields]["length"];
    document.getElementById(`allowNull${fields}`.replaceAll('"', '')).value = object[fields]["nullValue"];
    if(object[fields]["nullValue"] === "Allowed"){
        document.getElementById(`allowNull${fields}`.replaceAll('"', '')).checked = "checked";
    }
    document.getElementById(`date${fields}`.replaceAll('"', '')).value = object[fields]["date"];
    document.getElementById(`dateTime${fields}`.replaceAll('"', '')).value = object[fields]["dateTime"];
    document.getElementById(`dependent${fields}`.replaceAll('"', '')).value = object[fields]["dependentOn"];
    document.getElementById(`dep-val${fields}`.replaceAll('"', '')).value = object[fields]["dependentValue"];
    document.getElementById(`time${fields}`.replaceAll('"', '')).value = object[fields]["time"];

    alterDateTimeOptions(fields)
}

function alterDateTimeOptions(fields) {
    if(document.getElementById(`type${fields}`.replaceAll('"', '')).value === "Date Time"){
        document.getElementById(`dateTime${fields}`.replaceAll('"', '')).style.display = 'block'
        document.getElementById(`dateTimeDiv${fields}`.replaceAll('"', '')).style.display = 'flex'
        document.getElementById(`dateTimeFormats${fields}`.replaceAll('"', '')).style.display = 'block'
        document.getElementById(`length-div${fields}`.replaceAll('"', '')).style.display = 'none'
        document.getElementById(`time${fields}`.replaceAll('"', '')).value = ''
        document.getElementById(`date${fields}`.replaceAll('"', '')).value = ''
        document.getElementById(`fixed-len${fields}`.replaceAll('"', '')).value = ''
    }
    if( document.getElementById(`type${fields}`.replaceAll('"', '')).value === "Date"){
        document.getElementById(`date${fields}`.replaceAll('"', '')).style.display = 'block'
        document.getElementById(`dateDiv${fields}`.replaceAll('"', '')).style.display = 'flex'
        document.getElementById(`dateFormats${fields}`.replaceAll('"', '')).style.display = 'block'
        document.getElementById(`dateTimeDiv${fields}`.replaceAll('"', '')).style.display = 'none'
        document.getElementById(`length-div${fields}`.replaceAll('"', '')).style.display = 'none'
        document.getElementById(`dateTime${fields}`.replaceAll('"', '')).value = ''
        document.getElementById(`time${fields}`.replaceAll('"', '')).value = ''
        document.getElementById(`fixed-len${fields}`.replaceAll('"', '')).value = ''
    }

    if( document.getElementById(`type${fields}`.replaceAll('"', '')).value === "Time"){
        document.getElementById(`time${fields}`.replaceAll('"', '')).style.display = 'block'
        document.getElementById(`timeDiv${fields}`.replaceAll('"', '')).style.display = 'flex'
        document.getElementById(`timeFormats${fields}`.replaceAll('"', '')).style.display = 'block'
        document.getElementById(`dateDiv${fields}`.replaceAll('"', '')).style.display = 'none'
        document.getElementById(`length-div${fields}`.replaceAll('"', '')).style.display = 'none'
        document.getElementById(`dateTime${fields}`.replaceAll('"', '')).value = ''
        document.getElementById(`date${fields}`.replaceAll('"', '')).value = ''
        document.getElementById(`fixed-len${fields}`.replaceAll('"', '')).value = ''
    }
    const typeValue = document.getElementById(`type${fields}`.replaceAll('"', '')).value;
    if( typeValue === "Date" || typeValue === "Time" || typeValue === "Date Time"){
        document.getElementById(`value-div${fields}`.replaceAll('"', '')).style.display = 'none'
    }

    if( typeValue !== "Date" ){
        document.getElementById(`date${fields}`.replaceAll('"', '')).value = ''
    }
    if( typeValue !== "Time" ){
        document.getElementById(`time${fields}`.replaceAll('"', '')).value = ''
    }
    if(  typeValue !== "Date Time"){
            document.getElementById(`dateTime${fields}`.replaceAll('"', '')).value = ''
    }
}

function showColFields(lines){
    var arr = lines[0].split(",")
    for (var i = 1, j = 0; i <= lines.length; i++,j++){
        fieldCount += 1
        var row = document.createElement('div');
        var field = `${lines[j]}`.replaceAll('"', '');
        row.setAttribute("class", "row")
        row.setAttribute("id", `row${field}`)
        row.innerHTML = `<div id="addConfig${field}">
                        <div id="fields">
         <div class="input-field col s4"
          style="display:flex;  background: transparent;width: 400px;border-radius: 7px; height: 40px;margin-right: 3% ;margin-left:35%;padding: 1em;margin-bottom: 3em;border-left: 4px solid grey;border-top: 4px solid grey;border-bottom: 4px solid grey;border-right: 4px solid grey;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">
           <h4 style= " text-transform: uppercase;"> ${field}</h4>
           </div>
                     <p style="color:red; margin-left: -25%; font-size: 15px;"><span id="typeEmpty${field}" class="error"></span></p>

                     <div style="display:flex; ">
                         <div class="input-field col s4"
                                     style="display:flex;  background: transparent;width: 300px;border-radius: 7px; height: 40px;margin-right:13% ;margin-left:15.4%;padding: 1em;margin-bottom: 2em;border-left: 4px solid grey;border-right: 4px solid grey;border-top: 1px solid black;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">

                            <label  style="border-radius: 150px;" for="type" class ="required-field">Type </label>
                            <select placeholder="Choose Type" data-cy="type" id="type${field}"
                            onchange="showDateTimeOption(this.value,'dateDiv${field}','dateFormats${field}' , 'date${field}','timeDiv${field}','timeFormats${field}','time${field}','dateTimeDiv${field}','dateTimeFormats${field}' , 'dateTime${field}' ,'length-div${field}', 'value-div${field}');">
                               <option value="">Choose Type of Data</option>
                                <option value="Number">Number</option>
                                <option value="AlphaNumeric">AlphaNumeric</option>
                                <option value="Alphabets">Alphabets</option>
                                <option value="Floating Number">Floating Number</option>
                                <option value="Special Characters">Text</option>
                                <option value="Date Time">Date Time</option>
                                <option value="Date">Date</option>
                                <option value="Time">Time</option>
                                <option value="Email">Email</option>
                            </select>
                         </div>

                     <div  id = "dateTimeDiv${field}" class="input-field  col s4" 
                                        style="display:none;  background: transparent;width: 300px;border-radius: 7px; height: 40px;margin-right: 13% ;margin-left:10%;padding: 1em;margin-bottom: 2em;border-left: 4px solid grey;border-right: 4px solid grey;border-top: 1px solid black;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">

    <label class ="required-field" for="datetime" id="dateTimeFormats${field}" style='display:none;'>Date-Time Format</label>
                                                
    <select placeholder="Choose date time format"  name="datetime" id='dateTime${field}' style='display:none;'>
         <option value="">Choose Date Time format</option>
         <option value="HH:mm:ss.SSSZ">HH:MM:SS.SSSZ</option>
         <option value="MMMM dd, yy">MMMM DD, YYYY</option>
         <option value="MMM dd, yyyy hh:mm:ss a">MMM dd, yyyy hh:mm:ss a</option>
         <option value="MMM dd HH:mm:ss ZZZZ yyyy">MMM dd HH:mm:ss ZZZZ yyyy</option>
         <option value="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'">yyyy-MM-dd'T'HH:mm:ss.SSS'Z'</option>
         <option value="yyyy-MM-dd'T'HH:mm:ss">YYYY-MM-DD'T'HH:MM:SS</option>
         <option value="MMM dd, yyyy hh:mm:ss a">MMM DD, YYYY HH:MM:SS AM</option>
         <option value="dd/MMM/yyyy:HH:mm:ss ZZZZ">DD/MMM/YYYY:HH:MM:SS ZZZZ</option>
         <option value="MMM dd HH:mm:ss ZZZZ yyyy">MMM DD HH:MM:SS ZZZZ YYYY</option>
         <option value="MMM dd yyyy HH:mm:ss">MMM DD YYYY HH:MM:SS</option>
         <option value="MM/dd/yyyy hh:mm:ss a">MM/DD/YYYY HH:MM:SS AM</option>
         <option value="MM/dd/yyyy hh:mm:ss a:SSS">MM/DD/YYYY HH:MM:SS AM:SSS</option>
         <option value="MMdd_HH:mm:ss.SSS">MMDD_HH:MM:SS.SSS</option>
         <option value="MMdd_HH:mm:ss">MMDD_HH:MM:SS</option>
         <option value="dd MMM yyyy HH:mm:ss*SSS">DD MMM YYYY HH:MM:SS*SSS</option>
         <option value="dd MMM yyyy HH:mm:ss">DD MMM YYYY HH:MM:SS</option>
         <option value="dd/MMM/yyyy HH:mm:ss"">DD/MMM/YYYY HH:MM:SS</option>
         <option value="dd/MMM HH:mm:ss,SSS"">DD/MMM HH:MM:SS,SSS</option>
    </select>
</div>

                         
                         
                         <div id = "dateDiv${field}" class="input-field  col s4"
                                                style="display:none;  background: transparent;width: 300px;border-radius: 7px; height: 40px;margin-right: 2% ;margin-left:6%;padding: 1em;margin-bottom: 2em;border-left: 4px solid grey;border-right: 4px solid grey;border-top: 1px solid black;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">
                                                
                            <label class ="required-field"  for="date" id="dateFormats${field}" style='display:none;'>Date Format</label>
                            <select placeholder="Choose date format"  name="date" id='date${field}' style='display:none;'>
                                 <option value="">"Choose Date Format"</option>
                                 <option value="MM-dd-yyyy">MM-DD-YYYY</option>
                                 <option value="dd-MM-yyyy">DD-MM-YYYY</option>
                                 <option value="dd/MM/yyyy, yy">DD/MM/YYYY</option>
                                <option value="yy/MM/dd">YY/MM/DD</option>
                                <option value="yyyy/MM/dd">YYYY/MM/DD</option>
                                <option value="M/d/yyy">M/D/yyy</option>
                                <option value="d/M/yyyy">D/M/YYYY</option>
                                <option value="yyyy/M/dd">YYYY/M/DD</option>
                                <option value="ddMMyYYy">DDMMYYYY</option>
                                <option value="yyyy-MM-dd">YYYY-MM-DD</option>
                            </select>
                         </div>


                     <div id = "timeDiv${field}" class="input-field  col s4"
                      style="display:none;border-radius: 7px;  background: transparent;width: 300px; height: 30px;margin-right: 3% ;margin-left:6%;padding: 1em;margin-bottom: 2em;border-left: 4px solid grey;border-right: 4px solid grey;border-top: 1px solid black;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">

                       <label class ="required-field"  for="time" style='display:none;' id="timeFormats${field}">Time Format</label>
                           <select  placeholder="Choose time format"  name="time" id='time${field}' style='display:none;'>
                                <option value="">Choose Time Format</option>
                                <option value="hh:mm:ss">HH:MM:SS</option>
                                <option value="HH:mm:ss zzz">HH:MM:SS ZZZ</option>
                                <option value="HH:mm:ss.SSSZ">HH:MM:SS.SSSZ</option>
                           </select>
                      </div>


                         <div id="length-div${field}"class="input-field  col s4"
                         style="display:flex;  background: transparent;width: 300px;margin-right: 7% ;border-radius: 7px;margin-left:7%; height: 40px;padding: 1em;margin-bottom: 2em;border-left: 4px solid grey;border-right: 4px solid grey;border-top: 1px solid black;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">
                            <label for="fixed-len">Length</label>
                            <input placeholder="Enter Length" min=0 onkeypress="return event.charCode >= 49" style="padding:10px" type="number" id="fixed-len${field}" data-cy="fixed-len">
                         </div>
                       </div>

                         <div style="display:flex; ">

                         <div>
                         <div id="allowNullDiv${field}" class="input-field  col s4"
                         style="display:flex;  background: transparent;width: 300px;margin-right: 53% ;border-radius:7px;margin-left:55%; height: 40px;padding: 1em;margin-bottom: 2em;border-left: 4px solid grey;border-right: 4px solid grey;border-top: 1px solid black;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">
                         <label style="border-radius:60px;margin-right:-140%;margin-top:7%;" for="values">Select to allow empty values</label>
                         <p style="font-size:14px;display: inline-block;white-space: nowrap;margin-top:-12%;margin-left:36%;margin-right:160%;width:20px;"></p>
                         <label class="switch">
                           <input id="allowNull${field}" type="checkbox" value="Not Allowed" onclick="toggleYesOrNo(this.id);">
                           <span class="slider round"></span>
                         </label>
                         </div>

                         <div id="value-div${field}" class="input-field  col s4"
                                  style="display:flex;  background: transparent;width: 300px;margin-right: 3% ;border-radius:7px;margin-left:55%; height: 40px;padding: 1em;margin-bottom: 2em;border-left: 4px solid grey;border-right: 4px solid grey;border-top: 1px solid black;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">
                                  <label for="values">Values</label>
                                     <select style="margin-left:5%;" type="text" name="values" id="values${field}"
                                       onchange="onChangeHandler(this.value,'text_file_id${field}','popUp${field}','addConfig${field}');">
                                     <option  style="display:flex;border-radius:60px;margin-top:7%;" value=""> Choose</option>
                                       <option value="Upload File">Upload File</option>
                                                   <option value="Type Values">Type Values</option>
                                                            </select>
                                             <input onchange="readFile(event,'${field}');" type="file" id="text_file_id${field}" style="display:none;" accept=".txt">
                                                  </label>
                                                  </div>


                            
                         </div>


                         <div class="input-field  col s4"
                           style="display:flex;  background: transparent;border-radius: 7px;width: 300px;margin-right: 10% ;margin-left:35%; height: 40px;padding: 1em;margin-bottom: 2em;border-left: 4px solid grey;border-right: 4px solid grey;border-top: 1px solid black;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">

                             <label for="dependent">Dependent</label>
                             <select placeholder="Choose dependant-field" name="dependentField" style="display: block;" id="dependent${field}">
                                 <option value="">Choose Dependent Field</option>
                                     ${lines.map((element) => {
            return `<option value='${element}'>${element.replaceAll('"', '')}</option>`;
        })}
                             </select>
                         </div>

                         <div class="input-field  col s4"
                          style="display:flex;border-radius: 7px;  background: transparent;width: 300px;margin-right: 7% ;margin-left:-39%;margin-top: 9%; height: 40px;padding: 1em;margin-bottom: 2em;border-left: 4px solid grey;border-right: 4px solid grey;border-top: 1px solid grey;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;font-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">
                             <label for="dep-val">Dependent Value</label>
                             <input type="text" id="dep-val${field}" data-cy="dep-val">
                         </div>

                    </div>
                     <br> </br> <br>
                  </div>
                  </div>
                  
                   <div class="form-popup" id="popUp${field}" >
                                             <div class="form-container">
                                               <h1>Enter Values</h1>

                                               <textarea id="textArea${field}" placeholder="Enter Values"></textarea>
                                               <div id = "list"> 
                                               <ul style='margin-left: 55%; margin-top: -20%'>
                                                    <li> Please Enter the values in newline without any delimiters</li> 
                                                    <li> Example:</li>
                                                    <ul>
                                                        <li> Val1 </li>
                                                        <li> Val2 </li>
                                                        <li> Val3 </li>
                                                    </ul>

                                               </ul>
                                               </div>
                                               <div id="outer">
                                                 <div class="inner">
                                                    <button onclick="closeForm('popUp${field}','addConfig${field}')" type="button" class="btn" style='display:block;'>Submit</button>
                                                 </div>
                                                 <div class="inner">
                                                    <button type="button" class="btn cancel" onclick="closeForm('popUp${field}','addConfig${field}')">Close</button>
                                                 </div>
                                               </div>

                                             </div>
                                           </div>
                      <br>
                 `
        document.getElementById("myform").appendChild(row)
    }
}

function typeMandatory() {
    var emptyFields = 0
    for (var i = 1, j = 0; i <= fieldCount; i++,j++){
         var typeField = document.getElementById(`type${fields[0][j]}`.replaceAll('"', ''))
         if (typeField.value == ""){
             document.getElementById(`typeEmpty${fields[0][j]}`.replaceAll('"', '')).innerHTML="Please select a type";
             document.getElementById("fields-empty").innerHTML = "You have left mandatory fields empty!"
             emptyFields += 1
         }
         else {
                document.getElementById(`typeEmpty${fields[0][j]}`.replaceAll('"', '')).innerHTML="";
         }
    }
    return (emptyFields == 0)
}

function closeForm(popUp , addConfig, valueOption) {
    document.getElementById(popUp).style.display = "none";
    document.getElementById(addConfig).style.filter = "blur(0px)";
}

function onChangeHandler(valueOption, fileInput, textAreaInput,addConfig){
    var uploadFile = document.getElementById(fileInput);
    console.log(uploadFile.files[0])
    var typeValues = document.getElementById(textAreaInput);
    var addConfig = document.getElementById(addConfig);
    if (valueOption === "Type Values"){
        console.log("Changing")
        uploadFile.style.display = "none";
        typeValues.style.display = "block";
        addConfig.style.filter = "blur(3px)";
    }
    else {
        uploadFile.style.display = "flex";
        typeValues.style.display = "none";
    }
}


function toggleYesOrNo(element) {
    let oldValue = document.getElementById(element).value
    if (oldValue === "Not Allowed") {
        document.getElementById(element).value = "Allowed"
        return
    }
    document.getElementById(element).value = "Not Allowed"
}

function showDateTimeOption(value, dateDivID, dateFormatId, dateId , timeDivID, timeFormatId,timeId, dateTimeDivID, dateTimeFormatId, dateTimeId ,lengthDivId, valueDivId){
    var dateDivIDElement = document.getElementById(dateDivID);
    var dateFormatElement = document.getElementById(dateFormatId);
    var dateIdFormatElement = document.getElementById(dateId);
    var timeDivIDElement = document.getElementById(timeDivID);
    var timeFormatElement = document.getElementById(timeFormatId);
    var timeIdFormatElement = document.getElementById(timeId);
    var lengthDivElement = document.getElementById(lengthDivId);
    var valueDivElement = document.getElementById(valueDivId);
    var dateTimeDivIDElement = document.getElementById(dateTimeDivID);
    var dateTimeFormatElement = document.getElementById(dateTimeFormatId);
    var dateTimeIdFormatElement = document.getElementById(dateTimeId);

    if(value === 'Date Time'){
        dateTimeDivIDElement.style.display='flex';
        dateTimeFormatElement.style.display='block';
        dateTimeIdFormatElement.style.display='block';
        dateDivIDElement.style.display='none';
        dateFormatElement.style.display='none';
        dateIdFormatElement.style.display='none';
        timeDivIDElement.style.display='none';
        timeFormatElement.style.display='none';
        timeIdFormatElement.style.display='none';
        valueDivElement.style.display='none';
        lengthDivElement.style.display='none';
    }

    else if(value === 'Date'){
        dateDivIDElement.style.display='flex';
        dateFormatElement.style.display='block';
        dateIdFormatElement.style.display='block';
        timeDivIDElement.style.display='none';
        timeFormatElement.style.display='none';
        timeIdFormatElement.style.display='none';
        dateTimeDivIDElement.style.display='none';
        dateTimeFormatElement.style.display='none';
        dateTimeIdFormatElement.style.display='none';
        valueDivElement.style.display='none';
        lengthDivElement.style.display='none';
    }
    else if(value === 'Time'){
        timeDivIDElement.style.display='flex';
        timeFormatElement.style.display='block';
        timeIdFormatElement.style.display='block';
        dateDivIDElement.style.display='none';
        dateFormatElement.style.display='none';
        dateIdFormatElement.style.display='none';
        dateTimeDivIDElement.style.display='none';
        dateTimeFormatElement.style.display='none';
        dateTimeIdFormatElement.style.display='none';
        valueDivElement.style.display='none';
        lengthDivElement.style.display='none';
    }
    else if(value === 'Date Time'){
        dateDivIDElement.style.display='none';
        dateFormatElement.style.display='none';
        dateIdFormatElement.style.display='none';
        timeDivIDElement.style.display='flex';
        timeFormatElement.style.display='block';
        timeIdFormatElement.style.display='block';
        dateTimeDivIDElement.style.display='none';
        dateTimeFormatElement.style.display='none';
        dateTimeIdFormatElement.style.display='none';
        valueDivElement.style.display='none';
        lengthDivElement.style.display='none';
    }
    else{
        dateDivIDElement.style.display='none';
        dateFormatElement.style.display='none';
        dateIdFormatElement.style.display='none';
        dateTimeDivIDElement.style.display='none';
        dateTimeFormatElement.style.display='none';
        dateTimeIdFormatElement.style.display='none';
        timeDivIDElement.style.display='none';
        timeFormatElement.style.display='none';
        timeIdFormatElement.style.display='none';
        dateTimeDivIDElement.style.display='none';
        dateTimeFormatElement.style.display='none';
        dateTimeIdFormatElement.style.display='none';
        valueDivElement.style.display='flex';
        lengthDivElement.style.display='block';
    }
}



function readFile(event, fieldName){
    var value = document.getElementById(`text_file_id${fieldName}`).files[0];
    if (value != null){
        let reader = new FileReader();
        reader.addEventListener('load', function(e) {
            let text = e.target.result
            console.log(JSON.stringify(text.split('\n')))
            localStorage.setItem(fieldName, JSON.stringify(text.split('\n')));
        });
        reader.readAsText(value)
    }
    return null;
}

function addDataToJson() {
    for (var i = 1, j = 0; i <= fieldCount; i++,j++){
        let jsonObj = {}
        var field = fields[0][j]
        var configName = document.getElementById("fileName")
        var type = document.getElementById(`type${fields[0][j]}`.replaceAll('"', ''))
        var value = document.getElementById(`text_file_id${fields[0][j]}`.replaceAll('"', '')).files[0]
        var typedValues = document.getElementById(`textArea${fields[0][j]}`.replaceAll('"', ''))
        var fixed_len = document.getElementById(`fixed-len${fields[0][j]}`.replaceAll('"', ''))
        var dependentOn = document.getElementById(`dependent${fields[0][j]}`.replaceAll('"', ''))
        var dependentValue = document.getElementById(`dep-val${fields[0][j]}`.replaceAll('"', ''))
        var dateFormat = document.getElementById(`date${fields[0][j]}`.replaceAll('"', ''))
        var timeFormat = document.getElementById(`time${fields[0][j]}`.replaceAll('"', ''))
        var dateTimeFormat = document.getElementById(`dateTime${fields[0][j]}`.replaceAll('"', ''))
        var nullValues = document.getElementById(`allowNull${fields[0][j]}`.replaceAll('"', ''))
        var configCheckBox = document.getElementById("configCheckBox").checked
        jsonObj["configName"] = document.getElementById("fileName").value
        console.log(document.getElementById("fileName").value)
        jsonObj["datetime"] = dateTimeFormat.value
        jsonObj["date"] = dateFormat.value
        jsonObj["time"] = timeFormat.value
        jsonObj["nullValue"] = nullValues.value
        console.log(nullValues.value)
        jsonObj["fieldName"] = field
        jsonObj["type"] = type.value
        if(!configCheckBox){
            jsonObj["configName"] = ""
        }
        if (value != null){
            jsonObj["values"] =JSON.parse(localStorage.getItem(field))
            console.log(localStorage.getItem(field))
        }
        if(typedValues.value != '' )
        {
            jsonObj["values"] = typedValues.value.split('\n')
        }
        jsonObj["length"] = fixed_len.value
        jsonObj["dependentOn"] = dependentOn.value
        jsonObj["dependentValue"] = dependentValue.value
        const typeValue = type.value
        if( typeValue !== "Date" && typeValue !== "Time" && typeValue !== "Date Time"){
                jsonObj["datetime"] = ''
                jsonObj["date"] = ''
                jsonObj["time"] = ''
        }else if(typeValue === "Date"){
            jsonObj["datetime"] = ''
            jsonObj["length"] = ''
            jsonObj["time"] = ''
        }else if(typeValue === "Time"){
            jsonObj["datetime"] = ''
            jsonObj["date"] = ''
            jsonObj["length"] = ''
        }else if(typeValue === "Date Time"){
            jsonObj["length"] = ''
            jsonObj["date"] = ''
            jsonObj["time"] = ''
        }

        payload.push(jsonObj)
    }
    console.log(payload)
}

async function sendConfigData(){
    addDataToJson()
    var resp = await fetch('add-meta-data', {
        method: 'POST',
        body: JSON.stringify(payload)
    })

    if (resp.status === 200) {
        var jsonData = await resp.json();
        console.log(jsonData)
    }
}

async function displayErrors(){
    emptyErrorList()
    document.getElementById("config_name_validation").style.display = 'none';
    if(validateConfigName() && typeMandatory()){
        document.getElementById("fields-empty").innerHTML = ""
        loadingEffect()
        sendConfigData()
        const response = await fetch('csv', {
            method: 'POST',
            body: JSON.stringify(result)
        })

        if (response.status === 200) {
            var jsonData =  await response.json();
            loadingEffect();
            console.log(jsonData)
            traverse(jsonData)
        }
        var loader = document.getElementById("button-load")
        loader.style.visibility = "hidden";
        payload = []
        configName = []
    }
}

function traverse(object){
    errMap={}
    let errorBase = document.getElementById("error-msgs");
    for(var i in object){
        let key = Object.keys(object[i])[0]
        console.log(key)
        key = key.replaceAll('"', '')
        let value = Object.values(object[i])
        createDivElement(key , value[0])
        let fieldsDivElement = document.getElementById(`${key}`)
        if(fieldsDivElement.innerHTML === ''){
            removeErrorDiv(key)
        }
    }
    if(errorBase.innerHTML === ''){
        createSuccessErrMsg(errorBase)
    }
}

function createSuccessErrMsg(errorBase){
    let row = document.createElement("div");
        row.innerHTML = `
        <div style="display:flex; flex-direction: row;padding:20px;">
          <div style="width: 92%;font-size:20px; font-weight:400; box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.25);  border-radius: 3px;  padding:38px; text-align:left;color:white;margin:auto">
          <marquee scrollamount="12">No Error Found In Your CSV File</marquee>
          </div>
      </div>`;

      errorBase.appendChild(row);
}

function removeErrorDiv(key){
    let errorBase = document.getElementById("error-msgs");
    var child = document.getElementById(key+" error");
    errorBase.removeChild(child);
}

function createDivElement(key , value){
    key = key.replaceAll('"', '')
    let errorBase = document.getElementById("error-msgs");
    let row = document.createElement("div");
    row.setAttribute("id", key+" error")
                row.innerHTML = `
                <div style="display:flex; flex-direction: row;padding:20px;">
                  <div style="width: 92%;font-size:20px; font-weight:400; box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.25);  border-radius: 3px;  padding:18px; text-align:left;color:white;margin:auto">
                  <p style="margin:auto;">${key}
                  <svg  style="float:right;" width="15" height="25" viewBox="0 0 9 7" fill="black" xmlns="http://www.w3.org/2000/svg" >
                  <path style="display:block;z-index:-1" d="M5.81565 1.5L4.4261 3.75802L2.86285 1.5H5.81565Z" stroke="black" stroke-width="4" onclick="goUp('${key}')" id="UpDrop${key}"/>
                  </svg>
                  </p>
                  </div>
              </div>
              <div class="card-panel left-align" style="margin:auto; width:83%; font-size:20px;font-weight:200;
              background: white; box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.25); border-radius: 3px; padding:50px;
              text-align:left; color:black; display:none;"
              id="${key}"></div>
          </div>
        `;

        errorBase.appendChild(row)
        for(i in value){
            console.log(value[i].length)
            if(i === 'Duplicate Errors' && value[i].length != 0 && document.getElementById("Duplicate Errors error") === null){
                createDuplicationErrMsg(value[i],i)
            }
            console.log(i)
            if(value[i].length != 0 && i !== 'Duplicate Errors'){
                createTableOfErrors(value[i],key,i)
            }
        }
}

function createDuplicationErrMsg(value,key){

    let errorBase = document.getElementById("error-msgs");
    let row = document.createElement("div");
    row.setAttribute("id", key+" error")
        row.innerHTML = `
        <div style="display:flex; flex-direction: row;padding:20px;">
          <div style="width: 92%;font-size:20px; font-weight:400; box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.25);  border-radius: 3px;  padding:18px; text-align:left;color:white;margin:auto">
          <p style="margin:auto;">${key}
          <svg  style="float:right;" width="15" height="25" viewBox="0 0 9 7" fill="black" xmlns="http://www.w3.org/2000/svg" >
          <path style="display:block;z-index:-1" d="M5.81565 1.5L4.4261 3.75802L2.86285 1.5H5.81565Z" stroke="black" stroke-width="4" onclick="goUp('${key}')" id="UpDrop${key}"/>
          </svg>
          </p>
          </div>
      </div>
      <div class="card-panel left-align" style="margin:auto; width:83%; font-size:20px; font-weight:200;background: white;
      box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.25); border-radius: 3px; padding:50px; text-align:left; color:black; display:none;
      "
      id="${key}"></div>
      </div>
    `;

    errorBase.appendChild(row)
    let p = document.createElement("p")
    p.setAttribute("id", "error")
    p.style.marginTop="0px"
    p.innerHTML = `<b style="font-weight: 900;">${key} present in rows:</b><br/><br/>`
    let table=document.createElement("table")
    console.log(table)
    let i=0;
    console.log("size",value.length)
    let firstRow = document.createElement("tr");
    let firstHeading = document.createElement("td")
    let secondHeading = document.createElement("td")
    firstHeading.innerHTML = "Row Number "
    secondHeading.innerHTML = "Copied From Row Number"
    firstRow.appendChild(firstHeading);
    firstRow.appendChild(secondHeading);
    table.appendChild(firstRow)
     while(i<value.length){
            let newRow = document.createElement("tr");
            let td_1 = document.createElement("td")
            let td_2 = document.createElement("td")
            td_1.innerHTML = value[i]
            td_2.innerHTML = value[i+1]
            newRow.appendChild(td_1);
            newRow.appendChild(td_2);
            table.appendChild(newRow)
            i = i+2;
     }
     p.appendChild(table)
     let parent = document.getElementById(`${key}`)
     parent.appendChild(p)
}

function createTableOfErrors(value,key,type){
    let p = document.createElement("p")
    p.setAttribute("id", "error")
    p.style.marginTop="0px"
    p.innerHTML = `<b style="font-weight: 900;">${type} present in rows:</b><br/><br/>`
    let table=document.createElement("table")
    console.log(table)
    let i=0;
    let j=0;
    console.log("size",value.length)
     while(i<value.length){
         let newRow=document.createElement("tr");
         while(j<i+5 && j<value.length){
             let td=document.createElement("td")
             td.innerHTML = value[j]
             newRow.appendChild(td);
             j++;
         }
         table.appendChild(newRow)
         i=j;
     }
     p.appendChild(table)
     let parent = document.getElementById(`${key}`)
     parent.appendChild(p)
}

function goDown(key)
{
    document.getElementById(`DownDrop${key}`).outerHTML=`<path style="display:block;z-index:-1" d="M5.81565 1.5L4.4261 3.75802L2.86285 1.5H5.81565Z" stroke="black" stroke-width="4" onclick="goUp('${key}')" id="UpDrop${key}"/>`
    document.getElementById(`${key}`).style.display="none"
}
function goUp(key)
{
    document.getElementById(`UpDrop${key}`).outerHTML=`  <path d="M5.81565 5L4.4261 2.74198L2.86285 5H5.81565Z" stroke="black" stroke-width="4" onclick="goDown('${key}')" id="DownDrop${key}"/>`
    document.getElementById(`${key}`).style.display="block"
}



var current_page = 1;
var obj_per_page = 5;
function totNumPages()
{
    return Math.ceil(Object.keys(errMap).length / obj_per_page);
}

function emptyErrorList(){
    const el = document.getElementById("error-msgs");
    while (el.firstChild) {
        el.removeChild(el.firstChild)
    };
}

function loadingEffect(){
    var loader = document.getElementById("button-load")
    loader.style.visibility = "visible";
}
