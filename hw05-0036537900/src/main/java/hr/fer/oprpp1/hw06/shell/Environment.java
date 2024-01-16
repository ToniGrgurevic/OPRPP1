package hr.fer.oprpp1.hw06.shell;

import hr.fer.oprpp1.hw06.shell.comands.ShellCommand;

import java.util.SortedMap;

public interface Environment {

    /**
     * Reads a line from the user (System in)
     * @return read line
     * @throws ShellIOException if reading fails
     */
    String readLine() throws ShellIOException;

    /**
     * Writes the given text to the user (System out)
     * @param text text to write
     * @throws ShellIOException if writing fails
     */
    void write(String text) throws ShellIOException;

    /**
     * Writes the given text to the user (System out) and adds a new line
     * @param text text to write
     * @throws ShellIOException if writing fails
     */
    void writeln(String text) throws ShellIOException;

    /**
     * Returns a map of all commands supported by this shell
     * String key is the name of the command, ShellCommand value is the command itself
     * @return map of all commands supported by this shell
     */
    SortedMap<String, ShellCommand> commands();

    /**
     * Returns the current multiline symbol
     * @return current multiline symbol
     */
    Character getMultilineSymbol();

    /**
     * Sets the current multiline symbol
     * @param symbol new multiline symbol
     */
    void setMultilineSymbol(Character symbol);

    /**
     * Returns the current prompt symbol
     * @return current prompt symbol
     */
    Character getPromptSymbol();

    /**
     * Sets the current prompt symbol
     * @param symbol new prompt symbol
     */
    void setPromptSymbol(Character symbol);

    /**
     * Returns the current morelines symbol
     * @return current morelines symbol
     */
    Character getMorelinesSymbol();

    /**
     * Sets the current morelines symbol
     * @param symbol new morelines symbol
     */
    void setMorelinesSymbol(Character symbol);
}
