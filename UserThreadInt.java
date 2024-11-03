public interface UserThreadInt {
    UserEntry searchUser(UserEntry user);
    void blockUser(UserEntry user);
    MessageEntry newConvo(UserEntry user, String content);
    void sendMessage(MessageEntry message);
}
