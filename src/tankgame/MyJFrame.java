package tankgame;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

public class MyJFrame extends JFrame {

    MyPanel mp = null;
    Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        MyJFrame myJFrame = new MyJFrame();

    }
    public MyJFrame(){
        System.out.println("请输入选择 1： 新游戏 2： 继续上局 ");
        String key = sc.next();
        mp = new MyPanel(key);
        Thread thread = new Thread(mp);
        thread.start();
        this.add(mp);
        this.setVisible(true);
        this.addKeyListener(mp);
        this.setSize(1300,750);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Recorder.keepRecord();
                System.exit(0);
            }
        });
    }

}
