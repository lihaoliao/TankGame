package pri.llh.tankgame.panel;

import pri.llh.tankgame.enums.Direction;
import pri.llh.tankgame.operations.Shot;
import pri.llh.tankgame.tank.EnemyTank;
import pri.llh.tankgame.tank.PlayerTank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

/**
 * @author LiHao Liao
 * @version 1.0
 * @Package_Name pri.llh.tankgame.panel
 * @date 2023/11/28 14:08
 * @since 1.0
 */
public class GamePanel extends JPanel implements KeyListener,Runnable {

    private int screenWidth;
    private int screenHeight;

    PlayerTank playerTank;
    Vector<EnemyTank> enemyTanks = new Vector<>();
    int enemies = 3;

    /**
     * 面板里面坦克初始化
     */
    public GamePanel(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        //玩家出生的位置
        playerTank = new PlayerTank(screenWidth/2, (int) (screenHeight*0.8),Direction.UP);
        for (int i = 0; i < enemies; i++) {
            enemyTanks.add(new EnemyTank(((i+1)*100),0,Direction.DOWN));
        }
    }

    /**
     * 绘制面板以及坦克
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //默认是黑色
        g.fillRect(0,0,this.screenWidth,this.screenHeight);
        //画坦克
        drawTank(playerTank.getX(),playerTank.getY(),g,playerTank.getDirection(),2);
        //画敌人的坦克
        for (EnemyTank enemyTank : enemyTanks) {
            drawTank(enemyTank.getX(),enemyTank.getY(),g,enemyTank.getDirection(),1);
            if(enemyTank.isShot()) {
                drawBullet(g, enemyTank);
            }
        }
        //画子弹
        if(playerTank.isShot()) {
            drawBullet(g, playerTank);
        }

        //敌人发射子弹的频率
        for (EnemyTank enemyTank : enemyTanks) {
            if(Math.random() < 0.05) {
                enemyTank.shot(this);
            }
        }

    }

    /**
     * 坦克的初始化以及重绘
     * @param x 坦克左上角x坐标
     * @param y 坦克左上角y坐标
     * @param g
     * @param direction 坦克方向 - 上下左右
     * @param type  坦克类型
     */
    public void drawTank(int x, int y, Graphics g, Direction direction,int type) {

        switch (type){
            //enemies
            case 1:
                g.setColor(Color.RED);
                break;
            //player
            case 2:
                g.setColor(Color.cyan);
                break;
            case 3:
                g.setColor(Color.GREEN);
                break;
            case 4:
                g.setColor(Color.YELLOW);
                break;
            default:
                g.setColor(Color.BLACK);
                break;
        }

        switch (direction){
            //向上
            case UP:
                g.fill3DRect(x,y,10,60,false);
                g.fill3DRect(x+10,y+10,20,40,false);
                g.fill3DRect(x+10+20,y,10,60,false);
                g.fillOval(x+10,y+20,20,20);
                g.drawLine(x+20,y-10,x+20,y+30);
                break;
            //向下
            case DOWN:
                g.fill3DRect(x,y,10,60,false);
                g.fill3DRect(x+10,y+10,20,40,false);
                g.fill3DRect(x+10+20,y,10,60,false);
                g.fillOval(x+10,y+20,20,20);
                g.drawLine(x+20,y+30,x+20,y+70);
                break;
            //向左
            case LEFT:
                g.fill3DRect(x,y,60,10,false);
                g.fill3DRect(x+10,y+10,40,20,false);
                g.fill3DRect(x,y+10+20,60,10,false);
                g.fillOval(x+20,y+10,20,20);
                g.drawLine(x-10,y+20,x+30,y+20);
                break;
            //向右
            case RIGHT:
                g.fill3DRect(x,y,60,10,false);
                g.fill3DRect(x+10,y+10,40,20,false);
                g.fill3DRect(x,y+10+20,60,10,false);
                g.fillOval(x+20,y+10,20,20);
                g.drawLine(x+30,y+20,x+70,y+20);
                break;
            default:
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * 用于修改坦克的方向以及坦克的坐标使其移动
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_W){
            playerTank.setDirection(Direction.UP);
            playerTank.move();
        }else if (e.getKeyCode()==KeyEvent.VK_S){
            playerTank.setDirection(Direction.DOWN);
            playerTank.move();
        }else if (e.getKeyCode()==KeyEvent.VK_A){
            playerTank.setDirection(Direction.LEFT);
            playerTank.move();
        }else if (e.getKeyCode()==KeyEvent.VK_D){
            playerTank.setDirection(Direction.RIGHT);
            playerTank.move();
        }

        //玩家射击监听
        if (e.getKeyCode()==KeyEvent.VK_J){
            playerTank.shot(this);
        }
    }

    public void drawBullet(Graphics g, PlayerTank playerTank){
        g.setColor(Color.white);
        g.fillOval(playerTank.getShot().getBullet()[0],playerTank.getShot().getBullet()[1],5,5);
    }
    public void drawBullet(Graphics g,EnemyTank enemyTank){
        g.setColor(Color.white);
        g.fillOval(enemyTank.getShot().getBullet()[0],enemyTank.getShot().getBullet()[1],5,5);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.repaint();
        }
    }
}
