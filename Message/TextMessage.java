package Message;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * This class represents a TextMessage that contains a string of content, the senderID number, and the recipientID number.
 * @author Nikita Sirandasu
 * @version 11/17/2024
 */
public class TextMessage implements Message, Serializable {
    private String content;
    private int senderID;
    private int recipientID;
    private LocalDateTime timestamp;
  
    public TextMessage(String content, int senderID, int recipientID) {
        this.content = content;
        this.senderID = senderID;
        this.recipientID = recipientID;
        this.timestamp = LocalDateTime.now(); //will allow for viewmessage implementation in phase 3
    }

    public String getMessage() {
        return content;
    }

    @Override
    public LocalDateTime getTimeStamp() {
        return timestamp;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getRecipientID() {
        return recipientID;
    }

    public void setRecipientID(int recipientID) {
        this.recipientID = recipientID;
    }
}
