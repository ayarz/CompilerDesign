package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class def {

    public static final int LETTER = 0;
    public static final int DIGIT = 1;
    public static final int UNKNOWN = 99;
    public static final int EOS = 100;
    public static final int IDENT = 11;
    public static final int INT_LIT = 10;
    public static final int ASSIGN_OP = 20;
    public static final int ADD_OP = 21;
    public static final int SUB_OP = 22;
    public static final int MULT_OP = 23;
    public static final int DIV_OP = 24;
    public static final int READ_ST = 37;
    public static final int WRITE_ST = 38;

    public static final int LEFT_PAREN = 25;
    public static final int RIGHT_PAREN = 26;
    public static final int ECHFONEME_OP = 27;

    //Comparison operators
    public static final int COMP_OP = 30;

    //Statements
    public static final int UNTIL_ST = 31;
    public static final int END_ST = 32;
    public static final int IF_ST = 33;
    public static final int THEN_ST = 34;
    public static final int REPEAT_ST = 35;
    public static final int PLUSPLUS_ST = 36;
    public static final int ELSE_ST = 39;
    public static final int FALSE_ST = 40;

    public static int current = 0;
    public static int charClass;
    public static String lexeme = "";
    public static char nextChar;
    public static int lexLen = 0;
    public static int token;
    public static int nextToken;
    public static int stnumber = 0;
    public static String id;
    public static String Id;
    public static String Const;
    public static boolean bool;
    public static boolean flag;
    public static boolean isfirst;
    public static List<String> lines;
    public static List<String> sourceCode;
    public static List<String> syntaxTree;
    public static HashMap<String, String> symbolTable;
    public static ArrayList<String> keys;
    public static String prg = "";
    public static String assambly = "";
    public static int index = 0;
    public static boolean isAs = false;
    public static int PC = 0;
    public static Stack<Integer> rep = new Stack<Integer>();
    public static Stack<Integer> else_end = new Stack<Integer>();
    public static Stack<Integer> cu_PC = new Stack<Integer>();
    public static boolean if_else = false;
}
