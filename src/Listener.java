import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Listener extends Thread{

    private Socket socket = null;

    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private Message message;

    public Listener(Socket socket) throws IOException {
        this.socket = socket;
        this.message = new Message();
    }

    @Override
    public void run() {
        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.oos.flush();
            this.ois = new ObjectInputStream(socket.getInputStream());
            while(true) {
                this.message = (Message) ois.readObject();
                switch(message.getType()) {
                    case MessageType.WIN:
                        System.out.println(message.toString());
                        break;
                    case MessageType.NEW:
                        System.out.println(message.toString());
                        break;
                    default:
                        break;
            }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
