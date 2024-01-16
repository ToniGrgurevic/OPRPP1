package hr.fer.oprpp1.hw06.shell.comands;

import hr.fer.oprpp1.hw06.shell.Environment;
import hr.fer.oprpp1.hw06.shell.ShellStatus;
import hr.fer.oprpp1.hw06.util.Util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class HexdumpCommand implements ShellCommand {


    List<String> helpList;

    public HexdumpCommand() {
        helpList = new ArrayList<>();
        helpList.add(this.header());
        helpList.add("Command hexdump takes one argument.");
        helpList.add("The argument is path to some file and is mandatory.");
        helpList.add("This command opens given file and writes its content in hexadecimal format to console.");
        helpList.add("Alongside the hexadecimal output, the command writes the corresponding characters in the right column.");
        helpList.add("For all characters whose value is less than 32 or greater than 127 , a '.' is printed instead.");
        helpList.add("The output is formatted in such a way that 16 bytes are printed in one line.");
        helpList.add("The first column prints the offset in hexadecimal, and the second column prints the bytes in hexadecimal.");
        helpList.add("The third column prints the bytes in the right column.");

    }


    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String arg = Util.getArgument(arguments);
        Path path = Util.getRealPath(Path.of(arg));
        BasicFileAttributes fileAttributes = Util.getAttributes(path);

        if(fileAttributes == null){
            env.writeln("File does not exist");
            return ShellStatus.CONTINUE;
        } else if (fileAttributes.isDirectory()){
            env.writeln("File is a directory");
            return ShellStatus.CONTINUE;
        }

        try(InputStream br = Files.newInputStream(path)){

            int read  = 0;
            StringBuilder sb = new StringBuilder();

            while(br.available() > 0){

                sb.append(String.format("%08X",read)).append(": ");

                StringBuilder rightSide = new StringBuilder();
                rightSide.append("| ");

                byte[] bytes = new byte[16];
                int n = br.read(bytes,0 ,16);

                if(n == -1) break;
                for(int i =0 ; i<n ; i++){
                    sb.append(String.format("%02X ",bytes[i] ));
                    if(bytes[i] < 32) rightSide.append(".");
                    else rightSide.append((char)bytes[i]);
                    if(i == 7) {
                        sb.append("|");
                    }
                }
                read += n;

                while(n < 16){
                    sb.append("   ");
                    if(n == 7) sb.append("|");
                    rightSide.append(" ");
                    n++;
                }
                sb.append(rightSide);
                env.writeln(sb.toString());
                sb.setLength(0);

            }
        } catch (IOException e) {
            env.writeln("Error while reading file " + path);
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "hexdump";
    }

    @Override
    public List<String> getCommandDescription() {
        return null;
    }
}
