This project showcases an example of how to resolve a resource in a Maven project.

The `FileResolver.java` class demonstrates the usage of resolving a directory of files using the `getResource()` all the way to copying the directory and its files to the temp directory of the host operating system. Do note that this class will fail when running a JAR file of this project, as the file resolve method does not work in such context.

# How to run
Run `mvn clean compile exec:java`

# How to build (standalone JAR)
Run `mvn clean install assembly:single`