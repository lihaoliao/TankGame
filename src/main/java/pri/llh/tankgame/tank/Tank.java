package pri.llh.tankgame.tank;

import pri.llh.tankgame.enums.Direction;
import pri.llh.tankgame.operations.Bullet;
import pri.llh.tankgame.panel.GamePanel;

import java.util.Vector;

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
    private int speed = 3;
    /**
     * 坦克的生命值
     */
    private int tankLife = 1;

    /**
     * 坦克所在的画板
     */
    private GamePanel gamePanel;
    /**
     * 多颗子弹的集合
     */
    private Vector<Bullet> bulletVector = new Vector<>();

    public Tank(int x, int y, Direction direction, GamePanel gamePanel) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.gamePanel = gamePanel;
    }

    public Direction getDirection() {
        return direction;
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

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * 坦克射击子弹方法
     */
    public void shot(){
        int[] bulletCoordinate = new int[2];
        switch (direction){
            case UP:
                bulletCoordinate[0] = (int) (x+17.5);
                bulletCoordinate[1] = y-10;
                break;
            case DOWN:
                bulletCoordinate[0] = (int) (x+17.5);
                bulletCoordinate[1] = y+70;
                break;
            case LEFT:
                bulletCoordinate[0] = x-10;
                bulletCoordinate[1] = (int) (y+17.5);
                break;
            case RIGHT:
                bulletCoordinate[0] = x+70;
                bulletCoordinate[1] = (int) (y+17.5);
                break;
            default:
                break;
        }
        if(bulletVector.size() >= 5 ){
            return;
        }
        bulletVector.add(new Bullet(bulletCoordinate, this.speed * 3, this.direction));
        for (int i = 0; i < bulletVector.size(); i++) {
            Bullet bullet = bulletVector.get(i);
            bullet.getGamePanel(this.gamePanel);
            Thread bulletThread = new Thread(bullet);
            bulletThread.start();
        }
    }

    public Vector<Bullet> getBulletVector() {
        return bulletVector;
    }

    public void setBulletVector(Vector<Bullet> bulletVector) {
        this.bulletVector = bulletVector;
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
     * 坦克移动以及边界控制
     */
    public void move(){
        switch (getDirection()){
            case UP:
                if (y-speed>0) {
                    y -= speed;
                }else {
                    setDirection(Direction.DOWN);
                }
                break;
            case DOWN:
                if(y+speed+Tank.TANK_TOTAL_HEIGHT < getGamePanel().getScreenHeight()) {
                    y += speed;
                }else {
                    setDirection(Direction.UP);
                }
                break;
            case LEFT:
                if(x-speed>0) {
                    x -= speed;
                }else {
                    setDirection(Direction.RIGHT);
                }
                break;
            case RIGHT:
                if(x+speed+Tank.TANK_TOTAL_HEIGHT < getGamePanel().getScreenWidth()) {
                    x += speed;
                }else {
                    setDirection(Direction.LEFT);
                }
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
        return this.bulletVector.size() > 0;
    }
}
