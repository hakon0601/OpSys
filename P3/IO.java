import java.util.Random;

/**
 * This class implements functionality associated with
 * the I/O device of the simulated system.
 */
public class IO {
    /** The queue of processes waiting for a free IO device */
    private Queue IOQueue;
    /** A reference to the statistics collector */
    private Statistics statistics;
    /** The average IO time */
    private long avgIoTime;

    /**
     * Creates a new IO device with the given parameters.
     * @param IOQueue	The cpu queue to be used.
     * @param avgIoTime	The average computation time of the IO device.
     * @param statistics	A reference to the statistics collector.
     */
    public IO(Queue IOQueue, long avgIoTime, Statistics statistics) {
        this.IOQueue = IOQueue;
        this.avgIoTime = avgIoTime;
        this.statistics = statistics;
    }
    /*
        /**
         * Returns the average IO processing time.
         * @return	The average IO processing time.
         */
    public long getavgIoTime() {
        return avgIoTime;
    }

    /**
     * Adds a process to the IO queue.
     * @param p	The process to be added.
     */
    public void insertProcess(Process p) {
        IOQueue.insert(p);
    }

    public void setActiveIO(Gui gui) {
        if (!IOQueue.isEmpty()) {
            gui.setIoActive((Process)IOQueue.getNext());
        }
        else {
            gui.setIoActive(null);
        }
    }

    public Process getFirstProcess() {
        if (!IOQueue.isEmpty()) {
            return (Process) IOQueue.getNext();
        }
        return null;
    }

    public long getRadomIoExecuteTime() {
        int a = new Random().nextInt(120 - 80) + 80;
        double b = a/100.0;
        double c = b * avgIoTime;
        long d = Math.round(c);
        return d;
    }


    public void moveProcessToCpu(CPU cpu, long clock) {
        if (!IOQueue.isEmpty()) {
            Process nextProcess = (Process) IOQueue.getNext();
            nextProcess.setTimeSpentInIo(nextProcess.getTimeSpentInIo() + (clock - nextProcess.timeEnteredIO));
            nextProcess.setTimeSpentWaitingForIo(nextProcess.getTimeSpentWaitingForIo() + (clock - nextProcess.timeEnteredIOQueue));
            nextProcess.timeEnteredReadyQueue = clock;
            nextProcess.incrementnofTimesInReadyQueue();
            cpu.insertProcess(nextProcess);
            IOQueue.removeNext();
            nextProcess.setTimeToNextIoOperation(nextProcess.generateTimeToNextIoOperation());
            nextProcess.isAssignedEvent = false;
        }
    }

    /**
     * This method is called when a discrete amount of time has passed.
     * @param timePassed	The amount of time that has passed since the last call to this method.
     */
    public void timePassed(long timePassed) {
        statistics.ioQueueLengthTime += IOQueue.getQueueLength()*timePassed;
        if (IOQueue.getQueueLength() > statistics.IOQueueLargestLength) {
            statistics.IOQueueLargestLength = IOQueue.getQueueLength();
        }
    }
}

