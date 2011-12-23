package ru.ifmo.vizi.find_max;

import ru.ifmo.vizi.base.auto.*;
import java.util.Locale;

public final class FindMaximum extends BaseAutomataWithListener {
    /**
      * Модель данных.
      */
    public final Data d = new Data();

    /**
      * Конструктор для языка
      */
    public FindMaximum(Locale locale) {
        super("ru.ifmo.vizi.find_max.Comments", locale); 
        init(new Main(), d); 
    }

    /**
      * Данные.
      */
    public final class Data {
        /**
          * Массив для поиска.
          */
        public int[] a = new int[]{1, 2, 3, 1, 3, 5, 6};

        /**
          * Экземпляр апплета.
          */
        public FindMaximumVisualizer visualizer = null;

        /**
          * Текущий максимум.
          */
        public int max = 0;

        /**
          * Переменная цикла (Процедура Main).
          */
        public int Main_i;

        public String toString() {
            StringBuffer s = new StringBuffer();
            s.append("max = ").append(d.max).append("\n");
            s.append("i = ").append(d.Main_i).append("\n");
            return s.toString();
        }
    }

    /**
      * Ищет максимум в массиве.
      */
    private final class Main extends BaseAutomata implements Automata {
        /**
          * Начальное состояние автомата.
          */
        private final int START_STATE = 0;

        /**
          * Конечное состояние автомата.
          */
        private final int END_STATE = 8;

        /**
          * Конструктор.
          */
        public Main() {
            super( 
                "Main", 
                0, // Номер начального состояния 
                8, // Номер конечного состояния 
                new String[]{ 
                    "Начальное состояние",  
                    "Инициализация", 
                    "Начало цикла", 
                    "Цикл", 
                    "Условие", 
                    "Условие (окончание)", 
                    "Обновление максимума", 
                    "Инкремент", 
                    "Конечное состояние" 
                }, new int[]{ 
                    Integer.MAX_VALUE, // Начальное состояние,  
                    0, // Инициализация 
                    -1, // Начало цикла 
                    -1, // Цикл 
                    0, // Условие 
                    -1, // Условие (окончание) 
                    0, // Обновление максимума 
                    -1, // Инкремент 
                    Integer.MAX_VALUE, // Конечное состояние 
                } 
            ); 
        }

        /**
          * Сделать один шаг автомата в перед.
          */
        protected void doStepForward(int level) {
            // Переход в следующее состояние
            switch (state) {
                case START_STATE: { // Начальное состояние
                    state = 1; // Инициализация
                    break;
                }
                case 1: { // Инициализация
                    state = 2; // Начало цикла
                    break;
                }
                case 2: { // Начало цикла
                    stack.pushBoolean(false); 
                    state = 3; // Цикл
                    break;
                }
                case 3: { // Цикл
                    if (d.Main_i < d.a.length) {
                        state = 4; // Условие
                    } else {
                        state = END_STATE; 
                    }
                    break;
                }
                case 4: { // Условие
                    if (d.max < d.a[d.Main_i]) {
                        state = 6; // Обновление максимума
                    } else {
                        stack.pushBoolean(false); 
                        state = 5; // Условие (окончание)
                    }
                    break;
                }
                case 5: { // Условие (окончание)
                    state = 7; // Инкремент
                    break;
                }
                case 6: { // Обновление максимума
                    stack.pushBoolean(true); 
                    state = 5; // Условие (окончание)
                    break;
                }
                case 7: { // Инкремент
                    stack.pushBoolean(true); 
                    state = 3; // Цикл
                    break;
                }
            }

            // Действие в текущем состоянии
            switch (state) {
                case 1: { // Инициализация
                    d.max = 0;
                    break;
                }
                case 2: { // Начало цикла
                    d.Main_i = 0;
                    break;
                }
                case 3: { // Цикл
                    break;
                }
                case 4: { // Условие
                    break;
                }
                case 5: { // Условие (окончание)
                    break;
                }
                case 6: { // Обновление максимума
                    // @!max
                    stack.pushInteger(d.max);
                    d.max = d.a[d.Main_i];
                    break;
                }
                case 7: { // Инкремент
                    d.Main_i++;
                    break;
                }
            }
        }

