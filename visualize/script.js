document.querySelector('#search').addEventListener("submit", function(event) {
  event.preventDefault();

  var board_state = document.getElementById('board_state').value
  var request = new XMLHttpRequest();

  request.open("POST", "http://localhost:8000/query.php");
  request.setRequestHeader('Content-Type', 'application/json');

  request.onloadend = function(e) {
    document.getElementById('chart').innerHTML = "";
    render(null, JSON.parse(request.responseText));
  }

  request.send(JSON.stringify({
    "board_state": board_state
  }));
});

var margin = {top: 20, right: 20, bottom: 70, left: 70},
    width = 700 - margin.left - margin.right,
    height = 300 - margin.top - margin.bottom;

// set the ranges
var x = d3.scaleBand()
          .range([0, width])
          .padding(0.1);
var y = d3.scaleLinear().range([height, 0]);


function render(error, data) {
  // add the SVG element
  var svg = d3.select("#chart").append("svg")
      .attr("width", width + margin.left + margin.right)
      .attr("height", height + margin.top + margin.bottom)
    .append("g")
      .attr("transform",
            "translate(" + margin.left + "," + margin.top + ")")

  if(error) throw error;
  console.log(data);

  // format data
  data.forEach(function(d) {
    //d.state = d.state;
    d.move = d.move;
    d.count = +d.count;
    return d;
  });

  // scale the range of data in the domains
  x.domain(data.map(function(d) { return d.move; }));
  y.domain([0, d3.max(data, function(d) { return d.count; })]);

  svg.append("g")
      .attr("transform", "translate(0," + height + ")")
      .call(d3.axisBottom(x));

  svg.selectAll("g")
  .exit().remove();

  // text label for the x axis
  svg.append("text")
      .attr("transform",
            "translate(" + (width/2) + " ," +
                           (height + margin.top + 20) + ")")
      .style("text-anchor", "middle")
      .text("Move");

  // text label for the y axis
  svg.append("text")
      .attr("transform", "rotate(-90)")
      .attr("y", 0 - margin.left)
      .attr("x",0 - (height / 2))
      .attr("dy", "1em")
      .style("text-anchor", "middle")
      .text("Frequency");

  svg.append("g")
      .attr("class", "y axis")
      .call(d3.axisLeft(y).ticks())
    .append("text")
      .attr("transform", "rotate(-90)")
      .attr("y", 5)
      .attr("dy", ".71em")
      .style("text-anchor", "end")
      .text("Frequency");

  svg.selectAll("bar")
  .data(data)
  .enter().append("rect")
    .attr("class", "bar")
    .attr("x", function(d) { return x(d.move); })
    .attr("width", x.bandwidth())
    .attr("y", function(d) { return y(d.count); })
    .attr("height", function(d) { return height - y(d.count); });

};
