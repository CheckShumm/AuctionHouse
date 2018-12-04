import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

public class ClientHandler extends Thread{

    //Logger
    private static Logger log = LogManager.getLogger("auctionhouse");

    // TCP
    private Socket socket = null;
    private Item item;

    public ObjectOutputStream oos;
    private ObjectInputStream ois;

    private User user;
    private Message msg;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.user = new User();
    }

    @Override
    public void run() {
        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());

            while (true) {
                this.msg = (Message)ois.readObject();
                log.trace(msg  + " " + msg.getUser().getRequestCount() + " " + socket.getRemoteSocketAddress().toString()) ;
                switch(msg.getType()) {
                    case "OFFER":
                        offer();
                        if (item.getMinPrice() < 0 && msg.getUser().getItemCount() > 3) {
                            offerDenied("price not valid ");
                        } else {
                            offerConfirm();
                        }
                        break;
                    case "BID":
                        bid();
                        break;
                    case "EXIT":
                        break;
                    case "CONNECT":
                        this.user = msg.getUser();
//                        System.out.println("Client has connected");
                        log.trace("Client has connected" + " " + msg.getUser().getRequestCount() + " " + socket.getRemoteSocketAddress().toString());
                        break;
                    default:
                        break;
                }
                user.incRequestCount();
            }
        } catch (IOException | ClassNotFoundException e) {
            try {
                socket.close();
            } catch (IOException e1) {
//                e1.printStackTrace();
            }
        }
    }

    private void offer() throws IOException, ClassNotFoundException {
        item = msg.getItem();
//        System.out.println(msg);
    }

    private void bid() {
        for ( int i = 0; i < ItemHandler.getInstance().getArray().size(); i++) {
            if(ItemHandler.getInstance().getArray().get(i).getItemNumber() == msg.getItemID() ) {
                this.msg.setItem(ItemHandler.getInstance().getArray().get(i));
//                System.out.println(msg);
                if(msg.getAmount() > ItemHandler.getInstance().getArray().get(i).getCurrentBid() &&
                        msg.getAmount() >= ItemHandler.getInstance().getArray().get(i).getCurrentBid()) {
                    ItemHandler.getInstance().getArray().get(i).setTopBidder(msg.getUser());
//                    System.out.println("USERNAME in BID: " +ItemHandler.getInstance().getArray().get(i).getTopBidder().getUsername());
                    this.msg.getItem().setCurrentBid(msg.getAmount());
                    this.msg.getItem().setTopBidder(this.user);
                    this.msg.getItem().addBidder(this.user);
                    this.msg.setType(MessageType.HIGHEST);
                    notifyUsers();
                }
            }
        }
    }


    private void offerConfirm() throws IOException {
      // send offer confirmed MSG
        this.msg.setType(MessageType.OFFER_CONFIRM);
        msg.getItem().setStartTime(Server.auctionTimer.getElapsedTime());
        ItemHandler.getInstance().add(item);
        msg.getUser().incItemCount();
        oos.writeUnshared(msg);
        log.trace(msg + " " + msg.getUser().getRequestCount() + " " + this.socket.getRemoteSocketAddress());
        oos.flush();
        msg.setType(MessageType.NEW);
        notifyUsers();
    }

    private void notifyUsers() {
        for(ClientHandler handler : ClientHandlers.getInstance().getArray()) {
            if(handler != this) {
                try {
                    handler.setMsg(this.msg);
                    handler.sendMessage();
                    log.trace(msg + " " + msg.getUser().getRequestCount() + " " + this.socket.getRemoteSocketAddress());
                } catch (IOException e) {
//                    e.printStackTrace();
                }
            }
        }
    }

    private void offerDenied(String err) throws IOException {
       // send offer denied MSG
        this.msg.setType("OFFER-DENIED");
        this.msg.setReason(err);
        oos.writeUnshared(msg);
        log.trace(msg + " " + msg.getUser().getRequestCount() + " " + this.socket.getRemoteSocketAddress());
        oos.flush();
    }

    public void sendMessage() throws IOException {
        this.oos.writeUnshared(this.msg);
        log.trace(msg);
        //System.out.println("Sending this msg: " + msg);
        this.oos.flush();
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public Message getMsg() {
        return msg;
    }

    public void setMsg(Message msg) {
        this.msg = msg;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
