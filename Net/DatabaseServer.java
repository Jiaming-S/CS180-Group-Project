package Net;

/**
 * Interface for all database servers.
 * @author Jiaming Situ
 * @version 11/16/24
 */
public interface DatabaseServer {
  public Packet handlePacket(Packet P);
}
