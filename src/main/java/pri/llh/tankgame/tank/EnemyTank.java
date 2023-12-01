package pri.llh.tankgame.tank;

import pri.llh.tankgame.enums.Direction;
import pri.llh.tankgame.operations.Shot;

import java.util.Vector;

/**
 * @author LiHao Liao
 * @version 1.0
 * @Package_Name pri.llh.tankgame.tank
 * @date 2023/11/29 18:53
 * @since 1.0
 */
public class EnemyTank extends Tank {
    public Vector<Shot> shotVector;
    public EnemyTank(int x, int y, Direction direction) {
        super(x, y, direction);
    }
}
