Database Classes:
- Both `UserDatabase` and `MessageDatabase` extend `GenericDatabase` which implements `Database`. 
- Both `UserEntry` and `MessageEntry` implement `GenericEntry`.
- `UserDatabase` and `MessageDatabase` have persistent data storage using .txt files in XML format. 
- `UserDatabase` and `MessageDatabase` have ArrayLists of `UserEntry` and `MessageEntry` which parse XML to populate.

Testing Classes:
- We created basic JUnit tests to ensure that UserEntry, MessageEntry, and UserDatabase worked as expected. 
- We did not test any "corner cases" or unconventional uses of the classes as we have not implemented the ability to handle those yet.

We have a basic main method contained in Runner.java that allows us to test database access and begin preliminary work on full-scale implementation. However, this is more just for trial use, and the main form of testing for Phase 1 is done through the JUnit tests. Runner.java will be greatly revised in the future as we implement networking, concurrency, and GUI.

User.java and Message.java are simple classes that will contain future methods allowing for the viewing of a user profile.


UserThread.java handles user actions such as being able to search for other users, block individuals, start new conversations, send new messages, and view messages. It uses the UserDatabase to gain access to user information and MessageDatabase to access message data. UserThreadInt is an interface for UserThread that defines essential methods for the user interaction, such as searchUser, viewProfile, and blockUser. The method viewMsg will be implemented in a later phase because we need to work on developing a way to search the message database for a user's conversations. This is a basic implementation and will need to altered as we incorporate the GUI into our project. 
