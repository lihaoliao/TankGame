package pri.llh.tankgame.utils;

import pri.llh.tankgame.enums.Direction;
import pri.llh.tankgame.operations.Shot;
import pri.llh.tankgame.tank.Tank;

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
     * @param shot 射出的子弹
     * @param tank 被判定的目标
     */
    public static boolean hitJudge(Shot shot, Tank tank){
        int bulletX = shot.getBullet()[0];
        int bulletY = shot.getBullet()[1];
        int tankX = tank.getX();
        int tankY = tank.getY();
        Direction tankDirection = tank.getDirection();
        switch (tankDirection){
            case DOWN:
            case UP:
                if (bulletX > tankX && bulletX < tankX+Tank.TANK_TOTAL_HEIGHT && bulletY > tankY && bulletY < tankY+Tank.TANK_TOTAL_HEIGHT){
                    shot.setRunning(false);
                    tank.setTankLife(tank.getTankLife()-1);
                    return true;
                }
            case LEFT:
            case RIGHT:
                if(bulletX > tankX && bulletX < tankX+Tank.TANK_TOTAL_HEIGHT && bulletY > tankY && bulletY < tankY+Tank.TANK_TOTAL_WIDTH){
                    shot.setRunning(false);
                    tank.setTankLife(tank.getTankLife()-1);
                    return true;
                }
            default:
                return false;
        }
    }
}
