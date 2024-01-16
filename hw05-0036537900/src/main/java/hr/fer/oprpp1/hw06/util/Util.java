package hr.fer.oprpp1.hw06.util;

import hr.fer.oprpp1.hw06.shell.Environment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;

public class Util {


    public static Path prePath = Path.of("src/main/resources");


    public static Path getRealPath(Path path) {
        if(!path.toString().startsWith("C:\\") && !path.toString().startsWith("src\\main\\resources"))
            path = Paths.get(Util.prePath.toString(), String.valueOf(path));
        return path;
    }

    public static BasicFileAttributes getAttributes(Path path) {


        try{
            BasicFileAttributeView faView = Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
            return faView.readAttributes();
        } catch (IOException ignored) {

        }
        return  null;
    }

    public static String getArgument(String argument) {

        argument = argument.trim();

        if (argument.contains("\"")) {

            int beginInd = argument.indexOf('\"') + 1;
            return argument.substring(beginInd, argument.indexOf('\"', beginInd) - 1);
        } else return argument.split(" ")[0].trim();
    }


}

