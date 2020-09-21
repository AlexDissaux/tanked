package com.andigago.tanked.websockets.objects;

import com.andigago.tanked.models.User;
import com.andigago.tanked.util.RandomGenerator;
import com.andigago.tanked.config.GameConstants;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data()
public class Tank {

    private float x;
    private float y;
    private int orientation = 0;
    private String tankName = "Guest";
    private int points = 0;
    private String color = "rgb(255,0,0)";
    private int canonOrientation = 0;

    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.NONE)
    private User user = null;

    public Tank(){}

    private Tank(User user, String tankName, String color) {
        if(tankName != null)
            this.tankName = tankName;
        this.color = color;
        this.user = user;
        if(user != null)
            this.points = user.getPoints();
    }

    public static Tank generateNewTank(User user, String tankName) {
        String color = "rgb(" + RandomGenerator.getBetween(0,255) + ',' + RandomGenerator.getBetween(0,255) + ',' + RandomGenerator.getBetween(0,255) + ')';
        Tank tank = new Tank(user, tankName, color);
        tank.setRandomPosition();
        return tank;
    }

    public void setRandomPosition() {
        this.x = RandomGenerator.getBetween(GameConstants.BORDER_SECURITY,GameConstants.BOARD_WIDTH- GameConstants.BORDER_SECURITY);
        this.y = RandomGenerator.getBetween(GameConstants.BORDER_SECURITY,GameConstants.BOARD_HEIGHT- GameConstants.BORDER_SECURITY);
        this.orientation = RandomGenerator.getBetween(0, 359);
    }

    public void increasePoints() {
        this.points += 1;
    }

    public void move(TankMove tankMove) {
        this.x = tankMove.getX();
        this.y = tankMove.getY();
        this.canonOrientation = tankMove.getCanonOrientation();
        this.orientation = tankMove.getOrientation();
    }

    @Override
    public String toString() {
        return "Tank (x="+x+", y="+y+", rot="+orientation+ ")";
    }
}
