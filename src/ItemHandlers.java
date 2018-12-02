import java.util.ArrayList;

public class ItemHandlers  {

    private static ItemHandlers mInstance;
    private ArrayList<Item> list = null;
    private int itemCount;

    public static ItemHandlers getInstance() {
        if(mInstance == null)
            mInstance = new ItemHandlers();

        return mInstance;
    }

    private ItemHandlers() {
        this.itemCount = 0;
        this.list = new ArrayList<Item>();
    }

    // retrieve array from anywhere
    public ArrayList<Item> getArray() {

        return this.list;
    }

    //Add element to array
    public void add(Item item) {
        itemCount++;
        item.setItemNumber(itemCount);
        list.add(item);
    }
}