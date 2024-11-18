Nikita Sirandasu - Submitted Vocareum workspace.
Database Classes:
- Both `UserDatabase` and `MessageDatabase` extend `GenericDatabase` which implements `Database`. 
- Both `UserEntry` and `MessageEntry` implement `GenericEntry`.
- `UserDatabase` and `MessageDatabase` have persistent data storage using .txt files in XML format. 
- `UserDatabase` and `MessageDatabase` have ArrayLists of `UserEntry` and `MessageEntry` which parse XML to populate.

Testing Classes:
- We created basic JUnit tests to ensure that UserEntry, MessageEntry, and UserDatabase worked as expected. 
- We did not test any "corner cases" or unconventional uses of the classes as we have not implemented the ability to handle those yet.

Runner.java is the program's main method that allows for client-database interaction through the network, performs login/password validation, and begins the threads for databases and multi-client access. 

User.java and Message.java act as a framework and method holder for users and messages that can be used by the client-side classes like Runner and UserThread without touching database classes such as UserEntry and MessageEntry.

UserThread.java handles user actions such as being able to search for other users, block individuals, start new conversations, send new messages, and view messages. It uses the UserDatabase to gain access to user information and MessageDatabase to access message data. UserThreadInt is an interface for UserThread that defines essential methods for the user interaction, such as searchUser, viewProfile, and blockUser. The method viewMsg will be implemented in a later phase because we need to work on developing a way to search the message database for a user's conversations. This is a basic implementation and will need to altered as we incorporate the GUI into our project. The methods sendTextMsg() and sendPhotoMsg() use an assumed version of PhotoMessage and TextMessage classes based on interface and may be changed.
