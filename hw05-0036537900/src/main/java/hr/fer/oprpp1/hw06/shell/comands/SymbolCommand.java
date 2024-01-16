package hr.fer.oprpp1.hw06.shell.comands;

import hr.fer.oprpp1.hw06.shell.Environment;
import hr.fer.oprpp1.hw06.shell.ShellStatus;

import java.util.List;
import java.util.function.Consumer;

public class SymbolCommand implements ShellCommand {

    List<String> helpList;

    public SymbolCommand() {
        helpList = List.of(
                this.header(),
                "Command symbol takes one or two arguments.",
                "The first argument is type of symbol that should be changed.",
                "The second argument is new symbol that should be used.",
                "Symbol for PROMPT, MORELINES and MULTILINE can be changed.",
                "If second argument is not given, symbol for given type is printed.",
                "If second argument is given, symbol for given type is changed to new symbol."
        );
    }

    /**
     * Changes the symbol for the given type.Can change MORELINES, MULTILINE and PROMPT symbols.
     * new symbol must be one character long.
     * @param env environment in which the command is executed
     * @param arguments arguments of the command (after the command name) -> type and symbol
     * @return status of shell after the command is executed -> CONTINUE
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        var args = arguments.split("\\s+");
        String type = arguments.split("\\s+")[0];
        String symbol = null;
        if(args.length ==  2) symbol = args[1];

        if(args.length > 2) env.writeln("To many arguments! Plese read help for command symbol.");

        switch (type) {
            case "PROMPT" -> change(env.getPromptSymbol(), env::setPromptSymbol, symbol, type, env);
            case "MORELINES" -> change(env.getMorelinesSymbol(), env::setMorelinesSymbol, symbol, type, env);
            case "MULTILINE" -> change(env.getMultilineSymbol(), env::setMultilineSymbol, symbol, type, env);
            default -> env.writeln("Symbol " + type + " dosen't exist!");
        }
        return ShellStatus.CONTINUE;
    }

    private void change(Character curentSymbol, Consumer<Character> op, String symbol, String type, Environment env) {

        if(symbol == null) {
            env.writeln("Symbol for " + type + " is '" + curentSymbol + "'" );
            return;
        }
        if(symbol.length() != 1){
            env.writeln("Symbol for " + type + " must be one character!");
            return;
        }

        if(curentSymbol == symbol.charAt(0)){
            env.writeln("Symbol for " + type + " is already " + symbol.charAt(0));
            return;
        }
        op.accept(symbol.charAt(0));
        env.writeln("Symbol for " + type + " changed from " + curentSymbol + " to " + symbol.charAt(0));
    }


    @Override
    public String getCommandName() {
        return "symbol";
    }

    @Override
    public List<String> getCommandDescription() {

        return helpList;
    }
}
