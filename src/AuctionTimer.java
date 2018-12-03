
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class AuctionTimer extends Thread{

    private Timer timer;

    private int elapsedTime;
    private int timeOut = 60;
    private TimerTask task;

    public AuctionTimer(){
        task = new TimerTask() {
            @Override
            public void run() {
             elapsedTime++;
             for (Item item : ItemHandler.getInstance().getArray()) {
                 if(!item.getSold()){
                     if((elapsedTime - item.getStartTime()) > timeOut ){
                        // System.out.println("BID OVER");
                         handleItem(item);
                     }
                 }
                 //System.out.println("Elapsed Time: " + elapsedTime);
                 //System.out.println("Time left:" + (timeOut - (elapsedTime - item.getStartTime())));;
             }
            }
        };
    }

    @Override
    public void run() {
        timer = new Timer();
        timer.scheduleAtFixedRate(task, 1 , 1000 );
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    private void handleItem(Item item) {
        item.setSold(true);
        notifyBidders(item, MessageType.BIG_OVER);

        if( item.getCurrentBid() == 0) {
            notifyBidders(item, MessageType.NOT_SOLD);
        } else {
           // notify winner
            notifyWinner(item);
            // notify users
            notifyBidders(item, MessageType.SOLDTO);
        }

    }

    private void notifyBidders(Item item, String type) {
        for(ClientHandler handler : ClientHandlers.getInstance().getArray()) {
            if(item.getBidders().contains(handler.getUser()) || handler.getUser() == item.getOwner())
            try {
                handler.getMsg().setType(type);
                handler.getMsg().setItem(item);
                handler.sendMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void notifyWinner(Item item) {
        for(ClientHandler handler : ClientHandlers.getInstance().getArray()) {
            if(handler.getUser() == item.getTopBidder()) {
                try {
                    handler.getMsg().setType(MessageType.WIN);
                    handler.getMsg().setItem(item);
                    handler.sendMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
