package compiler.codegen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static compiler.def.else_end;
import static compiler.def.cu_PC;

public class Codegen {

    BufferedWriter bw;
    File file;
    public String[] R;

    public Codegen() {
        bw = null;
        file = new File("assamblyCode.tm");
        R = new String[7];

    }

    public int getFirstEmptyReg() {
        for (int i = 0; i < 7; i++) {
            if (R[i] == null) {
                return i;
            }
        }
        return 6;
    }

    public void write(String text) {
        try {

            bw = new BufferedWriter(new FileWriter(file));
            text = reviewForJumping(text);
            bw.write(text);
            System.out.println(text);
            bw.newLine();
        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (bw != null) {
                    bw.close();
                }

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }

    }

    public String reviewForJumping(String text) {
        String[] t = text.split(";");

        String newText = "";
        for (int i = 0; i < t.length - 1; i++) {
            newText += t[i] + (else_end.get(i) - cu_PC.get(i) - 1);
        }
        newText += t[t.length - 1];
        return newText;
    }
}
