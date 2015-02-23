/**
 * This class implements the barber's part of the
 * Barbershop thread synchronization example.
 */
public class Barber extends Thread {

    private final CustomerQueue queue;
    private final Gui gui;
    private final int pos;
    private boolean working;

    /**
	 * Creates a new barber.
	 * @param queue		The customer queue.
	 * @param gui		The GUI.
	 * @param pos		The position of this barber's chair
	 */
	public Barber(CustomerQueue queue, Gui gui, int pos) {
        this.queue = queue;
        this.gui = gui;
        this.pos = pos;
        working = false;
    }

	/**
	 * Starts the barber running as a separate thread.
	 */
	public void startThread() {
		run();
	}

	/**
	 * Stops the barber thread.
	 */
	public void stopThread(int sleepTime) {
        try {
            sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        Customer customer = queue.getQueue().poll();
        if (customer != null) {
            queue.removeFromSeating(customer);
            gui.fillBarberChair(pos, customer);
            stopThread(Globals.barberWork);
            gui.emptyBarberChair(pos);
        }
        stopThread(Globals.barberSleep);
        startThread();
    }

	// Add more methods as needed
}

