package Database;

import java.io.*;
import java.util.ArrayList;

/**
 * This class represents a UserDatabase storing user information.
 * @author Jiaming Situ
 * @version 11/02/2024
 */
public class UserDatabase extends GenericDatabase {
  private static final Object USR_DB_LOCK = new Object();
  private ArrayList<UserEntry> db;

  public UserDatabase(String filepath) throws IOException {
    super(filepath);
    this.db = new ArrayList<>();
    ArrayList<String> lines = readStringsFromFile();
    for (String s : lines) {
      try {
        db.add(new UserEntry(s));
      } catch (ParseExceptionXML e) {
        e.printStackTrace();
      }
    }

    for (UserEntry ue : this.db) UserEntry.HIGHEST_ID = Math.max(UserEntry.HIGHEST_ID, ue.getID());
  }

  public UserEntry searchByName (String name) {
    synchronized(USR_DB_LOCK) {
      for (UserEntry ue : db) {
        if (ue.getUsername().equals(name)) {
          return ue;
        }
      }
      return null;
    }
  }

  public UserEntry searchByID (int id) {
    synchronized(USR_DB_LOCK) {
      for (UserEntry ue : db) {
        if (ue.getID() == id) {
          return ue;
        }
      }
      return null;
    }
  }

  public void addFriend(int myID, int friendID) {
    UserEntry me = searchByID(myID);
    synchronized(USR_DB_LOCK) {
      me.getFriendList().add(friendID);
      writeStringsToFile(this.db);
    }
  }

  public void removeFriend(int myID, int friendID) {
    UserEntry me = searchByID(myID);
    synchronized(USR_DB_LOCK) {
      me.getFriendList().remove(friendID);
      writeStringsToFile(this.db);
    }
  }

  @Override
  public Object getEntry(int rowNum) {
    synchronized(USR_DB_LOCK) {
      return db.get(rowNum);
    }
  }

  @Override
  public void insertEntry(Object entry) {
    synchronized(USR_DB_LOCK) {
      UserEntry ue = (UserEntry) entry;
      ue.setID(ue.getID() > UserEntry.HIGHEST_ID ? ue.getID() : UserEntry.incrementAndGetID());
      db.add(ue);
      writeStringsToFile(this.db);
    }
  }
}
