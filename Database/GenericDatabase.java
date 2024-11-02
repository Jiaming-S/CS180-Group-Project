package Database;

import java.io.*;
import java.util.ArrayList;

/**
 * This abstract class represents a generic database that implements some features of Database interface. 
 * Not intended for direct use.
 * @author Jiaming Situ
 * @version 11/02/2024
 */
public abstract class GenericDatabase implements Database {
  protected File file;
  protected ArrayList<GenericEntry> db;

  public GenericDatabase(String filepath) throws IOException {
    this.file = new File(filepath);
    file.createNewFile();
  }
}
