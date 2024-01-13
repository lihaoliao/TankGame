package pri.llh.tankgame.operations;

import pri.llh.tankgame.tank.EnemyTank;
import pri.llh.tankgame.tank.Tank;
import pri.llh.tankgame.tank.TankNode;

import java.io.*;
import java.util.Vector;

/**
 * @author LiHao Liao
 * @version 1.0
 * @Package_Name pri.llh.tankgame.operations
 * @date 2023/12/14 17:55
 * @Description 用于记录相关信息
 * @since 1.0
 */
public class Recorder {
    private static int playerOnePoints = 0;
    private static int playerTwoPoints = 0;
    private static BufferedWriter bufferedWriter = null;
    private static BufferedReader bufferedReader = null;
    private static String recordPath = "src/main/resources/record/record.txt";
    private static Vector<EnemyTank> enemyTanks = null;

    /**
     * 保存敌人坦克信息
     */
    private static Vector<TankNode> tankNodes = new Vector<>();

    public static void setEnemyTanks(Vector<EnemyTank> enemyTanks) {
        Recorder.enemyTanks = enemyTanks;
    }

    public static int getPlayerOnePoints() {
        return playerOnePoints;
    }

    public static void setPlayerOnePoints(int playerOnePoints) {
        Recorder.playerOnePoints = playerOnePoints;
    }

    public static int getPlayerTwoPoints() {
        return playerTwoPoints;
    }

    public static void addPlayerOnePoints(Tank tank) {
        playerOnePoints += tank.getType();
    }

    public static void setPlayerTwoPoints(int playerTwoPoints) {
        Recorder.playerTwoPoints = playerTwoPoints;
    }

    /**
     * 信息记录方法
     * TODO:添加玩家坐标用于恢复游戏
     * @throws IOException++
     */
    public static void keepRecord() throws IOException {
        bufferedWriter = new BufferedWriter(new FileWriter(recordPath));
        bufferedWriter.write(String.valueOf(playerOnePoints));
        bufferedWriter.newLine();
//        bufferedWriter.write(playerTwoPoints);
        //遍历敌人坦克的Vector
        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            if (enemyTank.getTankLife() > 0) {
                //保存信息
                String record = enemyTank.getX() + " " + enemyTank.getY() + " " +
                        enemyTank.getDirection() + " " + enemyTank.getTankLife() +
                        " " + enemyTank.getType() + " " + enemyTank.getSpeed();
                bufferedWriter.write(record);
                bufferedWriter.newLine();
            }
        }
        if (bufferedWriter != null) {
            bufferedWriter.close();
        }
    }

    public static Vector<TankNode> recoverRecord() {
        try {
            bufferedReader = new BufferedReader(new FileReader(recordPath));
            try {
                playerOnePoints = Integer.parseInt(bufferedReader.readLine());
                String line = "";
                while ((line = bufferedReader.readLine()) != null){
                    String[] strings = line.split(" ");
                    TankNode tankNode = new TankNode(Integer.parseInt(strings[0]), Integer.parseInt(strings[1])
                            , strings[2], Integer.parseInt(strings[3]), Integer.parseInt(strings[4])
                            , Integer.parseInt(strings[5]));
                    tankNodes.add(tankNode);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tankNodes;
    }
}
