package Message;

import java.time.LocalDateTime;

public interface Message {
    Object getMessage();
    LocalDateTime getTimeStamp();
}
