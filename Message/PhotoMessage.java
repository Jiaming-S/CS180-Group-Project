package Message;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PhotoMessage implements Message {

    private Object message;
    int senderID;
    int recipientID;
    private LocalDateTime timeStamp;

    public PhotoMessage(Object message, int senderID, int recipientID, LocalDateTime timeStamp) {
        this.message = message;
        this.senderID = senderID;
        this.recipientID = recipientID;
        this.timeStamp = timeStamp;
    }

    public Object getMessage() {
        return message;
    }

    @Override
    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
}
