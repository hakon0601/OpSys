/**
 * This class implements functionality associated with
 * the memory device of the simulated system.
 */
public class CPU {
    /** The queue of processes waiting for free memory */
    private Queue cpuQueue;
    /** A reference to the statistics collector */
    private Statistics statistics;
    /** The amount of memory in the memory device */
    private long maxCpuTime;
    private boolean cpuOccupied;

    /**
     * Creates a new memory device with the given parameters.
     * @param cpuQueue	The cpu queue to be used.
     * @param maxCpuTime	The maximum processing time for the CPu device.
     * @param statistics	A reference to the statistics collector.
     */
    public CPU(Queue cpuQueue, long maxCpuTime, Statistics statistics) {
        this.cpuQueue = cpuQueue;
        this.maxCpuTime = maxCpuTime;
        this.statistics = statistics;
        cpuOccupied = false;
    }
/*
    /**
     * Returns the amount of memory in the memory device.
     * @return	The size of the memory device.
     */
    public long getMemorySize() {
        return memorySize;
    }

    /**
     * Adds a process to the memory queue.
     * @param p	The process to be added.
     */
    public void insertProcess(Process p) {
        cpuQueue.insert(p);
    }

    /**
     * Checks whether or not there is enough free memory to let
     * the first process in the memory queue proceed to the cpu queue.
     * If there is, the process that was granted memory is returned,
     * otherwise null is returned.
     */
    public Process checkCpu() {
        if(!cpuQueue.isEmpty()) {
            Process nextProcess = (Process)cpuQueue.getNext();
            if(nextProcess.getTimeSpentInCpu() + maxCpuTime < nextProcess.getCpuTimeNeeded()) { // hvis denne prosessen skal gjøres noe med
                //prosessen skal flyttes tilbake fordi den ikke blir ferdig i prosessoren
                start ny event for switch process
                //nextProcess.leftMemoryQueue(clock);
                cpuQueue.removeNext();
                return nextProcess;
            }
        }
        return null;
    }

    /**
     * This method is called when a discrete amount of time has passed.
     * @param timePassed	The amount of time that has passed since the last call to this method.
     */
    public void timePassed(long timePassed) {
        statistics.memoryQueueLengthTime += memoryQueue.getQueueLength()*timePassed;
        if (memoryQueue.getQueueLength() > statistics.memoryQueueLargestLength) {
            statistics.memoryQueueLargestLength = memoryQueue.getQueueLength();
        }
    }

    /**
     * This method is called when a process is exiting the system.
     * The memory allocated to this process is freed.
     * @param p	The process that is leaving the system.
     */
    public void processCompleted(Process p) {
        freeMemory += p.getMemoryNeeded();
    }
}

