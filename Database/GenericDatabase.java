package Database;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This abstract class represents a generic database that implements some features of Database interface. 
 * Not intended for direct use.
 * @author Jiaming Situ
 * @version 11/02/2024
 */
public abstract class GenericDatabase implements Database {
  public final Pattern REGEX_XML_TOP_LEVEL = Pattern.compile("<(\\w+)>(.*?)</\\1>");

  protected File file;

  public GenericDatabase(String filepath) throws IOException {
    this.file = new File(filepath);
    if (!file.exists()) file.createNewFile();
  }

  synchronized public void writeStringsToFile(ArrayList<? extends GenericEntry> db) {
    // Clears the current file contents
    System.out.println("Updating database: " + this.file.toPath());
    PrintWriter pw;
    try {
      pw = new PrintWriter(this.file);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return;
    }

    // Updates database file with most recent db ArrayList contents
    for (GenericEntry ent : db) pw.print(ent.toString());
    pw.flush();
    pw.close();
  }

  synchronized public ArrayList<String> readStringsFromFile() throws IOException {
    System.out.println("Database linked to: " + this.file.toPath());
    String fileContents = "";

    BufferedReader bfr = new BufferedReader(new FileReader(this.file));
    String line = bfr.readLine();
    while (line != null) {
      fileContents += line;
      line = bfr.readLine();
    }

    Matcher toplevelXMLMatcher = REGEX_XML_TOP_LEVEL.matcher(fileContents);

    ArrayList<String> entries = new ArrayList<>();
    int curPosition = 0;
    while (curPosition < fileContents.length()) {
      if (toplevelXMLMatcher.find(curPosition) && toplevelXMLMatcher.start() == curPosition) {
        entries.add(toplevelXMLMatcher.group());
        curPosition = toplevelXMLMatcher.end();
      }
    }

    bfr.close();
    return entries;
  }
}
