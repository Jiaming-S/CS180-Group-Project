package User;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Database.*;

public class ConversationPage extends JComponent {
  private JFrame frame;
  private UserThread userThread;
  private User currUser;

  public ConversationPage(UserThread userThread) {
    this.userThread = userThread;
    this.currUser = userThread.getCurrUser();

    frame = new JFrame("AOL TWO");
    frame.setSize(600, 400);
    frame.setLocationRelativeTo(null);
  }

  private JPanel makeMessageTile(MessageEntry me) {
    JPanel tile = new JPanel();
    tile.setLayout(new FlowLayout(FlowLayout.LEFT));
    tile.setBorder(new EmptyBorder(10, 10, 10, 10));
    
    JPanel wrapper = new JPanel();
    wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
    wrapper.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createLineBorder(Color.black),
      BorderFactory.createEmptyBorder(10, 10, 10, 10)
    ));

    JLabel tileHeader = new JLabel(
      String.format(
        "Message from %s at %s.",
        userThread.userFromID(me.getSender()).getUsername(),
        me.getTimestamp().toString()
      )
    );
    tileHeader.setLayout(new FlowLayout(FlowLayout.LEFT));
    wrapper.add(tileHeader);

    JLabel tileContent;
    if (me.getContent() == null) tileContent = new JLabel("null Message");
    else tileContent = new JLabel((String) (me.getContent().getMessage()));
    
    tileContent.setLayout(new FlowLayout(FlowLayout.LEFT));
    
    wrapper.add(tileContent);

    tile.add(wrapper);
    return tile;
  }

  public void viewMessagePage() {
    Container content = new Container();
    content.setLayout(new BorderLayout());

    JPanel messageDisplays = new JPanel();
    messageDisplays.setLayout(new BoxLayout(messageDisplays, BoxLayout.Y_AXIS));
    messageDisplays.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    ArrayList<MessageEntry> messagesToUser = userThread.getAllMessagesToUser(currUser.getID());
    ArrayList<MessageEntry> messagesFromUser = userThread.getAllMessagesFromUser(currUser.getID());

    HashSet<Integer> allConversations = new HashSet<>();
    if (messagesToUser != null) for (MessageEntry me : messagesToUser) allConversations.add(me.getSender());
    if (messagesFromUser != null) for (MessageEntry me : messagesFromUser) allConversations.add(me.getRecipient());
    
    for (int otherUserID : allConversations) {
      UserEntry ue = userThread.userFromID(otherUserID);

      JPanel convo = new JPanel();
      convo.setLayout(new BoxLayout(convo, BoxLayout.X_AXIS));

      JLabel convoUser = new JLabel(
        String.format(
          "<html>%s<h1>Conversation with %s:</h1><p>ID: %d</p><span>Timezone: %s</span></html>",
          (currUser.getBlockList().contains(otherUserID) ? "<h2>[BLOCKED USER]</h2>" : ""),
          ue.getUsername(),
          ue.getID(),
          ue.getRegion()
        )
      );
      convo.add(convoUser);

      ArrayList<MessageEntry> combinedInvolvingUser = new ArrayList<>();
      if (messagesToUser != null) combinedInvolvingUser.addAll(messagesToUser);
      if (messagesFromUser != null) combinedInvolvingUser.addAll(messagesFromUser);

      if (currUser.getBlockList().contains(otherUserID)) {
        JButton blockedContinueMsgButton = new JButton("BLOCKED USER");
        blockedContinueMsgButton.addActionListener(_ -> {
          JOptionPane.showMessageDialog(
            null, 
            "This user is blocked. Update your privacy preferences from 'Friends' to 'All' to view.", 
            "Info", 
            JOptionPane.INFORMATION_MESSAGE
          );
        });
        convo.add(blockedContinueMsgButton);
      } else {
        JButton continueMsgButton = new JButton("Continue Messaging");
        continueMsgButton.addActionListener(_ -> {
          DirectMessagePage dmp = new DirectMessagePage(userThread, otherUserID);
          dmp.viewDMPage();
        });
        convo.add(continueMsgButton);
      }

      messageDisplays.add(convo);

      for (MessageEntry me : combinedInvolvingUser.reversed()) {
        if (me.getSender() == otherUserID || me.getRecipient() == otherUserID) {
          JPanel msgTile = makeMessageTile(me);
          messageDisplays.add(msgTile);
          break;
        }
      }
    }

    JScrollPane messageScroller = new JScrollPane(messageDisplays);
    content.add(messageScroller);
    frame.add(content);
    frame.setVisible(true);
  }
}
