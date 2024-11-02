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

    public static void addUser(Scanner scanner) {
        System.out.println("Please enter your new username");
        String username = scanner.next();
        if (username.contains(" ") || username.length() < 3) {
            System.out.println("Invalid username");
            addUser(scanner);
        }
        System.out.println("Please enter your new password");
        String password = scanner.next();
        if (password.length() < 3) {
            System.out.println("Please increase password length");
            addUser(scanner);
        }
    }

    public static boolean logIn(String username, String pwInput) {
        //UserDB.checkLogin(username, pwInput);
        return true;
    }

    public void openGUI() {

    }

    public static void attemptLogin(Scanner scanner) {
        System.out.println("Enter username: ");
        String username = scanner.next();
        System.out.println("Enter password: ");
        String pw = scanner.next();
        if (!logIn(username, pw)) {
            System.out.println("Incorrect!");
            attemptLogin(scanner);
        } else {
            System.out.println("Welcome " + username);
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = false;
        int selection = 0;

        System.out.println("Welcome");

        while (!loggedIn) {
            System.out.println("1 - Create New User\n2 - Log In");
            try {
                selection = scanner.nextInt();
                scanner.next();
            } catch (InputMismatchException e) {
                System.out.println("Please enter 1 or 2.");
                break;
            } switch (selection) {
                case 1:
                    System.out.println("User created");
                    break;
                case 2:
                    attemptLogin(scanner);
                    loggedIn = true;
                    break;
                default:
                    System.out.println("Please select a valid option.");
                    break;
            }
        }
        while (loggedIn) {
            selection = 0;
            System.out.println("1 - View conversations\n2 - Search users\n3 - Edit profile\n4 - Log out");
            try {
                selection = scanner.nextInt();
                scanner.next();
            } catch (InputMismatchException e) {
                System.out.println("Please enter 1 or 2.");
                break;
            }
            switch (selection) {
                case 1: // TODO: View Conversations capability
                case 2: // TODO: User search capability
                case 3: // TODO: Edit profile capability
                case 4:
                    loggedIn = false; //will probably have to implement a more complex solution with threads later
                default:
                    System.out.println("Please select a valid option.");
                    break;
            }
        }
    }

}
