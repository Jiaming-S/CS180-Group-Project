package Database;

import java.io.*;
import java.util.ArrayList;

/**
 * This class represents a MessageDatabase storing message information.
 * @author Jiaming Situ
 * @version 11/02/2024
 */
public class MessageDatabase extends GenericDatabase {
  private static final Object MSG_DB_LOCK = new Object();
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

  public MessageEntry searchFirstBySenderID(int id) {
    synchronized(MSG_DB_LOCK) {
      for (MessageEntry me : db) {
        if (me.getSender() == id) {
          return me;
        }
      }
      return null;
    }
  }

  public ArrayList<MessageEntry> searchAllBySenderID(int id) {
    synchronized(MSG_DB_LOCK) {
    ArrayList<MessageEntry> results = new ArrayList<>();
      for (MessageEntry me : db) {
        if (me.getSender() == id) {
          results.add(me);
        }
      }
      return results;
    }
  }

  public MessageEntry searchFirstByRecipientID(int id) {
    synchronized(MSG_DB_LOCK) {
      for (MessageEntry me : db) {
        if (me.getRecipient() == id) {
          return me;
        }
      }
      return null;
    }
  }

  public ArrayList<MessageEntry> searchAllByRecipientID(int id) {
    synchronized(MSG_DB_LOCK) {
      ArrayList<MessageEntry> results = new ArrayList<>();
      for (MessageEntry me : db) {
        if (me.getRecipient() == id) {
          results.add(me);
        }
      }
      return results;
    }
  }

  public void deleteMessage(MessageEntry entry) {
    synchronized(MSG_DB_LOCK) {
      this.db.remove(entry);
    }
  }

  @Override
  public Object getEntry(int rowNum) {
    synchronized(MSG_DB_LOCK) {
      return db.get(rowNum);
    }
  }

  @Override
  public void insertEntry(Object entry) {
    synchronized(MSG_DB_LOCK) {
      db.add((MessageEntry) entry);
    }
  }
}
