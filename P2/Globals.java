/**
 * Class containing three globally available variables modified by the GUI
 * and used by the Barber and Doorman threads.
 */
public class Globals implements Constants
{
	/** The number of milliseconds a barber sleeps between each work period */
	public static int barberSleep = (MIN_BARBER_SLEEP + (int)(Math.random()*(MAX_BARBER_SLEEP-MIN_BARBER_SLEEP + 1)));
	/** The number of milliseconds it takes a barber to cut a customer's hair */
	public static int barberWork = (MIN_BARBER_WORK + (int)(Math.random()*(MAX_BARBER_WORK-MIN_BARBER_WORK + 1)));
	/** The number of milliseconds between each time a new customer arrives */
	public static int doormanSleep = (MIN_DOORMAN_SLEEP + (int)(Math.random()*(MAX_DOORMAN_SLEEP-MIN_DOORMAN_SLEEP + 1)));
}
