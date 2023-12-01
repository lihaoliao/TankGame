package pri.llh.tankgame.tank;

import pri.llh.tankgame.enums.Direction;
import pri.llh.tankgame.operations.Shot;
import pri.llh.tankgame.panel.GamePanel;

/**
 * @author LiHao Liao
 * @version 1.0
 * @Package_Name pri.llh.tankgame.tank
 * @date 2023/11/28 14:07
 * @since 1.0
 */
public class Tank {
    public static final int TANK_TOTAL_WIDTH = 40;
    public static final int TANK_TOTAL_HEIGHT = 60;
    /**
     * 坦克的左上角横坐标
     */
    private int x;
    /**
     * 坦克的左上角纵坐标
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
    /**
     * 坦克的子弹
     */
    private Shot shot;
    /**
     * 坦克的生命值
     */
    private int tankLife = 1;

    public Tank(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public Shot getShot() {
        return shot;
    }

    public void setShot(Shot shot) {
        this.shot = shot;
    }

    public int getTankLife() {
        return tankLife;
    }

    public void setTankLife(int tankLife) {
        if (tankLife < 0){
            this.tankLife = 0;
        }else {
            this.tankLife = tankLife;
        }
    }

    public void shot(GamePanel gamePanel){
        int[] bullet = new int[2];
        switch (direction){
            case UP:
                bullet[0] = (int) (x+17.5);
                bullet[1] = y-10;
                break;
            case DOWN:
                bullet[0] = (int) (x+17.5);
                bullet[1] = y+70;
                break;
            case LEFT:
                bullet[0] = x-10;
                bullet[1] = (int) (y+17.5);
                break;
            case RIGHT:
                bullet[0] = x+70;
                bullet[1] = (int) (y+17.5);
                break;
            default:
                break;
        }
        //TODO:同时显示多个子弹，并且考虑如何把这里的每个子弹线程区分名称
        this.shot = new Shot(bullet, this.speed * 3, this.direction);
        shot.getGamePanel(gamePanel);
        Thread bulletThread = new Thread(shot);
        bulletThread.start();
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

    /**
     * 判断子弹是否为空
     * @return true or false
     */
    public boolean isShot(){
        return shot!=null && shot.isRunning();
    }
}
