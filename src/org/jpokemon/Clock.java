package org.jpokemon;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class Clock {
	/** Indicates the queue of events to be triggered. Lock on this object */
	private static final Queue<Calendar> events = new PriorityQueue<Calendar>();

	/** Indicates which calendar events map to which callbacks */
	private static final Map<Calendar, Runnable> callbacks = new HashMap<Calendar, Runnable>();

	/** Provides a private constructor */
	private Clock() {
	}

	public static void main(String[] args) {
		Clock.timeout(40, new Runnable() {
			public void run() {
				System.out.println("40 seconds: " + Calendar.getInstance().getTimeInMillis() / 1000);
			}
		});
		Clock.timeout(10, new Runnable() {
			public void run() {
				System.out.println("10 seconds: " + Calendar.getInstance().getTimeInMillis() / 1000);
			}
		});
		Clock.timeout(20, new Runnable() {
			public void run() {
				System.out.println("20 seconds: " + Calendar.getInstance().getTimeInMillis() / 1000);
			}
		});
	}

	public static void schedule(int hours, int minutes, int seconds, Runnable callback) {
		Calendar time = Calendar.getInstance();
		time.set(Calendar.HOUR_OF_DAY, hours);
		time.set(Calendar.MINUTE, minutes);
		time.set(Calendar.SECOND, seconds);
		schedule(time, callback);
	}

	public static void schedule(int month, int days, int hours, int minutes, int seconds, Runnable callback) {
		Calendar time = Calendar.getInstance();
		time.set(Calendar.MONTH, month);
		time.set(Calendar.DAY_OF_MONTH, days);
		time.set(Calendar.HOUR_OF_DAY, hours);
		time.set(Calendar.MINUTE, minutes);
		time.set(Calendar.SECOND, seconds);
		schedule(time, callback);
	}

	public static void timeout(int seconds, Runnable callback) {
		Calendar time = Calendar.getInstance();
		time.set(Calendar.SECOND, seconds);
		schedule(time, callback);
	}

	private static void schedule(Calendar time, Runnable callback) {
		synchronized (events) {
			events.add(time);
			System.out.println("event scheduled: " + time.getTimeInMillis() / 1000 + " ("
					+ (time.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / 1000 + ")");
			callbacks.put(time, callback);
		}
	}

	static {
		new Thread(new Runnable() {
			public void run() {
				System.out.println("Clock started at " + Calendar.getInstance().getTimeInMillis() / 1000);

				for (;;) {
					synchronized (events) {
						if (!events.isEmpty() && events.peek().getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
							System.out.println(events.peek().getTimeInMillis() / 1000 + " is after "
									+ Calendar.getInstance().getTimeInMillis() / 1000);
							Calendar time = events.remove();
							Runnable callback = callbacks.remove(time);
							new Thread(callback).start();
						}
						else if (!events.isEmpty()) {
							System.out.println(events.peek().getTimeInMillis() / 1000 + " is not after "
									+ Calendar.getInstance().getTimeInMillis() / 1000);
						}
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}
		}).start();
	}
}