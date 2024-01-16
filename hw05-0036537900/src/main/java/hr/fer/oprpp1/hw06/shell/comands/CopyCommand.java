package hr.fer.oprpp1.hw06.shell.comands;

import hr.fer.oprpp1.hw06.shell.Environment;
import hr.fer.oprpp1.hw06.shell.ShellStatus;
import hr.fer.oprpp1.hw06.util.Util;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static hr.fer.oprpp1.hw06.util.Util.getAttributes;

public class CopyCommand implements ShellCommand {



    List<String> helpList;

    public CopyCommand() {
        helpList = new ArrayList<>();
        helpList.add(this.header());
        helpList.add("Command copy takes two arguments.");
        helpList.add("The first argument is path to some file and is mandatory.");
        helpList.add("The second argument is path to some directory and is mandatory.");
        helpList.add("If the second argument is directory, the original file will be copied into that directory using the original file name.");
        helpList.add("This command copies the file to the given directory.");


    }



    @Override
    public ShellStatus executeCommand(Environment env, String arguments)  {

        String input ="";
        String output ="";
        try{
            if(arguments.length() == 0){
                env.writeln("Copy command expects two arguments but was given none");
                return ShellStatus.CONTINUE;
            }

            input = Util.getArgument(arguments);

            if(arguments.length() == input.length()){
                env.writeln("Copy command expects two arguments but was given one");
                return ShellStatus.CONTINUE;
            }

            output = Util.getArgument(arguments.substring(input.length()));

        }catch (Exception e){
            env.writeln("Error while parsing file names");
        }
        Path original = Util.getRealPath( Path.of(input) );
        Path copy = Util.getRealPath(Path.of(output));


        BasicFileAttributes atrsOriginal= getAttributes(original);
        BasicFileAttributes atrsCopy= getAttributes(copy);
        if( atrsOriginal == null){
            env.writeln("failed to get attributes of files");
            return ShellStatus.CONTINUE;
        }


        if(atrsOriginal.isDirectory()){
            env.writeln("first argument can't be directory!");
            return ShellStatus.CONTINUE;
        }
        if(atrsCopy!=null && atrsCopy.isDirectory()){
            copy = Paths.get(copy.toString(),original.getFileName().toString());
            copy = Util.getRealPath(copy);
        }


        try (   BufferedReader br = Files.newBufferedReader(original);
                BufferedWriter bw = Files.newBufferedWriter(copy) ) {
            while (br.ready()) {
                bw.write(br.readLine() + "\n");
            }
            env.writeln("File " + original.toString() + " copied to " + copy.toString());
        } catch (IOException e) {
            env.writeln("Error while copying file " + original.toString() + " to " + copy.toString());
        }

        return ShellStatus.CONTINUE;

    }





    @Override
    public String getCommandName() {
        return "copy";
    }

    @Override
    public List<String> getCommandDescription() {
        return helpList;
    }
}
