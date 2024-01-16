package hr.fer.oprpp1.hw06.shell.comands;

import hr.fer.oprpp1.hw06.shell.Environment;
import hr.fer.oprpp1.hw06.shell.ShellStatus;

import java.util.List;


/**
 * Interface that represents a strategy for executing a command
 */
public interface ShellCommand {


    /**
     * Executes the command with the given arguments
     * @param env environment in which the command is executed
     * @param arguments arguments of the command (after the command name)
     * @return status of shell after the command is executed
     */
    ShellStatus executeCommand(Environment env, String arguments);


    /**
     * Returns the name of the command
     * @return comand name
     */
    String getCommandName();

    /**
     * Returns a list of strings that describe the command
     * @return list of strings that describe the command
     */
    List<String> getCommandDescription();


    default String header(){
        return "Command " + getCommandName() ;
    }

}
