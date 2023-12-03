package pri.llh.tankgame.panel;

import pri.llh.tankgame.enums.Direction;
import pri.llh.tankgame.operations.Boom;
import pri.llh.tankgame.operations.Bullet;
import pri.llh.tankgame.tank.EnemyTank;
import pri.llh.tankgame.tank.PlayerTank;
import pri.llh.tankgame.utils.GameJudgeUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicLong;


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
    /**
     * 敌人坦克
     */
    Vector<EnemyTank> enemyTanks = new Vector<>();
    /**
     * 敌人数量，默认为3
     */
    int enemies = 10;
    Vector<Boom> booms = new Vector<>();
    Image boom1;
    Image boom2;
    Image boom3;

    /**
     * 玩家子弹发射时间控制变量
     */
    private AtomicLong lastShotTime = new AtomicLong(0);

    /**
     * 面板里面坦克初始化
     */
    public GamePanel(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        //玩家出生的位置
        playerTank = new PlayerTank(screenWidth/2, (int) (screenHeight*0.8),Direction.UP,this);
//        playerTank = new PlayerTank(screenWidth-1250, (int) (screenHeight*0.8),Direction.UP);
        //敌人坦克的数量以及出生位置设置
        for (int i = 0; i < enemies; i++) {
            enemyTanks.add(new EnemyTank(((i+1)*100),0,Direction.DOWN,this));
        }
        boom1 = Toolkit.getDefaultToolkit().getImage("src/main/resources/image/boom1.gif");
        boom2 = Toolkit.getDefaultToolkit().getImage("src/main/resources/image/boom2.gif");
        boom3 = Toolkit.getDefaultToolkit().getImage("src/main/resources/image/boom3.gif");
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
        //画玩家坦克
        if(playerTank != null && playerTank.getTankLife() > 0) {
            drawTank(playerTank.getX(), playerTank.getY(), g, playerTank.getDirection(), 2);
        }
        //画玩家子弹
        if(playerTank.isShot()) {
            drawBullet(g, playerTank);
        }

        //画敌人的坦克并且装子弹
        for (int i = 0;i<enemyTanks.size();i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            //敌人是否发射子弹
            if(enemyTank.isShot()) {
                drawBullet(g, enemyTank);
            }
            if (enemyTank.getTankLife()>0){
                drawTank(enemyTank.getX(),enemyTank.getY(),g,enemyTank.getDirection(),1);
            }else {
                enemyTanks.remove(i);
            }
            Thread tankThread = new Thread(enemyTank);
            tankThread.start();
        }

        //敌人发射子弹的频率
        for (int i = 0;i<enemyTanks.size();i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            if(Math.random() < 0.05) {
                enemyTank.shot();
            }
        }

        //画爆炸效果
        for (int i = 0; i < booms.size(); i++) {
            Boom boom = booms.get(i);
            if (boom.getLife() > 6){
                g.drawImage(boom1, boom.getX(), boom.getY(), 60,60,null);
            }else if (boom.getLife() > 3){
                g.drawImage(boom2, boom.getX(), boom.getY(), 60,60,null);
            }else {
                g.drawImage(boom3, boom.getX(), boom.getY(), 60,60,null);
            }
            boom.lifeDown();
            if (boom.getLife() <= 0){
                booms.remove(i);
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
            long currentTimeMillis = System.currentTimeMillis();
            if(System.currentTimeMillis() - lastShotTime.get() >= 1000){
                lastShotTime.set(currentTimeMillis);
                playerTank.shot();
            }
        }
    }

    public void drawBullet(Graphics g, PlayerTank playerTank){
        g.setColor(Color.white);
        Vector<Bullet> bulletVector = playerTank.getBulletVector();
        synchronized (bulletVector) {
            for (int i = 0; i < bulletVector.size(); i++) {
                Bullet bullet = bulletVector.get(i);
                if (!bullet.isRunning()) {
                    bulletVector.remove(i);
                }
                g.fillOval(bullet.getBullet()[0], bullet.getBullet()[1], 5, 5);
            }
        }
    }
    public void drawBullet(Graphics g,EnemyTank enemyTank){
        g.setColor(Color.white);
        Vector<Bullet> bulletVector = enemyTank.getBulletVector();
        synchronized (bulletVector) {
            for (int i = 0; i < bulletVector.size(); i++) {
                Bullet bullet = bulletVector.get(i);
                if (!bullet.isRunning()) {
                    bulletVector.remove(i);
                }
                g.fillOval(bullet.getBullet()[0], bullet.getBullet()[1], 5, 5);
            }
        }
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
            //判断玩家是否击中敌方坦克以及敌方是否击中玩家坦克
            if (playerTank.isShot()){
                for (int i = 0;i<enemyTanks.size();i++) {
                    EnemyTank enemyTank = enemyTanks.get(i);
                    Vector<Bullet> bulletVector = this.playerTank.getBulletVector();
                    for (int j = 0; j < bulletVector.size(); j++) {
                        boolean isHitEnemies = GameJudgeUtils.hitJudge(bulletVector.get(j),enemyTank);
                        if (isHitEnemies){
                            booms.add(new Boom(enemyTank.getX(),enemyTank.getY()));
                        }
                    }
                }
            }
            for (int i = 0; i < enemyTanks.size(); i++) {
                EnemyTank enemyTank = enemyTanks.get(i);
                Vector<Bullet> bulletVector = enemyTank.getBulletVector();
                for (int j = 0; j < bulletVector.size(); j++) {
                    boolean isHitEnemies = GameJudgeUtils.hitJudge(bulletVector.get(j),this.playerTank);
                    if (isHitEnemies){
                        booms.add(new Boom(this.playerTank.getX(),this.playerTank.getY()));
                    }
                }
            }
            this.repaint();
        }
    }
}
