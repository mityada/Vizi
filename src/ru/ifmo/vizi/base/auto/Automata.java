package ru.ifmo.vizi.base.auto;

/**
 * Конечный автомат.
 *
 * @author  Georgiy Korneev
 * @version $Id: Automata.java,v 1.2 2006/05/16 10:15:47 geo Exp $
 */
public interface Automata {
    /**
     * Переходит в начальное состояние.
     */
    public void toStart();

    /**
     * Переходит в конечное состояние.
     */
    public void toEnd();

    /**
     * Проверяет, находится ли автомат в начальном состоянии.
     *
     * @return находится ли автомат в начальном состоянии.
     */
    public boolean isAtStart();

    /**
     * Проверяет находится ли автомат в конечном состоянии.
     *
     * @return находится ли автомат в конечном состоянии.
     */
    public boolean isAtEnd();

    /**
     * Возвращает номер текущего шага.
     *
     * @return номер текущего шага.
     */
    public int getStep();

    /**
     * Делает шаг вперед.
     *
     * @param level уровень состояния в котором остановится.
     */
    public void stepForward(int level);

    /**
     * Делает шаг назад.
     *
     * @param level уровень состояния в котором остановится.
     */
    public void stepBackward(int level);

    /**
     * Возвращает комментарий к текущему состоянию.
     *
     * @return комментарий к текущему состоянию.
     */
    public String getComment();

    /**
     * Отображает текущее состояние.
     */
    public void drawState();

    /**
     * Строковое представление состояния автомата.
     *
     * @param s буфер для состояния.
     * @return буфер <code>s</code>.
     */
    public StringBuffer toString(StringBuffer s);
}