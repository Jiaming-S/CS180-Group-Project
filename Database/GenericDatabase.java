package Database;

import java.io.*;

/**
 * This abstract class represents a generic database that implements some features of Database interface. 
 * Not intended for direct use.
 * @author Jiaming Situ
 * @version 11/02/2024
 */
public abstract class GenericDatabase implements Database {
  protected File file;
  protected BufferedReader bfr;
  protected PrintWriter pw;

  public GenericDatabase(String filepath) throws IOException {
    this.file = new File(filepath);
    file.createNewFile();

    this.bfr = new BufferedReader(new FileReader(this.file));
    this.pw = new PrintWriter(new FileOutputStream(this.file));
  }
}
