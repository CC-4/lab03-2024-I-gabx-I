/* 
    Laboratorio No. 3 - Recursive Descent Parsing
    CC4 - Compiladores

    En este archivo ustedes tienen que crear un lexer que sea capaz de reconocer
    los tokens de la siguiente gramática:

    S ::= E;
    E ::= E + E
        | E - E
        | E * E
        | E / E
        | E % E
        | E ^ E
        | - E
        | (E)
        | number

    **** Cosas Importantes ****:

    1. Lo que está en minúscula son terminales
    2. Lo que está en mayúscula son no terminales
    3. number son números double de java que siguen el siguiente formato:

        signo   = [+-]
        digitos = [0-9]
        punto = .
        exponente = [eE]
        (digitos)+((punto)?(digitos)*)?((exponente)(signo)?(digitos)+)?

        donde '+' es uno o más veces
        donde '*' es cero o más veces
        donde '?' es cero o una vez

    4. Cuando hacemos match con un token devolvemos un objeto de tipo Token
        4.1 Ver clase Token
        4.2 Si encuentran un numero el constructor que tienen que usar es:
            Token(id, val)
        4.3 En la clase token hay constantes que definen el id de cada token
*/

import java.io.StringReader;
import java.io.IOException;

%%

%{

    public static void main(String[] args) throws IOException {
        String input = "";
        for(int i=0; i < args.length; i++) {
            input += args[i];
        }
        Lexer lexer = new Lexer(new StringReader(input));
        Token token;
        while((token = lexer.nextToken()) != null) {
            System.out.println(token);
        }
    }

%}

%public
%class Lexer
%function nextToken
%type Token

DIGIT = [0-9]
SIGN = [+-]?
POINT = "."
EXPONENT = [eE][+-]?

DOUBLE = {SIGN}{DIGIT}+({POINT}{DIGIT}*)?({EXPONENT}{DIGIT}+)? 
WHITE = [ \t\r\n]+

%%

<YYINITIAL>"+"          { return new Token(Token.PLUS);   }
<YYINITIAL>"-"          { return new Token(Token.MINUS);  }
<YYINITIAL>"*"          { return new Token(Token.MULT);   }
<YYINITIAL>"/"          { return new Token(Token.DIV);    }
<YYINITIAL>"%"          { return new Token(Token.MOD);    }
<YYINITIAL>"^"          { return new Token(Token.EXP);    }
<YYINITIAL>"("          { return new Token(Token.LPAREN); }
<YYINITIAL>")"          { return new Token(Token.RPAREN); }
<YYINITIAL>";"          { return new Token(Token.SEMI);   }
<YYINITIAL>{DOUBLE}     { return new Token(Token.NUMBER, yytext()); }
<YYINITIAL>{WHITE}      { /* ignorar espacios en blanco */ }
<YYINITIAL>.            { return new Token(Token.ERROR);  }
