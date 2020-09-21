let w = window,
    d = document,
    e = d.documentElement,
    g = d.getElementsByTagName('body')[0],
    x = w.innerWidth || e.clientWidth || g.clientWidth,
    y = w.innerHeight|| e.clientHeight|| g.clientHeight;

let scale = 1;

function autoScale(){
    let canvasWidth = window.innerWidth;
    let canvasHeight = window.innerHeight;
    canvasWidth -= 3;
    canvasHeight -= 3;
    let scaledWidth = (MAP_WIDTH/MAP_HEIGHT) * canvasHeight;
    let scaledHeight = (MAP_HEIGHT/MAP_WIDTH) * canvasWidth;
    if (scaledHeight > canvasHeight){
        canvasWidth = scaledWidth;
    } else {
        canvasHeight = scaledHeight;
    }
    canvas.width = canvasWidth;
    canvas.height = canvasHeight
    canvasMap.width = canvasWidth;
    canvasMap.height = canvasHeight;
    scale = canvasWidth / MAP_WIDTH;
    if (map != null){
        resizeMap(map);
    }
}

function resizeMap(map){
    ctxMap.setTransform(1, 0, 0, 1, 0, 0);
    eraseMap();
    ctxMap.scale(scale, scale);
    drawMap(map);
    ctxMap.scale(1/scale, 1/scale);
}

let width = x,
    height = y,
    pi = Math.PI;

const canvasMap = document.getElementById("canvascontainder").appendChild(document.createElement('canvas'));
canvasMap.width = width;
canvasMap.height = height;
const ctxMap = canvasMap.getContext('2d');
const canvas = document.getElementById("canvascontainder").appendChild(document.createElement('canvas'));
canvas.width = width;
canvas.height = height;
const ctx = canvas.getContext('2d');



//------utility-----//

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

function toRadian(angle){
    return angle * pi/180;
}

function getAngleMouse(tankCX, tankCY, mouseX, mouseY){
    return pi/2 + Math.atan2(tankCX - mouseX, tankCY - mouseY);
}

//-----shot-----//

function drawShot(shot){
    ctx.save();
    ctx.beginPath();
    ctx.fillStyle = tanks[shot.owner];
    ctx.arc(shot.x, shot.y, RADIUS, 0, 2 * pi);
    ctx.fill();
    ctx.closePath();
    ctx.restore();
}

function drawShots(){
    ctx.save();
    Object.keys(shots).map(function(objectKey, index) {
        let value = shots[objectKey];
        drawShot(value, objectKey);
    });
    ctx.restore();
}

//-----canon-----//

function drawOuterCanon(tankX, tankY, angle){
    let xfin = tankX + (Math.cos(angle) * CANONLENGTH);
    let yfin = tankY - (Math.sin(angle) * CANONLENGTH);
    ctx.beginPath();
    ctx.lineWidth=CANONWIDTH;
    ctx.strokeStyle = 'black';
    ctx.moveTo(tankX, tankY);
    ctx.lineTo(xfin, yfin);
    ctx.stroke();
    ctx.closePath();
}

function drawInnerCanon(tankX, tankY, angle, color){
    xfin = tankX + (Math.cos(angle) * (CANONLENGTH-3));
    yfin = tankY - (Math.sin(angle) * (CANONLENGTH-3));
    ctx.beginPath();
    ctx.lineWidth=CANONWIDTH-3;
    ctx.strokeStyle = color;
    ctx.moveTo(tankX, tankY);
    ctx.lineTo(xfin, yfin);
    ctx.stroke();
    ctx.closePath();
}

function drawCanon(tankX, tankY, color, angle=0){
    ctx.save();
    drawOuterCanon(tankX, tankY, angle)
    drawInnerCanon(tankX, tankY, angle, color);
    ctx.restore();
}


function debugDisplay(tankX, tankY){
    ctx.save();
    ctx.beginPath();
    ctx.arc(tankX, tankY, Math.sqrt(COLLISION_DIST_SQUARED), 0, 2 * pi);
    ctx.fillStyle = "yellow";
    ctx.fill();
    ctx.closePath();
    ctx.restore();
}

function rotateCanvas(x, y , angle){
    ctx.translate(x, y);
    ctx.rotate(toRadian(angle));
    ctx.translate(-x, -y);
}


