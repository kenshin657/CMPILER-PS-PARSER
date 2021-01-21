import javax.print.DocFlavor;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*READ FILE LOCATION*/
        //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader reader0 = null;
        String filePath = null;
        ArrayList<String> lineArray = new ArrayList<>();
        ArrayList<String> lexicals = new ArrayList<>();
        ArrayList<String> lexemes = new ArrayList<>();

        try {
            //filePath = reader.readLine();
            filePath = "src/input.txt";
            reader0 = new BufferedReader(new FileReader(filePath));

            String line = reader0.readLine();
            while (line != null) {
                //System.out.println(line);
                lineArray.add(line);
                line = reader0.readLine();
            }

            //reader.close();
            reader0.close();
        } catch (IOException err) {
            err.printStackTrace();
        }

            //FileWriter fw = new FileWriter("output.txt");
        ArrayList<String> tmp = new ArrayList<String>();
        for (String s : lineArray) {
            tmp.add(s);

            s = s.replaceAll("\\(", "( ");
            s = s.replaceAll("\\)", " )");
            s = s.replaceAll("\\[", "[ ");
            s = s.replaceAll("\\]", " ]");

                //System.out.println(s);

            String[] fileLine = s.split(" ");
            StringBuilder sb = new StringBuilder();
            StringBuilder sb0 = new StringBuilder();
            for (String value : fileLine) {
                Token token = new Token(value);
                //System.out.print(token.lexeme);
                if (value.equals("\n") || value.equals(""))
                    continue;
                sb.append(token.tokenType + " ");
                sb0.append(token.lexeme + " ");
                //fw.write(token.tokenType + " ");
                //System.out.print(token.tokenType + " ");
            }

            lexicals.add(sb.toString());
            lexemes.add(sb0.toString());
                //System.out.println("\n");
                //fw.write("\n");
                //System.out.println("");

            }
        /*for (String value : lexicals) {
            System.out.println(value);
        }*/


        //EVERYTHING BELOW IS FOR THE RECURSIVE DESCENT
        RecursiveDescent rd = new RecursiveDescent();
        HashMap<String, Rule> rules = rd.createRules();

        int count = 1;
        int nLexe = 0;

        try {
            FileWriter fw = new FileWriter("output.txt");

            for (String lex : lexicals) {
                if (lex.equals("")) {
                    nLexe++;
                    fw.write("\n");
                    continue;
                }

                String fileText = lexemes.get(nLexe).trim() + " - " + rd.parse(lex, rules, lexemes.get(nLexe)) + "\n";
                fw.write(fileText);

                //System.out.println(lex);
                //System.out.println("Test Case [" + count + "]: " + rd.parse(lex, rules, lexemes.get(nLexe)));
                nLexe++;
                count++;
            }

            fw.close();
        } catch (IOException err) {
            err.printStackTrace();
        }
        //Token token = new Token(",DMULTU,");
        //System.out.println(token.tokenType);

    }
}
