package User;

import Net.Packet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import Database.UserEntry;
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
    private ObjectOutputStream userOut;
    private ObjectInputStream userIn;
    private ObjectOutputStream msgOut;
    private ObjectInputStream msgIn;
    private final Object lock = new Object();
    private Scanner scanner;

    public UserThread(User currUser, Socket userDBSocket, Socket msgDBSocket) throws IOException, ClassNotFoundException {
        this.currUser = currUser;
        this.userDBSocket = userDBSocket;
        this.msgDBSocket = msgDBSocket;
        this.userOut = new ObjectOutputStream(userDBSocket.getOutputStream());
        this.userIn = new ObjectInputStream(userDBSocket.getInputStream());
        userOut.writeObject("START_USER_DB");
        this.msgOut = new ObjectOutputStream(msgDBSocket.getOutputStream());
        this.msgIn = new ObjectInputStream(msgDBSocket.getInputStream());
        msgOut.writeObject("START_MESSAGE_DB");
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
            Packet packet = new Packet("searchByName", username, null);
            try {
                userOut.writeObject(packet);
                Packet response = (Packet) userIn.readObject();
                if (response.query.equals("success")) {
                    UserEntry userToFind = (UserEntry) response.content;
                    System.out.println("User found: " + userToFind.getUsername());
                    System.out.println("ID: " + userToFind.getID());
                    System.out.println("Region: " + userToFind.getRegion());
                } else {
                    System.out.println("User was not found.");
                }
            } catch (Exception e) {
                System.out.println("Error when searching user.");
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
            Packet packet = new Packet("searchByName", blockedUsername, null);
            try {
                userOut.writeObject(packet);
                Packet response = (Packet) userIn.readObject();
                if (response.query.equals("success")) {
                    UserEntry blocked = (UserEntry) response.content;
                    currUser.getBlockList().add(blocked.getID());
                    System.out.println("Blocked user is: " + blocked.getUsername());
                } else {
                    System.out.println("User not found.");
                }
            } catch (Exception e) {
                System.out.println("Error when blocking user.");
            }
        }
    }

    public void newConvo() {
        synchronized (lock) {
            System.out.print("Enter username to start a conversation with: ");
            String username = scanner.nextLine();
            Packet packet = new Packet("searchByName", username, null);
            try {
                userOut.writeObject(packet);
                Packet response = (Packet) userIn.readObject();
                if (response.query.equals("success")) {
                    UserEntry recipientEntry = (UserEntry) response.content;
                    System.out.println("Starting a conversation with " + recipientEntry.getUsername());
                } else {
                    System.out.println("User not found.");
                }
            } catch (Exception e) {
                System.out.println("Error when starting conversation.");
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
            Packet packet = new Packet("searchByName", recipientUsername, null);
            try {
                userOut.writeObject(packet);
                Packet response = (Packet) userIn.readObject();
                if (response.query.equals("success")) {
                    UserEntry recipient = (UserEntry) response.content;
                    System.out.print("Enter your message: ");
                    String messageContent = scanner.nextLine();
                    // Assuming implementation of TextMessage is created here
                    //Packet textMsgPacket = new Packet("sendTextMessage", new TextMessage(messageContent, currUser.getID(), recipient.getID()), null);
                    //msgOut.writeObject(textMsgPacket); // Send the text message packet
                    System.out.println("Text message sent to " + recipientUsername);
                } else {
                    System.out.println("User was not found.");
                }
            } catch (Exception e) {
                System.out.println("Error when sending text message.");
            }
        }
    }

    public void sendPhotoMsg() {
        synchronized (lock) {
            System.out.print("Enter recipient's username: ");
            String recipientUsername = scanner.nextLine();
            Packet packet = new Packet("searchByName", recipientUsername, null);
            try {
                userOut.writeObject(packet);
                Packet response = (Packet) userIn.readObject();
                if (response.query.equals("success")) {
                    UserEntry recipient = (UserEntry) response.content;
                    System.out.print("Enter the path of the photo: ");
                    String photoPath = scanner.nextLine();
                    // Assumed implementation of PhotoMessage is created here
                    //Packet photoMsgPacket = new Packet("sendPhotoMessage", new PhotoMessage(photoPath, currUser.getID(), recipient.getID()), null);
                    //msgOut.writeObject(photoMsgPacket); // Send the photo message packet
                    System.out.println("Photo message sent to " + recipientUsername);
                } else {
                    System.out.println("User was not found.");
                }
            } catch (Exception e) {
                System.out.println("Error when sending photo message.");
            }
        }
    }

}