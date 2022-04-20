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
        console.log(lines)
        console.log(lines[0])
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
                       <h4> ${lines[j]}</h4>

                     <div style="display:flex; ">
                         <div class="input-field col s4"
                          style="display:flex;  background: transparent;width: 400px; height: 40px;padding: 1em;margin-bottom: 2em;border-left: 0.5px solid black;border-top: 1px solid black;border-radius: 5000px;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">
                            <label for="type">Type</label>
                            <select placeholder="Choose Type" data-cy="type" id="type${lines[j]}" onchange="showDateTimeOption(this.value,'dateDiv${lines[j]}','dateFormats${lines[j]}' , 'date${lines[j]}','timeDiv${lines[j]}','timeFormats${lines[j]}','time${lines[j]}');">
                               <option value="">Choose Type of Data</option>
                                <option value="Number">Number</option>
                                <option value="AlphaNumeric">AlphaNumeric</option>
                                <option value="Alphabets">Alphabets</option>
                                <option value="Date Time">Date Time</option>
                            </select>
                         </div>



                         <div id = "dateDiv${lines[j]}" class="input-field  col s4"
                                                style="display:none;  background: transparent;width: 300px; height: 40px;margin-right: 3% ;margin-left:3%;padding: 1em;margin-bottom: 2em;border-left: 0.5px solid black;border-top: 1px solid black;border-radius: 5000px;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">
                            <label for="date" id="dateFormats${lines[j]}" style='display:none;'>Date Format</label>
                            <select placeholder="Choose date format"  name="date" id='date${lines[j]}' style='display:none;'>
                                 <option>"Choose Date Format"</option>
<<<<<<< Updated upstream
                                 <option value="MM-dd-yyyy">MM-dd-yyyy</option>
                                 <option value="MMMM dd, yy">MMMM dd, yy</option>
                                <option value="yy/MM/dd">yy/MM/dd</option>
                                <option value="dd/MM/yy">dd/MM/yy</option>
                                <option value="MMM dd, yyyy">MMM dd, yyyy</option>
                                <option value="MMM dd yyyy">MMM dd yyyy</option>
                                <option value="MMM dd yyyy">MMM dd yyyy</option>
                               <option value="yyyy-MM-dd'T'">yyyy-MM-dd'T'</option>
=======
                                 <option value="MM-dd-yyyy">MM-DD-YYYY</option>
                                 <option value="dd-MM-yyyy">DD-MM-YYYY</option>
                                 <option value="dd/MM/yyyy, yy">DD/MM/YYYY</option>
                                <option value="yy/MM/dd">YY/MM/DD</option>
                                <option value="yyyy/MM/dd">YYYY/MM/DD</option>
                                <option value="M/d/yyy">M/D/yyy</option>
                                <option value="d/M/yyyy">D/M/YYYY</option>
                                <option value="yyyy/M/dd">YYYY/M/DD</option>
                                <option value="ddMMyYYy">DDMMYYYY</option>
                               <option value="yyyy-MM-dd'T'">YYYY-MM-DD'T'</option>
