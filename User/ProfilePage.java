package User;

import Database.UserEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ProfilePage extends JComponent {

    private JFrame frame;
    private UserThread userThread;
    private UserEntry profiledUser;
    private UserEntry currUser;
    private final Object lock = new Object();
    private JButton blockButton;
    private JButton friendButton;
    private JButton messageButton;

    public ProfilePage(UserThread userThread, UserEntry user) {
        this.profiledUser = user;
        this.userThread = userThread;
        this.currUser = new UserEntry(userThread.getCurrUser());
        frame = new JFrame("AOL TWO");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        blockButton = new JButton();
        friendButton = new JButton();
        messageButton = new JButton();
    }

    public void viewProfile() {
        Container content = new Container();
        content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        leftPanel.add(new JLabel(profiledUser.getUsername()));
        if (profiledUser.getProfilePicture() != null && !profiledUser.getProfilePicture().equals("")) {
            ImageIcon image = scaleImageIcon(new ImageIcon(profiledUser.getProfilePicture()), 100, 100);
            JLabel jLabel = new JLabel();
            jLabel.setIcon(image);
            leftPanel.add(jLabel);
        } else {
            ImageIcon image = scaleImageIcon(new ImageIcon("aol.jpg"), 100, 100);
            JLabel jLabel = new JLabel();
            jLabel.setIcon(image);
            leftPanel.add(jLabel);
        }

        leftPanel.add(new JLabel("User bio:"));
        leftPanel.add(new JLabel(profiledUser.getBio()));

        content.add(leftPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        rightPanel.add(new JLabel("ID: " + profiledUser.getID()));
        rightPanel.add(new JLabel("Location: " + profiledUser.getRegion()));

        if (currUser.getBlockList().contains(profiledUser.getID())) {
            blockButton.setText("Unblock user");
            blockButton.addActionListener(e -> {
                userThread.unblockUser(profiledUser.getUsername());
            });
        } else {
            blockButton.setText("Block User");
            blockButton.addActionListener(e -> {
                userThread.blockUser(profiledUser.getUsername());
            });
        }
        if (currUser.getFriendList().contains(profiledUser.getID())) {
            friendButton.setText("Unfriend user");
            friendButton.addActionListener(e -> {
                userThread.unfriendUser(profiledUser.getUsername());
            });
        } else {
            friendButton.setText("Friend User");
            friendButton.addActionListener(e -> {
                userThread.friendUser(profiledUser.getUsername());
            });
        }

        messageButton.setText("Message User");
        if (currUser.getID() != profiledUser.getID()) {
            rightPanel.add(friendButton);
            rightPanel.add(blockButton);
            rightPanel.add(messageButton);
        }

        content.add(rightPanel);

        frame.add(content);

        frame.setVisible(true);
    }


    private ImageIcon scaleImageIcon(ImageIcon icon, int width, int height) {
        BufferedImage bimage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        icon = new ImageIcon(bimage.getScaledInstance(100, 50, Image.SCALE_DEFAULT));
        bGr.drawImage(icon.getImage(), 0, 0, null);
        bGr.dispose();
        return icon;
    }
}