        /**
          * Сделать один шаг автомата назад.
          */
        protected void doStepBackward(int level) {
            // Обращение действия в текущем состоянии
            switch (state) {
                case 1: { // Инициализация
                    break;
                }
                case 2: { // Начало цикла
                    break;
                }
                case 3: { // Цикл
                    break;
                }
                case 4: { // Условие
                    break;
                }
                case 5: { // Условие (окончание)
                    break;
                }
                case 6: { // Обновление максимума
                    // @?max
                    d.max = stack.popInteger();
                    break;
                }
                case 7: { // Инкремент
                    d.Main_i--;
                    break;
                }
            }

            // Переход в предыдущее состояние
            switch (state) {
                case 1: { // Инициализация
                    state = START_STATE; 
                    break;
                }
                case 2: { // Начало цикла
                    state = 1; // Инициализация
                    break;
                }
                case 3: { // Цикл
                    if (stack.popBoolean()) {
                        state = 7; // Инкремент
                    } else {
                        state = 2; // Начало цикла
                    }
                    break;
                }
                case 4: { // Условие
                    state = 3; // Цикл
                    break;
                }
                case 5: { // Условие (окончание)
                    if (stack.popBoolean()) {
                        state = 6; // Обновление максимума
                    } else {
                        state = 4; // Условие
                    }
                    break;
                }
                case 6: { // Обновление максимума
                    state = 4; // Условие
                    break;
                }
                case 7: { // Инкремент
                    state = 5; // Условие (окончание)
                    break;
                }
                case END_STATE: { // Начальное состояние
                    state = 3; // Цикл
                    break;
                }
            }
        }

        /**
          * Комментарий к текущему состоянию
          */
        public String getComment() {
            String comment = ""; 
            Object[] args = null; 
            // Выбор комментария
            switch (state) {
                case START_STATE: { // Начальное состояние
                    comment = FindMaximum.this.getComment("Main.START_STATE"); 
                    break;
                }
                case 1: { // Инициализация
                    comment = FindMaximum.this.getComment("Main.Initialization"); 
                    break;
                }
                case 4: { // Условие
                    if (d.max < d.a[d.Main_i]) {
                        comment = FindMaximum.this.getComment("Main.Cond.true"); 
                    } else {
                        comment = FindMaximum.this.getComment("Main.Cond.false"); 
                    }
                    args = new Object[]{new Integer(d.a[d.Main_i]), new Integer(d.max)}; 
                    break;
                }
                case 6: { // Обновление максимума
                    comment = FindMaximum.this.getComment("Main.newMax"); 
                    break;
                }
                case END_STATE: { // Конечное состояние
                    comment = FindMaximum.this.getComment("Main.END_STATE"); 
                    args = new Object[]{new Integer(d.max)}; 
                    break;
                }
            }

            return java.text.MessageFormat.format(comment, args); 
        }

        /**
          * Выполняет действия по отрисовке состояния
          */
        public void drawState() {
            switch (state) {
                case START_STATE: { // Начальное состояние
                    d.visualizer.updateArray(0, 0);
                    break;
                }
                case 1: { // Инициализация
                    d.visualizer.updateArray(0, 0);
                    break;
                }
                case 4: { // Условие
                    d.visualizer.updateArray(d.Main_i, 1);
                    break;
                }
                case 6: { // Обновление максимума
                    d.visualizer.updateArray(d.Main_i, 2);
                    break;
                }
                case END_STATE: { // Конечное состояние
                    d.visualizer.updateArray(0, 0);
                    break;
                }
            }
        }
    }
}
