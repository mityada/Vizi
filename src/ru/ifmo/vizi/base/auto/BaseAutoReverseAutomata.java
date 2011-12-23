package ru.ifmo.vizi.base.auto;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Locale;

/**
 * Базовый класс для автомата с автоматическим обращением.
 *
 * @author Georgiy Korneev
 * @version $Id: BaseAutoReverseAutomata.java,v 1.2 2006/05/16 10:15:47 geo Exp $
 */
public abstract class BaseAutoReverseAutomata extends BaseAutomataWithListener {
    /**
     * Создает новый автомат с автоматическим обращением.
     * Перед использованием автомат должен быть проинициализирован вызовом
     * {@link #init}.
     *
     * @param commentFile имя файла с комментариями.
     * @param locale локаль для комментариев.
     */
    public BaseAutoReverseAutomata(String commentFile, Locale locale) {
        super(commentFile, locale);
    }

    /**
     * Начинает секцию присваиваний.
     */
    protected void startSection() {
        stack.push(null);
    }

    /**
     * Восстанавливает присваивания в секции.
     */
    protected void restoreSection() {
        Object o = stack.pop();
        while (o != null) {
            if (o instanceof StoredField) {
                ((StoredField) o).restore();
            } else if (o instanceof StoredElement) {
                ((StoredElement) o).restore();
            } else {
                System.err.println("Stack corrupted");
            }
            o = stack.pop();
        }
    }
    /**
     * Сохранянет поле объекта.
     * @param object объект.
     * @param field имя поля.
     */
    protected void storeField(Object object, String field) {
        stack.push(new StoredField(object, field));
    }

    /**
     * Сохраняет элемент массива.
     * @param array массив.
     * @param index индекс элемента.
     */
    protected void storeArray(Object array, int index) {
        stack.push(new StoredElement(array, index));
    }

    /**
     * Сохраненное значение поля.
     */
    private final static class StoredField {
        /**
         * Объект, поле которого сохранено.
         */
        private final Object object;

        /**
         * Поле, которое сохранено.
         */
        private final Field field;

        /**
         * Сохраненное значение.
         */
        private final Object value;

        /**
         * Сохраняет новое значение поля.
         * @param object объект, поле которого следует сохранить.
         * @param field имя сохраняемого поля.
         */
        private StoredField(Object object, String field) {
            this.object = object;
            try {
                this.field = object.getClass().getField(field);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Fail: Field not found " + object.getClass().getName() + " " + field);
            }
            try {
                this.value = this.field.get(object);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Fail: Illegal access " + object.getClass().getName() + " " + field);
            }
        }

        /**
         * Восстанавливает сохраненное поле.
         */
        private void restore() {
            try {
                field.set(object, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Fail: Illegal access " + object + " " + field);
            }
        }
    }

    /**
     * Сохраненный элемент массива.
     */
    private final static class StoredElement {
        /**
         * Массив, элемент которого сохранен.
         */
        private final Object array;

        /**
         * Индекс сохраненного элемента в массиве.
         */
        private final int index;

        /**
         * Сохраненное значение.
         */
        private final Object value;

        /**
         * Сохраняет новый элемент массива.
         * @param array массив, элемент которого следует сохранить.
         * @param index индекс сохраняемого элемента.
         */
        private StoredElement(Object array, int index) {
            this.array = array;
            this.index = index;
            value = Array.get(array, index);
        }

        /**
         * Восстанавливает сохраенное значение.
         */
        private void restore() {
            Array.set(array, index, value);
        }
    }
}
