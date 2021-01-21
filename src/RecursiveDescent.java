import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class RecursiveDescent {

    private ArrayList<String> grammar;
    private Stack<String> historyStack;
    private String historyProdRule;
    private int ptr;

    public RecursiveDescent() {
        this.ptr = 0;
        this.grammar = new ArrayList<String>();
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

        rule.setLHS(prodRule[0].trim());

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
                    value = "epsilon";
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
            //System.out.println(rules.get(prodRule).getLHS());
             System.out.println(prodRule + ": " + val);
        }*/

        return rules;
    }

    public String parse(String input, HashMap<String, Rule> rules){
        Stack<String> stack = new Stack<String>();
        Stack<String> inputStack = new Stack<String>();
        historyStack = new Stack<>();

        //processing input into a stack
        String[] inputArray = input.split(" ");
        Collections.reverse(Arrays.asList(inputArray));
        for (String inp : Arrays.asList(inputArray)) {
            inputStack.push(inp);
        }

        //System.out.println(inputStack.toString());

        String start = rules.get("e").getLHS();
        stack.push(start);

        //System.out.println(stack.peek());

        while (!stack.isEmpty()) {
            if (Character.isLowerCase(stack.peek().charAt(0))) {
                stack = expand(stack, stack.peek(), rules);
            }
            else if (Character.isUpperCase(stack.peek().charAt(0))){
                //part wherein you match with current input
                if (inputStack.peek().equals(stack.peek())) {

                }
                else {
                    //do backtracking
                    stack = performBacktrack(stack, historyProdRule, rules);
                    break;
                }

            }
        }

        //System.out.println("it exited");

        return "";
    }

    private Stack expand(Stack stack, String production, HashMap<String, Rule> rules) {
        if (stack.isEmpty())
            return stack;

        stack.pop();

        //System.out.println("I am expanding");
        //algorithm for expanding the stack
        List<String> grammarRHS = rules.get(production).getRHS();
        String curRHS = grammarRHS.get(0); //replace with history check later

        String[] tmp = curRHS.split(" ");
        Collections.reverse(Arrays.asList(tmp));

        for (String arr : Arrays.asList(tmp)) {
            stack.push(arr);
            historyStack.push(arr);
        }

        //System.out.println(stack);

        return stack;
    }

    private Stack performBacktrack(Stack stack, String production, HashMap<String, Rule> rules) {

        //removing previous rules from the stack




        return stack;
    }

}
