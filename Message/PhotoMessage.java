package Message;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PhotoMessage implements Message, Serializable {

    private Object message;
    int senderID;
    int recipientID;
    private LocalDateTime timeStamp;

    public PhotoMessage(Object message, int senderID, int recipientID) {
        this.message = message; //photo path
        this.senderID = senderID;
        this.recipientID = recipientID;
        this.timeStamp = LocalDateTime.now(); //will allow for viewmessage implementation in phase 3
    }

    public PhotoMessage(String content) {
        String[] contentSplit = content.split("|");
        if (!contentSplit[0].equals("PhotoMessage")) return;

        this.senderID = Integer.parseInt(contentSplit[1]);
        this.recipientID = Integer.parseInt(contentSplit[2]);
        this.timeStamp = LocalDateTime.parse(contentSplit[3]);
        this.message = "PHOTO-BYTES-GO-HERE=="; // TODO
    }

    @Override
    public String toString() {
        return String.format("PhotoMessage|%s|%s|%s|%s", senderID, recipientID, timeStamp, message);
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
