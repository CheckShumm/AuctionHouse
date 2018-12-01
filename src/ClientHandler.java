import java.io.*;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread{

    //UDP
    DatagramSocket udpSocket = null;

    // TCP
    private Socket socket = null;
    private Item item;

    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    
    private User user;
    private Message msg;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.user = new User();
    }

    public ClientHandler(DatagramSocket udpsocket) throws IOException {
        this.udpSocket = udpsocket;
        this.user = new User();
    }

    @Override
    public void run() {
        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());

            this.ois = new ObjectInputStream(socket.getInputStream());
            this.dis = new DataInputStream(socket.getInputStream());

            while (true) {
                this.msg = (Message)ois.readObject();
//                if(socket!=null) this.msg = (Message)ois.readObject();
//                if(udpSocket!=null) this.msg = (Message)ois.readObject();
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

    private void offerConfirm() throws IOException {
      // send offer confirmed MSG
        this.msg.setType("OFFER-CONF");
        msg.getItem().setStartTime(Server.auctionTimer.getElapsedTime());
        oos.writeObject(msg);
        oos.flush();
        //notifyUsers();
    }

    private void notifyUsers() {
        System.out.println("HERE!");
        for(int i = 0; i < ClientHandlers.getInstance().getArray().size(); i++) {
            ClientHandler handler = ClientHandlers.getInstance().getArray().get(i);
            try {
                oos.writeObject(msg);
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void offerDenied(String err) throws IOException {
       // send offer denied MSG
        this.msg.setType("OFFER-DENIED");
        this.msg.setReason(err);
        oos.writeObject(msg);
        oos.flush();
    }


}
