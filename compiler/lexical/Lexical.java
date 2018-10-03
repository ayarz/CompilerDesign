package compiler.lexical;

import static compiler.def.*;

public class Lexical {

    public boolean isAlpha(char c) {

        if ((c == 'a') || (c == 'b') || (c == 'c') || (c == 'd')
                || (c == 'e') || (c == 'f') || (c == 'g') || (c == 'h')
                || (c == 'i') || (c == 'j') || (c == 'k') || (c == 'l')
                || (c == 'm') || (c == 'n') || (c == 'o') || (c == 'p')
                || (c == 'q') || (c == 'r') || (c == 's') || (c == 't')
                || (c == 'u') || (c == 'v') || (c == 'w') || (c == 'x')
                || (c == 'y') || (c == 'z') || (c == 'A') || (c == 'B')
                || (c == 'C') || (c == 'D') || (c == 'E') || (c == 'F')
                || (c == 'G') || (c == 'H') || (c == 'I') || (c == 'J')
                || (c == 'K') || (c == 'L') || (c == 'M') || (c == 'N')
                || (c == 'O') || (c == 'P') || (c == 'Q') || (c == 'R')
                || (c == 'S') || (c == 'T') || (c == 'U') || (c == 'V')
                || (c == 'W') || (c == 'X') || (c == 'Y') || (c == 'Z')) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDigit(char c) {
        if ((c == '0') || (c == '1') || (c == '2') || (c == '3')
                || (c == '4') || (c == '5') || (c == '6') || (c == '7')
                || (c == '8') || (c == '9')) {
            return true;
        } else {
            return false;
        }
    }

    public void getChar() {
        if (current < prg.length()) {
            nextChar = prg.charAt(current);
            current++;
            if (isAlpha(nextChar)) {

                charClass = LETTER;
            } else if (isDigit(nextChar)) {

                charClass = DIGIT;
            } else {

                charClass = UNKNOWN;
            }
        } else {

            charClass = EOS;

        }
    }

    public int lookupIdentStatement(String s) {
        switch (s) {

            case "read":
                nextToken = READ_ST;
                break;
            case "write":
                nextToken = WRITE_ST;
                break;

            case "until":
                nextToken = UNTIL_ST;
                break;
            case "end":
                nextToken = END_ST;
                break;

            case "if":
                nextToken = IF_ST;
                break;
            case "then":
                nextToken = THEN_ST;
                break;
            case "repeat":
                nextToken = REPEAT_ST;
                break;
            case "else":
                nextToken = ELSE_ST;
                break;

            default:
                nextToken = IDENT;
                break;
        }
        return nextToken;
    }

    public int lookup(String s) {
        switch (s) {

            case "+":
                sourceCode.add("\t" + stnumber + ": " + "+");
                addChar();
                nextToken = ADD_OP;
                break;
            case "-":
                sourceCode.add("\t" + stnumber + ": " + "-");
                addChar();
                nextToken = SUB_OP;
                break;
            case "*":
                sourceCode.add("\t" + stnumber + ": " + "*");
                addChar();
                nextToken = MULT_OP;
                break;
            case "/":
                sourceCode.add("\t" + stnumber + ": " + "/");
                addChar();
                nextToken = DIV_OP;
                break;
            case ":":
                addChar();
                if ((current < prg.length())
                        && (prg.charAt(current)
                        == '=')) {

                    getChar();
                    addChar();
                }
                nextToken = ASSIGN_OP;
                break;

            case ";":
                sourceCode.add("\t" + stnumber + ": " + ";");
                addChar();
                nextToken = EOS;
                break;

            case "<":
                sourceCode.add("\t" + stnumber + ": " + "<");
                syntaxTree.add("Op: " + "<");
                addChar();
                nextToken = COMP_OP;
                break;

            case "=":
                sourceCode.add("\t" + stnumber + ": " + "=");
                syntaxTree.add("Op: " + "=");
                addChar();
                nextToken = COMP_OP;
                break;

            default:
                addChar();
                nextToken = UNKNOWN;
                break;
        }
        return nextToken;

    }

    public void addChar() {
        if (lexLen <= 1000) {
            lexeme += nextChar;
        } else {
            System.out.println("Error - Lexeme is too long");
        }
    }

    public int lex() {
        lexeme = "";
        lexLen = 0;
        getNonBlank();
        switch (charClass) {
            // Lexical identifiers
            case LETTER:
                addChar();
                getChar();
                while (charClass == LETTER || charClass == DIGIT) {
                    addChar();
                    getChar();
                }

                lookupIdentStatement(lexeme);

                break;

            case DIGIT:
                addChar();
                getChar();
                while (charClass == DIGIT) {
                    addChar();
                    getChar();
                }

                nextToken = INT_LIT;
                break;

            case UNKNOWN:
                String s = "";
                s = s + nextChar;
                lookup(s);

                getChar();
                break;

        }
        return nextToken;
    }

    public void getNonBlank() {
        while (isSpace(nextChar)) {
            getChar();
        }
    }

    public boolean isSpace(char c) {
        if (c == ' ') {
            return true;
        } else {
            return false;
        }
    }

}
