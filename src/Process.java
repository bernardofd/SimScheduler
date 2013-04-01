import java.util.Random;

public class Process {
	private int pc; // Program Counter
	private int burstPC; // Burst specific program counter
	private int numBursts;
	private int[] cpuBursts; // CPU Bursts of this process
	private int id; // PID
	private int status;
	/*	Status of the process:
	*	0 - Ready
	*	1 - Waiting
	*	2 -	Finished
	*/
	private int maxPC; // Maximum value of the Program Counter - if pc >= maxPC then process is finished
	private int currentBurst;

	// Statistics
	private int waitingTime;
	private int turnaround;
	private int responseTime;

	// Constructor
	public Process(int pid, Random gen) {
		this.pc = 0;
		this.maxPC = 0;
		this.burstPC = 0;
		this.numBursts = 2 + gen.nextInt(10);
		this.cpuBursts = new int[this.numBursts];
		boolean isCPUBound = false;
		if (gen.nextDouble() < 0.5) {
			//50% chance of being CPU-Bound
			isCPUBound = true;
		}
		for (int i=0; i<this.numBursts; i++) {
			if(isCPUBound) {
				cpuBursts[i] = 20 + gen.nextInt(100);
			} else {
				cpuBursts[i] = 2 + gen.nextInt(10);
			}
			this.maxPC += cpuBursts[i];
		}
		this.currentBurst = 0;
		this.id = pid;
		this.status = 0; //Ready
		System.out.printf("Process %d created and it is %s.\n", pid, isCPUBound?"CPU-Bound":"I/O-Bound");
		// Statistics
		this.waitingTime = 0;
		this.responseTime = 0;
		this.turnaround = 0;
	}

	// Get/Set
	public int getPID() {
		return this.id;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus (int s) {
		this.status = s;
	}

	public int getNextBurstDuration() {
		if (this.status == 0) {
			return this.cpuBursts[this.currentBurst];
		} else {
			return 0;
		}
	}

	public int getBurstRemainingTime() {
		return this.cpuBursts[this.currentBurst] - this.burstPC;
	}

	public void executeBurst() { //non-preemptive
		if (this.status == 0) {
			this.pc += this.cpuBursts[this.currentBurst];
			this.responseTime += this.cpuBursts[this.currentBurst];
			this.turnaround += this.cpuBursts[this.currentBurst++];
			if (this.currentBurst >= this.cpuBursts.length) {
				this.status = 2; //Finished
			} else {
				this.status = 1; //Waiting
			}
		}
	}

	/**
	*	Check if the cycles executed are enough to change the process' status
	*/
	public void executeBurst(int cycles) { //preemptive
		if (this.status == 0) {
			this.burstPC += cycles;
			if (this.burstPC >= this.cpuBursts[currentBurst]) { //Burst Ended
				int diff = this.cpuBursts[currentBurst] - (this.burstPC - cycles);
				this.currentBurst++;
				this.burstPC = 0;
				this.turnaround += diff;
				this.responseTime += diff;
				if (this.currentBurst >= this.cpuBursts.length) {
					this.status = 2; //Finished
				} else {
					this.status = 1; //Waiting
				}
			} else {
				this.pc += cycles;
				this.turnaround += cycles;
				this.responseTime += cycles;
			}
		}
	}

	// Statistics
	public int getTurnaround() {
		if (this.status == 2) {
			return this.turnaround;
		} else {
			return -1;
		}
	}

	public void addTotalTime(int cycles) {
		this.turnaround += cycles;
	}

	public double getAvgResponseTime() {
		if (this.status == 2) {
			return this.responseTime/this.numBursts;
		} else {
			return -1;
		}
	}

	public void addWaitingTime(int cycles) {
		if (this.status == 0) {
			this.waitingTime += cycles;
			this.turnaround += cycles;
			this.responseTime += cycles;
		}
	}

	public int getWaitingTime() {
		if (this.status == 2) {
			return this.waitingTime;
		} else {
			return -1;
		}
	}
}