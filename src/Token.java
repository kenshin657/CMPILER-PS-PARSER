public class Token {
    enum TokenType{
        DIGIT,
        PLUS,
        MULT,
        LBRAK,
        RBRAK,
        LPAREN,
        RPAREN,
        ERROR,
    }

    public TokenType tokenType;
    public String lexeme;

    public Token(String word) {
        this.tokenType = identifyToken(word);
        this.lexeme = word;
    }

    private static TokenType identifyToken(String word) {
        int state = 0; //STARTING STATE OF DFA. THE DEAD STATE WOULD BE 999
        char[] ch = word.toCharArray();

        for (char c : ch) {
            switch (c) {
                case '0':
                    if (state == 0)
                        state = 1; // STATE 1 IS A FINAL STATE FOR DIGITS
                    else if (state == 2)
                        state = 2; // STATE 2 IS ALSO A FINAL STATE FOR DIGITS
                    else
                        state = 999;
                    break;
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    if (state == 0)
                        state = 2;
                    else if (state == 2)
                        state = 2;
                    else
                        state = 999;
                    break;
                case '+':
                    if (state == 0)
                        state = 3; // FINAL STATE FOR PLUS
                    else
                        state = 999;
                    break;
                case '*':
                    if (state == 0)
                        state = 4; // FINAL STATE FOR MULT
                    else
                        state = 999;
                    break;
                case '(':
                    if (state == 0)
                        state = 5; // FINAL STATE FOR LPAREN
                    else
                        state = 999;
                    break;
                case ')':
                    if (state == 0)
                        state = 6; // FINAL STATE FOR RPAREN
                    else
                        state = 999;
                    break;
                case '[':
                    if (state == 0)
                        state = 7; // FINAL STATE FOR LBRAK
                    else
                        state = 999;
                    break;
                case ']':
                    if (state == 0)
                        state = 8; // FINAL STATE FOR RBRAK
                    else
                        state = 999;
                    break;
                default:
                    state = 999; // DEAD STATE
                    break;

            }
        }

        if (state == 1 || state == 2)
            return TokenType.DIGIT;
        else if (state == 3)
            return TokenType.PLUS;
        else if (state == 4)
            return TokenType.MULT;
        else if (state == 5)
            return TokenType.LPAREN;
        else if (state == 6)
            return TokenType.RPAREN;
        else if (state == 7)
            return TokenType.LBRAK;
        else if (state == 8)
            return TokenType.RBRAK;
        else
            return TokenType.ERROR;
    }
}

