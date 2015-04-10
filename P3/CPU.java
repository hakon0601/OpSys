/**
 * This class implements functionality associated with
 * the cpu device of the simulated system.
 */
public class CPU {
    /** The queue of processes waiting for free cpu time */
    private Queue cpuQueue;
    /** A reference to the statistics collector */
    private Statistics statistics;
    /** The amount of maximal computation time in the cpu */
    private long maxCpuTime;

    /**
     * Creates a new cpu device with the given parameters.
     * @param cpuQueue	    The cpu queue to be used.
     * @param maxCpuTime	The maximum processing time for the CPU device.
     * @param statistics	A reference to the statistics collector.
     */
    public CPU(Queue cpuQueue, long maxCpuTime, Statistics statistics) {
        this.cpuQueue = cpuQueue;
        this.maxCpuTime = maxCpuTime;
        this.statistics = statistics;
    }

    public long getMaxCpuTime() {
        return maxCpuTime;
    }

    /**
     * Adds a process to the CPU queue.
     * @param p	The process to be added.
     */
    public void insertProcess(Process p) {
        cpuQueue.insert(p);
    }

    public void switchFront() {
        if(!cpuQueue.isEmpty()) {
            Process nextProcess = (Process) cpuQueue.getNext();
            statistics.totalCpuProcessingTime += maxCpuTime;
            nextProcess.setTimeSpentInCpu(nextProcess.getTimeSpentInCpu() + maxCpuTime);
            nextProcess.incrementnofTimesInReadyQueue();
            cpuQueue.removeNext();
            insertProcess(nextProcess);
            nextProcess.isAssignedEvent = false;
        }
    }

    public void endProcess(Memory memory, long clock) {
        if(!cpuQueue.isEmpty()) {
            Process nextProcess = (Process) cpuQueue.getNext();
            nextProcess.setTimeSpentInReadyQueue(nextProcess.getTimeSpentInReadyQueue() + (clock - nextProcess.timeEnteredReadyQueue));
            statistics.totalTimeSpentInCpuQueueByCompleted += nextProcess.getTimeSpentInReadyQueue();
            statistics.totalTimeSpentInIOQueueByCompleted += nextProcess.getTimeSpentWaitingForIo();
            statistics.totalTimeSpentInIOByCompleted += nextProcess.getTimeSpentInIo();
            statistics.totalCpuProcessingTime += nextProcess.getCpuTimeNeeded() - nextProcess.getTimeSpentInCpu();
            statistics.noofTimesCompletedProcessesInReadyQueue += nextProcess.getNofTimesInReadyQueue();
            statistics.noofTimesCompletedProcessesInIOQueue += nextProcess.getNofTimesInIoQueue();
            statistics.totalTimeSpentInSystemByCompleted += clock - nextProcess.getTimeOfLastEvent();
            statistics.totalTimeSpentInMemoryQueueByCompleted += nextProcess.getTimeSpentWaitingForMemory();
            statistics.totalTimeSpentCpuByCompleted += nextProcess.getCpuTimeNeeded();

            nextProcess.setTimeSpentInCpu(nextProcess.getCpuTimeNeeded());
            memory.processCompleted(nextProcess);
            cpuQueue.removeNext();
        }
    }

    public void moveProcessToIo(IO io, long clock) {
        if (!cpuQueue.isEmpty()) {
            Process nextProcess = (Process) cpuQueue.getNext();
            nextProcess.setTimeSpentInReadyQueue(nextProcess.getTimeSpentInReadyQueue() + (clock - nextProcess.timeEnteredReadyQueue));
            statistics.totalCpuProcessingTime += nextProcess.getTimeToNextIoOperation();
            nextProcess.timeEnteredIOQueue = clock;
            nextProcess.incrementenofTimesInIoQueue();
            nextProcess.setTimeSpentInCpu(nextProcess.getTimeSpentInCpu() + nextProcess.getTimeToNextIoOperation());
            nextProcess.setTimeToNextIoOperation(0);
            nextProcess.isAssignedEvent = false;
            io.insertProcess(nextProcess);
            cpuQueue.removeNext();
        }
    }


    public Process getFirstProcess() {
        if (!cpuQueue.isEmpty()) {
            return (Process) cpuQueue.getNext();
        }
        return null;
    }


    public void setActiveCpu(Gui gui) {
        if (!cpuQueue.isEmpty()) {
            gui.setCpuActive((Process)cpuQueue.getNext());
        }
        else {
            gui.setCpuActive(null);
        }
    }

    /**
     * This method is called when a discrete amount of time has passed.
     * @param timePassed	The amount of time that has passed since the last call to this method.
     */
    public void timePassed(long timePassed) {
        statistics.cpuQueueLengthTime += cpuQueue.getQueueLength()*timePassed;
        if (cpuQueue.getQueueLength() > statistics.cpuQueueLargestLength) {
            statistics.cpuQueueLargestLength = cpuQueue.getQueueLength();
        }
    }
}

