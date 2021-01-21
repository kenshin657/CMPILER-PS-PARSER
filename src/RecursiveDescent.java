import javax.sound.midi.Soundbank;
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

    public String parse(String input, HashMap<String, Rule> rules, String lexeme){
        //System.out.println("\nLEXEME: " + lexeme);
        //System.out.println("INPUT: " + input);
        Stack<String> stack = new Stack<String>();
        Stack<String> inputStack = new Stack<String>();
        Stack<String> lexemeStack = new Stack<String>();

        historyStack = new Stack<>();
        int lhsCheck = 0;

        //processing input into a stack
        String[] inputArray = input.split(" ");
        String[] lexemeArray = lexeme.split(" ");

        Collections.reverse(Arrays.asList(inputArray));
        Collections.reverse(Arrays.asList(lexemeArray));

        for (String inp : Arrays.asList(inputArray)) {
            inputStack.push(inp);
        }

        //for error Recovery
        for (String lexInput : Arrays.asList(lexemeArray)) {
            lexemeStack.push(lexInput);
        }

        //System.out.println(inputStack.toString());

        String start = rules.get("e").getLHS(); // starting entry point
        stack.push(start);

        //System.out.println(stack.peek());

        //if (inputStack.contains("ERROR"))
        //    return "NOT ACCEPTED";

        while (!stack.isEmpty()) {
            if (inputStack.isEmpty()) {
                if (stack.peek().equals("ep") || stack.peek().equals("tp")) {
                    stack.pop();
                }
                else {
                    return "REJECT. Missing Token '" + stack.peek() + "'";
                }
                continue;
            }

            if (Character.isLowerCase(stack.peek().charAt(0))) {
                if (!stack.peek().equals(historyProdRule))
                    lhsCheck = 0;
                stack = expand(stack, stack.peek(), rules, lhsCheck);
                lhsCheck++;
            }
            else if (Character.isUpperCase(stack.peek().charAt(0))){
                //part wherein you match with current input
                if (inputStack.peek().equals(stack.peek())) {
                    stack.pop();
                    inputStack.pop();
                    lexemeStack.pop();
                    //System.out.println("IT MATCHED");

                }
                else {
                    //do backtracking
                    stack = performBacktrack(stack, historyProdRule, rules);
                    //System.out.println(lhsCheck);
                }

            }
        }

        if (stack.isEmpty() && !inputStack.isEmpty()) {
            return "REJECT. Offending Token '" + lexemeStack.pop() + "'";
        }

        //System.out.println("it exited");

        return "ACCEPT";
    }

    private Stack expand(Stack stack, String production, HashMap<String, Rule> rules, int lhsCheck) {
        if (stack.isEmpty())
            return stack;

        historyProdRule = production;
        stack.pop();

        //System.out.println("I am expanding");
        //algorithm for expanding the stack
        List<String> grammarRHS = rules.get(production).getRHS();

        if (lhsCheck > grammarRHS.size()-1) {
            stack.clear();
            return stack;
        }


        String curRHS = grammarRHS.get(lhsCheck); //replace with history check later

        String[] tmp = curRHS.split(" ");
        if (tmp[0].equals("epsilon")) {
            return stack;
        }
        ptr = tmp.length;
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
        List<String> rhs = rules.get(production).getRHS();

        for (int i = 0; i < ptr; i++) {
            stack.pop();
        }

        ptr = 0;

        stack.push(production);

        //System.out.println(stack);

        return stack;
    }

}
