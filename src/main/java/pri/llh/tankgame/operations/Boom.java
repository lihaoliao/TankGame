package pri.llh.tankgame.operations;

/**
 * @author LiHao Liao
 * @version 1.0
 * @Package_Name pri.llh.tankgame.operations
 * @date 2023/12/01 16:10
 * @Description 制作被击中时爆炸效果
 * @since 1.0
 */
public class Boom {
    private int x;
    private int y;
    private boolean isRunning = true;
    private int life = 12;

    public Boom(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void lifeDown() {
        if (life > 0) {
            life--;
        } else {
            isRunning = false;
        }
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

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }
}
