package Net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

import Database.UserDatabase;

/**
 * A set of JUnit tests testing netcode.
 * @author
 * @version 11/16/24
 */
public class NetworkTest {
  // @Test
  // public void testUserDatabaseAPI() throws IOException, InterruptedException, ClassNotFoundException {
  public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
    UserDatabaseServer udbserv = new UserDatabaseServer(
      new ServerSocket(12345),
      new UserDatabase("./Database/databaseTestFile.txt")
    );

    Thread serverThread = new Thread(udbserv);
    serverThread.start();

    Socket client = new Socket("127.0.0.1", 12345);
    ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
    ObjectInputStream ois  = new ObjectInputStream(client.getInputStream());

    oos.writeObject(new Packet(
      "searchByName",
      "joebiden", 
      null
    ));

    System.out.println(ois.readObject());

    serverThread.join();
    client.close();
  }
}
