
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author n_shumm
 */
public class Message implements Serializable{
    
    private Item item;
    private String type;
    private User user;
    
    public Message(){
        this.type = "default";
    }
    
    public Item getItem() {
        return this.item;
    }
    
    public void setItem(Item item) {
        this.item = item;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        switch(type) {
            case "OFFER":
                sb.append(type + " ");
                sb.append('1' + " "); // user.getRequestCount();
                sb.append(item.getName() + " ");
                sb.append(item.getDescription() + " ");
                sb.append(item.getMinPrice());
                break;
            case "OFFER-CONF":
                sb.append(type + " 1 " + item.getName() + " " + item.getDescription() + " " + item.getMinPrice());
                break;
            case "NEW-ITEM":
                sb.append(type + " 1 " + item.getDescription() + item.getMinPrice());
            default:
                break;

        }
        return sb.toString();
    }
    
}
