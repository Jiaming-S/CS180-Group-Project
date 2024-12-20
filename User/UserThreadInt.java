package User;

/**
 * User.UserThreadInt is interface for UserThread class and details methods for it
 * @author Nikita Sirandasu
 * @version 11/03/2024
 */
public interface UserThreadInt {
    void searchUser(String username);
    void blockUser(String blockedUsername);
    void newConvo();
    void viewMsg();
    void sendPhotoMsg();
    User getCurrUser();
}
