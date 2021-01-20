import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RecursiveDescent {

    private ArrayList<String> grammar = new ArrayList<>();

    public RecursiveDescent() {
        readGrammar();
    }

    private void readGrammar() {
        BufferedReader reader = null;

        try {
            //filePath = reader.readLine();
            String filePath = "src/grammar.txt";
            reader = new BufferedReader(new FileReader(filePath));

            String line = reader.readLine();
            while (line != null) {
                //System.out.println(line);
                this.grammar.add(line);
                line = reader.readLine();
            }

            //reader.close();
            reader.close();
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    private Rule processGrammarRules(String val) {
        Rule rule = new Rule();

        //System.out.println(val);
        String[] prodRule = val.split(":");
        //System.out.println(prodRule[1]);

        rule.setLHS(prodRule[0]);

        if (!prodRule[1].contains("|")) {
            String tmp = prodRule[1].replaceAll(";", "");
            tmp = tmp.trim();
            //System.out.println(tmp);
            rule.addRHS(tmp);
            //System.out.println("New Grammar");
        }
        else {
            String[] prodGrammar = prodRule[1].split("\\|");

            for (String value : prodGrammar) {
                value = value.trim();
                if (value.equals(";"))
                    value = "";
                else {
                    value = value.replaceAll(";", "");
                    value = value.trim();
                }
                rule.addRHS(value);
                //System.out.println(value);
            }
        }


        return rule;
    }

    public HashMap<String, Rule> createRules() {
        HashMap<String , Rule> rules = new HashMap<String, Rule>();

        for (String val : this.grammar) {
            Rule rule;
            rule = processGrammarRules(val);

            rules.put(rule.getLHS(), rule);
        }

        /*for (String prodRule : rules.keySet()) { //FOR CHECKING IF RULES ARE PUT INTO HASHMAP CORRECTLY
            String val = rules.get(prodRule).getRHS().toString();
            System.out.println(prodRule + ": " + val);
        }*/

        return rules;
    }

    public ArrayList<String> getGrammar() {
        return grammar;
    }
}
