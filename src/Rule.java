import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Rule {
    private String LHS;
    private ArrayList<String> RHS;

    public Rule() {
        this.RHS = new ArrayList<String>();
    }

    public String getLHS() {
        return LHS;
    }

    public void setLHS(String LHS) {
        this.LHS = LHS;
    }

    public List<String> getRHS() {
        return RHS;
    }

    public void setRHS(ArrayList<String> RHS) {
        this.RHS = RHS;
    }

    public void addRHS(String RHS) {
        this.RHS.add(RHS);
    }
}
