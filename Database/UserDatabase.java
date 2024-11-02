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
  }

  @Override
  public Object getEntry(int rowNum) {
    return db.get(rowNum);
  }

  @Override
  public void insertEntry(Object entry) {
    db.add((UserEntry) entry);
  }
}
