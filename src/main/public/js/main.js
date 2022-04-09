function csvReader() {
    var csv = document.getElementById("csv_id").files[0];
    const reader = new FileReader();
    reader.onload = async function (event) {
        csv = event.target.result
        var lines = csv.toString().split("\n");
        console.log(lines)
        var result = [];
        var headers = lines[0].split(",");
        for (var i = 1; i < lines.length; i++) {
            var obj = {};
            var currentline = lines[i].split(",");
            for (var j = 0; j < headers.length; j++) {
                obj[headers[j]] = currentline[j];
            }
            result.push(obj);
        }

       const response = await fetch('csv', {
           method: 'POST',
           body: JSON.stringify(result)
       })
       if (response.status === 200) {
                  var jsonData =  await response.json();
                  console.log(jsonData)
       }
          /*response.forEach(element => {
            const node = document.createElement("li");
            const textnode = document.createTextNode(`Line Number ${Object.keys(element)[0]}: ${element[Object.keys(element)[0]]}`);
            node.appendChild(textnode);
            document.getElementById("error_msgs_list").appendChild(node)
          });*/
        console.log(response)
    };
    reader.readAsText(csv);
}
