package Database;

/**
 * This class represents an exception that occurs when parsing XML. 
 * @author Jiaming Situ
 * @version 11/02/2024
 */
public class ParseExceptionXML extends RuntimeException{
  public ParseExceptionXML() {
    super();
  }

  public ParseExceptionXML(String s) {
    super(s);
  }
}
