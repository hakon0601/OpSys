/**
 * This class contains a lot of public variables that can be updated
 * by other classes during a simulation, to collect information about
 * the run.
 */
public class Statistics
{
	/** The number of processes that have exited the system */
	public long nofCompletedProcesses = 0;
	/** The number of processes that have entered the system */
	public long nofCreatedProcesses = 0;

    public long nofProcessSwitches = 0;
    public long nofCompletedIOOperations = 0;

	/** The total time that all completed processes have spent waiting for memory */
	public long totalTimeSpentWaitingForMemory = 0;
	public long totalCpuProcessingTime = 0;
	/** The time-weighted length of the memory queue, divide this number by the total time to get average queue length */
	public long memoryQueueLengthTime = 0;
	public long cpuQueueLengthTime = 0;
	public long ioQueueLengthTime = 0;
	/** The largest memory queue length that has occured */
	public long memoryQueueLargestLength = 0;
    public long cpuQueueLargestLength = 0;
    public long IOQueueLargestLength = 0;

    public long noofTimesCompletedProcessesInReadyQueue = 0;
    public long noofTimesCompletedProcessesInIOQueue = 0;

    public long totalTimeSpentInSystemByCompleted = 0;
    public long totalTimeSpentInMemoryQueueByCompleted = 0;
    public long totalTimeSpentInCpuQueueByCompleted = 0;
    public long totalTimeSpentInIOQueueByCompleted = 0;

    public long totalTimeSpentCpuByCompleted = 0;
    public long totalTimeSpentInIOByCompleted = 0;



    /**
	 * Prints out a report summarizing all collected data about the simulation.
	 * @param simulationLength	The number of milliseconds that the simulation covered.
	 */
	public void printReport(long simulationLength) {
		System.out.println();
		System.out.println("Simulation statistics:");
		System.out.println();
		System.out.println("Number of completed processes:                                "+nofCompletedProcesses);
		System.out.println("Number of created processes:                                  "+nofCreatedProcesses);
		System.out.println("Number of process switches:                                   "+nofProcessSwitches);
		System.out.println("Number of completed I/O operations:                           "+nofCompletedIOOperations);
		System.out.println("Avg speed (Nr of processes completed/second):                 "+(nofCompletedProcesses/(simulationLength/1000.0)));
		System.out.println();
		System.out.println("Total processing time:                                        "+totalCpuProcessingTime + " ms");
		System.out.println("Total idle cpu time:                                          "+(simulationLength - totalCpuProcessingTime) + " ms");
		System.out.println("CPU utilization:                                              "+((double)totalCpuProcessingTime*100.0/simulationLength)  + " %");
		System.out.println("CPU not utilized:                                             "+(100.0 - (double)totalCpuProcessingTime*100.0/simulationLength) + " %");
        System.out.println();
        System.out.println("Largest occuring memory queue length:                         "+memoryQueueLargestLength);
		System.out.println("Average memory queue length:                                  "+(float)memoryQueueLengthTime/simulationLength);

        System.out.println("Largest occuring cpu queue length:                            "+cpuQueueLargestLength);
        System.out.println("Average cpu queue length:                                     "+(float)cpuQueueLengthTime/simulationLength);

        System.out.println("Largest occuring I/O queue length:                            "+IOQueueLargestLength);
        System.out.println("Average I/O queue length:                                     "+(float)ioQueueLengthTime/simulationLength);
        System.out.println();

        if(nofCompletedProcesses > 0) {
			System.out.println("Average # of times a completed process has been placed in memory queue:   " + 1);
			System.out.println("Average # of times a completed process has been placed in cpu queue:      " + (double)noofTimesCompletedProcessesInReadyQueue/nofCompletedProcesses);
			System.out.println("Average # of times a completed process has been placed in I/O queue:      " + (double)noofTimesCompletedProcessesInIOQueue/nofCompletedProcesses);

            System.out.println("Average time spent in system by completed processes:                      " + (double)totalTimeSpentInSystemByCompleted/nofCompletedProcesses + " ms");
            System.out.println("Average time spent waiting for memory by completed processes:             " + (double)totalTimeSpentInMemoryQueueByCompleted/nofCompletedProcesses + " ms");
            System.out.println("Average time spent waiting in ready queue by completed processes:         " + (double)totalTimeSpentInCpuQueueByCompleted/nofCompletedProcesses + " ms");
            System.out.println("Average time spent waiting in IO queue by completed processes:            " + (double)totalTimeSpentInIOQueueByCompleted/nofCompletedProcesses + " ms");

            System.out.println("Average time used in cpu by completed processes:                          " + (double)totalTimeSpentCpuByCompleted/nofCompletedProcesses + " ms");
            System.out.println("Average time used in I/O by completed processes:                          " + (double)totalTimeSpentInIOByCompleted/nofCompletedProcesses + " ms");


		}
	}
}
