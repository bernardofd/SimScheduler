import java.util.*;

public class OS {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		Random gen = new Random(); // Random Number Generator
		
		System.out.println("How many processes will be created?");
		int numProcesses = input.nextInt();
		if (numProcesses <= 0) {
			System.out.println("Please insert a number greater than 0. Exiting...");
			return;
		}
		Process[] processPool = new Process [numProcesses];

		// System Queues
		LinkedList<Process> readyQueue = new LinkedList<Process>();
		LinkedList<Process> waitingQueue = new LinkedList<Process>();
		LinkedList<Process> finishedProcesses = new LinkedList<Process>();

		System.out.println("Creating Processes...");
		for (int i=0; i<numProcesses; i++) {
			processPool[i] = new Process(i+1, gen);
			// As new processes are born ready, we add them to the Ready Queue
			readyQueue.add(processPool[i]);
		}
		
		System.out.println("Creating CPU");
		CPU cpu = new CPU(); // The System's CPU
		Scheduler scheduler = new Scheduler();
		Process p,q;
		Iterator<Process> it;
		// Main loop - exits when all processes are done
		while (readyQueue.size() > 0 || waitingQueue.size() > 0) {
			p = scheduler.nextProcess(readyQueue);
			if (p == null) {
				System.out.println("KERNEL PANIC!");
				return;
			}
			System.out.printf("Loading process %d in the CPU\n", p.getPID());
			cpu.load(p);
			int burst = cpu.executeBurst();
			p = cpu.unload();
			
			//Add waiting time for all ready processes
			it = readyQueue.listIterator();
			while (it.hasNext()) {
				q = it.next();
				q.addWaitingTime(burst);
			}

			//Add turnaround time for the remaining active processes
			it = waitingQueue.listIterator();
			while (it.hasNext()) {
				q = it.next();
				q.addTotalTime(burst);
			}

			// Process screening
			if (p.getStatus() == 1) { // Process is waiting for I/O
				waitingQueue.add(p);
			} else if (p.getStatus() == 2) { //Process finished!
				finishedProcesses.add(p);
			} else { // Process Preempted, back to the readyQueue
				System.out.printf("Process PID<%d> preempted.\n", p.getPID());
				readyQueue.add(p);
			}
			
			// If the readyQueue is empty and there's any processes waiting for I/O, the CPU will wait for the next one
			if (readyQueue.size() == 0 && waitingQueue.size() > 0) {
				// Add a random idle time (between 50 and 100 cycles) in the CPU
				int idleTime = 50 + gen.nextInt(50);
				cpu.addIdleCycles(idleTime);
				System.out.printf("Ready Queue is empty, waiting %d cycles for next process\n", idleTime);
				//Add total time for all active processes
				it = waitingQueue.listIterator();
				while (it.hasNext()) {
					q = it.next();
					q.addTotalTime(idleTime);
				}
				// Ready the process
				q = waitingQueue.removeFirst();
				q.setStatus(0);
				readyQueue.add(q);
				System.out.printf("PID(%d) is ready.\n", q.getPID());
			} else if (waitingQueue.size() > 0 && gen.nextDouble() < 0.75) {
				// There's a 75% chance of a process waiting for I/O to be serviced
				q = waitingQueue.removeFirst();
				q.setStatus(0);
				readyQueue.add(q);
				System.out.printf("PID(%d) is ready.\n", q.getPID());
			}
		}
		System.out.println("Execution Ended!");

		// Statistics
		System.out.printf("Average CPU Utilization: %.2f%%\n", cpu.getAvgUtilization()*100);
		// Compute average Waiting time, Response time and Turnaround
		double avgWaitingTime = 0.0, avgResponseTime = 0.0, avgTurnaround = 0.0;
		it = finishedProcesses.listIterator();
		while (it.hasNext()) {
			q = it.next();
			avgWaitingTime += q.getWaitingTime();
			avgResponseTime += q.getAvgResponseTime();
			avgTurnaround += q.getTurnaround();
		}
		avgWaitingTime /= numProcesses;
		avgResponseTime /= numProcesses;
		avgTurnaround /= numProcesses;
		System.out.printf("Average Process Waiting Time: %.2f cycles\n", avgWaitingTime);
		System.out.printf("Average Process Response Time: %.2f cycles\n", avgResponseTime);
		System.out.printf("Average Process Turnaround: %.2f cycles\n", avgTurnaround);
	}
}