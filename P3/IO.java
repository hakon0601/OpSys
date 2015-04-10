import java.util.Random;

/**
 * This class implements functionality associated with
 * the memory device of the simulated system.
 */
public class IO {
    /** The queue of processes waiting for free memory */
    private Queue IOQueue;
    /** A reference to the statistics collector */
    private Statistics statistics;
    /** The amount of memory in the memory device */
    private long avgIoTime;

    /**
     * Creates a new memory device with the given parameters.
     * @param IOQueue	The cpu queue to be used.
     * @param avgIoTime	The maximum processing time for the CPu device.
     * @param statistics	A reference to the statistics collector.
     */
    public IO(Queue IOQueue, long avgIoTime, Statistics statistics) {
        this.IOQueue = IOQueue;
        this.avgIoTime = avgIoTime;
        this.statistics = statistics;
    }
    /*
        /**
         * Returns the amount of memory in the memory device.
         * @return	The size of the memory device.
         */
    public long getavgIoTime() {
        return avgIoTime;
    }

    /**
     * Adds a process to the memory queue.
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


    public void moveProcessToCpu(CPU cpu) {
        if (!IOQueue.isEmpty()) {
            Process nextProcess = (Process) IOQueue.getNext();
            cpu.insertProcess(nextProcess);
            IOQueue.removeNext();
            nextProcess.setTimeToNextIoOperation(nextProcess.generateTimeToNextIoOperation());
            nextProcess.isAssignedEvent = false;
        }
    }
}

