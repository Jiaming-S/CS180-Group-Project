Testing classes:
We created basic JUnit tests to ensure that UserEntry, MessageEntry, and UserDatabase worked as expected. We did not test any "corner cases" or unconventional uses of the classes as we have not implemented the ability to handle those yet.

We have a basic main method contained in Runner.java that allows us to test database access and begin preliminary work on full-scale implementation. However, this is more just for trial use, and the main form of testing for Phase 1 is done through the JUnit tests. Runner.java will be greatly revised in the future as we implement networking, concurrency, and GUI.

User.java and Message.java are simple classes that will contain future methods allowing for the viewing of a user profile.
