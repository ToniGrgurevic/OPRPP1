package hr.fer.oprpp1.hw06.shell.comands;

import hr.fer.oprpp1.hw06.shell.Environment;
import hr.fer.oprpp1.hw06.shell.ShellStatus;
import hr.fer.oprpp1.hw06.util.Util;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MkdirCommand implements ShellCommand {


    List<String> helpList;

    public MkdirCommand() {
        helpList = new ArrayList<>();
        helpList.add(this.header());
        helpList.add("Command mkdir takes a single argument.");
        helpList.add("The argument is a path to a directory which should be created.");
        helpList.add("This command creates the appropriate directory structure.");
    }


    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        Path directory =  Path.of(Util.getArgument(arguments));
         directory = Util.getRealPath(directory);

        try {
            // Create the directory
            Files.createDirectory(directory);
            System.out.println("Directory created successfully: " + directory.toAbsolutePath());
        } catch (Exception e) {
            env.writeln("Error while creating directory " + directory);
        }
        return ShellStatus.CONTINUE;
    }


    @Override
    public String getCommandName() {
        return "mkdir";
    }

    @Override
    public List<String> getCommandDescription() {
        return helpList;
    }
}
