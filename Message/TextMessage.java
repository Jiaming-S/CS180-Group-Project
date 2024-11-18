package Message;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TextMessage implements Message {

    private String message;
    private LocalDateTime timeStamp;

    public TextMessage(String message, LocalDateTime timeStamp) {
        this.message = message;
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
