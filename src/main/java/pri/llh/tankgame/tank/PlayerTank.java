package pri.llh.tankgame.tank;

import pri.llh.tankgame.enums.Direction;
import pri.llh.tankgame.panel.GamePanel;

/**
 * @author LiHao Liao
 * @version 1.0
 * @Package_Name pri.llh.tankgame.tank
 * @date 2023/11/28 14:09
 * @since 1.0
 */
public class PlayerTank extends Tank {

    public PlayerTank(int x, int y, Direction direction, GamePanel gamePanel) {
        super(x, y, direction, gamePanel);
    }

}
