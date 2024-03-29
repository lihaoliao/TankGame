package pri.llh.tankgame.operations;

import pri.llh.tankgame.enums.TankType;
import pri.llh.tankgame.tank.EnemyTank;
import pri.llh.tankgame.tank.PlayerTank;
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
    private static BufferedReader bufferedReader = null;
    private static String recordPath = "src/main/resources/record/record.txt";
    private static Vector<EnemyTank> enemyTanks = null;
    private static Vector<PlayerTank> playerTanks = null;

    public static Vector<PlayerTank> getPlayerTanks() {
        return playerTanks;
    }

    public static void setPlayerTanks(Vector<PlayerTank> playerTanks) {
        Recorder.playerTanks = playerTanks;
    }

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
        playerOnePoints += tank.getType().getValue();
    }

    public static void addPlayerTwoPoints(Tank tank) {
        playerTwoPoints += tank.getType().getValue();
    }

    public static void setPlayerTwoPoints(int playerTwoPoints) {
        Recorder.playerTwoPoints = playerTwoPoints;
    }

    /**
     * 信息记录方法
     *
     * @throws IOException++
     */
    public static void keepRecord() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(recordPath));
        bufferedWriter.write("playerOnePoints " + playerOnePoints);
        bufferedWriter.newLine();
        bufferedWriter.write("playerTwoPoints " + playerTwoPoints);
        bufferedWriter.newLine();
        bufferedWriter.flush();
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
                bufferedWriter.flush();
            }
        }
        for (int i = 0; i < playerTanks.size(); i++) {
            PlayerTank playerTank = playerTanks.get(i);
            if (playerTank.getTankLife() > 0) {
                //保存信息
                String record = playerTank.getX() + " " + playerTank.getY() + " " +
                        playerTank.getDirection() + " " + playerTank.getTankLife() +
                        " " + playerTank.getType() + " " + playerTank.getSpeed() + " " + (i + 1);
                bufferedWriter.write(record);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }
        if (bufferedWriter != null) {
            bufferedWriter.close();
        }
    }

    /**
     * 恢复游戏记录,获取坦克坐标
     *
     * @return 记录坦克坐标的节点列表
     */
    public static Vector<TankNode> recoverRecord() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(recordPath));
            try {
                String[] lineForPlayerOnePoints = bufferedReader.readLine().split(" ");
                String[] lineForPlayerTwoPoints = bufferedReader.readLine().split(" ");
                playerOnePoints = Integer.parseInt(lineForPlayerOnePoints[1]);
                playerTwoPoints = Integer.parseInt(lineForPlayerTwoPoints[1]);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] strings = line.split(" ");
                    TankNode tankNode = new TankNode(Integer.parseInt(strings[0]), Integer.parseInt(strings[1])
                            , strings[2], Integer.parseInt(strings[3]), TankType.valueOf(strings[4])
                            , Integer.parseInt(strings[5]));
                    if (strings.length > 6 && "1".equals(strings[6])) {
                        tankNode.setPlayerIndex(1);
                    } else if (strings.length > 6 && "2".equals(strings[6])) {
                        tankNode.setPlayerIndex(2);
                    }
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
