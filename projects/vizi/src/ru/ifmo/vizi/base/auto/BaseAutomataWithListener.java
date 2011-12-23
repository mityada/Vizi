package ru.ifmo.vizi.base.auto;

import java.util.*;

/**
 * Базовый класс для автомата с обработчиками событий.
 *
 * @author Georgiy Korneev
 * @version $Id: BaseAutomataWithListener.java,v 1.7 2006/05/16 10:15:47 geo Exp $
 */
public abstract class BaseAutomataWithListener implements AutomataWithListener {
    /** 
     * Уровень шага call-auto.
     */
    public final static int CALL_AUTO_LEVEL = -10000;

    /**
     * Автомат.
     */
    private Automata automata;

    /**
     * Модель данных.
     */
    private Object data;

    /**
     * Обаботчики событий.
     */
    private final Vector listeners;

    /**
     * Стек состояний.
     */
    protected final AutoStack stack;

    /**
     * Номер текущего шага.
     */
    protected int step;

    /**
     * Комментарии.
     */
    private ResourceBundle bundle;

    /**
     * Контроллер автомата.
     */
    private final AutomataController controller;

    /**
     * Создает новый автомат с обработчиками событий.
     * Перед использованием автомат должен быть проинициализирован вызовом
     * {@link #init}.
     *
     * @param commentFile имя файла с комментариями.
     * @param locale локаль для комментариев.
     */
    public BaseAutomataWithListener(String commentFile, Locale locale) {
        try {
            bundle = ResourceBundle.getBundle(commentFile, locale);
        } catch (MissingResourceException e) {
            try {
                bundle = ResourceBundle.getBundle(commentFile, Locale.US);
            } catch (MissingResourceException ex) {
                System.err.println("Cannot find bundle " + commentFile);
                bundle = null;
            }
        }

        listeners = new Vector();
        stack = new AutoStack();
        controller = new AutomataController(this);
    }

    /**
     * Инициализирует автомат.
     *
     * @param automata автомат.
     * @param data данные для автомама.
     */
    protected void init(Automata automata, Object data) {
        this.automata = automata;
        this.data = data;
    }

    /**
     * Переходит в начальное состояние.
     */
    public void toStart() {
        step = 0;
        automata.toStart();
        fireStateChanged();
    }

    /**
     * Переходит в конечное состояние.
     */
    public void toEnd() {
        automata.toEnd();
        fireStateChanged();
    }

    /**
     * Проверяет, находится ли автомат в начальном состоянии.
     *
     * @return находится ли автомат в начальном состоянии.
     */
    public boolean isAtStart() {
        return automata.isAtStart();
    }

    /**
     * Проверяет находится ли автомат в конечном состоянии.
     *
     * @return находится ли автомат в конечном состоянии.
     */
    public boolean isAtEnd() {
        return automata.isAtEnd();
    }

    /**
     * Делает шаг вперед.
     *
     * @param level уровень состояния в котором остановится.
     */
    public void stepForward(int level) {
        automata.stepForward(level);
        fireStateChanged();
    }

    /**
     * Делает шаг назад.
     *
     * @param level уровень состояния в котором остановится.
     */
    public void stepBackward(int level) {
        automata.stepBackward(level);
        fireStateChanged();
    }

    /**
     * Возвращает номер текущего шага.
     *
     * @return номер текущего шага.
     */
    public int getStep() {
        return automata.getStep();
    }

    /**
     * Возвращает комментарий к текущему состоянию.
     *
     * @return комментарий к текущему состоянию.
     */
    public String getComment() {
        return automata.getComment();
    }

