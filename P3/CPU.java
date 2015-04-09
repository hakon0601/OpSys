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
    public long getMaxCpuTime() {
        return maxCpuTime;
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
    public Process checkCpuSwitch() {
        if(!cpuQueue.isEmpty()) {
            Process nextProcess = (Process)cpuQueue.getNext();
            if(nextProcess.getTimeSpentInCpu() + maxCpuTime < nextProcess.getCpuTimeNeeded()) {
                //prosessen skal flyttes tilbake fordi den ikke blir ferdig i CPUen før max-tid har gått
                //start ny event for switch process
                //nextProcess.leftMemoryQueue(clock);
                return nextProcess;
            }
        }
        return null;
    }

    public Process checkCpuEnd() {
        if(!cpuQueue.isEmpty()) {
            Process nextProcess = (Process)cpuQueue.getNext();
            if(nextProcess.getTimeSpentInCpu() + maxCpuTime >= nextProcess.getCpuTimeNeeded()) { // hvis denne prosessen skal gjøres noe med
                //prosessen blir ferdig i max-tidsrommet
                //start ny event for end process
                //nextProcess.leftMemoryQueue(clock);
                return nextProcess;
            }
        }
        return null;
    }

    public Process checkIONeeds() {
        return null;
    }

    public void switchFront() {
        if(!cpuQueue.isEmpty()) {
            Process nextProcess = (Process) cpuQueue.getNext();
            nextProcess.setTimeSpentInCpu(nextProcess.getTimeSpentInCpu() + maxCpuTime);
            cpuQueue.removeNext();
            insertProcess(nextProcess);
        }
    }

    public void endProcess() {
        if(!cpuQueue.isEmpty()) {
            Process nextProcess = (Process) cpuQueue.getNext();
            nextProcess.setTimeSpentInCpu(nextProcess.getCpuTimeNeeded());
            cpuQueue.removeNext();
        }
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
    /*
    public void timePassed(long timePassed) {
        statistics.memoryQueueLengthTime += memoryQueue.getQueueLength()*timePassed;
        if (memoryQueue.getQueueLength() > statistics.memoryQueueLargestLength) {
            statistics.memoryQueueLargestLength = memoryQueue.getQueueLength();
        }
    }
    */
}

