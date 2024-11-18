Nikita Sirandasu - Submitted Vocareum workspace.
How to use the app:
Start the servers UserDatabaseServer and MessageDatabaseServer. The Runner class should be run to start the servers and runs them in separate threads. Users can interact with the app by connecting to the server on the appropriate ports (port 12345). The user will be asked to either log in or create an account. They can perform actions like searching for users, sending messages, and managing conversations. Once the user chooses to log out, the app will terminate.


Database Classes:
- Both `UserDatabase` and `MessageDatabase` extend `GenericDatabase` which implements `Database`. 
- Both `UserEntry` and `MessageEntry` implement `GenericEntry`.
- `UserDatabase` and `MessageDatabase` have persistent data storage using .txt files in XML format. 
- `UserDatabase` and `MessageDatabase` have ArrayLists of `UserEntry` and `MessageEntry` which parse XML to populate.

Testing Classes:
- We created basic JUnit tests to ensure that UserEntry, MessageEntry, UserDatabase, MessageDatabaseServer, UserDatabaseServer, and UserThread worked as expected. 
- We did not test any "corner cases" or unconventional uses of the classes as we have not implemented the ability to handle those yet.
  
Server Classes:
- Both `UserDatabaseServer` and `MessageDatabaseServer` extend `GenericDatabaseServer` which implements `DatabaseServer'.
- `UserDatabaseServer` is a thread-safe web API for interacting with a UserDatabase and handles queries like searching by name or ID and inserting new entries.
- `MessageDatabaseServer`is a thread-safe web API for managing message data, handling queries related to searching and inserting message entries in a MessageDatabase.
- Packet.java is used to communicate between clients and the DatabaseServers. A packet contains a query, content, and optionally a recipient. It is serializable so that it can be sent over a network.

Runner.java is the program's main method that allows for client-database interaction through the network, performs login/password validation, and begins the threads for databases and multi-client access. 

User.java and Message.java act as a framework and method holder for users and messages that can be used by the client-side classes like Runner and UserThread without touching database classes such as UserEntry and MessageEntry.

UserThread.java handles client-server communication in order for the server to have a multi-threaded environment, for a User Database server and Message Database Server. It runs on a separate thread for each client connection, which allows for multiple clients to interact with the server simultaneously. The class creates input and output streams for communication between the server and the client using ObjectInputStream and ObjectOutputStream. It processes incoming requests from clients, handles various query types, and sends appropriate responses back. This class implements the UserThreadInt interface, which defines essential methods for user interaction such as searchUser, viewProfile, and blockUser. The viewMsg method is currently not implemented and will be developed in a later phase, as it requires searching the message database for a user's conversations. The sendPhotoMsg() methods use assumed implementations of the PhotoMessage classes, which may be modified as the project evolves.
