/**
 * This class implements the doorman's part of the
 * Barbershop thread synchronization example.
 */
public class Doorman extends Thread {

    /**
	 * Creates a new doorman.
	 * @param queue		The customer queue.
	 * @param gui		A reference to the GUI interface.
	 */

    CustomerQueue queue;
    Gui gui;

	public Doorman(CustomerQueue queue, Gui gui) {
		this.queue = queue;
        this.gui = gui;
	}

	/**
	 * Starts the doorman running as a separate thread.
	 */
	public void startThread() {
        run();
	}

	/**
	 * Stops the doorman thread.
	 */
	public void stopThread() {
        try {
            sleep(Globals.doormanSleep);
            gui.println("sleeping for: " + Globals.doormanSleep + " millis");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startThread();
    }

    public CustomerQueue getQueue() {
        return queue;
    }

    public void run(){
        if (queue.getQueue().size() < queue.getQueueLength()) {
            Customer customer = new Customer();
            queue.addToQueue(customer, gui);
        }
        this.stopThread();
    }


}
