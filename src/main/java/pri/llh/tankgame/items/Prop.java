package pri.llh.tankgame.items;

/**
 * TODO:强化道具的设计
 *
 * @author LiHao Liao
 * @version 1.0
 * @Package_Name pri.llh.tankgame.items
 * @date 2024/01/13 12:32
 * @Description
 * @since 1.0
 */
public class Prop {
    private int x;
    private int y;
    private int height = 20;
    private int weight = 20;
    private boolean exist = true;

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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public Prop(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
