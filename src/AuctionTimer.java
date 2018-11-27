
import java.util.Timer;
import java.util.TimerTask;

public class AuctionTimer {

    private Timer timer;

    public AuctionTimer(){
        TimerTask task = new TimerTask()
        {
            int seconds = 8;
            int i = 0;
            @Override
            public void run()
            {
                i++;

                if(i % seconds == 0)
                    System.out.println("The auction timer has started!");
                else
                    System.out.println("Time left:" + (seconds - (i %seconds)) );
            }
        };

    }

}
