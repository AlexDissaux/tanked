let leftPressed = false;
let rightPressed = false;
let upPressed = false;
let downPressed = false;

function registerEvents() {
    window.onclick = shoot;
    window.onkeydown = (event) => triggerEvent(event, true);
    window.onkeyup = (event) => triggerEvent(event, false);
    window.onmousemove = updateXYMouse;
    window.onresize = autoScale;
    console.log("Event registered.");
}

function updateXYMouse(e){
    let tankX = (myTank.x + TANKWIDTH / 2);
    let tankY = (myTank.y + TANKHEIGHT / 2);
    let mouseX = e.clientX * 1/scale;
    let mouseY = e.clientY * 1/scale;
    myTank.canonOrientation = getAngleMouse(tankX, tankY, mouseX, mouseY);
}

function triggerEvent(event, bool){
    switch (event.keyCode) {
        case 81 /* Q */:
        case 37 /* ARROW LEFT */: leftPressed = bool; break;
        case 68 /* D */:
        case 39 /* ARROW RIGHT */ : rightPressed = bool; break;
        case 90 /* Z */:
        case 38 /* ARROW UP */: upPressed = bool; break;
        case 83 /* S */:
        case 40 /* ARROW DOWN */: downPressed = bool; break;
        case 32 /* SPACE */:
            if(!bool)
                shoot();
            break;
    }
}