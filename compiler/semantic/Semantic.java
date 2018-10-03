package compiler.semantic;

import static compiler.def.*;
import compiler.lexical.Lexical;
import compiler.codegen.Codegen;

public class Semantic {

    Lexical l;
    Codegen c;

    public Semantic() {
        l = new Lexical();
        c = new Codegen();
    }

    public void addToSymbolTable() {
        if (symbolTable.containsKey(lexeme)) {

            String v = symbolTable.get(lexeme);

            v = v + " " + stnumber;
            symbolTable.put(lexeme, v);
        } else {
            keys.add(lexeme);

            symbolTable.put(lexeme, " " + stnumber);
        }
    }

    public void READ() {
        if (nextToken != IDENT) {
            error("Type error at line " + stnumber + ": An identifier is expected");
        }

        sourceCode.add("\t" + stnumber + ": reserved word: read");
        sourceCode.add("\t" + stnumber + ": ID,name: " + lexeme);
        syntaxTree.add("Read: " + lexeme);
        addToSymbolTable();
        assambly += PC + ": IN " + c.getFirstEmptyReg() + ",0,0";
        PC++;
        c.R[c.getFirstEmptyReg()] = lexeme;
        assambly += "\n";
        l.lex();
    }

    public void WRITE() {
        sourceCode.add("\t" + stnumber + ": reserved word: write");
        syntaxTree.add("Write");
        expr();

        for (int i = 0; i < 7; i++) {
            if (c.R[i] != null) {
                if (c.R[i].equals(Id)) {
                    assambly += PC + ": OUT " + i + ",0,0\n";
                    PC++;
                    c.R[i] = null;
                    index = i;
                    break;
                }
            }
        }

        c.R[6] = null;

    }

    public void simpleExpr() {

        term();

        while (nextToken == ADD_OP
                || nextToken == SUB_OP) {
            int nt = nextToken;
            int k = 8;
            int j = 8;
            syntaxTree.add(" " + "Op: " + lexeme);
            l.lex();
            boolean g = false;
            for (int i = 0; i < 6; i++) {
                if (c.R[i] != null) {
                    if (c.R[i].equals(lexeme)) {
                        j = i;
                        g = true;
                        break;
                    }
                }
            }
            if (!g) {
                if (nextToken == INT_LIT) {
                    c.R[6] = lexeme;
                    j = 6;
                    assambly += PC + ": LDC 6," + lexeme + "(0)\n";
                    PC++;
                }
            }
            if (bool) {
                syntaxTree.add("  " + "Id: " + Id);
                for (int i = 0; i < 7; i++) {
                    if (c.R[i] != null) {
                        if (c.R[i].equals(Id)) {

                            k = i;
                            break;
                        }

                    }
                }

            } else {
                syntaxTree.add("  " + "Const: " + Const);
                k = 6;
            }
            if (nt == ADD_OP) {

                assambly += PC + ": ADD " + index + "," + k + "," + j + "\n";
                PC++;

            } else {

                assambly += PC + ": SUB " + index + "," + k + "," + j + "\n";
                PC++;

            }
            term();

        }
        if (bool) {
            syntaxTree.add("   " + "Id: " + Id);
        } else {
            syntaxTree.add("   " + "Const: " + Const);
        }

    }

