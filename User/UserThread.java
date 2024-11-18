package User;

import Database.MessageDatabase;
import Database.UserDatabase;
import Database.UserEntry;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * UserThread class with several methods related to User actions and will work with GUI
 * (Some methods may need to be altered during Phase 3 to work with GUI)
 * @author Nikita Sirandasu
 * @version 11/17/2024
 */
public class UserThread extends Thread implements UserThreadInt {
    private User currUser;
    private Socket userDBSocket;
    private Socket msgDBSocket;
    private UserDatabase userDB;
    private MessageDatabase msgDB;
    private final Object lock = new Object();
    private Scanner scanner;

    public UserThread(User currUser, Socket userDBSocket, Socket msgDBSocket) throws IOException, ClassNotFoundException {
        this.currUser = currUser;
        this.userDBSocket = userDBSocket;
        this.msgDBSocket = msgDBSocket;
        ObjectOutputStream userOut = new ObjectOutputStream(userDBSocket.getOutputStream());
        ObjectInputStream userIn = new ObjectInputStream(userDBSocket.getInputStream());
        userOut.writeObject("START_USER_DB");
        this.userDB = (UserDatabase) userIn.readObject();
        ObjectOutputStream msgOut = new ObjectOutputStream(msgDBSocket.getOutputStream());
        ObjectInputStream msgIn = new ObjectInputStream(msgDBSocket.getInputStream());
        msgOut.writeObject("START_MESSAGE_DB");
        this.msgDB = (MessageDatabase) msgIn.readObject();
        this.scanner = new Scanner(System.in);

    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("Welcome " + currUser.getUsername());
            System.out.println("1 - Search User\n2 - View Profile\n3 - Block User\n4 - Start New Conversation\n5 - View Message\n6 - Send TextMessage\n7 - Send PhotoMessage\n8 - Log Out");
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
             }
        }
    }

    public void searchUser() {
        synchronized (lock) {
            System.out.print("Enter username you want to search: ");
            String username = scanner.nextLine();
            UserEntry userToFind = userDB.searchByName(username);

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
            System.out.println(currUser.getUsername() + "'s Profile:");
            System.out.println("Username: " + currUser.getUsername());
            System.out.println("ID: " + currUser.getID());
            System.out.println("Region: " + currUser.getRegion());
        }
    }

    public void blockUser() {
        synchronized (lock) {
            System.out.print("Enter username to block: ");
            String blockedUsername = scanner.nextLine();
            UserEntry blocked = userDB.searchByName(blockedUsername);
            User userToBlock = new User(blocked);
            if (userToBlock != null) {
                currUser.getBlockList().add(userToBlock.getID());
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
            UserEntry receive = userDB.searchByName(username);
            User recipient = new User(receive);
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
            UserEntry receive = userDB.searchByName(recipientUsername);
            User recipient = new User(receive);
            if (recipient == null) {
                System.out.println("User not found.");
                return;
            }

            System.out.print("Enter your message: ");
            String messageContent = scanner.nextLine();
            //Message message = new TextMessage(messageContent, currUser.getID());
            //assumed implementation of TextMessage
            //msgDB.insertEntry(message.getTimeStamp(), currUser.getID(), recipient.getID(), message);

            System.out.println("Text message sent to " + recipientUsername);
        }
    }

    public void sendPhotoMsg() {
        synchronized (lock) {
            System.out.print("Enter recipient's username: ");
            String recipientUsername = scanner.nextLine();
            UserEntry recipient = userDB.searchByName(recipientUsername);

            if (recipient == null) {
                System.out.println("User not found.");
                return;
            }

            System.out.print("Enter the path of the photo: ");
            String photoPath = scanner.nextLine();

            //Message photoMessage = new PhotoMessage(photoPath, currUser.getID());
            //assumed implementation of PhotoMessage
            //msgDB.insertEntry(photoMessage.getTimeStamp(), currUser.getID(), recipient.getID(), photoMessage);

            System.out.println("Photo message was sent to " + recipientUsername);
        }
    }

}