package tankgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Vector;

public class MyPanel extends JPanel implements KeyListener, Runnable {
    MyTank mytank = null;
    Vector<EnemyTank> enemyTanks = new Vector<>();
    Vector<Node> nodes = new Vector<>();


    Vector<Boom> booms = new Vector<>();
    int enemyTankSize = 6;
    Image image1 = null;
    Image image2 = null;
    Image image3 = null;

    public MyPanel(String key) {
        File file = new File(Recorder.getRecordFile());
        if (file.exists()){
            nodes = Recorder.getNodes();
        }else {
            System.out.println("只能开启新游戏");
            key = "1";
        }

        mytank = new MyTank(600, 600);
        Recorder.setEnemyTanks(enemyTanks);

        switch (key){
            case "1":
                for (int i = 0; i < enemyTankSize; i++) {
                    EnemyTank enemyTank = new EnemyTank(100 * (i + 1), 0);
                    enemyTank.setEnemyTanks(enemyTanks);
                    enemyTank.setDirect(2);
                    Recorder.setTankNum(0);
                    new Thread(enemyTank).start();
                    Shot shot = new Shot(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirect());
                    enemyTank.shots.add(shot);
                    new Thread(shot).start();
                    enemyTanks.add(enemyTank);
                }
                break;
            case "2":
                for (int i = 0; i < nodes.size(); i++) {
                    Node node = nodes.get(i);
                    EnemyTank enemyTank = new EnemyTank(node.getX(), node.getY());
                    enemyTank.setEnemyTanks(enemyTanks);
                    enemyTank.setDirect(node.getDirect());

                    new Thread(enemyTank).start();
                    Shot shot = new Shot(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirect());
                    enemyTank.shots.add(shot);
                    new Thread(shot).start();
                    enemyTanks.add(enemyTank);
                }
                break;
        }


        //image1 = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("/boom1.gif"));
        //image1 = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("/boom2.gif"));
        //image1 = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("/boom3.gif"));
        mytank.setSpeed(2);
    }

    public void showInfo(Graphics g){
        g.setColor(Color.black);
        Font font = new Font("宋体", Font.BOLD, 25);
        g.setFont(font);
        g.drawString("您累计击毁敌方坦克", 1020, 30);
        drawTank(1020, 60, g, 0, 1);
        g.setColor(Color.black);
        g.drawString(Recorder.getTankNum()+ "", 1080, 100);
    }

