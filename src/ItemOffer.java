import java.util.Scanner;

public class ItemOffer {

    private Scanner in;
    private Item item;
    private User owner;

    public ItemOffer(User user) {
        in = new Scanner(System.in);
        this.owner = user;
    }

    public Item offer() {
        System.out.println("Enter name of the item?");
        String itemName = in.nextLine();

        System.out.println("Please give a short description of your item.");
        String itemDescription = in.nextLine();

        System.out.println("What is the starting bid for " + itemName);
        double minPrice = in.nextDouble();

        item = new Item(itemName, owner, itemDescription, minPrice);

        return item;
    }
}
