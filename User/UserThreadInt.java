package User;

/**
 * User.UserThreadInt is interface for UserThread class and details methods for it
 * @author Nikita Sirandasu
 * @version 11/03/2024
 */
public interface UserThreadInt {
    void searchUser(String username);
    void viewProfile();
    void blockUser();
    void newConvo();
    void viewMsg();
    void sendTextMsg();
    void sendPhotoMsg();
}
