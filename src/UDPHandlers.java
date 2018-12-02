import java.util.ArrayList;

public class UDPHandlers {

    private static UDPHandlers mInstance;
    private ArrayList<UDPHandler> list = null;

    public static UDPHandlers getInstance() {
        if(mInstance == null)
            mInstance = new UDPHandlers();

        return mInstance;
    }

    private UDPHandlers() {

        list = new ArrayList<UDPHandler>();
    }

    // retrieve array from anywhere
    public ArrayList<UDPHandler> getArray() {

        return this.list;
    }

    //Add element to array
    public void add(UDPHandler udpHandler)
    {
        list.add(udpHandler);
    }
}