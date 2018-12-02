import java.io.*;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread{

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
                switch(msg.getType()) {
                    case "OFFER":
                        offer();
                        if (item.getMinPrice() < 0 ) {
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
                        System.out.println("Client has connected");
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void offer() throws IOException, ClassNotFoundException {
        item = msg.getItem();
        System.out.println(msg);
    }

    private void bid() {
        for ( Item item : ItemHandlers.getInstance().getArray()) {
            if(item.getName().equals(msg.getItemName())) {
                this.msg.setItem(item);
                System.out.println(msg);
                if(msg.getAmount() > item.getCurrentBid()) {
                    this.msg.getItem().setCurrentBid(msg.getAmount());
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
        ItemHandlers.getInstance().add(item);
        oos.writeUnshared(msg);
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void offerDenied(String err) throws IOException {
       // send offer denied MSG
        this.msg.setType("OFFER-DENIED");
        this.msg.setReason(err);
        oos.writeUnshared(msg);
        oos.flush();
    }

    public void sendMessage() throws IOException {
        this.oos.writeUnshared(this.msg);
        System.out.println("Sending this msg: " + msg);
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
}
