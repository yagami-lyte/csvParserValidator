var payload=[]
var result = []
var fields = []
var fieldCount = 0


function removeConfiguredFields() {
    var addedField = document.getElementById("field");
    addedField.remove(addedField.selectedIndex);
}

function csvReader() {
    var csv = document.getElementById("csv_id").files[0];
    const reader = new FileReader();
    reader.onload = async function (event) {
        csv = event.target.result
        var lines = csv.toString().split("\n");
        var headers = lines[0].split(",");
        showColFields(headers);
        fields.push(headers)
        for (var i = 1; i < lines.length-1; i++) {
            var obj = {};
            var currentLine = lines[i].split(",");
            for (var j = 0; j < headers.length; j++) {
                obj[headers[j]] = currentLine[j];
            }
            result.push(obj);
            localStorage.setItem(csv, JSON.stringify(result));
        }
    };
    reader.readAsText(csv);
}

function showColFields(lines){
    var arr = lines[0].split(",")
    for (var i = 1, j = 0; i <= lines.length; i++,j++){
    fieldCount += 1
    var row = document.createElement('div');
    row.setAttribute("class", "row")
    row.setAttribute("id", `row${lines[j]}`)
        row.innerHTML = `<div id="fields">
         <div class="input-field col s4"
          style="display:flex;  background: transparent;width: 400px;border-radius: 7px; height: 40px;margin-right: 3% ;margin-left:35%;padding: 1em;margin-bottom: 3em;border-left: 0.5px solid black;border-top: 1px solid black;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">
           <h4> ${lines[j]}</h4>
           </div>

                     <div style="display:flex; ">
                         <div class="input-field col s4"
                                     style="display:flex;  background: transparent;width: 300px;border-radius: 7px; height: 40px;margin-right: 3% ;margin-left:3%;padding: 1em;margin-bottom: 2em;border-left: 0.5px solid black;border-top: 1px solid black;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">

                            <label  style="border-radius: 150px;" for="type">Type</label>
                            <select placeholder="Choose Type" data-cy="type" id="type${lines[j]}"
                            onchange="showDateTimeOption(this.value,'dateDiv${lines[j]}','dateFormats${lines[j]}' , 'date${lines[j]}','timeDiv${lines[j]}','timeFormats${lines[j]}','time${lines[j]}','dateTimeDiv${lines[j]}','dateTimeFormats${lines[j]}' , 'dateTime${lines[j]}' ,'length-div${lines[j]}', 'value-div${lines[j]}');">
                               <option value="">Choose Type of Data</option>
                                <option value="Number">Number</option>
                                <option value="AlphaNumeric">AlphaNumeric</option>
                                <option value="Alphabets">Alphabets</option>
                                <option value="Floating Number">Floating Number</option>
                                <option value="Special Characters">Special Characters</option>
                                <option value="Date Time">Date Time</option>
                                <option value="Date">Date</option>
                                <option value="Time">Time</option>
                                <option value="Email">Email</option>
                            </select>
                         </div>
                         
                     <div  id = "dateTimeDiv${lines[j]}" class="input-field  col s4" 
                                        style="display:none;  background: transparent;width: 300px;border-radius: 7px; height: 40px;margin-right: 3% ;margin-left:3%;padding: 1em;margin-bottom: 2em;border-left: 0.5px solid black;border-top: 1px solid black;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">

    <label for="datetime" id="dateTimeFormats${lines[j]}" style='display:none;'>Date-Time Format</label>
                                                
    <select placeholder="Choose date time format"  name="datetime" id='dateTime${lines[j]}' style='display:none;'>
         <option>"choose date time format"</option>
         <option value="MM-dd-yyyy">MM-dd-yyyy</option>
         <option value="HH:mm:ss.SSSZ">HH:mm:ss.SSSZ</option>
         <option value="MMMM dd, yy">MMMM dd, yy</option>
         <option value="yy/MM/dd">yy/MM/dd</option>
         <option value="dd/MM/yy">dd/MM/yy</option>
         <option value="MMM dd, yyyy hh:mm:ss a">MMM dd, yyyy hh:mm:ss a</option>
         <option value="MMM dd HH:mm:ss ZZZZ yyyy">MMM dd HH:mm:ss ZZZZ yyyy</option>
         <option value="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'">yyyy-MM-dd'T'HH:mm:ss.SSS'Z'</option>
    </select>
</div>

                         
                         
                         <div id = "dateDiv${lines[j]}" class="input-field  col s4"
                                                style="display:none;  background: transparent;width: 300px;border-radius: 7px; height: 40px;margin-right: 3% ;margin-left:3%;padding: 1em;margin-bottom: 2em;border-left: 0.5px solid black;border-top: 1px solid black;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">
                                                
                            <label for="date" id="dateFormats${lines[j]}" style='display:none;'>Date Format</label>
                            <select placeholder="Choose date format"  name="date" id='date${lines[j]}' style='display:none;'>
                                 <option>"Choose Date Format"</option>
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


                     <div id = "timeDiv${lines[j]}" class="input-field  col s4" 
                      style="display:none;border-radius: 7px;  background: transparent;width: 300px; height: 30px;margin-right: 3% ;margin-left:3%;padding: 1em;margin-bottom: 2em;border-left: 0.5px solid black;border-top: 1px solid black;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">

                       <label for="time" style='display:none;' id="timeFormats${lines[j]}">Time Format</label>
                           <select  placeholder="Choose time format"  name="time" id='time${lines[j]}' style='display:none;'>
                                <option>Choose Time Format</option>
                                <option value="hh:mm:ss">HH:MM:SS</option>
                                <option value="HH:mm:ss zzz">HH:MM:SS ZZZ</option>
                                <option value="HH:mm:ss.SSSZ">HH:MM:SS.SSSZ</option>
                           </select>
                      </div>


                         <div id="length-div${lines[j]}"class="input-field  col s4"
                         style="display:flex;  background: transparent;width: 300px;margin-right: 3% ;border-radius: 7px;margin-left:3%; height: 40px;padding: 1em;margin-bottom: 2em;border-left: 0.5px solid black;border-top: 1px solid black;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">
                            <label for="fixed-len">Length</label>
                            <input placeholder="Enter Length"  type="number" id="fixed-len${lines[j]}" data-cy="fixed-len">
                         </div>
                       </div>

                         <div style="display:flex; ">

                         <div id="value-div${lines[j]}" class="input-field  col s4"
                         style="display:flex;  background: transparent;width: 300px;margin-right: 3% ;margin-left:3%; height: 50px;padding: 1em;margin-bottom: 2em;border-left: 0.5px solid black;border-top: 1px solid black;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">
                         <label style="border-radius:60px;" for="values">Values</label>
                         <p> "Select to allow null values"</p>
                         <label class="switch">
                           <input id="allowNull${lines[j]}" type="checkbox" value="Not Allowed" onclick="toggleYesOrNo(this.id);">
                           <span class="slider round"></span>
                         </label>
                         <select type="text" name="values" id="values${lines[j]}"
                            onchange="onChangeHandler(this.value,'text_file_id${lines[j]}','popUp${lines[j]}');">
                            <option value=""> Choose</option>
                            <option value="Upload File">Upload File</option>
                            <option value="Type Values">Type Values</option>
                            </select>
                            <input onchange="readFile(event,'${lines[j]}');" type="file" id="text_file_id${lines[j]}" style="display:none;" accept=".txt">
                            
                         </div>


                         <div class="input-field  col s4"
                           style="display:flex;  background: transparent;border-radius: 7px;width: 300px;margin-right: 3% ;margin-left:3%; height: 40px;padding: 1em;margin-bottom: 2em;border-left: 0.5px solid black;border-top: 1px solid black;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">

                             <label for="dependent">Dependent</label>
                             <select placeholder="Choose dependant-field" name="dependentField" style="display: block;" id="dependent${lines[j]}">
                                 <option value="">Choose Dependent Field</option>
                                     ${lines.map((element) => {
                                         return `<option value='${element}'>${element}</option>`;
                                     })}
                             </select>
                         </div>

                         <div class="input-field  col s4"
                          style="display:flex;border-radius: 7px;  background: transparent;width: 300px;margin-right: 3% ;margin-left:3%; height: 40px;padding: 1em;margin-bottom: 2em;border-left: 0.5px solid black;border-top: 1px solid black;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;font-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">
                             <label for="dep-val">Dependent Value</label>
                             <input type="text" id="dep-val${lines[j]}" data-cy="dep-val">
                         </div>

                    </div>
                     <br> </br> <br>
                  </div>
                  
                   <div class="form-popup" id="popUp${lines[j]}" >
                                             <div class="form-container">
                                               <h1>Enter Values</h1>

                                               <textarea id="textArea${lines[j]}" placeholder="Enter Values"></textarea>
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
                                               
                                               <button onclick="closeForm('popUp${lines[j]}','div1${lines[j]}')" type="button" class="btn" style='display:block;'>Submit</button>
                                               
                                             </div>
                                           </div>
                      <br>
                 `
    document.getElementById("myform").appendChild(row)
}
}

