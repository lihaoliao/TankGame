package pri.llh.tankgame.tank;

import pri.llh.tankgame.enums.Direction;
import pri.llh.tankgame.panel.GamePanel;

/**
 * @author LiHao Liao
 * @version 1.0
 * @Package_Name pri.llh.tankgame.tank
 * @date 2023/11/29 18:53
 * @since 1.0
 */
public class EnemyTank extends Tank implements Runnable {

    public EnemyTank(int x, int y, Direction direction, GamePanel gamePanel) {
        super(x, y, direction, gamePanel);
    }

    /**
     * 敌人坦克的自由移动
     */
    @Override
    public synchronized void run() {
         while (true){
             for (int i = 0; i < 50; i++) {
                 move();
                 try {
                     Thread.sleep(50);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
             switch ((int)(Math.random()*4)){
                 case 0:
                     setDirection(Direction.LEFT);
                     break;
                 case 1:
                     setDirection(Direction.RIGHT);
                     break;
                 case 2:
                     setDirection(Direction.UP);
                     break;
                 case 3:
                     setDirection(Direction.DOWN);
                     break;
                 default:
                     break;
             }
             if (getTankLife() <= 0){
                 break;
             }
         }
    }
}
