package pri.llh.tankgame.tank;

import pri.llh.tankgame.enums.Direction;
import pri.llh.tankgame.enums.TankType;

/**
 * @author LiHao Liao
 * @version 1.0
 * @Package_Name pri.llh.tankgame.tank
 * @date 2023/12/14 21:33
 * @Description
 * @since 1.0
 */
public class TankNode {
    private int x;
    private int y;
    private String direction;
    private int tankLife;
    private TankType type;
    private int speed;

    public TankNode(int x, int y, String direction, int tankLife, TankType type, int speed) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.tankLife = tankLife;
        this.type = type;
        this.speed = speed;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getTankLife() {
        return tankLife;
    }

    public void setTankLife(int tankLife) {
        this.tankLife = tankLife;
    }

    public int getType() {
        return type.getValue();
    }

    public void setType(TankType type) {
        this.type = type;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
