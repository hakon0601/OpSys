
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * The main class of the P3 exercise. This class is only partially complete.
 */
public class Simulator implements Constants
{
	/** The queue of events to come */
    private EventQueue eventQueue;

    private Memory memory;
    private CPU cpu;
    private IO io;

	/** Reference to the GUI interface */
	private Gui gui;
	/** Reference to the statistics collector */
	private Statistics statistics;
	/** The global clock */
    private long clock;
	/** The length of the simulation */
	private long simulationLength;
	/** The average length between process arrivals */
	private long avgArrivalInterval;
	// Add member variables as needed
    private Random random;

	/**
	 * Constructs a scheduling simulator with the given parameters.
	 * @param memoryQueue			The memory queue to be used.
	 * @param cpuQueue				The CPU queue to be used.
	 * @param ioQueue				The I/O queue to be used.
	 * @param memorySize			The size of the memory.
	 * @param maxCpuTime			The maximum time quant used by the RR algorithm.
	 * @param avgIoTime				The average length of an I/O operation.
	 * @param simulationLength		The length of the simulation.
	 * @param avgArrivalInterval	The average time between process arrivals.
	 * @param gui					Reference to the GUI interface.
	 */
	public Simulator(Queue memoryQueue, Queue cpuQueue, Queue ioQueue, long memorySize,
			long maxCpuTime, long avgIoTime, long simulationLength, long avgArrivalInterval, Gui gui) {
		this.simulationLength = simulationLength;
		this.avgArrivalInterval = avgArrivalInterval;
		this.gui = gui;
		statistics = new Statistics();
        random = new Random();
		eventQueue = new EventQueue();

		memory = new Memory(memoryQueue, memorySize, statistics);
		cpu = new CPU(cpuQueue, maxCpuTime, statistics);
		io = new IO(ioQueue, avgIoTime, statistics);
		clock = 0;
    }

    /**
	 * Starts the simulation. Contains the main loop, processing events.
	 * This method is called when the "Start simulation" button in the
	 * GUI is clicked.
	 */
	public void simulate() {
		System.out.print("Simulating...");
		// Genererate the first process arrival event
		eventQueue.insertEvent(new Event(NEW_PROCESS, 0));
		// Process events until the simulation length is exceeded:
		while (clock < simulationLength && !eventQueue.isEmpty()) {
			// Find the next event
			Event event = eventQueue.getNextEvent();
			// Find out how much time that passed...
			long timeDifference = event.getTime()-clock;

			// ...and update the clock.
			clock = event.getTime();
			// Let the memory unit and the GUI know that time has passed
			memory.timePassed(timeDifference);
			cpu.timePassed(timeDifference);
			io.timePassed(timeDifference);
			gui.timePassed(timeDifference);
			// Deal with the event
			if (clock < simulationLength) {
				processEvent(event);
			}
		}
		System.out.println("..done.");
		// End the simulation by printing out the required statistics
		statistics.printReport(simulationLength);
	}

	/**
	 * Processes an event by inspecting its type and delegating
	 * the work to the appropriate method.
	 * @param event	The event to be processed.
	 */
	private void processEvent(Event event) {
		switch (event.getType()) {
			case NEW_PROCESS:
				createProcess();
				break;
			case SWITCH_PROCESS:
				switchProcess();
				break;
			case END_PROCESS:
				endProcess();
				break;
			case IO_REQUEST:
				processIoRequest();
				break;
			case END_IO:
				endIoOperation();
				break;
		}
	}

	/**
	 * Simulates a process arrival/creation.
	 */
	private void createProcess() {
		// Create a new process
		Process newProcess = new Process(random.nextInt((int)(memory.getMemorySize()*0.25) - 100) + 100 , clock);
		memory.insertProcess(newProcess);
		flushMemoryQueue();
        flushCpuQueue();
        flushIoQueue();
		// Add an event for the next process arrival
		long nextArrivalTime = clock + 1 + (long)(2*Math.random()*avgArrivalInterval);
		eventQueue.insertEvent(new Event(NEW_PROCESS, nextArrivalTime));
		// Update statistics
		statistics.nofCreatedProcesses++;
    }

    /**
	 * Transfers processes from the memory queue to the ready queue as long as there is enough
	 * memory for the processes.
	 */
	private void flushMemoryQueue() {
		Process p = memory.checkMemory(clock);
		// As long as there is enough memory, processes are moved from the memory queue to the cpu queue
		while(p != null) {
            cpu.insertProcess(p);
            p.timeEnteredReadyQueue = clock;
			// Try to use the freed memory:
			flushMemoryQueue();
			// Check for more free memory
			p = memory.checkMemory(clock);
		}
	}

