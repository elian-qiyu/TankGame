package tankgame;

import java.io.*;
import java.util.Vector;

public class Recorder {

    private static int TANK_NUM = 0;

    private static BufferedWriter bufferedWriter = null;
    private static BufferedReader bufferedReader = null;
    public static String recordFile = "myRecord.txt";

    public static Vector<EnemyTank> enemyTanks = null;

    private static Vector<Node> nodes = new Vector<>();

    public static String getRecordFile() {
        return recordFile;
    }

    public static Vector<Node> getNodes(){
        try {
            bufferedReader = new BufferedReader(new FileReader(recordFile));
            TANK_NUM =Integer.parseInt( bufferedReader.readLine());
            String line = null;
            while ((line = bufferedReader.readLine()) != null){
                String[] s = line.split(" ");
                Node node = new Node(Integer.parseInt(s[0]), Integer.parseInt(s[1]),
                        Integer.parseInt(s[2]));
                nodes.add(node);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return nodes;
    }

    public static void setEnemyTanks(Vector<EnemyTank> enemyTanks) {
        Recorder.enemyTanks = enemyTanks;
    }

    public static void keepRecord(){
        try {
             bufferedWriter = new BufferedWriter(new FileWriter(recordFile));
             bufferedWriter.write(TANK_NUM +"\r\n");
            for (int i = 0; i < enemyTanks.size(); i++) {
                EnemyTank enemyTank = enemyTanks.get(i);
                if (enemyTank.isLive){
                    String recorder = enemyTank.getX()+" "+ enemyTank.getY()+" "+ enemyTank.getDirect();
                    bufferedWriter.write(recorder + "\r\n");
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (bufferedWriter != null){
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static int getTankNum() {
        return TANK_NUM;
    }

    public static void setTankNum(int tankNum) {
        TANK_NUM = tankNum;
    }

    public static void addTankNum(){
        Recorder.TANK_NUM++;
    }
}
