document.getElementById("weatherSubmit").addEventListener("click", function(event) {
  event.preventDefault();
  const value = document.getElementById("weatherInput").value;
  if (value === "")
    return;
  console.log(value);
  const url = "http://api.openweathermap.org/data/2.5/weather?q=" + value + ",US&units=imperial" + "&APPID=77f80b7a65e640d2be6cc5a9bee1cce4";
  /* global fetch*/
  fetch(url)
    .then(function(response) {
      return response.json();
    }).then(function(json) {	
        let results = "";
        results += '<div class="weatherResults">';
        results += '<h2>Weather in ' + json.name + "</h2>";
        for (let i=0; i < json.weather.length; i++) {
            results += '<img src="http://openweathermap.org/img/w/' + json.weather[i].icon + '.png"/>';
        }
        results += "<h2>" + json.main.temp + " &deg;F</h2>"
        results += "<p>"
        for (let i=0; i < json.weather.length; i++) {
            results += json.weather[i].description
            if (i !== json.weather.length - 1) results += ", "
        }
        results += "</p>";
        results +="</div>";
        document.getElementById("weatherResults").innerHTML = results;
    });
    
    const url2 = "http://api.openweathermap.org/data/2.5/forecast?q=" + value + ", US&units=imperial" + "&APPID=77f80b7a65e640d2be6cc5a9bee1cce4";
    fetch(url2)
    .then(function(response) {
        return response.json();
    }).then(function(json) {
        let forecast = "";
        let currentDay = json.list[0].dt_txt.substring(0,10);
        forecast += '<div class="singleDay">';
        let makeNewDay = false;
        for (let i=0; i < json.list.length; i++) {
            let tempDay = json.list[i].dt_txt.substring(0,10);
            let closeDiv = true;
            if (currentDay == tempDay) {
                closeDiv = false;
            }
            if (closeDiv) {
                forecast += '</div>';
                currentDay = tempDay;
                makeNewDay = true;
            }
            if (makeNewDay) {
                forecast += '<div class="singleDay">';
                makeNewDay = false;
            }
            forecast += '<div class="forecastTile">';
            forecast += "<h2>" + moment(json.list[i].dt_txt).format('MMMM Do YYYY, h:mm:ss a') + "</h2>";
            forecast += "<p>Temperature: " + json.list[i].main.temp + "°</p>";
            forecast += '<img src="http://openweathermap.org/img/w/' + json.list[i].weather[0].icon + '.png"/>'
            forecast += "</div>";
        }
        document.getElementById("forecastResults").innerHTML = forecast;
    });
});
