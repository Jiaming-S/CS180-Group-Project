package User;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ProfilePage extends JComponent {

    private JFrame frame;
    private UserThread userThread;
    private User currUser;
    private final Object lock = new Object();
    private JButton blockButton;
    private JButton friendButton;
    private JButton messageButton;

    public ProfilePage(UserThread userThread) {
        this.userThread = userThread;
        User currUser = userThread.getCurrUser();
        frame = new JFrame("AOL TWO");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
    }

    public void viewProfile() {
        Container content = new Container();
        content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        leftPanel.add(new JLabel(currUser.getUsername()));

        ImageIcon image = new ImageIcon("aol.jpg");//scaleImageIcon(ImageIcon(currUser.getProfilePicture()), 100, 100);

        JLabel jLabel = new JLabel();
        jLabel.setIcon(image);
        leftPanel.add(jLabel);

        leftPanel.add(new JLabel("User bio:"));
        leftPanel.add(new JLabel(currUser.getBio()));

        content.add(leftPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        rightPanel.add(new JLabel("ID: " + currUser.getID()));
        rightPanel.add(new JLabel("Location: " + currUser.getRegion()));

        friendButton.setText("Add Friend");
        blockButton.setText("Block User");
        messageButton.setText("Message User");

        rightPanel.add(friendButton);
        rightPanel.add(blockButton);
        rightPanel.add(messageButton);

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
