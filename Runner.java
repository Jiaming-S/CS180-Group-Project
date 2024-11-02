import java.util.Scanner;

public class Runner {

    public static boolean addUser(Scanner scanner) {
        System.out.println("Please enter your new username");
        String username = scanner.next();
        if (username.contains(" ") || username.length() < 3) {
            System.out.println("Invalid username");
            return false;
        }
        System.out.println("Please enter your new password");
        String password = scanner.next();
        if (password.length() < 3) {
            System.out.println("Please increase password length");
            return false;
        } return true;
    }

    public boolean logIn(String username, String pwInput) {
        //UserDB.checkLogin(username, pwInput);
        return true;
    }

    public boolean logOut() {
        return true;
    }

    public void openGUI() {

    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome");
        System.out.println("1 - Create New User\n2 - Log In");
        int selection = scanner.nextInt();
        scanner.next();

        switch (selection) {
            case 1:
                if (!addUser(scanner)) {
                    addUser(scanner);
                } else {
                    System.out.println("User created");
                }
            case 2:
        }

    }

}