    /**
     * Возвращает комментарий по идентификатору.
     *
     * @param id идентификатор комментария.
     * @return комментарий.
     */
    protected String getComment(String id) {
        if (bundle == null) {
            return "";
        }
        try {
            return bundle.getString(id);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Отображает текущее состояние.
     */
    public void drawState() {
        automata.drawState();
    }

    /**
     * Добавляет обработчик событий.
     *
     * @param listener обработчик событий.
     */
    public void addListener(AutomataListener listener) {
        if (listener != null) {
            synchronized (listeners) {
                if (!listeners.contains(listener)) {
                    listeners.addElement(listener);
                }
            }
        }
    }

    /**
     * Убирает обработчик событий.
     *
     * @param listener обработчик событий.
     */
    public void removeListener(AutomataListener listener) {
        if (listener != null) {
            synchronized (listeners) {
                if (listeners.contains(listener)) {
                    listeners.removeElement(listener);
                }
            }
        }
    }

    /**
     * Вызывает обработчики событий.
     */
    public void fireStateChanged() {
        synchronized (listeners) {
            for (Enumeration e = listeners.elements(); e.hasMoreElements();) {
                AutomataListener listener = (AutomataListener) e.nextElement();
                listener.stateChanged();
            }
        }
    }

    /**
     * Получает контроллер автомата.
     *
     * @return контроллер автомата.
     */
    public AutomataController getController() {
        return controller;
    }

    /**
     * Строковое представление состояния автомата.
     *
     * @return строковое представление состояния автомата.
     */
    public String toString() {
        return toString(new StringBuffer()).toString();
    }

    /**
     * Строковое представление состояния автомата.
     *
     * @param s буфер для состояния.
     *
     * @return буфер <code>s</code>.
     */
    public StringBuffer toString(StringBuffer s) {
        s.append("Step: ").append(getStep()).append("\n");
        automata.toString(s);
        s.append("\n");
        s.append("Stack (").append(stack.size()).append(") ").append(stack).append("\n");
        s.append("--------------------\n");
        s.append(automata.getComment()).append("\n\n");
        s.append(data);
        return s;
    }

    /**
     * Базовый класс для автомата.
     *
     * @author Georgiy Korneev
     * @version $Id: BaseAutomataWithListener.java,v 1.7 2006/05/16 10:15:47 geo Exp $
     */
    public abstract class BaseAutomata implements Automata {
        /**
         * Название автомата.
         */
        private final String name;

        /**
         * Начальное состояние автомата.
         */
        private final int startState;

        /**
         * Конечное состояние автомата.
         */
        private final int endState;

        /**
         * Описания состояний.
         */
        private final String[] descriptions;

        /**
         * Уровни состояний.
         */
        private final int[] levels;

        /**
          * Текущее состояние автомата.
          */
        protected int state;

        /**
          * Текущий вложенный автомат.
          */
        protected Automata child;

        /**
         * Создает новый автомат.
         * 
         * @param name название автомата.
         * @param startState начальное состояние автомата.
         * @param endState конечное состояние автомата.
         * @param descriptions оисание состояний автомата.
         * @param levels уровни состояний автомата.
         */
        protected BaseAutomata(String name, int startState, int endState, 
                String[] descriptions, int[] levels) 
        {
            this.name = name;
            this.startState = startState;
            this.endState = endState;
            this.descriptions = descriptions;
            this.levels = levels;
        }

        /**
         * Переходит в начальное состояние.
         */
        public void toStart() {
            state = startState; 
            child = null; 
        }

        /**
         * Переходит в конечное состояние.
         */
        public void toEnd() {
            state = endState; 
            child = null; 
        }

        /**
         * Проверяет, находится ли автомат в начальном состоянии.
         *
         * @return находится ли автомат в начальном состоянии.
         */
        public boolean isAtStart() {
            return state == startState; 
        }

        /**
         * Проверяет находится ли автомат в конечном состоянии.
         *
         * @return находится ли автомат в конечном состоянии.
         */
        public boolean isAtEnd() {
            return state == endState; 
        }

        /**
         * Возвращает номер текущего шага.
         *
         * @return номер текущего шага.
         */
        public int getStep() {
            return step;
        }

        /**
         * Делает шаг вперед.
         *
         * @param level уровень состояния в котором остановится.
         */
        public void stepForward(int level) {
            do {
                step++;
                doStepForward(level);
            } while (!isInteresting(level));
        }

        /**
         * Делает шаг назад.
         *
         * @param level уровень состояния в котором остановится.
         */
        public void stepBackward(int level) {
            do {
                doStepBackward(level);
                step--; 
            } while (!isInteresting(level));
        }

        /**
          * Интересно ли текущее состояние.
          */
        private boolean isInteresting(int level) {
            if (levels[state] != CALL_AUTO_LEVEL) {
                return (levels[state] >= level);
            } else {
                return child != null && !child.isAtEnd();
            }
        }

        /**
         * Возвращает комментарий к текущему состоянию.
         *
         * @return комментарий к текущему состоянию.
         */
        public abstract String getComment();

        /**
         * Отображает текущее состояние.
         */
        public abstract void drawState();

        /**
         * Строковое представление состояния автомата.
         *
         * @param s буфер для состояния.
         * @return буфер <code>s</code>.
         */
        public StringBuffer toString(StringBuffer s) {
            s.append(name).append(' ').append(state).append(' '); 
            s.append('('); 
            s.append(descriptions[state]); 
            s.append(")\n"); 
            if (child != null && !child.isAtStart() && !child.isAtEnd()) {
                child.toString(s); 
            }
            return s; 
        }

        /**
         * Делает один шаг автомата вперед.
         *
         * @param level уровень состояния в котором остановится.
         */
        protected abstract void doStepForward(int level);

        /**
         * Делает один шаг автомата назад.
         *
         * @param level уровень состояния в котором остановится.
         */
        protected abstract void doStepBackward(int level);

    }
}
