lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

// Deca lexer rules.
//Ajouter trucs de Loan
READFLOAT: 'readFloat';
PRINT: 'print';
PRINTLN: 'println';
PRINTLNX: 'printlnx';
PRINTX: 'printx';
PROTECTED: 'protected';
RETURN: 'return';
THIS: 'this';
TRUE: 'true';
WHILE: 'while';

//Symboles spéciaux
INF: '<';
SUP: '>';
AFFECT: '=';
PLUS: '+';
MINUS: '-';
TIMES: '*';
DIV: '/';
PCENT: '%';
DOT: '.';
COMMA: ',';
OPARENT: '(';
CPARENT: ')';
OBRACE: '{';
CBRACE: '}';
NOT: '!';
EQ: '==';
SEMI: ';';
NEQ: '!=';
SUPEQ: '>=';
INFEQ: '<=';
AND: '&&';
OR: '||';

//Chaines de caractère
STRING_CAR: ~('"' | '\\' | EOL);   //comment écrire '\' ? Je sais plus
STRING: '"' (STRING_CAR | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING: '"' (STRING_CAR | EOL | '\\"' | '\\\\')* '"';

