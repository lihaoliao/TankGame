package pri.llh.tankgame.panel;

import pri.llh.tankgame.enums.Direction;
import pri.llh.tankgame.enums.TankType;
import pri.llh.tankgame.items.IndestructibleWall;
import pri.llh.tankgame.items.NormalWall;
import pri.llh.tankgame.items.Wall;
import pri.llh.tankgame.operations.Boom;
import pri.llh.tankgame.operations.Bullet;
import pri.llh.tankgame.operations.Recorder;
import pri.llh.tankgame.tank.EnemyTank;
import pri.llh.tankgame.tank.PlayerTank;
import pri.llh.tankgame.tank.TankNode;
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

    PlayerTank playerTankTwo;
    PlayerTank playerTankOne;
    /**
     * 敌人坦克
     */
    Vector<EnemyTank> enemyTanks = new Vector<>();
    /**
     * 玩家坦克
     */
    Vector<PlayerTank> playerTanks = new Vector<>();
    /**
     * 敌人数量，默认为3
     */
    int enemies = 11;
    /**
     * 用于记录上局游戏的Vector
     */
    Vector<TankNode> tankNodes;
    Vector<Wall> wallList = new Vector<>();
    Vector<Boom> booms = new Vector<>();
    Image boom1;
    Image boom2;
    Image boom3;

    /**
     * 玩家子弹发射时间控制变量
     */
    private AtomicLong lastShotTimePlayerOne = new AtomicLong(0);
    private AtomicLong lastShotTimePlayerTwo = new AtomicLong(0);

    /**
     * 面板里面坦克初始化
     */
    public GamePanel(int screenWidth, int screenHeight, String selection) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        Recorder.setEnemyTanks(this.enemyTanks);
        Recorder.setPlayerTanks(this.playerTanks);
        tankNodes = Recorder.recoverRecord();
        //playerTank = new PlayerTank(screenWidth-1250, (int) (screenHeight*0.8),Direction.UP);
        if ("1".equals(selection)) {
            //玩家出生的位置
            playerTankOne = new PlayerTank(screenWidth/2, (int) (screenHeight*0.8),Direction.UP,this,TankType.Player);
            playerTankTwo = new PlayerTank(screenWidth/2 + 100, (int) (screenHeight*0.8),Direction.UP,this,TankType.Player);
            playerTanks.add(playerTankOne);
            playerTanks.add(playerTankTwo);
            Recorder.setPlayerOnePoints(0);
            Recorder.setPlayerTwoPoints(0);
            //敌人坦克的数量以及出生位置设置
            for (int i = 0; i < enemies; i++) {
                enemyTanks.add(new EnemyTank(((i + 1) * 100), 0, Direction.DOWN, this, TankType.EnemyOne));
            }
        } else if ("2".equals(selection)) {
            for (int i = 0; i < tankNodes.size(); i++) {
                TankNode tankNode = tankNodes.get(i);
                Direction direction = null;
                if ("UP".equals(tankNode.getDirection())) {
                    direction = Direction.UP;
                }else if ("DOWN".equals(tankNode.getDirection())){
                    direction = Direction.DOWN;
                }else if("LEFT".equals(tankNode.getDirection())){
                    direction = Direction.LEFT;
                }else if("RIGHT".equals(tankNode.getDirection())){
                    direction = Direction.RIGHT;
                }
                EnemyTank enemyTank = new EnemyTank(tankNode.getX(), tankNode.getY(), direction, this, TankType.valueOf(tankNode.getType()));
                enemyTank.setTankLife(tankNode.getTankLife());
                enemyTank.setSpeed(tankNode.getSpeed());
                if(enemyTank.getType() == TankType.Player){
                    PlayerTank playerTank = new PlayerTank(tankNode.getX(), tankNode.getY(), direction, this, TankType.valueOf(tankNode.getType()));
                    if(tankNode.getPlayerIndex() == 1){
                        playerTankOne = playerTank;
                    }else if(tankNode.getPlayerIndex() == 2){
                        playerTankTwo = playerTank;
                    }
                    playerTanks.add(playerTank);
                    continue;
                }
                enemyTanks.add(enemyTank);
            }
        }
        boom1 = Toolkit.getDefaultToolkit().getImage("src/main/resources/image/boom1.gif");
        boom2 = Toolkit.getDefaultToolkit().getImage("src/main/resources/image/boom2.gif");
        boom3 = Toolkit.getDefaultToolkit().getImage("src/main/resources/image/boom3.gif");
    }

    /**
     * 绘制面板以及其他相关组件
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //默认是黑色
        g.fillRect(0,0,this.screenWidth,this.screenHeight);
        showInfo(g);

        //画玩家坦克
        drawPlayerTank(g);

        drawPlayerBullet(g);

        drawEnemyTankAndBullet(g);

        //敌人发射子弹,有敌人才会射击
        for (int i = 0;i<enemyTanks.size();i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            if (shotThePlayer(enemyTank)){
                enemyTank.shot();
            }
        }

        drawBoom(g);

        drawWall(g);

    }

    /**
     * 绘制玩家的坦克
     * @param g
     */
    private void drawPlayerTank(Graphics g) {
        for (int i = 0; i < playerTanks.size(); i++) {
            PlayerTank playerTank = playerTanks.get(i);
            if(playerTank != null && playerTank.getTankLife() > 0) {
                if( i == 0) {
                    drawTank(playerTank.getX(), playerTank.getY(), g, playerTank.getDirection(), playerTank.getType(), Color.cyan);
                }else {
                    drawTank(playerTank.getX(), playerTank.getY(), g, playerTank.getDirection(), playerTank.getType(), Color.BLUE);
                }
            }
        }
    }

    /**
     * 画玩家子弹
     * @param g
     */
    private void drawPlayerBullet(Graphics g) {
        assert playerTankOne != null;
        if(playerTankOne != null && playerTankOne.isShot()) {
            drawBullet(g, playerTankOne);
        }
        assert  playerTankTwo != null;
        if(playerTankTwo != null && playerTankTwo.isShot()) {
            drawBullet(g, playerTankTwo);
        }
    }

    /**
     * 判断玩家是否在弹道上
     * @param enemyTank
     * @return 是否射击
     */
    private boolean shotThePlayer(EnemyTank enemyTank) {
        int bulletY;
        int bulletX;
        for (int i = 0; i < playerTanks.size(); i++) {
            PlayerTank playerTank = playerTanks.get(i);
            switch (enemyTank.getDirection()){
                case UP:
                    bulletY = enemyTank.getY() - 10;
                    bulletX = (int) (enemyTank.getX() + 0.5* EnemyTank.TANK_TOTAL_WIDTH);
                    if (playerTank.getDirection().equals(Direction.UP) || playerTank.getDirection().equals(Direction.DOWN)){
                        if(playerTank.getY() < bulletY && playerTank.getX() <= bulletX && playerTank.getX() + PlayerTank.TANK_TOTAL_WIDTH >= bulletX){
                            return true;
                        }
                    }else {
                        if (playerTank.getY() < bulletY && playerTank.getX() <= bulletX && playerTank.getX() + PlayerTank.TANK_TOTAL_HEIGHT >= bulletX){
                            return true;
                        }
                    }
                case DOWN:
                    bulletY = enemyTank.getY() + EnemyTank.TANK_TOTAL_HEIGHT + 10;
                    bulletX = (int) (enemyTank.getX() + 0.5* EnemyTank.TANK_TOTAL_WIDTH);
                    if (playerTank.getDirection().equals(Direction.UP) || playerTank.getDirection().equals(Direction.DOWN)){
                        if(playerTank.getY() > bulletY && playerTank.getX() <= bulletX && playerTank.getX() + PlayerTank.TANK_TOTAL_WIDTH >= bulletX){
                            return true;
                        }
                    }else {
                        if (playerTank.getY() > bulletY && playerTank.getX() <= bulletX && playerTank.getX() + PlayerTank.TANK_TOTAL_HEIGHT >= bulletX){
                            return true;
                        }
                    }
                case LEFT:
                    bulletY = (int) (enemyTank.getY() + 0.5 * EnemyTank.TANK_TOTAL_WIDTH);
                    bulletX = enemyTank.getX() - 10;
                    if (playerTank.getDirection().equals(Direction.UP) || playerTank.getDirection().equals(Direction.DOWN)){
                        if(playerTank.getX() < bulletX && playerTank.getY() <= bulletY && playerTank.getY() + PlayerTank.TANK_TOTAL_HEIGHT >= bulletY){
                            return true;
                        }
                    }else {
                        if (playerTank.getX() < bulletX && playerTank.getY() <= bulletY && playerTank.getY() + PlayerTank.TANK_TOTAL_WIDTH >= bulletY){
                            return true;
                        }
                    }
                case RIGHT:
                    bulletY = (int) (enemyTank.getY() + 0.5 * EnemyTank.TANK_TOTAL_WIDTH);
                    bulletX = enemyTank.getX() + EnemyTank.TANK_TOTAL_HEIGHT + 10;
                    if (playerTank.getDirection().equals(Direction.UP) || playerTank.getDirection().equals(Direction.DOWN)){
                        if(playerTank.getX() > bulletX && playerTank.getY() <= bulletY && playerTank.getY() + PlayerTank.TANK_TOTAL_HEIGHT >= bulletY){
                            return true;
                        }
                    }else {
                        if (playerTank.getX() > bulletX && playerTank.getY() <= bulletY && playerTank.getY() + PlayerTank.TANK_TOTAL_WIDTH >= bulletY){
                            return true;
                        }
                    }
                default:
                    return false;
            }
        }
        return false;
    }

    private boolean init = true;

    /**
     * 绘制墙体
     * TODO:根据关卡绘制不同的墙体
     * @param g
     */
    private void drawWall(Graphics g) {
        int x = screenWidth/2 - 400;
        int y = screenHeight/2 - 200;
        if(init) {
            for (int i = 0; i < 10; i++) {
                NormalWall wall = new NormalWall(x, y);
                wallList.add(wall);
                x += wall.getWeight();
            }
            for (int i = 0; i < 30; i++) {
                IndestructibleWall wall = new IndestructibleWall(x, y);
                wall.setDestructible(false);
                wallList.add(wall);
                x += wall.getWeight();
            }
            init = false;
        }
        for (int i = 0; i < wallList.size(); i++) {
            Wall wall = wallList.get(i);
            if(wall.isExist()) {
                if(wall instanceof NormalWall) {
                    g.setColor(Color.orange);
                }
                if(wall instanceof IndestructibleWall){
                    g.setColor(Color.lightGray);
                }
                g.fill3DRect(wall.getX(), wall.getY(), wall.getHeight(), wall.getWeight(), true);
            }else {
                wallList.remove(i);
            }
        }
    }

    /**
     * 画敌人的坦克并且装子弹
     * @param g
     */
    private void drawEnemyTankAndBullet(Graphics g) {
        for (int i = 0;i<enemyTanks.size();i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            //敌人是否发射子弹
            if(enemyTank.isShot()) {
                drawBullet(g, enemyTank);
            }
            //判断敌人坦克是否已经死亡
            if (enemyTank.getTankLife()>0){
                drawTank(enemyTank.getX(),enemyTank.getY(), g,enemyTank.getDirection(),enemyTank.getType());
            }else {
                enemyTanks.remove(i);
            }
            Thread tankThread = new Thread(enemyTank);
            tankThread.start();
        }
    }

    /**
     * 画爆炸效果
     * @param g
     */
    private void drawBoom(Graphics g) {
        for (int i = 0; i < booms.size(); i++) {
            Boom boom = booms.get(i);
            if (boom.getLife() > 6){
                g.drawImage(boom1, boom.getX(), boom.getY(), 40,40,null);
            }else if (boom.getLife() > 3){
                g.drawImage(boom2, boom.getX(), boom.getY(), 40,40,null);
            }else {
                g.drawImage(boom3, boom.getX(), boom.getY(), 40,40,null);
            }
            boom.lifeDown();
            if (boom.getLife() <= 0){
                booms.remove(i);
            }
        }
    }

    /**
     * 坦克的初始化以及重绘
     * TODO:设计更多种类的坦克
     * @param x 坦克左上角x坐标
     * @param y 坦克左上角y坐标
     * @param g
     * @param direction 坦克方向 - 上下左右
     * @param type  坦克类型
     */
    public void drawTank(int x, int y, Graphics g, Direction direction, TankType type) {
        switch (type){
            //enemies 普通版本
            case EnemyOne:
                g.setColor(Color.RED);
                break;
            //player 玩家
            case Player:
                g.setColor(Color.cyan);
                break;
            case EnemyTwo:
                g.setColor(Color.GREEN);
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

    public void drawTank(int x, int y, Graphics g, Direction direction, TankType type, Color color) {
        switch (type){
            //enemies 普通版本
            case EnemyOne:
                g.setColor(Color.RED);
                break;
            //player 玩家
            //TODO:自选坦克颜色
            case Player:
                g.setColor(color);
                break;
            case EnemyTwo:
                g.setColor(Color.GREEN);
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

    private boolean upPressed, downPressed, leftPressed, rightPressed,onePressed;
    private boolean wPressed, sPressed, aPressed, dPressed, jPressed;
    /**
     * 用于修改坦克的方向以及坦克的坐标使其移动
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_W){
            wPressed = true;
        }else if (e.getKeyCode()==KeyEvent.VK_S){
            sPressed = true;
        }else if (e.getKeyCode()==KeyEvent.VK_A){
            aPressed = true;
        }else if (e.getKeyCode()==KeyEvent.VK_D){
            dPressed = true;
        }

        if(e.getKeyCode()==KeyEvent.VK_UP){
            upPressed = true;
        }else if (e.getKeyCode()==KeyEvent.VK_DOWN){
            downPressed = true;
        }else if (e.getKeyCode()==KeyEvent.VK_LEFT){
            leftPressed = true;
        }else if (e.getKeyCode()==KeyEvent.VK_RIGHT){
            rightPressed = true;
        }

        //玩家射击监听
        if (e.getKeyCode()==KeyEvent.VK_J){
            jPressed = true;
            }
        if (e.getKeyCode()==KeyEvent.VK_NUMPAD1){
            onePressed = true;
        }
    }
    /**
     * 绘制玩家坦克的子弹。
     * 此方法遍历玩家坦克的子弹向量（Vector），并为每个正在运动的子弹绘制一个圆形代表其当前位置。
     * 如果子弹不再运动（例如击中目标或超出界面），则从向量中移除该子弹。
     *
     * @param g 图形上下文环境，用于在面板上绘制图形。
     * @param playerTank 玩家的坦克对象，包含子弹的信息。
     */
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

    /**
     * 绘制敌方坦克的子弹。
     * 此方法遍历敌方坦克的子弹向量（Vector），并为每个正在运动的子弹绘制一个圆形代表其当前位置。
     * 如果子弹不再运动（例如击中目标或超出界面），则从向量中移除该子弹。
     *
     * @param g 图形上下文环境，用于在面板上绘制图形。
     * @param enemyTank 敌方坦克对象，包含子弹的信息。
     */
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

    public void showInfo(Graphics g){
        g.setColor(Color.black);
        Font font = new Font("宋体",Font.BOLD,25);
        g.setFont(font);
        g.drawString("玩家1分数: " + Recorder.getPlayerOnePoints(), screenWidth, (int) (screenHeight * 0.1));
        g.drawString("玩家2分数: " + Recorder.getPlayerTwoPoints(), screenWidth, (int) (screenHeight * 0.2));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_W){
            wPressed = false;
        }else if (e.getKeyCode()==KeyEvent.VK_S){
            sPressed = false;
        }else if (e.getKeyCode()==KeyEvent.VK_A){
            aPressed = false;
        }else if (e.getKeyCode()==KeyEvent.VK_D){
            dPressed = false;
        }

        if(e.getKeyCode()==KeyEvent.VK_UP){
            upPressed = false;
        }else if (e.getKeyCode()==KeyEvent.VK_DOWN){
            downPressed = false;
        }else if (e.getKeyCode()==KeyEvent.VK_LEFT){
            leftPressed = false;
        }else if (e.getKeyCode()==KeyEvent.VK_RIGHT){
            rightPressed = false;
        }

        //玩家射击监听
        if (e.getKeyCode()==KeyEvent.VK_J){
            jPressed = false;
        }
        if (e.getKeyCode()==KeyEvent.VK_NUMPAD1){
            onePressed = false;
        }
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public Vector<EnemyTank> getEnemyTanks() {
        return enemyTanks;
    }

    public Vector<Wall> getWallList() {
        return wallList;
    }

    public void setWallList(Vector<Wall> wallList) {
        this.wallList = wallList;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //游戏结束自动退出
            if (playerTankOne != null && playerTankOne.getTankLife() <= 0 && playerTankTwo != null && playerTankTwo.getTankLife() < 0){
                JOptionPane.showMessageDialog(null, "游戏结束", "TankGame", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
            //判断玩家是否击中敌方坦克以及敌方是否击中玩家坦克
            //判断玩家是否击中墙体
            if (playerTankOne != null && playerTankOne.isShot()){
                for (int i = 0;i<enemyTanks.size();i++) {
                    EnemyTank enemyTank = enemyTanks.get(i);
                    Vector<Bullet> bulletVector = playerTankOne.getBulletVector();
                    for (int j = 0; j < bulletVector.size(); j++) {
                        boolean isHitEnemies = GameJudgeUtils.hitTankJudge(bulletVector.get(j),enemyTank);
                        if (isHitEnemies){
                            Recorder.addPlayerOnePoints(enemyTank);
                            booms.add(new Boom(enemyTank.getX(),enemyTank.getY()));
                        }
                    }
                }
                for (int i = 0; i < wallList.size(); i++) {
                    Wall wall = wallList.get(i);
                    Vector<Bullet> bulletVector = this.playerTankOne.getBulletVector();
                    for (int j = 0; j < bulletVector.size(); j++) {
                        boolean isHitWall = GameJudgeUtils.hitWallJudge(bulletVector.get(j),wall);
                        if (isHitWall){
                            booms.add(new Boom(wall.getX(),wall.getY()));
                        }
                    }
                }
            }
            if (playerTankTwo != null && playerTankTwo.isShot()){
                for (int i = 0;i<enemyTanks.size();i++) {
                    EnemyTank enemyTank = enemyTanks.get(i);
                    Vector<Bullet> bulletVector = playerTankTwo.getBulletVector();
                    for (int j = 0; j < bulletVector.size(); j++) {
                        boolean isHitEnemies = GameJudgeUtils.hitTankJudge(bulletVector.get(j),enemyTank);
                        if (isHitEnemies){
                            Recorder.addPlayerTwoPoints(enemyTank);
                            booms.add(new Boom(enemyTank.getX(),enemyTank.getY()));
                        }
                    }
                }
                for (int i = 0; i < wallList.size(); i++) {
                    Wall wall = wallList.get(i);
                    Vector<Bullet> bulletVector = this.playerTankTwo.getBulletVector();
                    for (int j = 0; j < bulletVector.size(); j++) {
                        boolean isHitWall = GameJudgeUtils.hitWallJudge(bulletVector.get(j),wall);
                        if (isHitWall){
                            booms.add(new Boom(wall.getX(),wall.getY()));
                        }
                    }
                }
            }

            //敌方是否击中玩家
            //判断敌方是否击中墙体
            if(this.playerTankOne != null && this.playerTankOne.getTankLife() > 0) {
                for (int i = 0; i < enemyTanks.size(); i++) {
                    EnemyTank enemyTank = enemyTanks.get(i);
                    Vector<Bullet> bulletVector = enemyTank.getBulletVector();
                    for (int j = 0; j < bulletVector.size(); j++) {
                        boolean isHitEnemies = GameJudgeUtils.hitTankJudge(bulletVector.get(j), this.playerTankOne);
                        if (isHitEnemies) {
                            booms.add(new Boom(this.playerTankOne.getX(), this.playerTankOne.getY()));
                        }
                    }
                    for (int j = 0; j < wallList.size(); j++) {
                        Wall wall = wallList.get(j);
                        for (int k = 0; k < bulletVector.size(); k++) {
                            boolean isHitWall = GameJudgeUtils.hitWallJudge(bulletVector.get(k),wall);
                            if (isHitWall){
                                booms.add(new Boom(wall.getX(),wall.getY()));
                            }
                        }
                    }
                }
            }
            if(this.playerTankTwo != null && this.playerTankTwo.getTankLife() > 0) {
                for (int i = 0; i < enemyTanks.size(); i++) {
                    EnemyTank enemyTank = enemyTanks.get(i);
                    Vector<Bullet> bulletVector = enemyTank.getBulletVector();
                    for (int j = 0; j < bulletVector.size(); j++) {
                        boolean isHitEnemies = GameJudgeUtils.hitTankJudge(bulletVector.get(j), this.playerTankTwo);
                        if (isHitEnemies) {
                            booms.add(new Boom(this.playerTankTwo.getX(), this.playerTankTwo.getY()));
                        }
                    }
                    for (int j = 0; j < wallList.size(); j++) {
                        Wall wall = wallList.get(j);
                        for (int k = 0; k < bulletVector.size(); k++) {
                            boolean isHitWall = GameJudgeUtils.hitWallJudge(bulletVector.get(k),wall);
                            if (isHitWall){
                                booms.add(new Boom(wall.getX(),wall.getY()));
                            }
                        }
                    }
                }
            }
            playerTankMove();
            this.repaint();
        }
    }

    private void playerTankMove() {
        if(wPressed){
            Direction preDirection = playerTankOne.getDirection();
            playerTankOne.setDirection(Direction.UP);
            playerTankOne.move(preDirection);
        }else if (sPressed){
            Direction preDirection = playerTankOne.getDirection();
            playerTankOne.setDirection(Direction.DOWN);
            playerTankOne.move(preDirection);
        }else if (aPressed){
            Direction preDirection = playerTankOne.getDirection();
            playerTankOne.setDirection(Direction.LEFT);
            playerTankOne.move(preDirection);
        }else if (dPressed){
            Direction preDirection = playerTankOne.getDirection();
            playerTankOne.setDirection(Direction.RIGHT);
            playerTankOne.move(preDirection);
        }

        if(upPressed){
            Direction preDirection = playerTankTwo.getDirection();
            playerTankTwo.setDirection(Direction.UP);
            playerTankTwo.move(preDirection);
        }else if (downPressed){
            Direction preDirection = playerTankTwo.getDirection();
            playerTankTwo.setDirection(Direction.DOWN);
            playerTankTwo.move(preDirection);
        }else if (leftPressed){
            Direction preDirection = playerTankTwo.getDirection();
            playerTankTwo.setDirection(Direction.LEFT);
            playerTankTwo.move(preDirection);
        }else if (rightPressed){
            Direction preDirection = playerTankTwo.getDirection();
            playerTankTwo.setDirection(Direction.RIGHT);
            playerTankTwo.move(preDirection);
        }

        //玩家射击监听
        if (jPressed){
            long currentTimeMillis = System.currentTimeMillis();
            if(System.currentTimeMillis() - lastShotTimePlayerOne.get() >= 1000){
                lastShotTimePlayerOne.set(currentTimeMillis);
                playerTankOne.shot();
            }
        }
        if (onePressed){
            long currentTimeMillis = System.currentTimeMillis();
            if(System.currentTimeMillis() - lastShotTimePlayerTwo.get() >= 1000){
                lastShotTimePlayerTwo.set(currentTimeMillis);
                playerTankTwo.shot();
            }
        }
    }

    public Vector<PlayerTank> getPlayerTanks() {
        return playerTanks;
    }
}
