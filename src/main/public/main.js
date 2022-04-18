var payload=[]
var result = []

function createDropDownForDependant(lines){
    for (var i = 1, j = 0; i <= lines.length; i++,j++){
        var select = document.getElementById("dependent");
        opt = document.createElement("option");
        opt.value = lines[j];
        opt.textContent = lines[j];
        select.appendChild(opt);
    }
}

function createDropDownForFields(lines){
    for (var i = 1, j = 0; i <= lines.length; i++,j++){
        var selectField = document.getElementById("field");
        opt = document.createElement("option");
        opt.value = lines[j];
        opt.textContent = lines[j];
        selectField.appendChild(opt);
    }
}

function remove() {
    var addedField = document.getElementById("field");
    addedField.remove(addedField.selectedIndex);
}

function csvReader() {
    var csv = document.getElementById("csv_id").files[0];
    const reader = new FileReader();
    reader.onload = async function (event) {
        csv = event.target.result
        var lines = csv.toString().split("\n");
        document.getElementById("field").innerHTML = lines[0];
        console.log(lines)
        console.log(lines[0])
        var arr = lines[0].split(",")
        createDropDownForDependant(arr)
        createDropDownForFields(arr)
        var headers = lines[0].split(",");
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
    alert("CSV uploaded successfully!")
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


function addDataToJson() {
    let jsonObj = {}
    var field = document.getElementById("field")
    var type = document.getElementById("type")
    var value = document.getElementById("text_file_id").files[0]
    var fixed_len = document.getElementById("fixed-len")
    var dependentOn = document.getElementById("dependent")
    var dependentValue = document.getElementById("dep-val")
    var dateTimeFormat = document.getElementById("datetime")
    jsonObj["fieldName"] = field.value
    jsonObj["type"] = type.value
    let reader = new FileReader();
    if (value != null){
        reader.addEventListener('load', function(e) {
            let text = e.target.result
            jsonObj["values"] = text.split('\n')
        });
        reader.readAsText(value)
    }
    jsonObj["length"] = fixed_len.value
    jsonObj["datetime"] = dateTimeFormat.value
    if(type.value != "Date Time"){
        jsonObj["datetime"] = ""
    }
    jsonObj["dependentOn"] = dependentOn.value
    jsonObj["dependentValue"] = dependentValue.value
    payload.push(jsonObj)
    console.log(payload)
    remove()
    resetForm()
    alert("Field configuration added successfully!")
}

function resetForm(){
    document.getElementById("myform").reset()
}

async function sendConfigData(){
    alert("Successfully added configuration details for csv!")
    var resp = await fetch('add-meta-data', {
        method: 'POST',
        body: JSON.stringify(payload)
    })

    if (resp.status === 200) {
        var jsonData = await resp.json();
        console.log(jsonData)
    }
}

function checkDateFormat(value){
    var element=document.getElementById('datetime');
    var elementForFormats = document.getElementById('formats');
    if(value == 'Date Time'){
        element.style.display='block';
        elementForFormats.style.display='block';
    }
    else{
        element.style.display='none';
        elementForFormats.style.display='none';
    }
}