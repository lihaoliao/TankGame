package pri.llh.tankgame.operations;

import pri.llh.tankgame.enums.Direction;
import pri.llh.tankgame.panel.GamePanel;

/**
 * @author LiHao Liao
 * @version 1.0
 * @Package_Name pri.llh.tankgame.operations
 * @date 2023/12/01 09:31
 * @since 1.0
 */
public class Shot implements Runnable{
    /**
     * 子弹的横纵坐标
     */
    private int[] bullet;
    private int speed;
    private Direction direction;
    /**
     * 游戏面板
     */
    private GamePanel gamePanel;
    /**
     * 子弹线程是否还存在
     */
    boolean isRunning = true;

    public int[] getBullet() {
        return bullet;
    }

    public void setBullet(int[] bullet) {
        this.bullet = bullet;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Shot(int[] bullet, int speed, Direction direction) {
        this.bullet = bullet;
        this.speed = speed;
        this.direction = direction;
    }

    @Override
    public void run() {
        while (true){
            switch (direction){
                case UP:
                    bullet[1] -= speed;
                    break;
                case DOWN:
                    bullet[1] += speed;
                    break;
                case LEFT:
                    bullet[0] -= speed;
                    break;
                case RIGHT:
                    bullet[0] += speed;
                    break;
                default:
                    break;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //子弹还在面板内就没事，超出面板就销毁线程
            if (!(bullet[0] >= 0 && bullet[0] <= (gamePanel.getScreenWidth()) && bullet[1] >= 0 && bullet[1] <= gamePanel.getScreenHeight())){
                isRunning = false;
                break;
            }
            //子弹击中目标
            else if(!isRunning){
                break;
            }
        }
    }

    public void getGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

}
