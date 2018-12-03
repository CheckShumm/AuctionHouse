
import java.util.Timer;
import java.util.TimerTask;

public class AuctionTimer extends Thread{

    private Timer timer;

    private int elapsedTime;
    private TimerTask task;
    public AuctionTimer(){
        task = new TimerTask() {
            @Override
            public void run() {
             elapsedTime++;
            }
        };
    }

    @Override
    public void run() {
        timer = new Timer();
        timer.scheduleAtFixedRate(task, 5 , 3000 );
    }

    public int getElapsedTime() {
        return elapsedTime;
    }


}
