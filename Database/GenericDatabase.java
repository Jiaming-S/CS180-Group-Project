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
  protected BufferedReader bfr;
  protected PrintWriter pw;

  public GenericDatabase(String filepath) throws IOException {
    this.file = new File(filepath);
    if (!file.exists()) file.createNewFile();

    this.bfr = new BufferedReader(new FileReader(this.file));
    this.pw = new PrintWriter(new FileOutputStream(this.file, true));
  }

  public void writeStringsToFile(ArrayList<GenericEntry> db) {
    for (GenericEntry ge : db) {
      pw.println(ge.toString());
    }
  }

  public ArrayList<String> readStringsFromFile() throws IOException {
    String fileContents = "";

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

    return entries;
  }
}