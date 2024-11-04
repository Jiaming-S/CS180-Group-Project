package User;

import Database.MessageDatabase;
import Database.UserDatabase;
import Message.Message;
import Message.PhotoMessage;
import Message.TextMessage;
import java.util.Scanner;

/**
 * UserThread class with several methods related to User actions and will work with GUI
 * (Some methods may need to be altered during Phase 3 to work with GUI)
 * @author Nikita Sirandasu
 * @version 11/03/2024
 */
public class UserThread extends Thread implements UserThreadInt {
    private User currUser;
    private UserDatabase userDB;
    private MessageDatabase msgDB;
    private Scanner scanner;
    private final Object lock = new Object();

    public UserThread(User currUser, UserDatabase userDB, MessageDatabase msgDB) {
        this.currUser = currUser;
        this.userDB = userDB;
        this.msgDB = msgDB;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("Welcome " + currUser.getUsername());
            System.out.println("1 - Search User\n2 - View Profile\n3 - Block User\n4 - Start New Conversation\n5 - View Message\n6 - Send Message\n7 - Log Out");
            int answer = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (answer) {
                case 1:
                    searchUser();
                    break;
                case 2:
                    viewProfile();
                    break;
                case 3:
                    blockUser();
                    break;
                case 4:
                    newConvo();
                    break;
                case 5:
                    viewMsg();
                    break;
                case 6:
                    sendTextMsg();
                    break;
                case 7:
                    sendPhotoMsg();
                    break;
                case 8:
                    running = false;
                    System.out.println("Logging out");
                    break;
                default:
                    System.out.println("Invalid answer, please enter a valid answer.");
                    break;
            }
        }
    }

    public void searchUser() {
        synchronized (lock) {
            System.out.print("Enter username you want to search: ");
            String username = scanner.nextLine();
            User userToFind = userDatabase.searchByName(username);

            if (userToFind != null) {
                System.out.println("User found: " + userToFind.getUsername());
                System.out.println("ID: " + userToFind.getID());
                System.out.println("Region: " + userToFind.getRegion());
            } else {
                System.out.println("User not found.");
            }
        }
    }

    public void viewProfile() {
        synchronized (lock) {
            System.out.println(currentUser.getUsername() + "'s Profile:");
            System.out.println("Username: " + currentUser.getUsername());
            System.out.println("ID: " + currentUser.getID());
            System.out.println("Region: " + currentUser.getRegion());
        }
    }

    public void blockUser() {
        synchronized (lock) {
            System.out.print("Enter username to block: ");
            String blockedUsername = scanner.nextLine();
            User userToBlock = userDatabase.searchByName(blockedUsername);

            if (userToBlock != null) {
                currentUser.getBlockList().add(userToBlock.getID());
                System.out.println("Blocked user is: " + userToBlock.getUsername());
            } else {
                System.out.println("User not found.");
            }
        }
    }

    public void newConvo() {
        synchronized (lock) {
            System.out.print("Enter username to start a conversation with: ");
            String username = scanner.nextLine();
            User recipient = userDatabase.searchByName(username);

            if (recipient != null) {
                System.out.println("Starting a conversation with " + recipient.getUsername());
            } else {
                System.out.println("User not found.");
            }
        }
    }

    public void viewMsg() {
        //must be implemented after GUI is created
    }

    public void sendTextMsg() {
        synchronized (lock) {
            System.out.print("Enter recipient's username: ");
            String recipientUsername = scanner.nextLine();
            User recipient = userDatabase.searchByName(recipientUsername);

            if (recipient == null) {
                System.out.println("User not found.");
                return;
            }

            System.out.print("Enter your message: ");
            String messageContent = scanner.nextLine();
            Message message = new TextMessage(messageContent, currentUser.getID());
            //assumed implementation of TextMessage
            messageDatabase.insertEntry(message.getTimeStamp(), currentUser.getID(), recipient.getID(), message);

            System.out.println("Text message sent to " + recipientUsername);
        }
    }

    public void sendPhotoMsg() {
        synchronized (lock) {
            System.out.print("Enter recipient's username: ");
            String recipientUsername = scanner.nextLine();
            User recipient = userDatabase.searchByName(recipientUsername);

            if (recipient == null) {
                System.out.println("User not found.");
                return;
            }

            System.out.print("Enter the path of the photo: ");
            String photoPath = scanner.nextLine();

            Message photoMessage = new PhotoMessage(photoPath, currentUser.getID());
            //assumed implementation of PhotoMessage
            messageDatabase.insertEntry(photoMessage.getTimeStamp(), currentUser.getID(), recipient.getID(), photoMessage);

            System.out.println("Photo message was sent to " + recipientUsername);
        }
    }

}