package ru.ifmo.vizi.base.auto;

import java.util.Stack;

/**
 * Стек с возможностью заталкивать примитивные типы.
 *
 * @author Georgiy Korneev
 * @version $Id: AutoStack.java,v 1.1 2003/12/24 10:50:42 geo Exp $
 */
public final class AutoStack extends Stack {
    /**
     * Pushes a boolean value onto the top of this stack.
     *
     * @param value boolean value to push.
     * @return pushed value.
     */
    public boolean pushBoolean(boolean value) {
        push(new Boolean(value));
        return value;
    }

    /**
     * Removes the boolean value onto the top of this stack.
     *
     * @return popped value.
     */
    public boolean popBoolean() {
        return ((Boolean) pop()).booleanValue();
    }

    /**
     * Looks the boolean value at the top of this stack.
     *
     * @return the boolean value at the top of this stack.
     */
    public boolean peekBoolean() {
        return ((Boolean) peek()).booleanValue();
    }

    /**
     * Pushes a byte value onto the top of this stack.
     *
     * @param value byte value to push.
     * @return pushed value.
     */
    public byte pushByte(byte value) {
        push(new Byte(value));
        return value;
    }

    /**
     * Removes the byte value onto the top of this stack.
     *
     * @return popped value.
     */
    public byte popByte() {
        return ((Byte) pop()).byteValue();
    }

    /**
     * Looks the byte value at the top of this stack.
     *
     * @return the byte value at the top of this stack.
     */
    public byte peekByte() {
        return ((Byte) peek()).byteValue();
    }

    /**
     * Pushes a character value onto the top of this stack.
     * @param value character value to push.
     * @return pushed value.
     */
    public char pushCharacter(char value) {
        push(new Character(value));
        return value;
    }

    /**
     * Removes the character value onto the top of this stack.
     *
     * @return popped value.
     */
    public char popCharacter() {
        return ((Character) pop()).charValue();
    }

    /**
     * Looks the character value at the top of this stack.
     *
     * @return the character value at the top of this stack.
     */
    public char peekCharacter() {
        return ((Character) peek()).charValue();
    }

    /**
     * Pushes a double value onto the top of this stack.
     *
     * @param value double value to push.
     * @return pushed value.
     */
    public double pushDouble(double value) {
        push(new Double(value));
        return value;
    }

    /**
     * Removes the double value onto the top of this stack.
     *
     * @return popped value.
     */
    public double popDouble() {
        return ((Double) pop()).doubleValue();
    }

    /**
     * Looks the double value at the top of this stack.
     *
     * @return the double value at the top of this stack.
     */
    public double peekDouble() {
        return ((Double) peek()).doubleValue();
    }

    /**
     * Pushes a float value onto the top of this stack.
     *
     * @param value float value to push.
     * @return pushed value.
     */
    public float pushFloat(float value) {
        push(new Float(value));
        return value;
    }

    /**
     * Removes the float value onto the top of this stack.
     *
     * @return popped value.
     */
    public float popFloat() {
        return ((Float) pop()).floatValue();
    }

    /**
     * Looks the float value at the top of this stack.
     *
     * @return the float value at the top of this stack.
     */
    public float peekFloat() {
        return ((Float) peek()).floatValue();
    }

    /**
     * Pushes a integer value onto the top of this stack.
     *
     * @param value integer value to push.
     * @return pushed value.
     */
    public int pushInteger(int value) {
        push(new Integer(value));
        return value;
    }

    /**
     * Removes the integer value onto the top of this stack.
     *
     * @return popped value.
     */
    public int popInteger() {
        return ((Integer) pop()).intValue();
    }

    /**
     * Looks the integer value at the top of this stack.
     *
     * @return the integer value at the top of this stack.
     */
    public int peekInteger() {
        return ((Integer) peek()).intValue();
    }

    /**
     * Pushes a long value onto the top of this stack.
     *
     * @param value long value to push.
     * @return pushed value.
     */
    public long pushLong(long value) {
        push(new Long(value));
        return value;
    }

    /**
     * Removes the long value onto the top of this stack.
     *
     * @return popped value.
     */
    public long popLong() {
        return ((Long) pop()).longValue();
    }

    /**
     * Looks the long value at the top of this stack.
     *
     * @return the long value at the top of this stack.
     */
    public long peekLong() {
        return ((Long) peek()).longValue();
    }

    /**
     * Pushes a short value onto the top of this stack.
     *
     * @param value short value to push.
     * @return pushed value.
     */
    public short pushShort(short value) {
        push(new Short(value));
        return value;
    }

    /**
     * Removes the short value onto the top of this stack.
     *
     * @return popped value.
     */
    public short popShort() {
        return ((Short) pop()).shortValue();
    }

    /**
     * Looks the short value at the top of this stack.
     *
     * @return the short value at the top of this stack.
     */
    public short peekShort() {
        return ((Short) peek()).shortValue();
    }
}

