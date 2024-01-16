package hr.fer.oprpp1.hw06.shell.comands;

import hr.fer.oprpp1.hw06.shell.Environment;
import hr.fer.oprpp1.hw06.shell.ShellStatus;
import hr.fer.oprpp1.hw06.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CatCommand implements ShellCommand {

    List<String> helpList;

    public CatCommand() {
         helpList = new ArrayList<>();
        helpList.add(this.header());
        helpList.add("Command cat takes one or two arguments.");
        helpList.add("The first argument is path to some file and is mandatory.");
        helpList.add("The second argument is charset name that should be used to interpret chars from bytes.");
        helpList.add("If not provided, a default platform charset should be used");
        helpList.add("This command opens given file and writes its content to console.");
    }

    /**
     * Method executes the command cat.Method opens given file and writes its content to the console in
     * given charset or default charset if not given.
     * @param env environment in which the command is executed
     * @param arguments arguments of the command (after the command name)
     * @return status of the shell after the command is executed
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        String pathName = Util.getArgument(arguments);
        String chars = arguments.trim().substring(pathName.length());


        Path path =  Paths.get(Util.prePath.toString() ,pathName);

        Charset charset = Charset.defaultCharset();
        if(chars.length() != 0){
            try {
                charset = Charset.forName(chars.trim());
            }catch (Exception e){
                env.writeln("Charset " + chars + " not recognized.Seek help");
                return ShellStatus.CONTINUE;
            }
        }

        try(BufferedReader br = Files.newBufferedReader(path,charset)) {

           while(br.ready()){
               env.writeln(br.readLine());
           }
        } catch (IOException e) {
            env.writeln("Error while reading from file " + path);
        }
        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "cat";
    }

    @Override
    public List<String> getCommandDescription() {

        return helpList;
    }
}
