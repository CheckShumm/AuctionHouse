import java.util.ArrayList;

public class ItemHandler  {

    private static ItemHandler mInstance;
    private ArrayList<Item> list = null;
    private int itemCount;

    public static ItemHandler getInstance() {
        if(mInstance == null)
            mInstance = new ItemHandler();

        return mInstance;
    }

    private ItemHandler() {
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
    
    public String verifyUser (User user) {
        for(Item item : this.list) {
//            System.out.println("item's owner: " + item.getOwner().getUsername());
//            System.out.println("dereg user: " + user.getUsername());
//            System.out.println("Top Bidder: " + item.getTopBidder().getUsername());
            if(!item.getSold()) {
                if(item.getOwner().getUsername().equals(user.getUsername())) {
                    return "user has item up for auction";
                }
//                System.out.println();
//                if(item.getTopBidder().getUsername().equals(user.getUsername())) {
//                    return "user is top bidder for item";
//                }
            }
        }
        return MessageType.DEREG_CONF;
    }

//    public boolean updateItem(Item item) {
//        for(Item item : this.list) {
//
//            if(item.getSold()) {
//                if(item.getOwner().getUsername().equals(user.getUsername())) {
//                    return "user has item up for auction";
//                }
//                System.out.println();
//                if(item.getTopBidder().getUsername().equals(user.getUsername())) {
//                    return "user is top bidder for item";
//                }
//            }
//        }
//    }

}
