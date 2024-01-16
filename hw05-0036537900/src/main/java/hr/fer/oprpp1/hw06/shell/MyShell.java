package hr.fer.oprpp1.hw06.shell;

import hr.fer.oprpp1.hw06.shell.comands.*;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Class MyShell represents a shell that can be used to execute commands.
 * It is a simple shell that supports a few commands.
 */
public class MyShell{


    public static void main(String[] args) {

        SortedMap<String, ShellCommand> mapOfCommands = new TreeMap<>();
        mapOfCommands.put("cat",new CatCommand());
        mapOfCommands.put("exit",new ExitCommand());
        mapOfCommands.put("charsets",new CharsetsCommand());
        mapOfCommands.put("tree",new TreeCommand());
        mapOfCommands.put("copy",new CopyCommand());
        mapOfCommands.put("mkdir",new MkdirCommand());
        mapOfCommands.put("hexdump",new HexdumpCommand());
        mapOfCommands.put("help",new HelpCommand());
        mapOfCommands.put("ls",new LsCommand());
        mapOfCommands.put("symbol",new SymbolCommand());

        Environment environment = new ShellEnviorment(mapOfCommands);

        environment.writeln("Welcome to MyShell v 1.0");

        ShellStatus status = ShellStatus.CONTINUE;

        while(status == ShellStatus.CONTINUE){
            environment.write(environment.getPromptSymbol() + " ");
            String line = environment.readLine();
            String commandName = line.split(" ")[0];
            boolean multiLine = false;
            if(line.endsWith(environment.getMorelinesSymbol().toString())){
                line = line.substring(0,line.length()-1);
                multiLine = true;
            }


            StringBuilder arguments = new StringBuilder(line.substring(commandName.length()).trim());


            while(multiLine ){
                environment.write(environment.getMultilineSymbol() + " ");
                line = environment.readLine();

                if(line.endsWith(environment.getMorelinesSymbol().toString())){
                    line = line.substring(0,line.length()-1);
                }else multiLine = false;

                arguments.append(" ").append(line);
            }

            ShellCommand command = environment.commands().get(commandName);

            if(command == null){
                environment.writeln("Error! Command " + commandName + " is unvalid!");
                continue;
            }


            status = command.executeCommand(environment, arguments.toString());

        }


    }


}
