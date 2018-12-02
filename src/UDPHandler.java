import java.awt.*;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class UDPHandler extends Thread{

    //UDP
    private DatagramSocket socket = null;
    private DatagramPacket packet;
    private Message incomingMsg;
    private InetAddress address;
    private int port;

    private User user;


    public UDPHandler(DatagramSocket socket, DatagramPacket packet) throws IOException {
        this.socket = socket;
        this.packet = packet;

        //Get UDP receive host details
        this.address = packet.getAddress();
        this.port = packet.getPort();

        try {
            incomingMsg = (Message) Help.deserialize(packet.getData());
            user = incomingMsg.getUser();
        } catch (ClassNotFoundException e) {
            System.out.println("UDP packet receive is not an message type");
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                switch(incomingMsg.getType()) {
                    case MessageType.REGISTER:

                        //give user authentication
                        user.setAuth(true);

                        //prep reply message
                        Message reply = new Message();
                        reply.setUser(user);
                        reply.setType(MessageType.REGISTERED);
                        byte[] replyByte = Help.serialize(reply);
                        packet = new DatagramPacket(replyByte, replyByte.length, address, port);

                        //send reply message
                        socket.send(packet);
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
//            try {
//                socket.close();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
        }
    }
//
//    private void offer() throws IOException, ClassNotFoundException {
//        item = msg.getItem();
//        System.out.println(msg);
//
//    }
//
//    private void offerConfirm() throws IOException {
//      // send offer confirmed MSG
//        this.msg.setType("OFFER-CONF");
//        msg.getItem().setStartTime(Server.auctionTimer.getElapsedTime());
//        oos.writeObject(msg);
//        oos.flush();
//        //notifyUsers();
//    }
//
//    private void notifyUsers() {
//        System.out.println("HERE!");
//        for(int i = 0; i < ClientHandlers.getInstance().getArray().size(); i++) {
//            UDPHandler handler = ClientHandlers.getInstance().getArray().get(i);
//            try {
//                oos.writeObject(msg);
//                oos.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void offerDenied(String err) throws IOException {
//       // send offer denied MSG
//        this.msg.setType("OFFER-DENIED");
//        this.msg.setReason(err);
//        oos.writeObject(msg);
//        oos.flush();
//    }


}
