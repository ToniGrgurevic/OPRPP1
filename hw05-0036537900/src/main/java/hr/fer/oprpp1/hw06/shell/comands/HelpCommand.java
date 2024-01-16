package hr.fer.oprpp1.hw06.shell.comands;

import hr.fer.oprpp1.hw06.shell.Environment;
import hr.fer.oprpp1.hw06.shell.ShellStatus;

import java.util.List;


public class HelpCommand implements ShellCommand {

    List<String> helpList;

    public HelpCommand() {
        helpList = List.of(
                "Command help takes zero or one arguments.",
                "If no arguments are given, it lists names of all supported commands.",
                "If single argument is given, it prints name and the description of selected command.",
                "The command name is given as argument and it is case-insensitive."
        );
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        if(arguments.length() == 0){
            env.writeln("List of all commands:");
            env.commands().forEach((k,v) -> env.writeln(k));
            return ShellStatus.CONTINUE;
        }

        env.commands().get(arguments.trim()).getCommandDescription().forEach(env::writeln);

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public List<String> getCommandDescription() {

        return helpList;
    }
}
