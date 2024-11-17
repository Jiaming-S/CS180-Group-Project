import Database.MessageDatabase;
import Database.ParseExceptionXML;
import Database.UserDatabase;
import Database.UserEntry;
import Net.MessageDatabaseServer;
import Net.Packet;
import Net.UserDatabaseServer;
import User.*;

import javax.swing.*;
import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
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
        int portUDBS = 12345;
        int portMDBS = 12346;
        Socket socket;
        ObjectOutputStream oos;
        ObjectInputStream ois;

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

            socket = new Socket(hostName, portUDBS);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
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
                    addUser(scanner, userDatabase, oos, ois);
                    System.out.println("User created");
                    break;
                case 2:
                    currentUser = attemptLogin(scanner, userDatabase, oos, ois);
                    userThread = new UserThread(currentUser, userDatabase, messageDatabase);
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
            if (ois != null) ois.close();
            if (oos != null) oos.close();
            if (socket != null) socket.close();
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
        if (!logIn(username, pw, database, oos, ois)) {
            System.out.println("Incorrect!");
            attemptLogin(scanner, database, oos, ois);
        } else {
            return new User (database.searchByName(username));
        }
        return null;
    }

    public static boolean logIn(String username, String pwInput, UserDatabase database, ObjectOutputStream oos, ObjectInputStream ois) {
        Packet packet = new Packet("searchByName", username, null);
        UserEntry userE = null;
        try {
            oos.writeObject(packet);
            Packet response = (Packet) ois.readObject();
            userE = new UserEntry(String.valueOf(response.getContent()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (pwInput.equals(userE.getPassword())) {
            return true;
        } return false;
    }

    public void openGUI() {

    }
}
