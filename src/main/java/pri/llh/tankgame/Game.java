package pri.llh.tankgame;

import pri.llh.tankgame.panel.GamePanel;

import javax.swing.*;
import java.awt.*;

/**
 * @author LiHao Liao
 * @version 1.0
 * @Package_Name pri.llh.tankgame
 * @date 2023/11/28 14:16
 * @since 1.0
 */
public class Game extends JFrame {

    private GamePanel gamePanel;
    public static void main(String[] args) {
        Game game = new Game();
    }

    /**
     * 窗口相关参数设置
     */
    public Game() {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = defaultToolkit.getScreenSize();
        screenSize.width *= 0.75;
        screenSize.height *= 0.75;
        gamePanel = new GamePanel(screenSize.width,screenSize.height);
        this.add(gamePanel);
        this.setTitle("Tank Game");
        this.addKeyListener(gamePanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(screenSize.width, screenSize.height);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        Thread panelThread = new Thread(gamePanel);
        panelThread.setName("GamePanelThread");
        panelThread.start();
    }
}