    public void term() {

        factor();

        boolean s = false;
        if (!bool) {

            if (c.R[6] != null) {
                if (c.R[6].equals(Const)) {
                    s = true;
                    assambly += PC + ": LDC 6," + Const + "(0)\n";
                    PC++;
                }
            }

            if (!s) {
                if (isAs) {
                    assambly += PC + ": LDC " + index + "," + Const + "(0)\n";
                    PC++;
                } else {
                    assambly += PC + ": LDC 6," + Const + "(0)\n";
                    PC++;
                    c.R[6] = Const;
                }

            }
        } else if (isAs && nextToken != MULT_OP && nextToken != DIV_OP && nextToken != ADD_OP && nextToken != SUB_OP) {
            for (int i = 0; i < 6; i++) {
                if (c.R[i] != null) {
                    if (c.R[i].equals(Id)) {

                        assambly += PC + ": LDA " + index + ",0(" + i + ")\n";
                        PC++;
                        break;
                    }
                }
            }
        }

        isAs = false;
        while (nextToken == MULT_OP || nextToken == DIV_OP) {
            int k = 8;
            int j = 8;

            int nt = nextToken;
            syntaxTree.add("  " + "Op: " + lexeme);
            l.lex();
            if (bool) {
                syntaxTree.add("   " + "Id: " + Id);

                for (int i = 0; i < 7; i++) {
                    if (c.R[i] != null) {
                        if (c.R[i].equals(Id)) {
                            j = i;
                            break;
                        }
                    }
                }

            } else {
                syntaxTree.add("   " + "Const: " + Const);
                j = 6;

            }
            boolean f = false;
            for (int i = 0; i < 7; i++) {
                if (c.R[i] != null) {
                    if (c.R[i].equals(lexeme)) {
                        k = i;
                        f = true;
                        break;
                    }
                }
            }
            if (!f) {

                c.R[6] = lexeme;
                k = 6;
            }
            if (nt == MULT_OP) {
                assambly += PC + ": MUL " + index + "," + k + "," + j + "\n";
                PC++;
            } else {
                assambly += PC + ": DIV " + index + "," + k + "," + j + "\n";
                PC++;
            }
            factor();

        }

    }

    public void factor() {

        if (nextToken == IDENT) {
            addToSymbolTable();
            sourceCode.add("\t" + stnumber + ": ID,name: " + lexeme);
            Id = lexeme;
            bool = true;

            l.lex();
        } else if (nextToken == INT_LIT) {
            sourceCode.add("\t" + stnumber + ": NUM,val: " + lexeme);

            Const = lexeme;
            bool = false;
            l.lex();
        } else {
            error("Type error at line " + stnumber + ": Identifier or operator is expected.");
        }

    }

    public void ASSIGNSTMT() {
        if (nextToken != ASSIGN_OP) {
            error("Type error at line " + stnumber + ": Assign operator is expected.");
        } else {
            isAs = true;
            sourceCode.add("\t" + stnumber + ": ID,name: " + id);
            sourceCode.add("\t" + stnumber + ": " + ":=");
            syntaxTree.add("Assign to: " + id);
            l.lex();
            boolean f = false;
            for (int i = 0; i < 7; i++) {
                if (c.R[i] != null) {
                    if (c.R[i].equals(id)) {
                        f = true;
                        index = i;
                        break;

                    }
                }
            }
            if (!f) {
                int k = c.getFirstEmptyReg();
                c.R[k] = id;
                index = k;
            }

            expr();

            isAs = false;
        }

    }

    public void expr() {
        simpleExpr();
        int t = 8;
        int ll = 8;
        for (int i = 0; i < 7; i++) {
            if (c.R[i] != null) {
                if (c.R[i].equals(Id)) {
                    ll = i;
                    t = i;
                    break;
                }
            }

        }
        String comp = "";
        while (nextToken == COMP_OP) {
            flag = true;
            if (lexeme.equals("<")) {
                comp = "JGE";

            }
            if (lexeme.equals("=")) {
                comp = "JNE";

            }
            if (lexeme.equals("<=")) {
                comp = "JGT";

            }
            l.lex();
            int rr = 0;
            boolean f = false;
            for (int i = 0; i < 6; i++) {
                if (c.R[i] != null) {
                    if (c.R[i].equals(lexeme)) {
                        rr = i;
                        f = true;
                        break;
                    }
                }
            }

            if (!f) {
                if (!lexeme.equals("0")) {

                    assambly += PC + ": LDC 6," + lexeme + "(0)\n";
                    PC++;
                    assambly += PC + ": SUB 6," + ll + ",6\n";
                    PC++;
                    t = 6;
                }
            } else if (!lexeme.equals("0")) {
                assambly += PC + ": SUB 6," + ll + "," + rr + "\n";
                PC++;
                t = 6;
            }
            if (if_else) {
                assambly += PC + ": " + comp + " " + t + "," + ";" + "(7)\n";
                cu_PC.add(PC);
                PC++;
            } else {
                assambly += PC + ": " + comp + " " + t + "," + (rep.peek() - PC - 1) + "(7)\n";
                PC++;
            }
            simpleExpr();

        }
    }

