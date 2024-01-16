package hr.fer.oprpp1.hw06.shell;

import hr.fer.oprpp1.hw06.shell.comands.ShellCommand;

import java.util.Collections;
import java.util.Scanner;
import java.util.SortedMap;

/**
 * Class ShellEnviorment represents an environment in which the shell is executed.
 */
public class ShellEnviorment implements Environment{

    private char multilineSymbol;
    private char promptSymbol;
    private char morelinesSymbol;

    private Scanner scanner;

    private SortedMap<String, ShellCommand> mapOfCommands;


    public ShellEnviorment(SortedMap<String, ShellCommand> mapOfCommands) {
        this('|','>','\\',mapOfCommands);
        scanner = new Scanner(System.in);
    }

    public ShellEnviorment(char multilineSymbol, char promptSymbol, char morelinesSymbol, SortedMap<String, ShellCommand> mapOfCommands) {
        this.multilineSymbol = multilineSymbol;
        this.promptSymbol = promptSymbol;
        this.morelinesSymbol = morelinesSymbol;
        this.mapOfCommands = mapOfCommands;
    }



    @Override
    public String readLine() throws ShellIOException {
        try{
            return scanner.nextLine();
        } catch (Exception e) {
            throw new ShellIOException("Error while reading line!");
        }
    }

    @Override
    public void write(String text) throws ShellIOException {
        System.out.print(text);
    }

    @Override
    public void writeln(String text) throws ShellIOException {
        System.out.println(text);
    }

    @Override
    public SortedMap<String, ShellCommand> commands() {
        return Collections.unmodifiableSortedMap(mapOfCommands);
    }

    @Override
    public Character getMultilineSymbol() {
        return this.multilineSymbol;
    }

    @Override
    public void setMultilineSymbol(Character symbol) {
            this.multilineSymbol = symbol;
    }

    @Override
    public Character getPromptSymbol() {
        return this.promptSymbol;
    }

    @Override
    public void setPromptSymbol(Character symbol) {
        this.promptSymbol = symbol;
    }

    @Override
    public Character getMorelinesSymbol() {
        return this.morelinesSymbol;
    }

    @Override
    public void setMorelinesSymbol(Character symbol) {
        this.morelinesSymbol = symbol;
    }
}
