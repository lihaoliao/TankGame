package pri.llh.tankgame.utils;

import pri.llh.tankgame.enums.Direction;
import pri.llh.tankgame.items.Wall;
import pri.llh.tankgame.operations.Bullet;
import pri.llh.tankgame.tank.Tank;

import java.util.Vector;

/**
 * @author LiHao Liao
 * @version 1.0
 * @Package_Name pri.llh.tankgame.utils
 * @date 2023/12/01 15:21
 * @since 1.0
 */
public class GameJudgeUtils {
    /**
     * 判断子弹是否击中坦克
     *
     * @param bullet 射出的子弹
     * @param tank   被判定的目标
     */
    public static boolean hitTankJudge(Bullet bullet, Tank tank) {
        int bulletX = bullet.getBullet()[0];
        int bulletY = bullet.getBullet()[1];
        Direction tankDirection = tank.getDirection();
        int[][] tankRange = TankUtils.tankRange(tank);
        switch (tankDirection) {
            case DOWN:
            case UP:
                if (bulletCrashJudge(bullet, tank, bulletX, bulletY, tankRange)) {
                    return true;
                }
            case LEFT:
            case RIGHT:
                if (bulletCrashJudge(bullet, tank, bulletX, bulletY, tankRange)) {
                    return true;
                }
            default:
                return false;
        }
    }

    private static boolean bulletCrashJudge(Bullet bullet, Tank tank, int bulletX, int bulletY, int[][] tankRange) {
        if (bulletX > tankRange[0][0] && bulletX < tankRange[3][0] && bulletY > tankRange[0][1] && bulletY < tankRange[3][1]) {
            bullet.setRunning(false);
            tank.setTankLife(tank.getTankLife() - 1);
            return true;
        }
        return false;
    }

    /**
     * 判断坦克是否碰撞，此方法为敌人坦克判断
     *
     * @param currentTank 当前判断的坦克
     * @param tankVector  所有坦克的列表
     * @return 发生碰撞的坦克或者Null
     */
    public static Tank isTouchOtherTank(Tank currentTank, Vector<? extends Tank> tankVector) {
        Tank resultTank;
        int[][] currentTankRange;
        switch (currentTank.getDirection()) {
            case UP:
                currentTankRange = TankUtils.tankRange(currentTank);
                for (int i = 0; i < tankVector.size(); i++) {
                    Tank otherTank = tankVector.get(i);
                    int[][] tankRange = TankUtils.tankRange(otherTank);
                    if (!otherTank.equals(currentTank)) {
                        //当前坦克左上角判断在范围内则发生了碰撞
                        resultTank = leftUpJudge(currentTankRange, otherTank, tankRange);
                        if (resultTank != null) {
                            return resultTank;
                        }
                        //当前坦克右上角判断在范围内则发生了碰撞
                        resultTank = rightUpJudge(currentTankRange, otherTank, tankRange);
                        if (resultTank != null) {
                            return resultTank;
                        }
                    }
                }
                break;
            case DOWN:
                currentTankRange = TankUtils.tankRange(currentTank);
                for (int i = 0; i < tankVector.size(); i++) {
                    Tank otherTank = tankVector.get(i);
                    int[][] tankRange = TankUtils.tankRange(otherTank);
                    if (!otherTank.equals(currentTank)) {
                        //当前坦克左下角判断在范围内则发生了碰撞
                        resultTank = leftDownJudge(currentTankRange, otherTank, tankRange);
                        if (resultTank != null) {
                            return resultTank;
                        }
                        //当前坦克右下角判断在范围内则发生了碰撞
                        resultTank = rightDownJudge(currentTankRange, otherTank, tankRange);
                        if (resultTank != null) {
                            return resultTank;
                        }
                    }
                }
                break;
            case LEFT:
                currentTankRange = TankUtils.tankRange(currentTank);
                for (int i = 0; i < tankVector.size(); i++) {
                    Tank otherTank = tankVector.get(i);
                    int[][] tankRange = TankUtils.tankRange(otherTank);
                    if (!otherTank.equals(currentTank)) {
                        //当前坦克左上角判断在范围内则发生了碰撞
                        resultTank = leftUpJudge(currentTankRange, otherTank, tankRange);
                        if (resultTank != null) {
                            return resultTank;
                        }
                        //当前坦克左下角判断在范围内则发生了碰撞
                        resultTank = leftDownJudge(currentTankRange, otherTank, tankRange);
                        if (resultTank != null) {
                            return resultTank;
                        }
                    }
                }
                break;
            case RIGHT:
                currentTankRange = TankUtils.tankRange(currentTank);
                for (int i = 0; i < tankVector.size(); i++) {
                    Tank otherTank = tankVector.get(i);
                    int[][] tankRange = TankUtils.tankRange(otherTank);
                    if (!otherTank.equals(currentTank)) {
                        //当前坦克右上角判断在范围内则发生了碰撞
                        resultTank = rightUpJudge(currentTankRange, otherTank, tankRange);
                        if (resultTank != null) {
                            return resultTank;
                        }
                        //当前坦克右下角判断在范围内则发生了碰撞
                        resultTank = rightDownJudge(currentTankRange, otherTank, tankRange);
                        if (resultTank != null) {
                            return resultTank;
                        }
                    }
                }
                break;
            default:
                break;
        }
        return null;
    }