    public void stmt() {
        switch (nextToken) {
            case READ_ST:
                l.lex();
                READ();
                if (nextToken != EOS) {
                    error("Type error at line " + stnumber + " : EOS is expected!");
                    //  stnumber++;
                } else {
                    // stnumber++;
                    l.lex();
                }
                break;

            case WRITE_ST:
                l.lex();
                WRITE();
                if (nextToken != EOS) {
                    error("Type error at line " + stnumber + ": EOS is expected!");
                    // stnumber++;
                } else {
                    //  stnumber++;
                    l.lex();
                }
                break;

            case IDENT:
                addToSymbolTable();
                id = lexeme;
                l.lex();
                if (nextToken == ASSIGN_OP) {

                    ASSIGNSTMT();
                }
                if (nextToken != EOS) {
                    error("Type error at line " + stnumber + ": EOS is expected!");
                    //  stnumber++;
                } else {
                    //  stnumber++;
                    l.lex();
                }

                break;

            case IF_ST:
                l.lex();
                IFSTMT();

                break;

            case REPEAT_ST:
                l.lex();
                REPEATSTMT();
                if (nextToken != EOS) {
                    error("Type error at line " + stnumber + ": EOS is expected!");
                    //stnumber++;
                } else {
                    // stnumber++;
                    l.lex();
                }
                break;

        }
    }

    public void stmt_list() {
        stnumber++;
        sourceCode.add(stnumber + ": " + lines.get(stnumber - 1));
        stmt();
        while (current != prg.length()) {
            if (nextToken == UNTIL_ST || nextToken == END_ST || nextToken == ELSE_ST) {

                break;
            }

            stnumber++;
            sourceCode.add(stnumber + ": " + lines.get(stnumber - 1));
            stmt();
        }

    }

    public void program() {

        l.getChar();
        l.lex();

        if (nextToken == READ_ST || nextToken == WRITE_ST) {

            stmt_list();
            stnumber++;
            sourceCode.add("\t" + stnumber + ": " + "EOF");
            assambly += PC + ": HALT 0,0,0\n";
            PC++;

            c.write(assambly);

        } else {
            error("error");

        }
    }

    public void REPEATSTMT() {

        sourceCode.add("\t" + stnumber + ": reserved word: repeat");
        syntaxTree.add("Repeat");
        rep.push(PC);

        while (nextToken != UNTIL_ST) {

            stmt_list();

        }

        stnumber++;
        sourceCode.add(stnumber + ": " + lines.get(stnumber - 1));
        sourceCode.add("\t" + stnumber + ": reserved word: until");
        l.lex();
        expr();

    }

    public void IFSTMT() {
        if_else = true;
        sourceCode.add("\t" + stnumber + ": reserved word: if");
        syntaxTree.add("If");
        flag = false;
        expr();
        if_else = false;
        if (!flag) {
            error("Type error at line " + stnumber + ": if test is not Boolean");
        }
        if (nextToken != THEN_ST) {
            error("Type error at line " + stnumber + ": 'then' is expected.");
        } else {
            sourceCode.add("\t" + stnumber + ": reserved word: then");
            l.lex();

            stmt_list();

            if (nextToken == ELSE_ST) {
                assambly += PC + ": LDA 7,;(7)\n";
                cu_PC.add(PC);
                PC++;

                else_end.push(PC);
                stnumber++;
                sourceCode.add(stnumber + ": " + lines.get(stnumber - 1));
                sourceCode.add("\t" + stnumber + ": reserved word: else");
                l.lex();
                stmt_list();
            }
            if (nextToken != END_ST) {

                error("Type error at line " + stnumber + ": 'end' is expected.");
            }
            else_end.push(PC);

            stnumber++;
            sourceCode.add(stnumber + ": " + lines.get(stnumber - 1));
            sourceCode.add("\t" + stnumber + ": reserved word: end");

            l.lex();

        }

    }

    public void error(String msg) {
        System.out.println("ERROR: " + msg);
    }
}
