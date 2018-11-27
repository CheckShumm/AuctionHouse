public class Dialogue {

    private ItemOffer itemOffer;
    private User user;
    private Item item;
    private String msg;

    public Dialogue(User user) {
        this.user = user;
        this.itemOffer = new ItemOffer(user);
    }

    public void getDialogue(String type) {
        switch(type) {
            case "OFFER":
                item = itemOffer.offer();
                break;

            case "OFFER-CONF":

                break;

            case "NEW-ITEM":
                break;

            case "OFFER-DENIED":
                break;

            case "BID":
                break;

            case "HIGHEST":
                break;

            case "WIN":
                break;

            case "BID-OVER":
                break;

            case "SOLDTO":
                break;

            case "NOT-SOLD":
                break;
        }
    }

    public Item getItem(){
        return this.item;
    }
}