>>>>>>> Stashed changes
                            </select>
                         </div>


                     <div id = "timeDiv${lines[j]}" class="input-field  col s4" 
                                          style="display:none;  background: transparent;width: 300px; height: 40px;margin-right: 3% ;margin-left:3%;padding: 1em;margin-bottom: 2em;border-left: 0.5px solid black;border-top: 1px solid black;border-radius: 5000px;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">

                       <label for="time" style='display:none;' id="timeFormats${lines[j]}">Time Format</label>
                           <select placeholder="Choose time format"  name="time" id='time${lines[j]}' style='display:none;'>
                                <option>"Choose Time Format"</option>
                                <option value="hh:mm:ss">HH:MM:SS</option>
                                <option value="HH:mm:ss zzz">HH:MM:SS ZZZ</option>
                                <option value="HH:mm:ss.SSSZ">HH:MM:SS.SSSZ</option>
                                <option value="HH:mm:ss.SSS'Z'">HH:mm:ss.SSS'Z'</option>
                           </select>
                      </div>


                         <div class="input-field  col s4"
                                                 style="display:flex;  background: transparent;width: 300px;margin-right: 3% ;margin-left:3%; height: 40px;padding: 1em;margin-bottom: 2em;border-left: 0.5px solid black;border-top: 1px solid black;border-radius: 5000px;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">

                            <label for="fixed-len">Length</label>
                            <input placeholder="Enter length"  type="number" id="fixed-len${lines[j]}" data-cy="fixed-len">
                         </div>
                       </div>

                         <div style="display:flex; ">

                         <div class="input-field  col s4"
                                                  style="display:flex;  background: transparent;width: 500px;margin-right: 3% ;margin-left:3%; height: 40px;padding: 1em;margin-bottom: 2em;border-left: 0.5px solid black;border-top: 1px solid black;border-radius: 5000px;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">

                             <label for="text_file_id">Values</label>

                            <input class="custom-file-input" type="file" name="text-file" onchange="onChangeHandler(event,'${lines[j]}')" data-cy="text_file_id" id="text_file_id${lines[j]}" accept=".txt">

                             <h5 style="font-size:20px;"> or </h5>
                             <textarea placeholder="Type Allowed values in new lines" style="border-radius: 15px; padding-left:13px; " id="textArea${lines[j]}"></textarea>
                         </div>


                         <div class="input-field  col s4"
                                                  style="display:flex;  background: transparent;width: 300px;margin-right: 3% ;margin-left:3%; height: 40px;padding: 1em;margin-bottom: 2em;border-left: 0.5px solid black;border-top: 1px solid black;border-radius: 5000px;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">

                             <label for="dependent">Dependent On</label>
                             <select placeholder="Choose dependant-field" name="dependentField" style="display: block;" id="dependent${lines[j]}">
                                 <option value="">Choose DateTime Pattern</option>
                                     ${lines.map((element) => {
                                         return `<option value='${element}'>${element}</option>`;
                                     })}
                             </select>
                         </div>

                         <div class="input-field  col s4"
                                                  style="display:flex;  background: transparent;width: 300px;margin-right: 3% ;margin-left:3%; height: 40px;padding: 1em;margin-bottom: 2em;border-left: 0.5px solid black;border-top: 1px solid black;border-radius: 5000px;backdrop-filter: blur(5px); box-shadow: 4px 4px 60px rgba(0,0,0,0.2);color: #fff;   font-family: Montserrat, sans-serif;ont-weight: 500;transition: all 0.2s ease-in-out;     text-shadow: 2px 2px 4px rgba(0,0,0,0.2);flex-direction: row; justify-content: center; align-items: center">

                             <label for="dep-val">Dependent Value</label>
                             <input type="text" id="dep-val${lines[j]}" data-cy="dep-val">
                         </div>
                    </div>
                     <br> </br> <br>
                      </div>
                      <br>
                 `
    document.getElementById("myform").appendChild(row)
}
console.log(fieldCount)
}

function showDateTimeOption(value , dateDivID,dateFormatId, dateId , timeDivID,timeFormatId,timeId){
    var dateDivIDElement = document.getElementById(dateDivID);
    var dateFormatElement = document.getElementById(dateFormatId);
    var dateIdFormatElement = document.getElementById(dateId);
    var timeDivIDElement = document.getElementById(timeDivID);
    var timeFormatElement = document.getElementById(timeFormatId);
    var timeIdFormatElement = document.getElementById(timeId);
    if(value === 'Date Time'){
        dateDivIDElement.style.display='flex';
        dateFormatElement.style.display='block';
        dateIdFormatElement.style.display='block';
        timeDivIDElement.style.display='flex';
        timeFormatElement.style.display='block';
        timeIdFormatElement.style.display='block';
    }
    else{
        dateDivIDElement.style.display='none';
        dateFormatElement.style.display='none';
        dateIdFormatElement.style.display='none';
        timeDivIDElement.style.display='none';
        timeFormatElement.style.display='none';
        timeIdFormatElement.style.display='none';
    }
}


function onChangeHandler(event, fieldName){
console.log(fieldName)
    var value = document.getElementById(`text_file_id${fieldName}`).files[0];
    if (value != null){
        console.log('dj');
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
        if (type.value === 'Date Time'){
            var dateFormat = document.getElementById(`date${fields[0][j]}`)
            var timeFormat = document.getElementById(`time${fields[0][j]}`)
            var dateTimeFormat = dateFormat.value.toString() + timeFormat.value.toString()
            jsonObj["datetime"] = dateTimeFormat
        }
        console.log(field)
            jsonObj["fieldName"] = field
            jsonObj["type"] = type.value



            if (value != null){
                jsonObj["values"] =JSON.parse(localStorage.getItem(field))
                console.log(localStorage.getItem(field))
            }



            console.log(typedValues.value != '')
            if(typedValues.value != '' )
            {
            console.log(typedValues.value)
            jsonObj["values"] = typedValues.value.split('\n')
            }
            jsonObj["length"] = fixed_len.value

            jsonObj["dependentOn"] = dependentOn.value
            jsonObj["dependentValue"] = dependentValue.value
            payload.push(jsonObj)
            }
            console.log(payload)
    }

var errMap ={}
function pushErrToMaps(object){
    for (var i in object) {
      console.log(`${i}: ${object[i]}`);
      errMap[i] = errMap[i] || [];
      errMap[i].push(object[i]);
    }
}

function showErr(map){
    var errors = document.getElementById("error-msgs");
    for (const [key, value] of Object.entries(map)) {
            let row = document.createElement("div");
            row.setAttribute("class", "row");
            row.innerHTML = `<br>
            <div class="col s10 offset-s1">
          <div class="card-panel" id="${key}" style="color:blue">
              <h3>Errors at Row Number: ${key}</h3>
          </div>
      </div>`;
            errors.appendChild(row)
            value.forEach(element => {
                let p = document.createElement("p")
                p.setAttribute("id", "error")
                p.setAttribute("style", "font-weight: bold; color:red;")
                p.innerText = `    -  ${element}`
                let parent = document.getElementById(`${key}`)
                parent.appendChild(p)
            });
        }
}

function traverse(object){
    for(var i in object){
        console.log(object[i])
        console.log(object[i] != "")
        if(object[i] != ""){
            for( j in object[i]){
            console.log(object[i][j])
            pushErrToMaps(object[i][j])
            }
        }
    }
    console.log(errMap)
    showErr(errMap)
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


function resetForm(){
    document.getElementById("myform").reset()
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

