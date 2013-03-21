import java.util.LinkedList;

public class Scheduler {

	// Constructor
	public Scheduler() {
	};

	public Process nextProcess(LinkedList<Process> readyQueue) {
		// Implementar aqui o algoritmo de escalonamento
		// FCFS
		return readyQueue.removeFirst();
	}
}