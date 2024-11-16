package Database;

import java.io.*;
import java.util.ArrayList;

/**
 * This class represents a UserDatabase storing user information.
 * @author Jiaming Situ
 * @version 11/02/2024
 */
public class UserDatabase extends GenericDatabase {
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
  }

  synchronized public UserEntry searchByName (String name) {
    for (UserEntry ue : db) {
      if (ue.getUsername().equals(name)) {
        return ue;
      }
    }
    return null;
  }

  synchronized public UserEntry searchByID (int id) {
    for (UserEntry ue : db) {
      if (ue.getID() == id) {
        return ue;
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
    db.add((UserEntry) entry);
  }
}
