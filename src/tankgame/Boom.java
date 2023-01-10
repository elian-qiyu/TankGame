package tankgame;

public class Boom {
    int x;
    int y;
    int life = 9;
    boolean isLive = true;

    public Boom(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void liveDown() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (life > 0){
            life--;
        }else {
            isLive = false;
        }
    }
}
