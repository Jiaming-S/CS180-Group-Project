package Message;

import java.time.LocalDateTime;

public interface Message {
    Object getMessage();
    LocalDateTime getTimeStamp();
    String toString();

    static Message convertToMessage(String s) {
        String[] input = s.split("\\|");
        if (input[0].equals("PhotoMessage")) {
            return new PhotoMessage(s);
        } else if (input[0].equals("TextMessage")) {
            return new TextMessage(s);
        }
        return null;
    }
}
