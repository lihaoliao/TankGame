package pri.llh.tankgame;

import pri.llh.tankgame.operations.Recorder;
import pri.llh.tankgame.panel.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * @author LiHao Liao
 * @version 1.0
 * @Package_Name pri.llh.tankgame
 * @date 2023/11/28 14:16
 * @since 1.0
 */
public class Game extends JFrame {

    public static void main(String[] args) {
        // 在EDT中启动游戏
        SwingUtilities.invokeLater(() -> new Game());
    }

    /**
     * 窗口相关参数设置
     */
    public Game() {
        // 创建初始选择对话框
        Object[] options = {"新游戏", "继续游戏"};
        int selection = JOptionPane.showOptionDialog(null,
                "请选择游戏模式：",
                "TankGame",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = defaultToolkit.getScreenSize();
        screenSize.width *= 0.75;
        screenSize.height *= 0.75;

        // 根据用户的选择创建游戏面板
        GamePanel gamePanel = new GamePanel(screenSize.width, screenSize.height, selection == JOptionPane.YES_OPTION ? "1" : "2");
        this.add(gamePanel);
        this.setTitle("Tank Game");
        this.addKeyListener(gamePanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize((int) (screenSize.width * 1.2), (int) (screenSize.height * 1.2));
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        Thread panelThread = new Thread(gamePanel);
        panelThread.setName("GamePanelThread");
        panelThread.start();

        // 监听关闭窗口事件处理
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(
                        null,
                        "是否退出游戏？",
                        "TankGame",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (option == JOptionPane.YES_OPTION) {
                    try {
                        Recorder.keepRecord();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    System.exit(0);
                }
            }
        });
    }
}
