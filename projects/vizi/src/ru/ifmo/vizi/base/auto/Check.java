package ru.ifmo.vizi.base.auto;

import java.util.*;
import java.lang.reflect.Constructor;


/**
 * Проверяет автомат на правильность.
 *
 * @author Georgiy Korneev
 * @version $Id: Check.java,v 1.4 2004/06/07 13:56:35 geo Exp $
 */
public class Check {
    /**
     * Проверяет автомат на правильность.
     *
     * @param args аргументы командной строки.
     * @throws Exception если произошла ошибка.
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 1 || args.length > 3) {
            System.out.println("Usage: java ru.ifmo.vizi.base.auto.Check <class> [<level> [<first step>]]");
            return;
        }

        Class clazz = Class.forName(args[0]);
        int level = -1;
        if (args.length >= 2) {
            level = Integer.parseInt(args[1]);
        }
        int c = 0;
        if (args.length >= 3) {
            c = Integer.parseInt(args[2]);
        }

        boolean flag = false;
        while (!flag) {
            //Automata auto = (Automata) clazz.newInstance();
            Constructor constructor = clazz.getConstructor(
                    new Class[]{Locale.class});
            Automata auto = (Automata) constructor.newInstance(
                    new Object[]{Locale.getDefault()});

            c++;

            Hashtable map = new Hashtable();

            for (int i = 0; i < c; i++) {
                auto.stepForward(level);

                Object key = new Integer(auto.getStep());
                Object value = auto.toString();
                map.put(key, value);
            }

            System.out.print("Check " + auto.getStep() + " steps... ");

            String comment = auto.getComment();
            String state = auto.toString();
            String prev = state;

            flag = auto.isAtEnd();

            for (int i = 0; i < c; i++) {
                Object key = new Integer(auto.getStep());
                Object value = auto.toString();
                if (value.equals(map.get(key))) {
                    map.remove(key);
                } else {
                    System.out.println(" Error on step " + auto.getStep());
                    System.err.println(auto.getStep());
                    System.err.println(value);
                    System.err.println("---------- INSTEAD OF ----------");
                    System.err.println(map.get(key));
                    System.err.println("--------- INVALID STEP ----------");
                    System.err.println(state);
                    System.err.println("-------- PREVIOUS STEP ----------");
                    System.err.println(prev);
                    System.exit(1);
                }

                prev = auto.toString();
                auto.stepBackward(level);
            }
            System.out.println(" OK (" + comment + ")");
        }
        System.out.println("OK");
    }
}