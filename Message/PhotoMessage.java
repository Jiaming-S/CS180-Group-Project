package Message;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PhotoMessage implements Message {

    private Object message;
    int senderID;
    int recipientID;
    private LocalDateTime timeStamp;

    public PhotoMessage(Object message, int senderID, int recipientID) {
        this.message = message; //photo path
        this.senderID = senderID;
        this.recipientID = recipientID;
        this.timeStamp = LocalDateTime.now();
    }

    public Object getMessage() {
        return message;
    }

    @Override
    public LocalDateTime getTimeStamp() {
        return timeStamp;
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
