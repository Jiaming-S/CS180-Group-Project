package Message;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TextMessage implements Message {

    private String message;
    int senderID;
    int recipientID;
    private LocalDateTime timeStamp;

    public TextMessage(String message, int senderID, int recipientID, LocalDateTime timeStamp) {
        this.message = message;
        this.senderID = senderID;
        this.recipientID = recipientID;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
}
