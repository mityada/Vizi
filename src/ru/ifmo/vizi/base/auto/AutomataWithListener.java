package ru.ifmo.vizi.base.auto;

/**
 * Автомат поддерживающий обработку событий.
 *
 * @author Georgiy Korneev
 * @version $Id: AutomataWithListener.java,v 1.2 2004/06/07 13:58:33 geo Exp $
 */
public interface AutomataWithListener extends Automata {
    /**
     * Добавляет обработчик событий.
     *
     * @param listener обработчик событий.
     */
    public void addListener(AutomataListener listener);

    /**
     * Убирает обработчик событий.
     *
     * @param listener обработчик событий.
     */
    public void removeListener(AutomataListener listener);

    /**
     * Получает контроллер автомата.
     *
     * @return контроллер автомата.
     */
    public AutomataController getController();
}
