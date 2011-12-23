package ru.ifmo.vizi.base.timer;

/**
 * Task with tick time relative to current.
 *
 * @author  Georgiy Korneev
 * @version $Id: TimerRTask.java,v 1.1 2003/12/24 10:50:42 geo Exp $
 */
public abstract class TimerRTask extends TimerTask {
    /**
     * Creates a new relative task.
     */
    public TimerRTask() {
    }

    /**
     * Creates a new relative task.
     *
     * @param time tick time (relative to curren).
     */
    public TimerRTask(long time) {
        // Normal flow of operation, because TimerTask call
        // out setTime method.
        super(time);
    }

    /**
     * Sets time when to tick.
     *
     * @param time time to tick (relative to current).
     */
    public void setTime(long time) {
        super.setTime(System.currentTimeMillis() + time);
    }
}
