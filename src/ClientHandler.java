import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread{

    // TCP
    private Socket socket;
    private Item item;
    private int  tcpPort = 8888;

    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    
    private User user;
    private Message msg;
  

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        user = new User();

        System.out.println("Waiting for connection on port " + tcpPort);
        this.run();
    }
    @Override
    public void run() {
        System.out.println("Server is running");
        while (true) {
            try {

                oos = new ObjectOutputStream(socket.getOutputStream());
                dos = new DataOutputStream(socket.getOutputStream());

                ois = new ObjectInputStream(socket.getInputStream());
                dis = new DataInputStream(socket.getInputStream());
                
                msg = (Message)ois.readObject();
                switch(msg.getType()) {
                    case "OFFER":
                        offer();
                        if (item.getMinPrice() < 0 ) {
                            offerDenied("price not valid");
                        } else {
                            offerConfirm();
                        }
                        break;
                    case "bid":
                        break;
                    case"exit":
                        break;
                    default:
                        dos.writeUTF("Sorry that option is not available. \n Please select another\n" + printOptions());
                        break;
                }

//                while ((item = (Item) ois.readObject()) != null) {
//                    item.printInfo();
//
//                    oos.writeObject("bye bye");
//                    break;
//                }
//                oos.close();
//
//                fromClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void offer() throws IOException, ClassNotFoundException {
        item = msg.getItem();
        System.out.println(msg);

    }

    public void offerConfirm() throws IOException {
      // send offer confirmed MSG
    }

    public void offerDenied(String err) throws IOException {
       // send offer denied MSG
    }

    public String printOptions() throws IOException {
        return ("Enter 'offer' to put an item up for auction\n"+
                "Enter 'bid' if you would like to bid on an item\n"+
                "Enter 'exit' if you would like to exit");
    }

}
