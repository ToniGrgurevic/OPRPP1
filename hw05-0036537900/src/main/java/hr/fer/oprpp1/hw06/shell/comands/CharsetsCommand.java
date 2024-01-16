package hr.fer.oprpp1.hw06.shell.comands;

import hr.fer.oprpp1.hw06.shell.Environment;
import hr.fer.oprpp1.hw06.shell.ShellStatus;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CharsetsCommand implements ShellCommand {

    List<String> helpList;

    public CharsetsCommand() {
        helpList= new ArrayList<>();

        helpList.add(this.header());
        helpList.add("Command charsets takes no arguments and lists names of supported charsets for your Java platform");
        helpList.add("Single name is written per line.");
        helpList.add("See java.nio.charset.Charset for more details.");
    }

    /**
     * lists names of supported charsets for your Java platform.Single name is written per line.
     * @param env environment in which the command is executed
     * @param arguments arguments of the command (after the command name) that are ignored
     * @return ShellStatus.CONTINUE
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        Charset.availableCharsets().forEach((k, v)->env.writeln(k));
        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "charsets";
    }

    @Override
    public List<String> getCommandDescription() {

        return helpList;
    }
}
