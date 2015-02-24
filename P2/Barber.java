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
        this.start();
	}

	/**
	 * Stops the barber thread.
	 */
	public void stopThread() {

    }

    public void run() {
        while (true) {
            Customer customer;
            synchronized (this) {
                customer = queue.getQueue().poll();
                if (customer != null) {
                    queue.removeFromSeating(customer);
                }
            }
            if (customer != null) {
                gui.fillBarberChair(pos, customer);
                gui.println("Barber #" + pos + " got customer #" + customer.getCustomerID());
                barberWork();
                gui.emptyBarberChair(pos);
                gui.println("Barber #" + pos + " finished and went to sleep");
            }
            barberSleep();
        }
    }

    private void barberSleep() {
        try {
            gui.barberIsSleeping(pos);
            this.sleep(Globals.barberSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void barberWork() {
        try {
            gui.barberIsAwake(pos);
            this.sleep(Globals.barberWork);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Add more methods as needed
}

