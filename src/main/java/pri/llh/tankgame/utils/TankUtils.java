package pri.llh.tankgame.utils;

import pri.llh.tankgame.enums.Direction;
import pri.llh.tankgame.tank.Tank;

/**
 * @author LiHao Liao
 * @version 1.0
 * @Package_Name pri.llh.tankgame.utils
 * @date 2023/12/13 23:07
 * @Description
 * @since 1.0
 */
public class TankUtils {
    /**
     * 根据坦克的朝向计算其四个角的坐标。
     * @param tank 坦克对象
     * @return 一个二维数组，包含坦克的四个角的坐标。
     * 数组结构如下：
     * [0][0] -> 左上角的 X 坐标
     * [0][1] -> 左上角的 Y 坐标
     * [1][0] -> 右上角的 X 坐标
     * [1][1] -> 右上角的 Y 坐标
     * [2][0] -> 左下角的 X 坐标
     * [2][1] -> 左下角的 Y 坐标
     * [3][0] -> 右下角的 X 坐标
     * [3][1] -> 右下角的 Y 坐标
     */
    public static int[][] tankRange(Tank tank) {
        Direction direction = tank.getDirection();
        int x = tank.getX();
        int y = tank.getY();
        int width = Tank.TANK_TOTAL_WIDTH;
        int height = Tank.TANK_TOTAL_HEIGHT;

        if (direction == Direction.UP || direction == Direction.DOWN) {
            return new int[][] {
                    {x, y},
                    {x + width, y},
                    {x, y + height},
                    {x + width, y + height}
            };
        } else {
            return new int[][] {
                    {x, y},
                    {x + height, y},
                    {x, y + width},
                    {x + height, y + width}
            };
        }
    }
}
