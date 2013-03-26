SimScheduler
============

A simple CPU scheduler simulator made in Java for learning purposes. There are two classes which shouldn't be altered, unless you're *very* certain of what you're doing: `CPU` and `Process`. One should implement his scehduling policy inside the `Scheduler` class.

## Compiling

Before anything, you need to have a Java compiler installed (Go either [Oracle's SDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html) or [OpenJDK](http://openjdk.java.net/install/), or any Java SDK you like). 

Just go to the root of this project and do (assuming your Java compiler is `javac`):
```bash
javac -d build src/*.java
```

## Running

Go to the build directory and run the `OS.class` program:
```bash
cd build/
java OS
```
## Documentation

TODO