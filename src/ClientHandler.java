import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread{

    // TCP
    private Socket socket;
    private Item item;

    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    
    private User user;
    private Message msg;
    private Server server;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.user = new User();
        this.server = server;
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
        oos.writeObject(msg);
        oos.flush();

        msg.setType("NEW-ITEM");
        //notifyUsers();
    }

    private void notifyUsers() {
        System.out.println("HERE!");
        for(int i = 0; i < server.getClientHandlers().size(); i++) {
            ClientHandler handler = server.getClientHandlers().get(i);
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
