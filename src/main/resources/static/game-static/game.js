let tanks = {};
let myTank = {};
let shots = {};
let map = {};
let gameInterval = null;
let nextShot = new Date();
let shootLeft = SHOOT_CHARGER_SIZE;

function start() {
    ws.handle("GAME_JOIN", onGameJoin);
    ws.handle("STOP", onStop);
    ws.handle("NEW_TANK", onTankMove);
    ws.handle("TANK_MOVE", onTankMove);
    ws.handle("KILLED_TANK", onTankKilled);
    ws.handle("DELETE_TANK", onDeleteTank);
    ws.handle("NEW_SHOT", onNewShot);
    ws.handle("DELETE_SHOT", onDeleteShot);
    ws.start();
}

function onStop() {
    clearInterval(gameInterval);
}

function fillLeaderboard() {
    leaderboard.innerHTML = "";
    Object.keys(tanks).map(function(tankId) {
        addTankToLeaderboard(tankId, tanks[tankId])
    });
}

function addTankToLeaderboard(tankId, tank) {
    let div = document.createElement("div");
    div.innerText = tank.tankName + " - " + tank.points + "pts";
    div.style = "color: " + tank.color;
    div.id = "player-"+tankId;
    leaderboard.append(div);
}

function onGameJoin(receivedTanks, receivedMap) {
    tanks = receivedTanks;
    map = receivedMap;
    if (map != null) { resizeMap(map) };
    shots = {};
    leaderboard.innerHTML = "";

    fillLeaderboard();

    console.log("Tanks received : game starting...");
    registerEvents();
    autoScale();
    gameLoop();
}

function onTankMove(tankId, tank) {
    if(!(tankId in tanks)) {
        addTankToLeaderboard(tankId, tank);
    }

    if(tankId === ws.whoami()) {
        tank.canonOrientation = myTank.canonOrientation;
        myTank = tank;
    }
    tanks[tankId] = tank;
}

function onTankKilled(tankId, tank) {
    drawBoom(tanks[tankId]);
    tanks[tankId] = tank;
    if(tankId === ws.whoami()) {
        myTank = tank;
    }

    setTimeout(fillLeaderboard, 500);
}

function onDeleteTank(tankId) {
    delete tanks[tankId];

    let line = document.querySelector("#player-"+tankId);
    line.parentNode.removeChild(line);
}

function onNewShot(shotId, shot) {
    shots[shotId] = shot;
}

function onDeleteShot(shotId) {
    delete shots[shotId];
}

function sendMyTankInfos() {
    ws.send('MOVE_MY_TANK', myTank);
}

function shoot() {
    if (nextShot < new Date() && shootLeft > 0) {
        shootLeft--;
        let myTank = tanks[ws.whoami()];
        let shot = {
            x: (myTank.x + TANKWIDTH / 2) + (Math.cos(myTank.canonOrientation) * CANONLENGTH),
            y: (myTank.y + TANKHEIGHT / 2) - (Math.sin(myTank.canonOrientation) * CANONLENGTH),
            vy: -Math.sin(myTank.canonOrientation) * SHOT_SPEED,
            vx: Math.cos(myTank.canonOrientation) * SHOT_SPEED
        };

        ws.send('I_SHOOT', shot);
        let actualDate = new Date();
        nextShot = new Date(actualDate.getTime() + (TIME));
        setTimeout(() => {shootLeft++}, SHOOT_RELOADING_TIME);
    }
}

function collisionShot(shotId, playerId, ownerId) {
    ws.send('COLLISION_SHOT', {shotId : shotId, touchedPlayerId: playerId, ownerId: ownerId});
}

function moveShots() {
    Object.keys(shots).map(function(shotId) {
       shots[shotId].x += shots[shotId].vx;
       shots[shotId].y += shots[shotId].vy;

       if(shots[shotId].owner === ws.whoami()) {
           if(isOutsideOfTheMap(shots[shotId])) {
               collisionShot(shotId, null, null)
           } else {
               let touchedTank = getTouchedTank(shots[shotId]);
               if(touchedTank != null)
                   collisionShot(shotId, touchedTank, ws.whoami());
           }
       }
    });
}

