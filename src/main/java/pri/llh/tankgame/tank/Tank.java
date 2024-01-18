package pri.llh.tankgame.tank;

import pri.llh.tankgame.enums.Direction;
import pri.llh.tankgame.enums.TankType;
import pri.llh.tankgame.items.Wall;
import pri.llh.tankgame.operations.Bullet;
import pri.llh.tankgame.panel.GamePanel;
import pri.llh.tankgame.utils.GameJudgeUtils;

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
    /**
     * 坦克的类型
     */
    private TankType type;

    public Tank(int x, int y, Direction direction, GamePanel gamePanel, TankType type) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.gamePanel = gamePanel;
        this.type = type;
    }

    public TankType getType() {
        return type;
    }

    public void setType(TankType type) {
        this.type = type;
    }

    public Tank(int x, int y, Direction direction, int speed, int tankLife, GamePanel gamePanel, Vector<Bullet> bulletVector, TankType type) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = speed;
        this.tankLife = tankLife;
        this.gamePanel = gamePanel;
        this.bulletVector = bulletVector;
        this.type = type;
    }

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
        this.tankLife = Math.max(tankLife, 0);
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
    public void shot() {
        int[] bulletCoordinate = new int[2];
        switch (direction) {
            case UP:
                bulletCoordinate[0] = (int) (x + 17.5);
                bulletCoordinate[1] = y - 10;
                break;
            case DOWN:
                bulletCoordinate[0] = (int) (x + 17.5);
                bulletCoordinate[1] = y + 70;
                break;
            case LEFT:
                bulletCoordinate[0] = x - 10;
                bulletCoordinate[1] = (int) (y + 17.5);
                break;
            case RIGHT:
                bulletCoordinate[0] = x + 70;
                bulletCoordinate[1] = (int) (y + 17.5);
                break;
            default:
                break;
        }
        if (bulletVector.size() >= 5) {
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
     * 以及墙体碰撞控制
     * TODO:墙体碰撞可以分为炮管和其他履带
     */
    public void move(Direction preDirection) {
        Vector<Wall> wallList;
        int preY;
        int preX;
        Tank enemyTank;
        Tank otherPlayerTank;
        switch (getDirection()) {
            case UP:
                preY = y;
                //边界控制
                if (y - speed > 0) {
                    y -= speed;
                } else {
                    if (this.getType() != TankType.Player) {
                        setDirection(Direction.DOWN);
                    }
                }
                //墙体碰撞控制
                wallList = this.gamePanel.getWallList();
                for (int i = 0; i < wallList.size(); i++) {
                    Wall wall = wallList.get(i);
                    int wallLeftDownX = wall.getX();
                    int wallLeftDownY = wall.getY() + wall.getHeight();
                    int wallRightDownX = wall.getX() + wall.getWeight();
                    int wallLeftUpY = wall.getY();
                    if (preY - speed < wallLeftDownY + 10 && preY - speed > wallLeftUpY + 10 && preDirection != Direction.DOWN) {
                        for (int j = x; j <= x + TANK_TOTAL_WIDTH; j++) {
                            if (j >= wallLeftDownX && j <= wallRightDownX) {
                                y = preY;
                                if (this.getType() != TankType.Player) {
                                    setDirection(Direction.DOWN);
                                }
                            }
                        }
                    }
                }
                enemyTank = GameJudgeUtils.isTouchOtherTank(this, gamePanel.getEnemyTanks());
                otherPlayerTank = GameJudgeUtils.isTouchOtherTank(this, gamePanel.getPlayerTanks());
                if (enemyTank != null || otherPlayerTank != null) {
                    y = preY;
                }
                break;
            case DOWN:
                preY = y;
                //边界控制
                if (y + speed + Tank.TANK_TOTAL_HEIGHT < getGamePanel().getScreenHeight()) {
                    y += speed;
                } else {
                    if (this.getType() != TankType.Player) {
                        setDirection(Direction.UP);
                    }
                }
                //墙体碰撞控制
                wallList = this.gamePanel.getWallList();
                for (int i = 0; i < wallList.size(); i++) {
                    Wall wall = wallList.get(i);
                    int wallLeftUpX = wall.getX();
                    int wallLeftUpY = wall.getY();
                    int wallRightUpX = wall.getX() + wall.getWeight();
                    int wallLeftDownY = wall.getY() + wall.getHeight();
                    if (preY + speed + TANK_TOTAL_HEIGHT + 10 > wallLeftUpY && preY + speed + TANK_TOTAL_HEIGHT + 10 < wallLeftDownY && preDirection != Direction.UP) {
                        for (int j = x; j <= x + TANK_TOTAL_WIDTH; j++) {
                            if (j >= wallLeftUpX && j <= wallRightUpX) {
                                y = preY;
                                if (this.getType() != TankType.Player) {
                                    setDirection(Direction.UP);
                                }
                            }
                        }
                    }
                }
                //玩家与敌人碰撞判断
                enemyTank = GameJudgeUtils.isTouchOtherTank(this, gamePanel.getEnemyTanks());
                otherPlayerTank = GameJudgeUtils.isTouchOtherTank(this, gamePanel.getPlayerTanks());
                if (enemyTank != null || otherPlayerTank != null) {
                    y = preY;
                }
                break;
            case LEFT:
                preX = x;
                //边界控制
                if (x - speed > 0) {
                    x -= speed;
                } else {
                    if (this.getType() != TankType.Player) {
                        setDirection(Direction.RIGHT);
                    }
                }
                //墙体碰撞控制
                wallList = this.gamePanel.getWallList();
                for (int i = 0; i < wallList.size(); i++) {
                    Wall wall = wallList.get(i);
                    int wallLeftDownX = wall.getX();
                    int wallRightDownY = wall.getY() + wall.getHeight();
                    int wallRightDownX = wall.getX() + wall.getWeight();
                    int wallRightUpY = wall.getY();
                    if (preX - speed - 10 < wallRightDownX && preX - speed - 10 > wallLeftDownX && preDirection != Direction.RIGHT) {
                        for (int j = y; j <= y + TANK_TOTAL_WIDTH; j++) {
                            if (j >= wallRightUpY && j <= wallRightDownY) {
                                x = preX;
                                if (this.getType() != TankType.Player) {
                                    setDirection(Direction.RIGHT);
                                }
                            }
                        }
                    }
                }
                enemyTank = GameJudgeUtils.isTouchOtherTank(this, gamePanel.getEnemyTanks());
                otherPlayerTank = GameJudgeUtils.isTouchOtherTank(this, gamePanel.getPlayerTanks());
                if (enemyTank != null || otherPlayerTank != null) {
                    x = preX;
                }
                break;
            case RIGHT:
                preX = x;
                //边界控制
                if (x + speed + Tank.TANK_TOTAL_HEIGHT < getGamePanel().getScreenWidth()) {
                    x += speed;
                } else {
                    if (this.getType() != TankType.Player) {
                        setDirection(Direction.LEFT);
                    }
                }
                //墙体碰撞控制
                wallList = this.gamePanel.getWallList();
                for (int i = 0; i < wallList.size(); i++) {
                    Wall wall = wallList.get(i);
                    int wallLeftDownX = wall.getX();
                    int wallRightDownX = wall.getX() + wall.getWeight();
                    int wallLeftUpY = wall.getY();
                    int wallLeftDownY = wall.getY() + wall.getHeight();
                    if (preX + speed + 10 + TANK_TOTAL_HEIGHT > wallLeftDownX && preX + speed + 10 + TANK_TOTAL_HEIGHT < wallRightDownX && preDirection != Direction.LEFT) {
                        for (int j = y; j <= y + TANK_TOTAL_WIDTH; j++) {
                            if (j >= wallLeftUpY && j <= wallLeftDownY) {

                                x = preX;
                                if (this.getType() != TankType.Player) {
                                    setDirection(Direction.LEFT);
                                }
                            }
                        }
                    }
                }
                enemyTank = GameJudgeUtils.isTouchOtherTank(this, gamePanel.getEnemyTanks());
                otherPlayerTank = GameJudgeUtils.isTouchOtherTank(this, gamePanel.getPlayerTanks());
                if (enemyTank != null || otherPlayerTank != null) {
                    x = preX;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 判断子弹是否为空
     *
     * @return true or false
     */
    public boolean isShot() {
        return this.bulletVector.size() > 0;
    }

    public void changeDirection() {
        if (direction == Direction.UP) {
            direction = Direction.DOWN;
        } else if (direction == Direction.DOWN) {
            direction = Direction.UP;
        } else if (direction == Direction.LEFT) {
            direction = Direction.RIGHT;
        } else {
            direction = Direction.LEFT;
        }
    }
}
