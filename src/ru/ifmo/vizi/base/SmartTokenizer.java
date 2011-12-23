package ru.ifmo.vizi.base;

import java.io.*;
import java.util.StringTokenizer;

/**
 * Wrapper for {@link StreamTokenizer}.
 *
 * @author  Georgiy Korneev
 * @version $Id: SmartTokenizer.java,v 1.4 2004/03/18 12:45:41 geo Exp $
 */
public class SmartTokenizer extends StreamTokenizer {
    /**
     * Configuration for messages.
     */
    private final Configuration config;

    /**
     * Epsilon.
     */
    private final static double EPS = 1e-10;

    /**
     * Creates a new <code>SmartTokenizer</code> that parses the given character stream.
     *
     * @param reader a reader object providing the input stream.
     * @param config vizi configuration.
     */
    public SmartTokenizer(Reader reader, Configuration config) {
        super(reader);
        this.config = config;
    }

    /**
     * Creates a new <code>SmartTokenizer</code> that parses the given string.
     *
     * @param string a string to parse.
     * @param config vizi configuration.
     */
    public SmartTokenizer(String string, Configuration config) {
        this(new StringReader(string), config);
    }

    /**
     * Gets next double value from the stream.
     *
     * @return got value.
     *
     * @throws SmartTokenizerException if an error found or next token is not a double.
     */
    public double nextDouble() throws SmartTokenizerException {
        try {
            switch (nextToken()) {
                case TT_NUMBER:
                    return nval;
                case TT_WORD:
                    throw new SmartTokenizerException("ExpectedDouble", sval);
                case TT_EOF:
                    throw new SmartTokenizerException("UnexpectedEOF", "");
                case TT_EOL:
                    return nextDouble();
                default:
                    throw new SmartTokenizerException("ExpectedDouble", "" + (char) ttype);
            }
        } catch (IOException e) {
            throw new SmartTokenizerException(e);
        }
    }

    /**
     * Gets a next double value from the stream.
     *
     * @param min minimal allowed value.
     * @param max maximal allowed value.
     *
     * @return got integer.
     *
     * @throws SmartTokenizerException if next token is not a double or
     *      an error found.
     */
    public double nextDouble(double min, double max) throws SmartTokenizerException {
        double value = nextDouble();
        if (value < min - EPS || value > max + EPS) {
            throw new SmartTokenizerException("DoubleOutOfBounds",
                    new Object[]{
                        new Integer(lineno()),
                        new Double(value),
                        new Double(min),
                        new Double(max)
                    });
        }
        return value;
    }

    /**
     * Gets a next integer value from the stream.
     *
     * @return got integer.
     *
     * @throws SmartTokenizerException if next token is not an integer
     *      or an error found.
     */
    public int nextInt() throws SmartTokenizerException {
        try {
            switch (nextToken()) {
                case TT_NUMBER:
                    if (Math.round(nval) - nval > EPS) {
                        throw new SmartTokenizerException("ExpectedInt", "" + nval);
                    }
                    return (int) Math.round(nval);
                case TT_WORD:
                    throw new SmartTokenizerException("ExpectedInt", sval);
                case TT_EOF:
                    throw new SmartTokenizerException("UnexpectedEOF", "");
                case TT_EOL:
                    return nextInt();
                default:
                    throw new SmartTokenizerException("ExpectedInt", "" + (char) ttype);
            }
        } catch (IOException e) {
            throw new SmartTokenizerException(e);
        }
    }

    /**
     * Gets a next integer value from the stream.
     *
     * @param min minimal allowed value.
     * @param max maximal allowed value.
     *
     * @return got integer.
     *
     * @throws SmartTokenizerException if next token is not an integer or
     *      an error found.
     */
    public int nextInt(int min, int max) throws SmartTokenizerException {
        int value = nextInt();
        if (value < min || value > max) {
            throw new SmartTokenizerException("IntOutOfBounds",
                    new Object[]{
                        new Integer(lineno()),
                        new Integer(value),
                        new Integer(min),
                        new Integer(max)
                    });
        }
        return value;
    }

    /**
     * Gets a next word from the stream.
     *
     * @return got word.
     *
     * @throws SmartTokenizerException if an error found.
     */
    public String nextWord() throws SmartTokenizerException {
        try {
            switch (nextToken()) {
                case TT_NUMBER:
                    return "" + nval;
                case TT_WORD:
                case '"':
                    return sval;
                case TT_EOF:
                    throw new SmartTokenizerException("UnexpectedEOF", "");
                case TT_EOL:
                    return nextWord();
                default:
                    return "" + (char) ttype;
            }
        } catch (IOException e) {
            throw new SmartTokenizerException(e);
        }
    }

    /**
     * Gets a next boolean from the stream.
     *
     * @return got word.
     *
     * @throws SmartTokenizerException if next token is not a boolean of an error found.
     */
    public boolean nextBoolean() throws SmartTokenizerException {
        String token = nextWord();
        if (
            "true".equalsIgnoreCase(token) ||
            "yes".equalsIgnoreCase(token) ||
            "1".equalsIgnoreCase(token)
        ) {
            return true;
        }
        if (
            "false".equalsIgnoreCase(token) ||
            "no".equalsIgnoreCase(token) ||
            "0".equalsIgnoreCase(token)
        ) {
            return false;
        }
        throw new SmartTokenizerException("ExpectedBoolean", sval);
    }

    /**
     * Checks whether next word in the stream is equals to given word and removes it.
     * I words are not equal an {@link SmartTokenizerException} is thrown.
     *
     * @param word word to compare with.
     *
     * @throws SmartTokenizerException if next word is not equals to specified word or
     *      an error found.
     */
    public void expect(String word) throws SmartTokenizerException {
        String value = nextWord();
        if (!word.equalsIgnoreCase(value)) {
            throw new SmartTokenizerException("Expected",
                new Object[]{new Integer(lineno()), word, value});
        }
    }

    /**
     * Checks where input is read til the end.
     *
     * @throws SmartTokenizerException if there are more tokens in the input
     *      or an error found.
     */
    public void expectEOF() throws SmartTokenizerException {
        try {
            if (nextToken() != TT_EOF) {
                throw new SmartTokenizerException("EOFExpected", "");
            }
        } catch (IOException e) {
            throw new SmartTokenizerException(e);
        }
    }

    /**
     * Thrown when {@link SmartTokenizer} detects an error.
     */
    public final class SmartTokenizerException extends Exception {
        /**
         * Creates a new <code>SmartTokenizerException</code> using message
         * format.
         *
         * @param key message key.
         * @param param message parameter.
         */
        public SmartTokenizerException(String key, String param) {
            super(I18n.message(config.getParameter("SmartTokenizer-" + key),
                new Integer(lineno()), param));
        }

        /**
         * Creates a new <code>SmartTokenizerException</code> using message
         * format.
         *
         * @param key message key.
         * @param params message parameters.
         */
        public SmartTokenizerException(String key, Object[] params) {
            super(I18n.message(config.getParameter("SmartTokenizer-" + key), params));
        }

        /**
         * Creates a new <code>SmartTokenizerException</code> based on {@link IOException}.
         *
         * @param e base exception.
         */
        public SmartTokenizerException(IOException e) {
            this("IOException", e.getMessage());
        }
    }
}
