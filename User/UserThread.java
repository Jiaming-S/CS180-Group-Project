package User;

import Database.MessageDatabase;
import Database.UserDatabase;
import Database.UserEntry;
import Net.Packet;
import java.io.*;
import java.net.*;
//import Message.Message;
//import Message.PhotoMessage;
//import Message.TextMessage;
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
        Packet userStartPacket = new Packet("START_USER_DB", null, null);
        userOut.writeObject(userStartPacket);
        userOut.flush();
        Packet userResponse = (Packet) userIn.readObject();
        this.userDB = (UserDatabase) userResponse.getContent();
        ObjectOutputStream msgOut = new ObjectOutputStream(msgDBSocket.getOutputStream());
        ObjectInputStream msgIn = new ObjectInputStream(msgDBSocket.getInputStream());
        Packet msgStartPacket = new Packet("START_MESSAGE_DB", null, null);
        msgOut.writeObject(msgStartPacket);
        msgOut.flush();
        Packet msgResponse = (Packet) msgIn.readObject();
        this.msgDB = (MessageDatabase) msgResponse.getContent();
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
                    break;
            }
        }
    }

    public void searchUser() {
        synchronized (lock) {
            System.out.print("Enter username you want to search: ");
            String username = scanner.nextLine();

            Packet searchPacket = new Packet("SEARCH_USER", username, null);
            try {
                sendPacket(userDBSocket, searchPacket);
                Packet response = receivePacket(userDBSocket);
                UserEntry userToFind = (UserEntry) response.getContent();

                if (userToFind != null) {
                    System.out.println("User found: " + userToFind.getUsername());
                    System.out.println("ID: " + userToFind.getID());
                    System.out.println("Region: " + userToFind.getRegion());
                } else {
                    System.out.println("User not found.");
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error searching user: " + e.getMessage());
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

            Packet blockPacket = new Packet("BLOCK_USER", blockedUsername, null);
            try {
                sendPacket(userDBSocket, blockPacket);
                Packet response = receivePacket(userDBSocket);
                if (response.getContent() != null) {
                    UserEntry blocked = (UserEntry) response.getContent();
                    currUser.getBlockList().add(blocked.getID());
                    System.out.println("Blocked user: " + blocked.getUsername());
                } else {
                    System.out.println("User was not found.");
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error while blocking user: " + e.getMessage());
            }
        }
    }

    public void newConvo() {
        synchronized (lock) {
            System.out.print("Enter username to start a conversation with: ");
            String username = scanner.nextLine();

            Packet convoPacket = new Packet("START_CONVO", username, null);
            try {
                sendPacket(msgDBSocket, convoPacket);
                Packet response = receivePacket(msgDBSocket);
                if (response.getContent() != null) {
                    System.out.println("Started conversation with " + username);
                } else {
                    System.out.println("User was not found.");
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error while starting conversation: " + e.getMessage());
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

            Packet textPacket = new Packet("SEND_TEXT_MSG", recipientUsername, null);
            try {
                sendPacket(msgDBSocket, textPacket);
                Packet response = receivePacket(msgDBSocket);
                if (response.getContent() != null) {
                    System.out.println("Text message sent.");
                } else {
                    System.out.println("Error when sending text message.");
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error sending text message: " + e.getMessage());
            }
        }
    }

    public void sendPhotoMsg() {
        synchronized (lock) {
            System.out.print("Enter recipient's username: ");
            String recipientUsername = scanner.nextLine();

            Packet photoPacket = new Packet("SEND_PHOTO_MSG", recipientUsername, null);
            try {
                sendPacket(msgDBSocket, photoPacket);
                Packet response = receivePacket(msgDBSocket);
                if (response.getContent() != null) {
                    System.out.println("Photo message sent.");
                } else {
                    System.out.println("Error when sending photo message.");
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error sending photo message: " + e.getMessage());
            }
        }
    }
    private void sendPacket(Socket socket, Packet packet) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(packet);
        out.flush();
    }

    private Packet receivePacket(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        return (Packet) in.readObject();
    }

}