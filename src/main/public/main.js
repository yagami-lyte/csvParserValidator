var payload=[]
var result = []

function csvReader() {
    var csv = document.getElementById("csv_id").files[0];
    const reader = new FileReader();
    reader.onload = async function (event) {
        csv = event.target.result
        var lines = csv.toString().split("\n");
        document.getElementById("fields").innerHTML = lines[0];
        console.log(lines)
        console.log(lines[0])
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
}

function showError(key,value) {
    console.log(key + " : "+ typeof value);
    if(value != "" &&  (typeof value == 'string') ){
    const node = document.createElement("li");
    const textNode = document.createTextNode(`Line No ${key} has error : ${value}`);
    node.appendChild(textNode);
    document.getElementById("error_msgs_list").appendChild(node)
    }
}

function traverse(object,func) {
    for (var i in object) {
        func.apply(this,[i,object[i]]);
        if (object[i] !== null && typeof(object[i])=="object") {
            traverse(object[i],func);
        }
    }
}

async function displayErrors(){
 const response = await fetch('csv', {
            method: 'POST',
            body: JSON.stringify(result)
        })

        if (response.status === 200) {
            var jsonData =  await response.json();
            console.log(jsonData)
            traverse(jsonData,showError)
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
    jsonObj["dependentOn"] = dependentOn.value
    jsonObj["dependentValue"] = dependentValue.value
    payload.push(jsonObj)
    console.log(payload)
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
