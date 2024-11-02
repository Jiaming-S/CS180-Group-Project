package Database;

/**
 * This interface represents a contract for any database.
 * @author Jiaming Situ
 * @version 11/02/2024
 */
public interface Database {
  Object getEntry(int rowNum);
  boolean insertEntry(Object entry);
}