function closeForm(popUp , div1) {
    document.getElementById(popUp).style.display = "none";
    document.getElementById(div1).style.filter = "blur(0px)";
}

function onChangeHandler(valueOption, fileInput, textAreaInput){
    var uploadFile = document.getElementById(fileInput);
    console.log(uploadFile.files[0])
    var typeValues = document.getElementById(textAreaInput);
    if (valueOption === "Type Values"){
        console.log("Changing")
        uploadFile.style.display = "none";
        typeValues.style.display = "block";
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
        var type = document.getElementById(`type${fields[0][j]}`)
        var value = document.getElementById(`text_file_id${fields[0][j]}`).files[0]
        var typedValues = document.getElementById(`textArea${fields[0][j]}`)
        var fixed_len = document.getElementById(`fixed-len${fields[0][j]}`)
        var dependentOn = document.getElementById(`dependent${fields[0][j]}`)
        var dependentValue = document.getElementById(`dep-val${fields[0][j]}`)
        var dateFormat = document.getElementById(`date${fields[0][j]}`)
        var timeFormat = document.getElementById(`time${fields[0][j]}`)
        var dateTimeFormat = document.getElementById(`dateTime${fields[0][j]}`)
        var nullValues = document.getElementById(`allowNull${fields[0][j]}`)
        jsonObj["datetime"] = dateTimeFormat.value
        jsonObj["date"] = dateFormat.value
        jsonObj["time"] = timeFormat.value
        jsonObj["nullValue"] = nullValues.value
        console.log(nullValues.value)
            jsonObj["fieldName"] = field
            jsonObj["type"] = type.value
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
   sendConfigData()
    const response = await fetch('csv', {
        method: 'POST',
        body: JSON.stringify(result)
    })

    if (response.status === 200) {
        var jsonData =  await response.json();
        console.log(jsonData)
        traverse(jsonData)
    }
}

function traverse(object){
    for(var i in object){
        console.log(object[i])
        console.log(object[i] != "")
        if(object[i] != ""){
            for( j in object[i]){
            pushErrToMaps(object[i][j])
            }
        }
    }
    emptyErrorList()
    showErr(errMap)
}

var errMap ={}
function pushErrToMaps(object){
    for (var i in object) {
      errMap[i] = errMap[i] || [];
      errMap[i].push(object[i]);
    }
}

function showErr(map){

    var errors = document.getElementById("error-msgs");
    for (const [key, value] of Object.entries(map)) {
            var rowNo = parseInt(key)+1
            let row = document.createElement("div");
            row.setAttribute("class", "row");
            row.innerHTML = `<br>
            <div class="col s10 offset-s1">
          <div class="card-panel" id="${key}">
              <h3>Errors at Row Number: ${rowNo}</h3>
          </div>
      </div>`;
            errors.appendChild(row)
            value.forEach(element => {
                let p = document.createElement("p")
                p.setAttribute("id", "error")
                p.innerText = `    -  ${element}`
                let parent = document.getElementById(`${key}`)
                parent.appendChild(p)
            });
        }

        if(Object.keys(map).length === 0){
        errors.innerHTML = `<div class="success-msg">
                              <h1>No error in your uploaded CSV file</h1>
                            </div>`;
         setTimeout(function(){
               errors.removeChild(errors.firstChild);
              },80000);
        }
        errMap = {}
        payload = []

}


function emptyErrorList(){
    const el = document.getElementById("error-msgs");
      while (el.firstChild) {
      el.removeChild(el.firstChild)
      };
}


function Buttontoggle()
{
  var t = document.getElementById("myButton");
  if(t.value=="YES"){
      t.value="NO";}
  else if(t.value=="NO"){
      t.value="YES";}
}


function resetForm(){
    document.getElementById("myform").reset()
}

