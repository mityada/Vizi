package ru.ifmo.vizi.base.timer;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Simple timer task.
 * Ticks are performed in AWT event thread.
 *
 * @author  Georgiy Korneev
 * @version $Id: TimerTask.java,v 1.2 2005/02/28 11:44:06 geo Exp $
 */
public class TimerTask extends ActionEventCaster {
    /**
     * Tick time (in milliseconds).
     */
    private long time;

    /**
     * Scheduled tasks.
     */
    private final static Vector TASKS = new Vector();

    /**
     * Worker thread.
     * Synchornized by {@link #tasks}.
     */
    private static Thread WORKER;

    /**
     * Creates a new timer task.
     */
    public TimerTask() {
    }


    /**
     * Creates a new timer task and intialize it.
     *
     * @param time tick time.
     */
    public TimerTask(long time) {
        setTime(time);
    }

    /**
     * Sets timer tick time.
     *
     * @param time tick time.
     */
    public void setTime(long time) {
        synchronized (TASKS) {
            this.time = time;
            if (!TASKS.contains(this)) {
                TASKS.addElement(this);
                revalidate();
            }
            TASKS.notify();
        }
    }

    /**
     * Sets tick time relative to current.
     *
     * @param time tick time relative to current.
     */
    public void setRelativeTime(long time) {
        setTime(System.currentTimeMillis() + time);
    }

    /**
     * Gets timer tick time.
     *
     * @return timer tick time.
     */
    public long getTime() {
        return time;
    }

    /**
     * Cancels this task.
     */
    public void cancel() {
        synchronized (TASKS) {
            if (TASKS.removeElement(this)) {
                revalidate();
            }
        }
    }

    /**
     * Invoked on tick.
     */
    void tick() {
        dispatchEvent();
    }

    /**
     * Stops/starts worker thread when needed.
     */
    private static void revalidate() {
        synchronized (TASKS) {
            if (TASKS.isEmpty()) {
                if (WORKER != null) {
                    WORKER.interrupt();
                    WORKER = null;
                }
            } else {
                if (WORKER == null) {
                    WORKER = new Thread(new Worker());
                    WORKER.start();
                }
            }
        }
    }

    /**
     * Thread worker.
     */
    private final static class Worker implements Runnable {
        /**
         * Thread main.
         */
        public void run() {
            try {
                synchronized (TASKS) {
                    while (true) {
                        TimerTask minTask = null;
                        for (Enumeration e = TASKS.elements(); e.hasMoreElements();) {
                            TimerTask task = (TimerTask) e.nextElement();
                            if (minTask == null || minTask.time > task.time) {
                                minTask = task;
                            }
                        }

                        if (minTask != null) {
                            long dt = minTask.time  - System.currentTimeMillis();
                            if (dt <= 0) {
                                TASKS.removeElement(minTask);
                                revalidate();
                                minTask.tick();
                            } else {
                                TASKS.wait(dt);
                            }
                        } else {
                            TASKS.wait();
                        }
                    }
                }
            } catch (InterruptedException e) {
            }
        }
    }
}
