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

### Class Process

#### Constructor

The `Process` class contains all process related data, including status, execution data and time measuring statistics. The constructor function requires a integer PID and a already instatiated Random number generator (`java.util.Random`). The constructor will create a radomly generated process with several CPU-Bursts. To create a new process, do:
```java
import java.util.Random;
Random gen = new Random();
Process p = new Process(1, gen);
```
The Process `p` will have a PID 1 and will bre ready for execution.

#### Public Methods

```java
public int getPID()
```
Returns the Process' PID.

```java
public int getStatus()
```
Returns the Process' current status:
* 0 - Ready
* 1 - Waiting
* 2 - Finished

```java
public void setStatus (int s)
```
Sets the status of the process, following the rules above.

```java
public int getNextBurstDuration()
```
Returns the amount of cycles of the next CPU Burst of the process.

```java
public void executeBurst()
```
Execute the next CPU burst until the end. If it is the last burst, the process is finished, otherwise, it will wait for I/O.

```java
public int getTurnaround()
```
Get the total amount of cycles passed since the process creation until its end (Process must be finished).

```java
public void addTotalTime(int cycles)
```
Add elapsed `cycles` to the process' turnaround. (Added only when time passed while the process was waiting for I/O).

```java
public double getAvgResponseTime()
```
Returns the average Response Time across all CPU-I/O Bursts of the process (Process must be finished)

```java
public void addWaitingTime(int cycles)
```
Add elapsed `cycles` to the process' time count, only when the process is ready (on the Ready Queue). Adds to response time and waiting time.

```java
public int getWaitingTime()
```
Returns the Waiting Time of the process (Process must be finished).