    private static Tank rightDownJudge(int[][] currentTankRange, Tank otherTank, int[][] tankRange) {
        Tank resultTank;
        if (currentTankRange[3][0] >= tankRange[0][0] &&
                currentTankRange[3][0] <= tankRange[3][0] &&
                currentTankRange[3][1] >= tankRange[0][1] &&
                currentTankRange[3][1] <= tankRange[3][1]) {
            resultTank = otherTank;
            return resultTank;
        }
        return null;
    }

    private static Tank leftDownJudge(int[][] currentTankRange, Tank otherTank, int[][] tankRange) {
        Tank resultTank;
        if (currentTankRange[2][0] >= tankRange[0][0] &&
                currentTankRange[2][0] <= tankRange[3][0] &&
                currentTankRange[2][1] >= tankRange[0][1] &&
                currentTankRange[2][1] <= tankRange[3][1]) {
            resultTank = otherTank;
            return resultTank;
        }
        return null;
    }

    private static Tank rightUpJudge(int[][] currentTankRange, Tank otherTank, int[][] tankRange) {
        Tank resultTank;
        if (currentTankRange[1][0] >= tankRange[0][0] &&
                currentTankRange[1][0] <= tankRange[3][0] &&
                currentTankRange[1][1] >= tankRange[0][1] &&
                currentTankRange[1][1] <= tankRange[3][1]) {
            resultTank = otherTank;
            return resultTank;
        }
        return null;
    }

    private static Tank leftUpJudge(int[][] currentTankRange, Tank otherTank, int[][] tankRange) {
        Tank resultTank;
        if (currentTankRange[0][0] >= tankRange[0][0] &&
                currentTankRange[0][0] <= tankRange[3][0] &&
                currentTankRange[0][1] >= tankRange[0][1] &&
                currentTankRange[0][1] <= tankRange[3][1]) {
            resultTank = otherTank;
            return resultTank;
        }
        return null;
    }

    /**
     * 判断子弹是否击中墙体
     *
     * @param bullet 用于判断的子弹
     * @param wall 被判断的墙体
     * @return 是否击中墙体
     */
    public static boolean hitWallJudge(Bullet bullet, Wall wall) {
        int bulletX = bullet.getBullet()[0];
        int bulletY = bullet.getBullet()[1];
        if (bulletX >= wall.getX() && bulletX <= wall.getX() + wall.getWeight() && bulletY >= wall.getY() && bulletY <= wall.getY() + wall.getHeight()) {
            bullet.setRunning(false);
            if (wall.isDestructible()) {
                wall.setExist(false);
            }
            return true;
        }
        return false;
    }
}
