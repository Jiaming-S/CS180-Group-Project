import Database.MessageDatabase;
import Database.UserDatabase;
import User.User;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Runner {
    /**
     * This class is a sample class with the main method that creates the
     * shell for full implementation of the program. Handles user interaction,
     * and in the future will handle GUI.
     * @author Jane Bazzell
     * @version 11/02/2024
     */

    public static void addUser(Scanner scanner, UserDatabase userDatabase) {
        System.out.println("Please enter your new username");
        String username = scanner.next();
        if (username.contains(" ") || username.length() < 3) {
            System.out.println("Invalid username");
            addUser(scanner, userDatabase);
        }
        System.out.println("Please enter your new password");
        String password = scanner.next();
        if (password.length() < 3) {
            System.out.println("Please increase password length");
            addUser(scanner, userDatabase);
        } userDatabase.insertEntry((new User(username, password)).userToEntry());
    }

    public static boolean logIn(String username, String pwInput, UserDatabase database) {
        User user = new User(database.searchByName(username));
        if (pwInput.equals(user.getPassword())) {
            return true;
        } return false;
    }

    public void openGUI() {

    }

    public static User attemptLogin(Scanner scanner, UserDatabase database) {
        System.out.println("Enter username: ");
        String username = scanner.next();
        System.out.println("Enter password: ");
        String pw = scanner.next();
        if (!logIn(username, pw, database)) {
            System.out.println("Incorrect!");
            attemptLogin(scanner, database);
        } else {
            System.out.println("Welcome " + username);
            return new User (database.searchByName(username));
        }
        return null;
    }

    public static void main(String[] args) throws IOException {

        UserDatabase userDatabase;
        MessageDatabase messageDatabase;
        User currentUser = null;

        try {
            userDatabase = new UserDatabase("filepath");
            messageDatabase = new MessageDatabase("filepath");
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
            } switch (selection) {
                case 1:
                    addUser(scanner, userDatabase);
                    System.out.println("User created");
                    break;
                case 2:
                    currentUser = attemptLogin(scanner, userDatabase);
                    //where we will, in phase 2, start a new thread for this login session.
                    loggedIn = true;
                    break;
                default:
                    System.out.println("Please select a valid option.");
                    break;
            }
        }
        while (loggedIn) {
            selection = 0;
            System.out.println("1 - Search users\n2 - Log out");
            try {
                selection = scanner.nextInt();
                scanner.next();
            } catch (InputMismatchException e) {
                System.out.println("Please enter 1 or 2.");
                break;
            }
            switch (selection) {
                case 1:
                    System.out.println("Enter username to search");
                    String username = scanner.next();
                    User user = new User(userDatabase.searchByName(username));
                    System.out.println("Username: " + user.getUsername());
                    System.out.println("ID: " + user.getID());
                    System.out.println("Region: " + user.getRegion());
                    break;
                case 2:
                    loggedIn = false;
                    break; //will probably have to implement a more complex solution with threads later
                default:
                    System.out.println("Please select a valid option.");
                    break;
            } break;
        }
    }

}
