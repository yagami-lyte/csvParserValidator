var payload = []
var result = []
var fields = []


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
        localStorage.setItem('Csv-FileHeaders' , JSON.stringify(headers))

        for (var i = 1; i < lines.length-1; i++) {
            var obj = {};
            var currentLine = lines[i].split(",");
            for (var j = 0; j < headers.length; j++) {
                obj[headers[j]] = currentLine[j].replaceAll('"', '');
            }
            result.push(obj);
        }
        localStorage.setItem("CSV-File" , JSON.stringify(result))
    };
    reader.readAsText(csv);

    window.location.href = '/config.html'
}


function RestrictFirstZero(e) {
            if (e.srcElement.value.length == 0 && e.which == 48 && e.which == 109) {
                e.preventDefault();
                return false;
            }
        };

        function PreventFirstZero(event) {
            if (event.srcElement.value.charAt(0) == '0') {
                event.srcElement.value = event.srcElement.value.slice(1);
            }
        };

