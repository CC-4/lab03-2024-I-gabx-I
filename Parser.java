// Parser.java

import java.util.LinkedList;
import java.util.Stack;

public class Parser {

    private int next; // Puntero next que apunta al siguiente token
    private Stack<Double> operandos;
    private Stack<Token> operadores;
    private LinkedList<Token> tokens; // LinkedList de tokens

    public boolean parse(LinkedList<Token> tokens) {
        this.tokens = tokens;
        this.next = 0;
        this.operandos = new Stack<Double>();
        this.operadores = new Stack<Token>();

        System.out.println("Aceptada? " + S());

        if(this.next != this.tokens.size()) {
            return false;
        }

        System.out.println("Resultado: " + this.operandos.peek());
        return true;
    }

    private boolean term(int id) {
        if(this.next < this.tokens.size() && this.tokens.get(this.next).equals(id)) {
            
            if (id == Token.NUMBER) {
                operandos.push(this.tokens.get(this.next).getVal());
            } else if (id == Token.SEMI) {
                while (!this.operadores.empty()) {
                    popOp();
                }
            } else {
                pushOp(this.tokens.get(this.next));
            }

            this.next++;
            return true;
        }
        return false;
    }

    private int pre(Token op) {
        switch(op.getId()) {
            case Token.PLUS:
            case Token.MINUS:
                return 1;
            case Token.MULT:
            case Token.DIV:
            case Token.MOD:
                return 2;
            case Token.EXP:
                return 3;
            case Token.UNARY:
                return 4;
            default:
                return -1;
        }
    }

    private void popOp() {
        Token op = this.operadores.pop();

        if (op.equals(Token.UNARY)) {
            double a = this.operandos.pop();
            this.operandos.push(-a);
        } else {
            double b = this.operandos.pop();
            double a = this.operandos.pop();

            switch(op.getId()) {
                case Token.PLUS:
                    this.operandos.push(a + b);
                    break;
                case Token.MINUS:
                    this.operandos.push(a - b);
                    break;
                case Token.MULT:
                    this.operandos.push(a * b);
                    break;
                case Token.DIV:
                    this.operandos.push(a / b);
                    break;
                case Token.MOD:
                    this.operandos.push(a % b);
                    break;
                case Token.EXP:
                    this.operandos.push(Math.pow(a, b));
                    break;
            }
        }
    }

    private void pushOp(Token op) {
        while (!this.operadores.isEmpty() && pre(op) <= pre(this.operadores.peek())) {
            popOp();
        }
        this.operadores.push(op);
    }

    private boolean S() {
        return E() && term(Token.SEMI);
    }

    private boolean E() {
    if (term(Token.NUMBER)) {
        // Procesa operaciones binarias como E + E, E - E, etc.
        while (term(Token.PLUS) || term(Token.MINUS) || term(Token.MULT) || 
               term(Token.DIV) || term(Token.MOD) || term(Token.EXP)) {
            if (!E()) {
                return false; // Si la expresión siguiente no es válida, regresa false
            }
        }
        return true;
    } else if (term(Token.LPAREN)) {
        if (E() && term(Token.RPAREN)) {
            // Procesa operaciones binarias dentro de paréntesis
            while (term(Token.PLUS) || term(Token.MINUS) || term(Token.MULT) || 
                   term(Token.DIV) || term(Token.MOD) || term(Token.EXP)) {
                if (!E()) {
                    return false; // Si la expresión siguiente no es válida, regresa false
                }
            }
            return true;
        }
    } else if (term(Token.MINUS)) {
        // Procesa el operador unario
        if (E()) {
            pushOp(new Token(Token.UNARY));
            return true;
        }
    }
    return false;
}

}