function drawTankBody(tanki, tankX, tankY){
    ctx.save();
    ctx.beginPath();  // path commands must begin with beginPath
    rotateCanvas(tankX, tankY, tanki.orientation);
    ctx.strokeStyle = "black";
    ctx.fillStyle = tanki.color;
    ctx.rect(tanki.x, tanki.y, TANKWIDTH, TANKHEIGHT);
    ctx.fill();
    ctx.stroke();
    ctx.closePath();
    ctx.restore();
}

function drawTankArrow(tanki, tankX, tankY){
    ctx.save();
    ctx.beginPath();
    rotateCanvas(tankX, tankY, tanki.orientation);
    ctx.fillStyle = 'blue';
    ctx.moveTo(tanki.x, tanki.y);
    ctx.lineTo(tanki.x + TANKWIDTH, tanki.y);
    ctx.lineTo(tanki.x + TANKWIDTH/2, tanki.y + 10);
    ctx.fill();
    ctx.restore();
}

function drawDecoration(tanki, tankX, tankY){
    ctx.save();
    ctx.beginPath();
    ctx.fillStyle = tanki.color;
    ctx.arc(tankX, tankY, RADIUS_TANK, 0, 2 * pi);
    ctx.stroke();
    ctx.fill();
    ctx.closePath();
    ctx.restore();
}

//------tanks------//
function drawTank(tanki, id) {
    let tankX = (tanki.x + TANKWIDTH / 2);
    let tankY = (tanki.y + TANKHEIGHT / 2);
    let angle = tanki.canonOrientation;
    if (DEBUG_MODE) { debugDisplay(tankX, tankY) }
    drawTankBody(tanki, tankX, tankY);
    drawTankArrow(tanki, tankX, tankY);
    drawDecoration(tanki, tankX, tankY);
    drawCanon(tankX, tankY, tanki.color, angle);
    ctx.textAlign="center";
    ctx.fillText(tanki.tankName,tankX,tanki.y - 15);
}

function drawTanks(){
    ctx.save();
    Object.keys(tanks).map(function(objectKey, index) {
        let value = tanks[objectKey];
        drawTank(value, objectKey);
    });
    ctx.restore();
}

function erase(){
    ctx.clearRect(0, 0, canvas.width, canvas.height);
}

function eraseMap(){
    ctxMap.clearRect(0, 0, canvasMap.width, canvasMap.height);
}

function drawBoom(tank) {
    let x = (tank.x + TANKWIDTH / 2);
    let y = (tank.y + TANKHEIGHT / 2);
    ctx.save();
    ctx.beginPath();
    ctx.arc(x, y, Math.sqrt(COLLISION_TANK_DIST_SQUARED), 0, 2 * pi);
    ctx.fillStyle = "red";
    ctx.fill();
    ctx.closePath();
    ctx.restore();
}



function drawRectangleCanvas(x, y, width, height, fill){
    ctxMap.save();
    ctxMap.beginPath();
    ctxMap.fillStyle = fill;
    ctxMap.rect(x, y, width, height);
    ctxMap.fill();
    ctxMap.closePath();
    ctxMap.restore();
}

function drawCircleCanvas(cx, cy, r, fill){
    ctxMap.save();
    ctxMap.beginPath();
    ctxMap.fillStyle = fill;
    ctxMap.arc(cx, cy, r, 0, 2 * pi);
    ctxMap.fill();
    ctxMap.closePath();
    ctxMap.restore();
}

function drawLineCanvas(x1, x2, y1, y2, width, stroke){
    ctxMap.save();
    ctxMap.beginPath();
    ctxMap.strokeStyle = stroke;
    ctxMap.lineWidth = width;
    ctxMap.moveTo(x1, y1);
    ctxMap.lineTo(x2, y2);
    ctxMap.stroke();
    ctxMap.closePath();
    ctxMap.restore();
}

function drawRectangleCanvasObj(r){
    drawRectangleCanvas(r.x, r.y, r.width, r.height, r.fill);
}

function drawCircleCanvasObj(c){
    drawCircleCanvas(c.cx, c.cy, c.r, c.fill);
}

function drawLineCanvasObj(l){
    drawLineCanvas(l.x1, l.x2, l.y1, l.y2, l.strokeWidth, l.stroke);
}

function drawMap(map){
    map.rectangles.forEach(drawRectangleCanvasObj);
    map.circles.forEach(drawCircleCanvasObj);
    map.lines.forEach(drawLineCanvasObj);
}