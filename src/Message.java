
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
    private String reason;

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        switch(type) {
            case MessageType.REGISTER:
                sb.append(type + " ");
                sb.append("Attempt to register ");
                sb.append(user.getUsername());
                break;
            case MessageType.REGISTERED:
                sb.append(type + " ");
                sb.append("Confirm ");;
                sb.append(user.getUsername() + " registration");
                break;
            case MessageType.UNREGISTERED:
                sb.append(type + " ");
                sb.append("Unable to register ");
                sb.append(user.getUsername() + " ");
                sb.append("due to " + this.reason );
                break;
            case MessageType.OFFER:
                sb.append(type + " ");
                sb.append('1' + " "); // user.getRequestCount();
                sb.append(item.getName() + " ");
                sb.append(item.getDescription() + " ");
                sb.append(item.getMinPrice());
                break;
            case MessageType.OFFER_CONFIRM:
                sb.append(type + " 1 " + item.getName() + " " + item.getDescription() + " " + item.getMinPrice());
                break;
            case MessageType.OFFER_DENIED:
                sb.append(type + " 1 " + this.reason);
            default:
                break;

        }
        return sb.toString();
    }
    
}
