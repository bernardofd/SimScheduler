public class CPU {
	private Process p; // Process currently executing
	private int cycles;
	// Statistics
	private int idleTime;
	// Constructor
	public CPU() {
		this.p = null;
		this.cycles = 0;
		this.idleTime = 0;
	}

	// Load Process in CPU
	public void load(Process p) {
		this.p = p;
	}

	// Unload Process
	public Process unload() {
		Process proc = this.p;
		this.p = null;
		return proc;
	}

	// Execute a full CPU Burst of the resident process
	public int executeBurst() {
		if (p != null && p.getStatus() == 0) {
			int burst = p.getNextBurstDuration();
			this.cycles += burst;
			p.executeBurst();
			System.out.printf("CPU(Cycle %d): PID(%d) executed for %d cycles. New Status: %s\n", this.cycles, p.getPID(), burst, p.getStatus()==1?"Waiting":"Finished");
			return burst;
		} else {
			return 0;
		}
	}

	// Execute a process for a certain time
	public int execute(int quantum) {
		int neededCycles = p.getBurstRemainingTime();
		if (quantum >= neededCycles) { // It haves enough time for the process to end its burst
			p.executeBurst(neededCycles);
			this.cycles += neededCycles;
			System.out.printf("CPU(Cycle %d): PID(%d) executed for %d cycles. New Status: %s\n", this.cycles, p.getPID(), neededCycles, p.getStatus()==1?"Waiting":"Finished");
			return neededCycles;
		} else {
			// Will preempt the process that expired it's time share
			p.executeBurst(quantum);
			this.cycles += quantum;
			System.out.printf("CPU(Cycle %d): PID(%d) executed for %d cycles. New Status: %s\n", this.cycles, p.getPID(), quantum, p.getStatus()==1?"Waiting":"Finished");
			return quantum;
		}
	}

	public int getCycles() {
		return this.cycles;
	}

	// Statistics
	public void addIdleCycles(int cycles) {
		this.cycles += cycles;
		this.idleTime += cycles;
	}

	public int getIdleTime() {
		return this.idleTime;
	}

	public double getAvgUtilization() {
		return this.idleTime/this.cycles;
	}
}