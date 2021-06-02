import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Lexer {
    private final String s;
    Map<String, TokenType> keywords = new HashMap<>();
    private int line;
    private int pos;
    private int position;
    private char chr;

    Lexer(String source) {
        this.line = 1;
        this.pos = 0;
        this.position = 0;
        this.s = source;
        this.chr = this.s.charAt(0);

        this.keywords.put("Divisio", TokenType.Class);
        this.keywords.put("InferedFrom", TokenType.Inheritance);
        this.keywords.put("WhetherDo", TokenType.Condition);
        this.keywords.put("Else", TokenType.Condition);
        this.keywords.put("Ire", TokenType.Integer);
        this.keywords.put("Sire", TokenType.SInteger);
        this.keywords.put("Clo", TokenType.Character);
        this.keywords.put("SetOfClo", TokenType.String);
        this.keywords.put("FBU", TokenType.Float);
        this.keywords.put("SFBU", TokenType.SFloat);
        this.keywords.put("NoneValue", TokenType.Void);
        this.keywords.put("TerminateThisNow", TokenType.Break);
        this.keywords.put("RingWhen", TokenType.Loop);
        this.keywords.put("BackedValue", TokenType.Return);
        this.keywords.put("STT", TokenType.Struct);
        this.keywords.put("Check", TokenType.Switch);
        this.keywords.put("CaseOf", TokenType.Switch);
        this.keywords.put("Using", TokenType.Inclusion);
        this.keywords.put("Beginning", TokenType.Start_Statement);
        this.keywords.put("End", TokenType.End_Statement);

    }

    static void error(int line, int pos, String msg) {
        if (line > 0 && pos > 0) {
            System.out.printf("%s in line %d, pos %d\n", msg, line, pos);
        } else {
            System.out.println(msg);
        }
        System.exit(1);
    }

//    public static void main(String[] args) throws FileNotFoundException {
//
//        Scanner s = new Scanner(new File("src/input.txt"));
//        StringBuilder source = new StringBuilder(" ");
//        while (s.hasNext()) {
//            source.append(s.nextLine()).append("\n");
//        }
//        Lexer l = new Lexer(source.toString());
//        l.printTokens();
//    }

    Token follow(char expect, TokenType ifYes, TokenType ifNo, int line, int pos) {
        if (getNextChar() == expect) {
            getNextChar();
            return new Token(ifYes, "", line, pos);
        }
        if (ifNo == TokenType.End_of_input) {
            error(line, pos, String.format("follow: unrecognized character: (%d) '%c'", (int) this.chr, this.chr));
        }
        return new Token(ifNo, "", line, pos);
    }


    Token string_lit(char start, int line, int pos) {
        StringBuilder result = new StringBuilder();
        while (getNextChar() != start) {
            if (this.chr == '\u0000') {
                error(line, pos, "EOF while scanning string literal");
            }
            if (this.chr == '\n') {
                error(line, pos, "EOL while scanning string literal");
            }
            result.append(this.chr);
        }
        getNextChar();
        return new Token(TokenType.String, result.toString(), line, pos);
    }


    Token div_or_comment(int line, int pos) {
        getNextChar();
        if (this.chr == '#') {
            getNextChar();
            while (true) {
                if (this.chr == '\u0000') {
                    error(line, pos, "EOF in comment");
                } else if (this.chr == '#') {
                    if (getNextChar() == '/') {
                        getNextChar();
                        return new Token(TokenType.Comment, "/##/", line, pos);
                    }
                } else {
                    getNextChar();
                }
            }
        } else if (this.chr == '-') {
            getNextChar();
            while (true) {
                if (this.chr == '\n') {
                    return new Token(TokenType.Comment, "/-", line, pos);
                } else {
                    getNextChar();
                }
            }
        } else {
            return new Token(TokenType.Arithmetic_Operation, "/", line, pos);
        }


    }

    Token identifier_or_integer(int line, int pos) {
        boolean is_number = true;
        StringBuilder text = new StringBuilder();

        while (Character.isAlphabetic(this.chr) || Character.isDigit(this.chr) || this.chr == '_') {
            text.append(this.chr);
            if (!Character.isDigit(this.chr)) {
                is_number = false;
            }
            getNextChar();
        }

        if (text.toString().equals("")) {
            error(line, pos, String.format("identifier_or_integer unrecognized character: (%d) %c", (int) this.chr, this.chr));
        }

        if (Character.isDigit(text.charAt(0))) {
            if (!is_number) {
                error(line, pos, String.format("invalid number: %s", text.toString()));
            }
            return new Token(TokenType.Constant, text.toString(), line, pos);
        }

        if (this.keywords.containsKey(text.toString())) {
            return new Token(this.keywords.get(text.toString()), text.toString(), line, pos);
        }
        return new Token(TokenType.Identifier, text.toString(), line, pos);
    }


    Token getToken() {
        int line, pos;
        while (Character.isWhitespace(this.chr)) {
            getNextChar();
        }
        line = this.line;
        pos = this.pos;

        switch (this.chr) {
            case '\u0000':
                return new Token(TokenType.End_of_input, "", this.line, this.pos);
            case '/':
                return div_or_comment(line, pos);
            case '<':
                return follow('=', TokenType.Relational_Operators, TokenType.Op_less, line, pos);
            case '>':
                return follow('=', TokenType.Relational_Operators, TokenType.Op_greater, line, pos);
            case '=':
                return follow('=', TokenType.Relational_Operators, TokenType.Assignment_operator, line, pos);
            case '!':
                return follow('=', TokenType.Op_notequal, TokenType.End_of_input, line, pos);
            case '&':
                return follow('&', TokenType.Logic_operators, TokenType.End_of_input, line, pos);
            case '|':
                return follow('|', TokenType.Logic_operators, TokenType.End_of_input, line, pos);
            case '~':
                getNextChar();
                return new Token(TokenType.Logic_operators, "~", line, pos);
            // access or point
            case '.':
                getNextChar();
                return new Token(TokenType.Access_Operator, ".", line, pos);
            case '"':
                return string_lit(this.chr, line, pos);
            case '{':
                getNextChar();
                return new Token(TokenType.LeftBrace, "{", line, pos);
            case '}':
                getNextChar();
                return new Token(TokenType.RightBrace, "}", line, pos);
            case '(':
                getNextChar();
                return new Token(TokenType.LeftParen, "(", line, pos);
            case ')':
                getNextChar();
                return new Token(TokenType.RightParen, ")", line, pos);
            case '+':
                getNextChar();
                return new Token(TokenType.Arithmetic_Operation, "+", line, pos);
            case '-':
                getNextChar();
                return new Token(TokenType.Arithmetic_Operation, "-", line, pos);
            case '*':
                getNextChar();
                return new Token(TokenType.Arithmetic_Operation, "*", line, pos);
            case '%':
                getNextChar();
                return new Token(TokenType.Arithmetic_Operation, "%", line, pos);
            case ';':
                getNextChar();
                return new Token(TokenType.Delimiter, ";", line, pos);
            case '@':
                getNextChar();
                return new Token(TokenType.Delimiter, "@", line, pos);
            case ',':
                getNextChar();
                return new Token(TokenType.Comma, ",", line, pos);

            default:
                return identifier_or_integer(line, pos);
        }
    }

    char getNextChar() {
        this.pos++;
        this.position++;
        if (this.position >= this.s.length()) {
            this.chr = '\u0000';
            return this.chr;
        }
        this.chr = this.s.charAt(this.position);
        if (this.chr == '\n') {
            this.line++;
            this.pos = 0;
        }
        return this.chr;
    }

    ArrayList<Token> printTokens() {
        Token t;
        ArrayList<Token> res = new ArrayList<>();
        while ((t = getToken()).tokentype != TokenType.End_of_input) {
            res.add(t);
        }
        res.add(t);
        return res;
    }

    enum TokenType {
        End_of_input, Arithmetic_Operation,
        Logic_operators, Relational_Operators, Op_less, Op_greater, Op_notequal, Assignment_operator, Class,
        Inheritance, Condition, SInteger, Character, Float, SFloat, Void, Break, Loop, Return, Struct, Switch,
        Start_Statement, End_Statement, Inclusion, Integer, LeftParen, RightParen,
        LeftBrace, RightBrace, Delimiter, Comma, Identifier, String, Access_Operator, Comment, Constant
    }

    static class Token {
        public TokenType tokentype;
        public String lexeme;
        public int line;
        public int pos;

        Token(TokenType token, String Lexeme, int line, int pos) {
            this.tokentype = token;
            this.lexeme = Lexeme;
            this.line = line;
            this.pos = pos;
        }

        @Override
        public String toString() {
            String result = String.format("%5d  %5d %-15s", this.line, this.pos, this.tokentype);
            if (this.tokentype == TokenType.String)
                result += String.format(" \"%s\"", lexeme);
            else
                result += String.format(" %s", lexeme);

            return result;
        }
    }

}