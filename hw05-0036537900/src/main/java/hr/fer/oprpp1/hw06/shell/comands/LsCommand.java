package hr.fer.oprpp1.hw06.shell.comands;

import hr.fer.oprpp1.hw06.shell.Environment;
import hr.fer.oprpp1.hw06.shell.ShellStatus;
import hr.fer.oprpp1.hw06.util.Util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LsCommand implements ShellCommand {

    List<String> helpList = new ArrayList<>();

    public LsCommand() {
        helpList.add(this.header());
        helpList.add("Command ls takes a single argument – directory – and writes a directory listing (not recursive).");
        helpList.add("The output consists of 4 columns.");
        helpList.add("First column indicates if current object is directory (d), readable (r), writable (w) and executable (x).");
        helpList.add("Second column contains object size in bytes that is right aligned and occupies 10 characters.");
        helpList.add("Follows file creation date/time and finally file name.");
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String argument = Util.getArgument(arguments);
        if(argument == null){
            env.writeln("Ls command expects one argument but was given none");
            return ShellStatus.CONTINUE;
        }

        Path path = Paths.get(argument);
        if(path.getFileName() == path)
            path = Paths.get(Util.prePath.toString());

        try {
            Path finalPath = path;
            Files.walkFileTree(path, new FileVisitor<>() {

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                   if(finalPath.toString().equals(dir.toString()))
                       return FileVisitResult.CONTINUE;
                    return FileVisitResult.SKIP_SUBTREE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    StringBuilder sb = new StringBuilder();
                    sb.append(Files.isDirectory(file) ? "d" : "-");
                    sb.append(Files.isReadable(file) ? "r" : "-");
                    sb.append(Files.isWritable(file) ? "w" : "-");
                    sb.append(Files.isExecutable(file) ? "x" : "-");
                    sb.append( String.format("%10d",attrs.size()) ).append(" ");

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    FileTime fileTime = attrs.creationTime();
                    String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
                    sb.append(formattedDateTime).append(" " );
                    sb.append(file.getFileName()).append(" ");
                    env.writeln(sb.toString());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.TERMINATE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.TERMINATE;
                }
            });
        }catch (Exception e){
            env.writeln("Error while listing directory " + path);
        }

        return ShellStatus.CONTINUE;

    }

    @Override
    public String getCommandName() {
        return "ls";
    }

    @Override
    public List<String> getCommandDescription() {
        return this.helpList;
    }
}
