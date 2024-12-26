package FOP_Team_Project;
import java.util.*;
public class Main {
    public static void main(String[] args) {
        //Lexer-simple-model;


        enum TokenType {
            NUMBER, IDENTIFIER, ASSIGN, FOR, LPAREN, RPAREN, LBRACE, RBRACE, SEMICOLON, EOF
        }

        class Token {
            TokenType type;
            String value;

            Token(TokenType type, String value) {
                this.type = type;
                this.value = value;
            }
        }

        class Lexer {
            private String text;
            private int pos = 0;
            private char currentChar;

            public Lexer(String text) {
                this.text = text;
                this.currentChar = text.charAt(pos);
            }

            private void error() {
                throw new RuntimeException("Invalid character");
            }

            private void advance() {
                pos++;
                if (pos > text.length() - 1) {
                    currentChar = '\0'; // Indicates end of input
                } else {
                    currentChar = text.charAt(pos);
                }
            }

            private void skipWhitespace() {
                while (pos < text.length() && Character.isWhitespace(currentChar)) {
                    advance();
                }
            }

            public Token getNextToken() {
                while (currentChar != '\0') {
                    if (Character.isWhitespace(currentChar)) {
                        skipWhitespace();
                        continue;
                    }

                    if (Character.isDigit(currentChar)) {
                        return new Token(TokenType.NUMBER, String.valueOf(currentChar));
                    }

                    if (Character.isLetter(currentChar)) {
                        StringBuilder identifier = new StringBuilder();
                        while (Character.isLetter(currentChar)) {
                            identifier.append(currentChar);
                            advance();
                        }
                        String id = identifier.toString();
                        if (id.equals("for")) {
                            return new Token(TokenType.FOR, id);
                        }
                        return new Token(TokenType.IDENTIFIER, id);
                    }

                    if (currentChar == '=') {
                        advance();
                        return new Token(TokenType.ASSIGN, "=");
                    }

                    if (currentChar == ';') {
                        advance();
                        return new Token(TokenType.SEMICOLON, ";");
                    }

                    if (currentChar == '(') {
                        advance();
                        return new Token(TokenType.LPAREN, "(");
                    }

                    if (currentChar == ')') {
                        advance();
                        return new Token(TokenType.RPAREN, ")");
                    }

                    if (currentChar == '{') {
                        advance();
                        return new Token(TokenType.LBRACE, "{");
                    }

                    if (currentChar == '}') {
                        advance();
                        return new Token(TokenType.RBRACE, "}");
                    }

                    error();
                }

                return new Token(TokenType.EOF, null);
            }
        }
        //parser-model
        class AST {
            // Base class for AST nodes
        }

        class AssignNode extends AST {
            String variableName;
            String value;

            AssignNode(String variableName, String value) {
                this.variableName = variableName;
                this.value = value;
            }
        }

        class ForNode extends AST {
            String variableName;
            int start;
            int end;
            List<AST> body;

            ForNode(String variableName, int start, int end, List<AST> body) {
                this.variableName = variableName;
                this.start = start;
                this.end = end;
                this.body = body;
            }
        }
    }
}