    public void hitMytank(){
        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            for (int j = 0; j < enemyTank.shots.size(); j++) {
                Shot shot = enemyTank.shots.get(j);
                if (mytank.isLive && shot.isLive){
                    hitTank(shot, mytank);
                }
            }
        }
    }

    public void hitTanks(){
        for (int j = 0; j < mytank.shots.size(); j++) {
            Shot shot = mytank.shots.get(j);
            if (shot != null && shot.isLive) {
                for (int i = 0; i < enemyTanks.size(); i++) {
                    EnemyTank enemyTank = enemyTanks.get(i);
                    hitTank(shot, enemyTank);
                }
            }
        }

    }

    public void hitTank(Shot shot, Tank enemyTank) {
        switch (enemyTank.getDirect()) {
            case 0:
            case 2:
                if (shot.x > enemyTank.getX() && shot.x < enemyTank.getX() + 40 &&
                        shot.y > enemyTank.getY() && shot.y < enemyTank.getY() + 60) {
                    shot.isLive = false;
                    enemyTank.isLive = false;
                    enemyTanks.remove(enemyTank);
                    if (enemyTank instanceof EnemyTank){
                        Recorder.addTankNum();
                    }

                    Boom boom = new Boom(enemyTank.getX(), enemyTank.getY());
                    booms.add(boom);
                }
                break;
            case 1:
            case 3:
                if (shot.x > enemyTank.getX() && shot.x < enemyTank.getX() + 60 &&
                        shot.y > enemyTank.getY() && shot.y < enemyTank.getY() + 40) {
                    shot.isLive = false;
                    enemyTank.isLive = false;
                    enemyTanks.remove(enemyTank);
                    if (enemyTank instanceof EnemyTank){
                        Recorder.addTankNum();
                    }
                    Boom boom = new Boom(enemyTank.getX(), enemyTank.getY());
                    booms.add(boom);
                }
                break;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        showInfo(g);
        g.fillRect(0, 0, 1000, 750);
        if (mytank != null && mytank.isLive) {
            drawTank(mytank.getX(), mytank.getY(), g, mytank.getDirect(), 0);
        }

        for (int i = 0; i < booms.size(); i++) {
            Boom boom = booms.get(i);
            if (boom.life > 6) {
                g.drawImage(image1, boom.x, boom.y, 80, 80, this);
            } else if (boom.life > 3) {
                g.drawImage(image2, boom.x, boom.y, 80, 80, this);
            } else {
                g.drawImage(image3, boom.x, boom.y, 80, 80, this);
            }
            boom.liveDown();
            if (boom.life == 0) {
                booms.remove(boom);
            }
        }


        for (int i = 0; i < mytank.shots.size(); i++) {
            Shot shot = mytank.shots.get(i);
            if (shot != null && shot.isLive) {
                g.draw3DRect(shot.x, shot.y, 1, 1, false);
            } else {
                mytank.shots.remove(shot);
            }
        }

        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            if (enemyTank.isLive) {
                drawTank(enemyTank.getX(), enemyTank.getY(), g, enemyTank.getDirect(), 1);
                for (int i1 = 0; i1 < enemyTank.shots.size(); i1++) {
                    Shot shot = enemyTank.shots.get(i1);
                    if (shot.isLive) {
                        g.draw3DRect(shot.x, shot.y, 1, 1, false);
                    } else {
                        enemyTank.shots.remove(shot);
                    }
                }
            }

        }
    }

    public void drawTank(int x, int y, Graphics g, int dir, int type) {

        //颜色
        switch (type) {
            case 0://自己的坦克
                g.setColor(Color.PINK);
                break;
            case 1:
                g.setColor(Color.GREEN);
                break;
        }
        //方向
        switch (dir) {
            case 0://向上
                g.fill3DRect(x, y, 10, 60, false);
                g.fill3DRect(x + 30, y, 10, 60, false);
                g.fill3DRect(x + 10, y + 10, 20, 40, false);
                g.fillOval(x + 10, y + 20, 20, 20);
                g.drawLine(x + 20, y + 30, x + 20, y);
                break;
            case 1://向右
                g.fill3DRect(x, y, 60, 10, false);
                g.fill3DRect(x, y + 30, 60, 10, false);
                g.fill3DRect(x + 10, y + 10, 40, 20, false);
                g.fillOval(x + 20, y + 10, 20, 20);
                g.drawLine(x + 30, y + 20, x + 60, y + 20);
                break;
            case 2://向下
                g.fill3DRect(x, y, 10, 60, false);
                g.fill3DRect(x + 30, y, 10, 60, false);
                g.fill3DRect(x + 10, y + 10, 20, 40, false);
                g.fillOval(x + 10, y + 20, 20, 20);
                g.drawLine(x + 20, y + 30, x + 20, y + 60);
                break;
            case 3:
                g.fill3DRect(x, y, 60, 10, false);
                g.fill3DRect(x, y + 30, 60, 10, false);
                g.fill3DRect(x + 10, y + 10, 40, 20, false);
                g.fillOval(x + 20, y + 10, 20, 20);
                g.drawLine(x + 30, y + 20, x, y + 20);
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            mytank.setDirect(0);
            if (mytank.getY() > 0) {
                mytank.moveUp();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            mytank.setDirect(3);
            if (mytank.getX() > 0) {
                mytank.moveLeft();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            mytank.setDirect(2);
            if (mytank.getY() + 60 < 750) {
                mytank.moveDown();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            mytank.setDirect(1);
            if (mytank.getX() + 60 < 1000) {
                mytank.moveRight();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_J) {
            mytank.shotEnemy();
        }
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            hitTanks();
            hitMytank();
            this.repaint();
        }
    }
}
