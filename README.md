Jane Bazzell - Submitted Vocareum workspace.
How to use the app:
Starting ServerRunner.java sets up the server-side environment by initializing UserDatabase and MessageDatabase,Runner.java. Starting the Runner class for the ports to work with UserDatabaseServer and MessageDatabaseServer. In the window that pops up, the user will be asked to either log in or create an account. After entering username and password, the user is logged in. They can perform actions like searching for users, sending text messages, editing their profile, privacy settings, viewing messages, and logging out.


Database Classes:
- Both `UserDatabase` and `MessageDatabase` extend `GenericDatabase` which implements `Database`. 
- Both `UserEntry` and `MessageEntry` implement `GenericEntry`.
- `UserDatabase` and `MessageDatabase` have persistent data storage using .txt files in XML format. 
- `UserDatabase` and `MessageDatabase` have ArrayLists of `UserEntry` and `MessageEntry` which parse XML to populate.

Testing Classes:
- We created basic JUnit tests to ensure that UserEntry, MessageEntry, UserDatabase, MessageDatabaseServer, UserDatabaseServer, TextMessage, PhotoMessage, and UserThread worked as expected.
- There are also test text files to ensure that the database is properly recording the users and messages.
- We created basic tests for Network IO.
  
Network Classes:
- Both `UserDatabaseServer` and `MessageDatabaseServer` extend `GenericDatabaseServer` which implements `DatabaseServer'.
- `UserDatabaseServer` is a thread-safe web API for interacting with a UserDatabase and handles queries like searching by name or ID and inserting new entries.
- `MessageDatabaseServer`is a thread-safe web API for managing message data, handling queries related to searching and inserting message entries in a MessageDatabase.
- Packet.java is used to communicate between clients and the DatabaseServers. A packet contains a query, content, and optionally a recipient. It is serializable so that it can be sent over a network.

Runner.java is the program's main method that allows for client-database interaction through the network, performs login/password validation, and begins the threads for databases and multi-client access. 

User.java and Message.java act as a framework and method holder for users and messages that can be used by the client-side classes like Runner and UserThread without touching database classes such as UserEntry and MessageEntry.
TextMessage.java is a class that handles messages that contain a string of text and records the senderID number and the recipientID number.
PhotoMessage.java is a class that allows for messages that contain a filepath for a photo to be shared between users.
UserThread.java handles client-server communication in order for the server to have a multi-threaded environment, for a User Database server and Message Database Server. It runs on a separate thread for each client connection, which allows for multiple clients to interact with the server simultaneously. The class creates input and output streams for communication between the server and the client using ObjectInputStream and ObjectOutputStream. It processes incoming requests from clients, handles various query types, and sends appropriate responses back. This class implements the UserThreadInt interface, which defines essential methods for user interaction such as searchUser, friendUser, and blockUser. 

GUI Classes:

Conversation.java displays a list of conversations with users, allowing the current user to view message histories, continue messaging, and handle blocked or non-friend users based on their privacy settings.
DirectMessagePage.java handles the user functionality for viewing and sending direct messages between users, displaying message history and allowing message deletion or sending new messages.
ProfilePage.java allows the user to view and edit their profile information, such as username, password, and privacy preferences, in a GUI.
MainPage.java is the main interface for the user which allows them to navigate through different functionalities like viewing messages, managing profiles, and interacting with other users.
