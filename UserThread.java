
public class UserThread implements UserThreadInt {
    private UserDatabase UserDB;
    public UserThread() {
        this.UserDB = new UserDatabase();
    }

    public UserEntry searchUser(UserEntry user) {
        return user.getEntry();
    }

    public void viewProfile() {
    }
    public void blockUser(UserEntry user) {
        this.getBlockList().add(user.getID());
    }
    public MessageEntry newConvo(UserEntry user, String content) {
        return new MessageEntry(this.getID(), user.getID(), content);
    }
    public void viewMessage() {

    }
    public void sendMessage(MessageEntry message) {
        this.insertEntry(message);
    }
}
