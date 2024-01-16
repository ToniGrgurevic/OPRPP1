package hr.fer.oprpp1.hw06.shell.comands;

import hr.fer.oprpp1.hw06.shell.Environment;
import hr.fer.oprpp1.hw06.shell.ShellStatus;
import hr.fer.oprpp1.hw06.util.Util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TreeCommand implements ShellCommand {

    List<String> helpList = new ArrayList<>();

    public TreeCommand() {
        helpList.add(this.header());
        helpList.add("The tree command expects a single argument: directory name and prints a tree ");
        helpList.add( "each directory level shifts output two charatcers to the right.");
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
            BasicFileAttributeView faView = Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
            BasicFileAttributes attributes = faView.readAttributes();

            if(!attributes.isDirectory())
                env.writeln("path" + path.toString()+ "is not a directory");

            Files.walkFileTree(path, new FileVisitor<>() {

                int level = 0;
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {

                       StringBuilder sb = new StringBuilder();
                       sb.append(" ".repeat(level*2));
                       sb.append(dir.getFileName());
                       env.writeln(sb.toString());
                        level++;

                        return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                    StringBuilder sb = new StringBuilder();
                    sb.append(" ".repeat(level*2));
                    sb.append(file.getFileName());
                    env.writeln(sb.toString());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.TERMINATE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    level--;
                    return FileVisitResult.CONTINUE;
                }
            });
        }catch (Exception e){
            env.writeln("Error while listing directory " + path);
        }


        return ShellStatus.CONTINUE;

    }

    @Override
    public String getCommandName() {
        return "tree";
    }

    @Override
    public List<String> getCommandDescription() {
        return this.helpList;
    }
}
