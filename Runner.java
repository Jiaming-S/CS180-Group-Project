import Database.*;
import Net.*;
import User.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Runner {

    /**
     * This class is the main method run by the client. Handles user interaction, network
     * startup and interaction, thread creation, and in the future will handle GUI.
     * @author Jane Bazzell
     * @version 11/17/2024
     */

    public static void main(String[] args) throws IOException {

        UserDatabase userDatabase;
        MessageDatabase messageDatabase;

        UserThread userThread = null;
        User currentUser = null;


        String hostName = "localhost";
        int portUDBS = 19693;
        int portMDBS = 12422;
        Socket userSocket, messageSocket;
        ObjectOutputStream uoos, moos;
        ObjectInputStream uois, mois;

        try {
            userDatabase = new UserDatabase("user_db.txt");
            UserDatabaseServer userDatabaseServer = new UserDatabaseServer(
                new ServerSocket(portUDBS), 
                userDatabase
            );

            messageDatabase = new MessageDatabase("message_db.txt");
            MessageDatabaseServer messageDatabaseServer = new MessageDatabaseServer(
                new ServerSocket(portMDBS),
                messageDatabase
            );

            Thread udbs = new Thread(userDatabaseServer);
            udbs.start();

            Thread mdbs = new Thread(messageDatabaseServer);
            mdbs.start(); // note: probably don't need to do anything with message db in runner, good to just start it tho

            userSocket = new Socket(hostName, portUDBS);
            uoos = new ObjectOutputStream(userSocket.getOutputStream());
            uoos.flush();
            uois = new ObjectInputStream(userSocket.getInputStream());

            messageSocket = new Socket(hostName, portMDBS);
            moos = new ObjectOutputStream(messageSocket.getOutputStream());
            moos.flush();
            mois = new ObjectInputStream(messageSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = false;
        int selection = 0;

        System.out.println("Welcome");

        while (!loggedIn) {
            System.out.println("1 - Create New User\n2 - Log In");
            try {
                selection = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please enter 1 or 2.");
                break;
            }
            switch (selection) {
                case 1:
                    addUser(scanner, userDatabase, uoos, uois);
                    System.out.println("User created");
                    break;
                case 2:
                String newUserStr = """
        <User>
            <Username>name4</Username>
            <Password>password123</Password>
            <ID>00000004</ID>
            <FriendList>
                <ID>11223344</ID>
                <ID>55667788</ID>
                <ID>12345678</ID>
            </FriendList>
            <BlockList>
                <ID>10101010</ID>
                <ID>44444444</ID>
            </BlockList>
            <ProfilePicture>/path/to/image.png</ProfilePicture>
            <Region>USA/Midwest</Region>
        </User>""".replaceAll(" ", "").replaceAll("\n", "");
                    try {
                    currentUser = new User(new UserEntry(newUserStr));
                    } catch (Exception e) {
                        
                    }
                    if (currentUser == null) {
                        break;
                    }
                    try {
                        userThread = new UserThread(currentUser, uoos, uois, moos, mois);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    loggedIn = true;
                    break;
                default:
                    System.out.println("Please select a valid option.");
                    break;
            }
        } if (userThread != null) {
            userThread.start();
        } try {
            if (userThread != null) userThread.join();
            if (uois != null) uois.close();
            if (uoos != null) uoos.close();
            if (userSocket != null) userSocket.close();
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addUser(Scanner scanner, UserDatabase userDatabase, ObjectOutputStream oos, ObjectInputStream ois) {
        System.out.println("Please enter your new username");
        String username = scanner.next();
        if (username.contains(" ") || username.length() < 3) {
            System.out.println("Invalid username");
            addUser(scanner, userDatabase, oos, ois);
        }
        System.out.println("Please enter your new password");
        String password = scanner.next();
        if (password.length() < 3) {
            System.out.println("Please increase password length");
            addUser(scanner, userDatabase, oos, ois);
        }
        UserEntry userEntry = new UserEntry(username, password, 0, new ArrayList<>(), new ArrayList<>(), "", "");
        Packet packet = new Packet("insertEntry", userEntry, null);
        try {
            oos.writeObject(packet);
            Packet response = (Packet) ois.readObject();
            boolean success = response.getQuery().equals("success");
            if (!success) {
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static User attemptLogin(Scanner scanner, UserDatabase database, ObjectOutputStream oos, ObjectInputStream ois) {
        System.out.println("Enter username: ");
        String username = scanner.next();
        System.out.println("Enter password: ");
        String pw = scanner.next();
        UserEntry ue = logIn(username, database, oos, ois);
        if (ue.getPassword().equals(pw)) {
            return new User(ue);
        } else {
            System.out.println("Incorrect!");
            attemptLogin(scanner, database, oos, ois);
        }
        return null;
    }

    public static UserEntry logIn(String username, UserDatabase database, ObjectOutputStream oos, ObjectInputStream ois) {
        Packet packet = new Packet("searchByName", username, null);
        UserEntry userE = null;
        try {
            oos.writeObject(packet);
            Packet response = (Packet) ois.readObject();
            if (response.getContent() == null) {
                return null;
            }
            System.out.println("here: " + new UserEntry(response.getContent().toString()));
            return new UserEntry(response.getContent().toString());
        } catch (Exception e) {
            e.printStackTrace();
        } return null;
    }

    public void openGUI() {

    }
}
