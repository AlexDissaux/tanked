//-----svg-----//



const header = document.getElementById("bar");
const nav = document.getElementById("nav");

const main = document.getElementById("dessin");
const nameNode = document.getElementById("name");
const width = 1350;
const height = 675;

let viewbox = [0, 0, width, height];
let id = null;

let svg = d3.select("#svgcontainder").append("svg")
    .attr("width", width)
    .attr("height", height)
    .attr("preserveAspectRatio", "xMidYMid meet")
    .attr("viewBox", viewbox.reduce(function(total, current){ return total + " " + current; }))
    .classed("svg-content-responsive", true)
    .on("mousedown", startAction)
    .on("mouseup", endAction);


function copyAttr(obj, attrs) {
    let newObj = {};
    attrs.forEach(function(key) {
        if (key == "stroke-width"){
            newObj["strokeWidth"] = obj[key].nodeValue;
        } else {
            newObj[key] = obj[key].nodeValue;
        }
    });
    return newObj;
}

function saveSVG(){
    let map = {};
    map.name = nameNode.value;

    map.lines = svg.selectAll("line")[0].map(x => copyAttr(x.attributes, ['x1', 'x2', 'y1', 'y2', 'stroke-width', 'stroke']));
    map.circles = svg.selectAll("circle")[0].map(x => copyAttr(x.attributes, ['cx', 'cy', 'r', 'fill']));
    map.rectangles = svg.selectAll("rect")[0].map(x => copyAttr(x.attributes, ['x', 'y', 'width', 'height', 'fill']));
    if (id != null){ map.id = id; }

    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if ( xhr.readyState === 4 ) {
            id = parseInt(xhr.response);
        }
    };
    xhr.open("POST", "/save-map");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify(map));

}


function resetSVG(){
    svg.selectAll('*').remove();
    id = null;
}

function loadMap(map){
    if (map != null) {
        console.log(map);
        id = map.id;
        map.rectangles.forEach(drawRectangleSvgObj);
        map.circles.forEach(drawCircleSvgObj);
        map.lines.forEach(drawLineSvgObj);
    }
}

//-----zoom-----//

window.onresize = autoScale;
window.onload = () => {autoScale(), loadMap(map)};

function autoScale(){
    let svgWidth = window.innerWidth;
    let svgHeight = window.innerHeight - header.clientHeight - nav.clientHeight;
    svgWidth -= 3;
    svgHeight -= 3;
    let scaledWidth = (width/height) * svgHeight;
    let scaledHeight = (height/width) * svgWidth;
    if (scaledHeight > svgHeight){
        svgWidth = scaledWidth;
    } else {
        svgHeight = scaledHeight;
    }
    svg.attr("width", svgWidth).attr("height",svgHeight);
}



//-----utility-----//

const thick = document.getElementById("thick");
const thickbar = document.getElementById("thickbar");
const color = document.getElementById("color");
const mode = document.getElementById("mode");
let modeValue = 0;

function changeMode(i, div){
    modeValue = i;
    console.log(modeValue);
    mode.innerHTML = div.innerHTML;
    if (modeValue == 0){
        thickbar.hidden = false;
    } else {
        thickbar.hidden = true;
    }
}

function distCartesian(x1, y1, x2, y2){
    return Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
}

function getMouseXY(){
    return d3.mouse(svg[0][0]);
}

//-----interaction-----//

let x = 0;
let y = 0;
let silouhette = null;

function startAction(){
    switch(modeValue){
        case 0:
            startLine(this);
            break;
        case 1:
            startRectangle(this);
            break;
        case 2:
            startCircle(this);
            break;
    }
}

function endAction(){
    if (silouhette !== null){
        silouhette.remove();
    }
    switch(modeValue){
        case 0:
            endLine(this)
            break;
        case 1:
            endRectangle(this);
            break;
        case 2:
            endCircle(this);
            break;
    }
}

function startLine(){
    let point = getMouseXY();
    x = point[0];
    y = point[1];
    svg.on("mousemove", endSilouhetteLine);
}

function endLine(){
    let point = getMouseXY();
    let x2 = point[0];
    let y2 = point[1];
    svg.on('mousemove',null);
    drawLineSvg(x, y, x2, y2, parseInt(thick.value), color.value);
}

function startRectangle(){
    let point = getMouseXY();
    x = point[0];
    y = point[1];
    svg.on("mousemove",endSilouhetteRectangle);
}

function endRectangle(){
    let point = getMouseXY();
    let x2 = point[0];
    let y2 = point[1];
    let xRect = (x2 < x) ? x2 : x;
    let yRect = (y2 < y) ? y2 : y;
    svg.on('mousemove',null);
    drawRectangleSvg(xRect, yRect, Math.abs(x - x2), Math.abs(y - y2), color.value);
}

function startCircle(){
    let point = getMouseXY();
    x = point[0];
    y = point[1];
    svg.on("mousemove",endSilouhetteCircle);
}

function endCircle(){
    let point = getMouseXY();
    let x2 = point[0];
    let y2 = point[1];
    svg.on('mousemove',null);
    drawCircleSvg(x, y, distCartesian(x,y,x2,y2), color.value);
}

function endSilouhetteLine(){
    if (silouhette !== null){
        silouhette.remove();
    }
    let point = getMouseXY();
    let x2 = point[0];
    let y2 = point[1];
    silouhette = drawLineSvg(x,y,x2,y2,parseInt(thick.value), color.value);
}

function endSilouhetteRectangle(){
    if (silouhette !== null){
        silouhette.remove();
    }
    let point = getMouseXY();
    let x2 = point[0];
    let y2 = point[1];
    let xRect = (x2 < x) ? x2 : x;
    let yRect = (y2 < y) ? y2 : y;
    silouhette = drawRectangleSvg(xRect, yRect, Math.abs(x - x2), Math.abs(y - y2), color.value);
}

function endSilouhetteCircle(){
    if (silouhette !== null){
        silouhette.remove();
    }
    let point = getMouseXY();
    let x2 = point[0];
    let y2 = point[1];
    silouhette =  drawCircleSvg(x, y, distCartesian(x,y,x2,y2), color.value);
}

function deleteForm(e = this){
    if (modeValue == 3){
        e.remove();
    }
}

//-----drawing-----//

function drawLineSvg(x1, y1, x2, y2, thick, color){
    return svg.append("line")
        .attr("class", "line")
        .attr("x1", x1)
        .attr("x2", x2)
        .attr("y1", y1)
        .attr("y2", y2)
        .attr("stroke-width", thick)
        .attr("stroke", color)
        .on("click", deleteForm);
}

function drawRectangleSvg(x1, y1, w, h, color){
    return svg.append("rect")
        .attr("class", "line")
        .attr("x", x1)
        .attr("y", y1)
        .attr("width", w)
        .attr("height", h)
        .attr("fill", color)
        .on("click", deleteForm);
}

function drawCircleSvg(x1, y1, r, color){
    return svg.append("circle")
        .attr("class", "line")
        .attr("cx", x1)
        .attr("cy", y1)
        .attr("r", r)
        .attr("fill", color)
        .on("click", deleteForm);
}

function drawLineSvgObj(l){
    drawLineSvg(l.x1, l.y1, l.x2, l.y2, l.strokeWidth, l.stroke);
}

function drawRectangleSvgObj(r){
    drawRectangleSvg(r.x, r.y, r.width, r.height, r.fill);
}

function drawCircleSvgObj(c){
    drawCircleSvg(c.cx, c.cy, c.r, c.fill);
}