    private void flushCpuQueue() {
        Process p = cpu.getFirstProcess();
        cpu.setActiveCpu(gui);
        if (p != null && !p.isAssignedEvent) {
            p.isAssignedEvent = true;
            // Finding the first next event for the process at the head of the queue
            // Dependant on what is less, max-cpu time, time needed to complete the process or time to next IO event
            long timeLeftToCompleteProcess = (p.getCpuTimeNeeded() - p.getTimeSpentInCpu());
            long maxCpuTime = cpu.getMaxCpuTime();
            long timeToNextIoOperation = p.getTimeToNextIoOperation();

            if ((timeLeftToCompleteProcess < maxCpuTime) && (timeLeftToCompleteProcess <= timeToNextIoOperation)) {
                // Plan End process
                eventQueue.insertEvent(new Event(END_PROCESS, clock + timeLeftToCompleteProcess));
            }
            else if (maxCpuTime <= timeToNextIoOperation) {
                // Plan for switching
                eventQueue.insertEvent(new Event(SWITCH_PROCESS, clock + maxCpuTime));
            }
            else {
                // Do IO Request
                eventQueue.insertEvent(new Event(IO_REQUEST, clock + timeToNextIoOperation));
            }
        }
    }

    public void flushIoQueue() {
        Process p = io.getFirstProcess();
        io.setActiveIO(gui);
        if (p != null && !p.isAssignedEvent) {
            p.isAssignedEvent = true;
            long timeNeededInIO = io.getRadomIoExecuteTime();
            p.timeEnteredIO = clock;
            eventQueue.insertEvent(new Event(END_IO, clock + timeNeededInIO));
        }
    }

	/**
	 * Simulates a process switch.
	 */
	private void switchProcess() {
        cpu.switchFront();
        statistics.nofProcessSwitches++;
        flushMemoryQueue();
        flushCpuQueue();
        flushIoQueue();
	}

	/**
	 * Ends the active process
	 */
	private void endProcess() {
        cpu.endProcess(memory, clock);
        statistics.nofCompletedProcesses++;
        flushMemoryQueue();
        flushCpuQueue();
        flushIoQueue();

    }

	/**
	 * Processes an event signifying that the active process needs to
	 * perform an I/O operation.
	 */
	private void processIoRequest() {
		cpu.moveProcessToIo(io, clock);
        flushMemoryQueue();
        flushCpuQueue();
        flushIoQueue();
	}

	/**
	 * Processes an event signifying that the process currently doing I/O
	 * is done with its I/O operation.
	 */
	private void endIoOperation() {
		io.moveProcessToCpu(cpu, clock);
        statistics.nofCompletedIOOperations++;
        flushMemoryQueue();
        flushCpuQueue();
        flushIoQueue();
	}

	/**
	 * Reads a number from the an input reader.
	 * @param reader	The input reader from which to read a number.
	 * @return			The number that was inputted.
	 */
	public static long readLong(BufferedReader reader) {
		try {
			return Long.parseLong(reader.readLine());
		} catch (IOException ioe) {
			return 100;
		} catch (NumberFormatException nfe) {
			return 0;
		}
	}

	/**
	 * The startup method. Reads relevant parameters from the standard input,
	 * and starts up the GUI. The GUI will then start the simulation when
	 * the user clicks the "Start simulation" button.
	 * @param args	Parameters from the command line, they are ignored.
	 */
	public static void main(String args[]) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please input system parameters: ");

		System.out.print("Memory size (KB): ");
		long memorySize = readLong(reader);
		while(memorySize < 400) {
			System.out.println("Memory size must be at least 400 KB. Specify memory size (KB): ");
			memorySize = readLong(reader);
		}

		System.out.print("Maximum uninterrupted cpu time for a process (ms): ");
		long maxCpuTime = readLong(reader);

		System.out.print("Average I/O operation time (ms): ");
		long avgIoTime = readLong(reader);

		System.out.print("Simulation length (ms): ");
		long simulationLength = readLong(reader);
		while(simulationLength < 1) {
			System.out.println("Simulation length must be at least 1 ms. Specify simulation length (ms): ");
			simulationLength = readLong(reader);
		}

		System.out.print("Average time between process arrivals (ms): ");
		long avgArrivalInterval = readLong(reader);

		SimulationGui gui = new SimulationGui(memorySize, maxCpuTime, avgIoTime, simulationLength, avgArrivalInterval);
	}


    /*

    Kommentar om round robin eksperimentering.
    Det man kan gjøre her er å øke eller minke tidskvantet som når det utløper gjør at den fremste prosessen blir flyttet tilbake i køen.
    Dersom man har et lavt tidskvant vil switchingen skje langt hyppigere, dette gjør at prosesser vil bli prosessert mer jevnt seg i mellom. Dersom man har et høyt tidskvant
    er fokus på at man skal få unnagjort den prosesseringen det fremste elementet trenger så fort som mulig.
    (Her tar jo ikke switchingen noe tid, så man taper ikke noe tid på det)

     */
}
