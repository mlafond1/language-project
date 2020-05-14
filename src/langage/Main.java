package langage;

import langage.semantic.ClassAnalysis;
import langage.semantic.SemanticException;
import langage.semantic.structure.ClassTable;
import langage.semantic.generator.DaoGenerator;
import langage.semantic.generator.DtoGenerator;
import langage.semantic.generator.SqlGenerator;
import langage.grammar.lexer.Lexer;
import langage.grammar.lexer.LexerException;
import langage.grammar.node.Node;
import langage.grammar.parser.Parser;
import langage.grammar.parser.ParserException;

import java.io.*;

public class Main {

    private static String outputFolder = "generated";

    public static void main(String[] args){

        if(args.length != 1){
            System.err.println("ERROR: Need exactly 1 input file");
            System.exit(1);
        }

        try {
            Parser parser = new Parser(new Lexer(new PushbackReader(new FileReader(args[0]), 1024)));

            Node tree = parser.parse();
            ClassTable table = ClassAnalysis.verify(tree);

            for (var entry : SqlGenerator.generate(tree, table).entrySet()){
                printFile(entry.getKey(), entry.getValue());
            }
            for (var entry : DtoGenerator.generate(tree, table).entrySet()) {
                printFile(entry.getKey(), entry.getValue());
            }
            for (var entry : DaoGenerator.generate(tree, table).entrySet()) {
                printFile(entry.getKey(), entry.getValue());
            }

        } catch (SemanticException | ParserException | LexerException | SecurityException | IOException e) {
            String className = e.getClass().getSimpleName();
            String errorName = className.substring(0, className.indexOf("Exception")).toUpperCase();
            System.err.println(errorName + " ERROR: " + e.getMessage());
            System.exit(1);
        }

    }

    private static void printFile(String fileName, String content) throws IOException {
        //System.out.println(fileName + ":\n" + content);
        File file = new File(outputFolder, fileName);
        File dir = file.getParentFile();
        if(!dir.exists() && !dir.mkdirs())
            throw new IOException("Cannot write file at " + dir.getPath());
        // Garanti la fermeture de writer
        try(FileWriter writer = new FileWriter(file)){
            writer.write(content);
        }
    }


}
