package User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * MainPage provides GUi for user interactions, providing navigation options to
 * access key features such as messaging, profile management
 * @author Nikita Sirandasu
 * @version 12/8/2024
 */
public class MainPage extends JComponent {
    private JFrame frame;
    UserThread userThread;
    User currUser;
    JButton togglePrivacyButton;

    public MainPage(UserThread userThread) {
        this.userThread = userThread;
        currUser = userThread.getCurrUser();
        frame = new JFrame("AOL Squared");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
    }

    public void showPage() {
        // frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // frame.setUndecorated(true);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton searchButton = new JButton("Search User");
        JTextField searchField = new JTextField(15);
        JButton profileButton = new JButton("View Profile");
        JButton sendTextButton = new JButton("Send Text Message");
        // JButton sendPhotoButton = new JButton("Send Photo Message");
        JButton editProfileButton = new JButton("Edit Profile");
        JButton msgButton = new JButton("View Message");
        JButton logOutButton = new JButton("Log Out");
        togglePrivacyButton = new JButton();

        panel.add(searchButton);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.add(new JLabel("Enter user to search"));
        searchPanel.add(searchField);

        panel.add(searchPanel);

        panel.add(profileButton);
        panel.add(editProfileButton);
//        panel.add(blockButton);
//        panel.add(convoButton);
//        panel.add(msgButton);
        panel.add(msgButton);
        panel.add(sendTextButton);
        // panel.add(sendPhotoButton);
        panel.add(logOutButton);
        panel.add(togglePrivacyButton);

        checkPrivPref();
        togglePrivacyButton.addActionListener(toggleActionListener);

        // Action Listeners for buttons
        profileButton.addActionListener(_ -> {
            ProfilePage profilePage = new ProfilePage(userThread, userThread.getCurrUser().userToEntry());
            profilePage.viewProfile();
        });

        searchButton.addActionListener(_ -> {
            String searchUser;
            if (searchField.getText().equals("")) {
                JOptionPane.showMessageDialog(frame, "Please enter a user name");
            } else {
                searchUser = searchField.getText();
                userThread.searchUser(searchUser);
            }
        });


        msgButton.addActionListener(_ -> {
            ConversationPage convoPage = new ConversationPage(userThread);
            convoPage.viewMessagePage();
        });

        sendTextButton.addActionListener(_ -> {
            String otherUserName = JOptionPane.showInputDialog(
                null, 
                "Enter a username.", 
                "Search", 
                JOptionPane.QUESTION_MESSAGE
            );

            DirectMessagePage dmPage = new DirectMessagePage(
                userThread, 
                userThread.userFromUsername(otherUserName).getID()
            );
            
            dmPage.viewDMPage();
        });

        // sendPhotoButton.addActionListener(_ -> {
        //     userThread.sendPhotoMsg();
        // });


        logOutButton.addActionListener(_ -> {
            frame.dispose();

        });

        editProfileButton.addActionListener(_ -> {
            userThread.editProfile();
        });

//        profileButton.addActionListener(_ -> System.out.println("View Profile clicked"));
//        blockButton.addActionListener(_ -> System.out.println("Block User clicked"));
//        convoButton.addActionListener(_ -> System.out.println("Start New Conversation clicked"));
//        msgButton.addActionListener(_ -> System.out.println("View Message clicked"));
        // sendTextButton.addActionListener(_ -> System.out.println("Send Text Message clicked"));
        // sendPhotoButton.addActionListener(_ -> System.out.println("Send Photo Message clicked"));

        frame.add(panel, BorderLayout.NORTH);
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    public void checkPrivPref() {
        if (currUser.getPrivacyPreference().equals("Friends")) {
            togglePrivacyButton.setText("Allow messages from everyone");
        } else if (currUser.getPrivacyPreference().equals("All")) {
            togglePrivacyButton.setText("Allow messages from only friends");
        }
    }

    ActionListener toggleActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currUser.getPrivacyPreference().equals("All")) {
                userThread.updateUserPrivacy("Friends");
            } else if (currUser.getPrivacyPreference().equals("Friends")){
                userThread.updateUserPrivacy("All");
            }
            checkPrivPref();
        }
    };
}
