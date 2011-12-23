package ru.ifmo.vizi.base.auto;

/**
 * Обрабатывает события автомата.
 *
 * @author Georgiy Korneev
 * @version $Id: AutomataListener.java,v 1.1 2003/12/24 10:50:42 geo Exp $
 */
public interface AutomataListener {
    /**
     * Вызывается когда состояние автомата изменилось.
     */
    public void stateChanged();
}
