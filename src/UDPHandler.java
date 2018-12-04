import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;

public class UDPHandler extends Thread{

    //Logger
    private static Logger log = LogManager.getLogger("auctionhouse");

    //UDP
    private DatagramSocket socket = null;
    private DatagramPacket packet;
    private Message incomingMsg;
    private InetAddress address;
    private int port;

    private User user;

    Auth auth;

    public UDPHandler(DatagramSocket socket, DatagramPacket packet) throws IOException {

        try {
            auth = new Auth();
        } catch (SQLException e) {
            System.out.println("Unable to connect to database");
            e.printStackTrace();
        }

        this.socket = socket;
        this.packet = packet;

        //Get UDP receive host details
        this.address = packet.getAddress();
        this.port = packet.getPort();

        try {
            incomingMsg = (Message) Help.deserialize(packet.getData());
            log.trace(incomingMsg);
            user = incomingMsg.getUser();
            auth.setUser(user);
        } catch (ClassNotFoundException e) {
            System.out.println("UDP packet receive is not an message type");
        }
    }

    @Override
    public void run() {
        try {
            //prep reply message
            Message reply = new Message();
            byte[] replyByte;
            switch(incomingMsg.getType()) {
                case MessageType.REGISTER:

                    if (auth.register()) {
                        reply.setType(MessageType.REGISTERED);
                    } else {
                        reply.setType(MessageType.UNREGISTERED);
                        reply.setReason("Your username is taken");
                    }

                    break;
                case MessageType.LOGIN:

                    if (auth.login()) {
                        reply.setType(MessageType.REGISTERED);
                    } else {
                        reply.setType(MessageType.UNREGISTERED);
                        reply.setReason("Your username or password is incorrect");
                    }

                    break;
                case MessageType.DEREGISTER:

                    if (auth.login()) {
                        String deregVerify = ItemHandler.getInstance().verifyUser(this.user);
                        if(deregVerify.equals(MessageType.DEREG_CONF)) {
                            reply.setType(MessageType.DEREG_CONF);
                        } else {
                            reply.setReason(deregVerify);
                            reply.setType(MessageType.DEREG_DENIED);
                        }
                    }
                    log.trace(reply);
                    break;
                default:
                    reply.setType(MessageType.NULL);
                    break;
            }

            user.incRequestCount();
            user = auth.getUser();
            reply.setUser(auth.getUser());
            replyByte = Help.serialize(reply);
            packet = new DatagramPacket(replyByte, replyByte.length, address, port);

            log.trace(reply);

            //send reply message
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
