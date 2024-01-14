package pri.llh.tankgame;

import pri.llh.tankgame.operations.Recorder;
import pri.llh.tankgame.panel.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author LiHao Liao
 * @version 1.0
 * @Package_Name pri.llh.tankgame
 * @date 2023/11/28 14:16
 * @since 1.0
 */
public class Game extends JFrame {

    private GamePanel gamePanel;
    Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        Game game = new Game();
    }

    /**
     * 窗口相关参数设置
     */
    public Game() {
//        System.out.println("请选择:1.新游戏 2:继续游戏");
//        String selection = scanner.next();
        String selection = "2";
        while (!"1".equals(selection) && !"2".equals(selection)){
            System.out.println("请选择:1.新游戏 2:继续游戏");
            selection = scanner.next();
        }
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = defaultToolkit.getScreenSize();
        screenSize.width *= 0.75;
        screenSize.height *= 0.75;
        gamePanel = new GamePanel(screenSize.width,screenSize.height, selection);
        this.add(gamePanel);
        this.setTitle("Tank Game");
        this.addKeyListener(gamePanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize((int) (screenSize.width * 1.2), (int) (screenSize.height * 1.2));
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        Thread panelThread = new Thread(gamePanel);
        panelThread.setName("GamePanelThread");
        panelThread.start();

        //监听关闭窗口事件处理
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("游戏结束");
                try {
                    Recorder.keepRecord();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
                super.windowClosing(e);
            }
        });
    }
}
