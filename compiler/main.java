package compiler;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import compiler.semantic.Semantic;

public class main {

    public static String ReadFile(String name) {
        String allLine = "";
        String line = "";
        try {
            File file = new File(name);
            FileReader fr = new FileReader(file);
            Scanner sc = new Scanner(fr);

            while (sc.hasNextLine()) {
                line = sc.nextLine();
                def.lines.add(line);
                allLine = allLine + " " + line;
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("File does not exist!" + e);
        }

        return allLine;
    }

    public static void main(String[] args) {
        Semantic sm = new Semantic();
        String programsName = args[0];
        //String programsName = "parser.txt";

        def.lines = new ArrayList<String>();
        def.sourceCode = new ArrayList<String>();
        def.syntaxTree = new ArrayList<String>();
        def.symbolTable = new HashMap<String, String>();
        def.keys = new ArrayList<String>();

        def.prg = ReadFile(programsName);

        System.out.println("\nType Checking...");

        sm.program();
        System.out.println("\nType Checking Finished.");

        System.out.println("\n\nSymbol table: " + programsName);
        System.out.println("Variable Name" + "\t" + "Location" + "\t" + "LineNumbers");
        System.out.println("-------------" + "\t" + "--------" + "\t" + "------------");

        for (int i = 0; i < def.keys.size(); i++) {
            System.out.println(def.keys.get(i) + "\t\t" + i + "\t\t" + def.symbolTable.get(def.keys.get(i)));
        }

        System.out.println("\n\nSource code: " + programsName);
        for (int i = 0; i < def.sourceCode.size(); i++) {
            System.out.println(def.sourceCode.get(i));
        }

        System.out.println("\n\nSyntax tree: " + programsName);
        for (int i = 0; i < def.syntaxTree.size(); i++) {
            System.out.println(def.syntaxTree.get(i));
        }

    }

}
