package hr.fer.oprpp1.hw06.shell.comands;

import hr.fer.oprpp1.hw06.shell.Environment;
import hr.fer.oprpp1.hw06.shell.ShellStatus;
import hr.fer.oprpp1.hw06.shell.comands.ShellCommand;

import java.util.ArrayList;
import java.util.List;

public class ExitCommand implements ShellCommand {

    List<String> helpList;

    public ExitCommand() {
         helpList= new ArrayList<>();
        helpList.add(this.header());
        helpList.add("Comand dosen't take arguments.");
        helpList.add("Terminates the shell.");
    }

    /**
     *Terminates the shell.
     * @param env environment in which the command is executed
     * @param arguments arguments of the command (after the command name). This command doesn't take arguments.
     * @return status of shell after the command is executed.Always returns TERMINATE.
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        return ShellStatus.TERMINATE;
    }

    @Override
    public String getCommandName() {
        return "exit";
    }

    @Override
    public List<String> getCommandDescription() {

        return helpList;
    }
}
