package pri.llh.tankgame.utils;

import pri.llh.tankgame.enums.Direction;
import pri.llh.tankgame.operations.Bullet;
import pri.llh.tankgame.tank.EnemyTank;
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
    public static boolean hitJudge(Bullet bullet, Tank tank) {
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
     * TODO: 玩家与敌人碰撞判断
     * @param currentTank 当前判断的坦克
     * @param tankVector  所有坦克的列表
     * @return 发生碰撞的坦克或者Null
     */
    public static Tank isTouchTank(EnemyTank currentTank, Vector<EnemyTank> tankVector) {
        Tank resultTank;
        int[][] currentTankRange;
        switch (currentTank.getDirection()) {
            case UP:
                currentTankRange = TankUtils.tankRange(currentTank);
                for (int i = 0; i < tankVector.size(); i++) {
                    EnemyTank enemyTank = tankVector.get(i);
                    int[][] tankRange = TankUtils.tankRange(enemyTank);
                    if (!enemyTank.equals(currentTank)) {
                        //当前坦克左上角判断在范围内则发生了碰撞
                        resultTank = leftUpJudge(currentTankRange, enemyTank, tankRange);
                        if (resultTank != null) {
                            return resultTank;
                        }
                        //当前坦克右上角判断在范围内则发生了碰撞
                        resultTank = RightUpJudge(currentTankRange, enemyTank, tankRange);
                        if (resultTank != null) {
                            return resultTank;
                        }
                    }
                }
                break;
            case DOWN:
                currentTankRange = TankUtils.tankRange(currentTank);
                for (int i = 0; i < tankVector.size(); i++) {
                    EnemyTank enemyTank = tankVector.get(i);
                    int[][] tankRange = TankUtils.tankRange(enemyTank);
                    if (!enemyTank.equals(currentTank)) {
                        //当前坦克左下角判断在范围内则发生了碰撞
                        resultTank = LeftDownJudge(currentTankRange, enemyTank, tankRange);
                        if (resultTank != null) {
                            return resultTank;
                        }
                        //当前坦克右下角判断在范围内则发生了碰撞
                        resultTank = RightDownJudge(currentTankRange, enemyTank, tankRange);
                        if (resultTank != null) {
                            return resultTank;
                        }
                    }
                }
                break;
            case LEFT:
                currentTankRange = TankUtils.tankRange(currentTank);
                for (int i = 0; i < tankVector.size(); i++) {
                    EnemyTank enemyTank = tankVector.get(i);
                    int[][] tankRange = TankUtils.tankRange(enemyTank);
                    if (!enemyTank.equals(currentTank)) {
                        //当前坦克左上角判断在范围内则发生了碰撞
                        resultTank = leftUpJudge(currentTankRange, enemyTank, tankRange);
                        if (resultTank != null) {
                            return resultTank;
                        }
                        //当前坦克左下角判断在范围内则发生了碰撞
                        resultTank = LeftDownJudge(currentTankRange, enemyTank, tankRange);
                        if (resultTank != null) {
                            return resultTank;
                        }
                    }
                }
                break;
            case RIGHT:
                currentTankRange = TankUtils.tankRange(currentTank);
                for (int i = 0; i < tankVector.size(); i++) {
                    EnemyTank enemyTank = tankVector.get(i);
                    int[][] tankRange = TankUtils.tankRange(enemyTank);
                    if (!enemyTank.equals(currentTank)) {
                        //当前坦克右上角判断在范围内则发生了碰撞
                        resultTank = RightUpJudge(currentTankRange, enemyTank, tankRange);
                        if (resultTank != null) {
                            return resultTank;
                        }
                        //当前坦克右下角判断在范围内则发生了碰撞
                        resultTank = RightDownJudge(currentTankRange, enemyTank, tankRange);
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

    private static Tank RightDownJudge(int[][] currentTankRange, EnemyTank enemyTank, int[][] tankRange) {
        Tank resultTank;
        if (currentTankRange[3][0] >= tankRange[0][0] &&
                currentTankRange[3][0] <= tankRange[3][0] &&
                currentTankRange[3][1] >= tankRange[0][1] &&
                currentTankRange[3][1] <= tankRange[3][1]) {
            resultTank = enemyTank;
            return resultTank;
        }
        return null;
    }

    private static Tank LeftDownJudge(int[][] currentTankRange, EnemyTank enemyTank, int[][] tankRange) {
        Tank resultTank;
        if (currentTankRange[2][0] >= tankRange[0][0] &&
                currentTankRange[2][0] <= tankRange[3][0] &&
                currentTankRange[2][1] >= tankRange[0][1] &&
                currentTankRange[2][1] <= tankRange[3][1]) {
            resultTank = enemyTank;
            return resultTank;
        }
        return null;
    }

    private static Tank RightUpJudge(int[][] currentTankRange, EnemyTank enemyTank, int[][] tankRange) {
        Tank resultTank;
        if (currentTankRange[1][0] >= tankRange[0][0] &&
                currentTankRange[1][0] <= tankRange[3][0] &&
                currentTankRange[1][1] >= tankRange[0][1] &&
                currentTankRange[1][1] <= tankRange[3][1]) {
            resultTank = enemyTank;
            return resultTank;
        }
        return null;
    }

    private static Tank leftUpJudge(int[][] currentTankRange, EnemyTank enemyTank, int[][] tankRange) {
        Tank resultTank;
        if (currentTankRange[0][0] >= tankRange[0][0] &&
                currentTankRange[0][0] <= tankRange[3][0] &&
                currentTankRange[0][1] >= tankRange[0][1] &&
                currentTankRange[0][1] <= tankRange[3][1]) {
            resultTank = enemyTank;
            return resultTank;
        }
        return null;
    }
}
