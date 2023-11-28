package pri.llh.tankgame.tank;

import pri.llh.tankgame.enums.Direction;

/**
 * @author LiHao Liao
 * @version 1.0
 * @Package_Name pri.llh.tankgame.tank
 * @date 2023/11/28 14:07
 * @since 1.0
 */
public class Tank {
    /**
     * 坦克的横坐标
     */
    private int x;
    /**
     * 坦克的纵坐标
     */
    private int y;
    /**
     * 坦克的方向
     */
    private Direction direction;
    /**
     * 坦克的速度
     * 默认为3像素
     */
    private int speed=3;

    public Tank(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * 坦克移动
     */
    public void move(){
        switch (direction){
            case UP:
                y-=speed;
                break;
            case DOWN:
                y+=speed;
                break;
            case LEFT:
                x-=speed;
                break;
            case RIGHT:
                x+=speed;
                break;
            default:
                break;
        }
    }
}