function isOutsideOfTheMap(shot) {
    return !(
        0 < shot.x
        && shot.x < MAP_WIDTH
        && 0 < shot.y
        && shot.y < MAP_HEIGHT
    )
}

function distToTank(shot, tank) {
    let tankX = (tank.x + TANKWIDTH / 2);
    let tankY = (tank.y + TANKHEIGHT / 2);
    return Math.pow(shot.x - tankX,2) + Math.pow(shot.y - tankY,2)
}

function distTankToTank(tank1, tank2) {
    let tankX1 = (tank1.x + TANKWIDTH / 2);
    let tankY1 = (tank1.y + TANKHEIGHT / 2);
    let tankX2 = (tank2.x + TANKWIDTH / 2);
    let tankY2 = (tank2.y + TANKHEIGHT / 2);
    return Math.pow(tankX1 - tankX2, 2) + Math.pow(tankY1 - tankY2,2)
}

function getTouchedTank(shot) {
    let tanksId = Object.keys(tanks);
    let i = 0;
    let touchedTank = null;
    while(touchedTank === null && i < tanksId.length) {
        if(tanksId[i] !== ws.whoami() && distToTank(shot, tanks[tanksId[i]]) < COLLISION_DIST_SQUARED) {
            touchedTank = tanksId[i];
        }
        i++;
    }

    return touchedTank;
}

function isTouchingATank(tank) {
    let tanksId = Object.keys(tanks);
    let i = 0;
    let touchingATank = false;
    while(!touchingATank && i < tanksId.length) {
        if(tanksId[i] !== ws.whoami() && distTankToTank(tank, tanks[tanksId[i]]) < COLLISION_TANK_DIST_SQUARED) {
            touchingATank = true
        }
        i++;
    }

    return touchingATank;
}

function moveMyTank(){
    let previousPos = {x: myTank.x, y: myTank.y};

    if (leftPressed) {
        myTank.orientation = (myTank.orientation - SPEED + 360) % 360;
    }
    if (rightPressed){
        myTank.orientation = (myTank.orientation + SPEED + 360) % 360;
    }
    if (upPressed) {
        myTank.y -= Math.cos(toRadian(myTank.orientation))*SPEED;
        myTank.x += Math.sin(toRadian(myTank.orientation))*SPEED;
    }
    if (downPressed){
        myTank.y += (Math.cos(toRadian(myTank.orientation)) / 2)*SPEED;
        myTank.x -= (Math.sin(toRadian(myTank.orientation)) / 2)*SPEED;
    }

    if(myTank.x < 30 - (TANKWIDTH / 2)) {
        myTank.x = 30 - (TANKWIDTH / 2);
    } else if(myTank.x > MAP_WIDTH - (TANKWIDTH / 2) - 30) {
        myTank.x = MAP_WIDTH - (TANKWIDTH / 2) - 30;
    }

    if(myTank.y < 30 - (TANKHEIGHT / 2)) {
        myTank.y = 30 - (TANKHEIGHT / 2);
    } else if(myTank.y > MAP_HEIGHT - (TANKHEIGHT / 2) - 30) {
        myTank.y = MAP_HEIGHT  - (TANKHEIGHT / 2) - 30    ;
    }

    if(isTouchingATank(myTank)) {
        myTank.x = previousPos.x;
        myTank.y = previousPos.y;
    }
}

function gameLoop() {
    gameInterval = window.setInterval(function(){
        moveShots();
        ctx.setTransform(1, 0, 0, 1, 0, 0);
        erase();
        ctx.scale(scale, scale);
        moveMyTank();
        drawTanks();
        drawShots();
        ctx.scale(1/scale, 1/scale);
        sendMyTankInfos();
    }, GAME_LOOP_TIME);
}

window.onload = function(){start()};