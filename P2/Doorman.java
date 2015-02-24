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
        this.start();
	}

	/**
	 * Stops the doorman thread.
	 */
	public void stopThread() {
        this.stop();
    }

    public CustomerQueue getQueue() {
        return queue;
    }

    public void run(){
        while (true) {
            if (queue.getQueue().size() < queue.getQueueLength()) {
                Customer customer = new Customer();
                queue.addToQueue(customer, gui);
                doormanSleep();
            }
            else {
                waitForBarber();
            }
        }
    }

    private void doormanSleep() {
        try {
            this.sleep(Globals.doormanSleep - 2000); // TODO FIX
            gui.println("Doorman sleeping for: " + Globals.doormanSleep + " millis");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void waitForBarber() {
        gui.println("Doorman is now waiting for notification");
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
