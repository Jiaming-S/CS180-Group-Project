package Database;

import java.io.*;
import java.util.ArrayList;

/**
 * This class represents a MessageDatabase storing message information.
 * @author Jiaming Situ
 * @version 11/02/2024
 */
public class MessageDatabase extends GenericDatabase {
  private ArrayList<MessageEntry> db;

  public MessageDatabase(String filepath) throws IOException {
    super(filepath);

    this.db = new ArrayList<>();
    ArrayList<String> lines = readStringsFromFile();
    for (String s : lines) {
      try {
        db.add(new MessageEntry(s));
      } catch (ParseExceptionXML e) {
        e.printStackTrace();
      }
    }
  }

  synchronized public MessageEntry searchBySenderID(int id) {
    for (MessageEntry me : db) {
      if (me.getSender() == id) {
        return me;
      }
    }
    return null;
  }

  synchronized public MessageEntry searchByRecipientID(int id) {
    for (MessageEntry me : db) {
      if (me.getRecipient() == id) {
        return me;
      }
    }
    return null;
  }

  @Override
  synchronized public Object getEntry(int rowNum) {
    return db.get(rowNum);
  }

  @Override
  synchronized public void insertEntry(Object entry) {
    db.add((MessageEntry) entry);
  }
}
