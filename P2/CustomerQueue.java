import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

/**
 * This class implements a queue of customers as a circular buffer.
 */
public class CustomerQueue {
    /**
	 * Creates a new customer queue.
	 * @param queueLength	The maximum length of the queue.
	 * @param gui			A reference to the GUI interface.
	 */

    private Queue<Customer> queue;
    private ArrayList<Customer> seating;
    private Gui gui;

    private int queueLength;
    public CustomerQueue(int queueLength, Gui gui) {
        this.queueLength = queueLength;
        this.gui = gui;
        queue = new ArrayDeque<Customer>();
        seating = new ArrayList<Customer>();
        for (int i = 0; i < queueLength ; i++) {
            seating.add(null);
        }
	}

	// Add more methods as needed

    public int getQueueLength() {
        return queueLength;
    }

    public Queue<Customer> getQueue() {
        return queue;
    }

    public void setQueue(Queue<Customer> queue) {
        this.queue = queue;
    }

    public void addToQueue(Customer customer, Gui gui) {
        queue.add(customer);
        addToSeating(customer);
    }

    private void addToSeating(Customer customer) {
        for (int i = 0 ; i < seating.size() ; i++) {
            if (seating.get(i) == null) {
                seating.set(i, customer);
                gui.fillLoungeChair(i, customer);
                gui.println("customer #" + customer.getCustomerID() + " was seated in pos: " + (i + 1));
                return;
            }
        }
    }
}
