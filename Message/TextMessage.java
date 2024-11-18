package Message;

import java.time.LocalDateTime;

public class TextMessage implements Message {
    private String content;
    private int senderID;
    private int recipientID;
    private LocalDateTime timestamp;

    public TextMessage(String content, int senderID, int recipientID) {
        this.content = content;
        this.senderID = senderID;
        this.recipientID = recipientID;
        this.timestamp = LocalDateTime.now();
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
