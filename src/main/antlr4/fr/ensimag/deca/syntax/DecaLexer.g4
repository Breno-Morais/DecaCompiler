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
ASM: 'asm';
CLASS: 'class';
EXTENDS: 'extends';
ELSE: 'else';
FALSE: 'false';
IF: 'if';
INSTANCEOF: 'instanceof';
NEW: 'new';
NULL: 'null';
READINT: 'readInt';
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

//Identificateurs
LETTER: ( 'a' .. 'z' | 'A'..'Z');
DIGIT: '0' .. '9';
IDENT: (LETTER | '$' | '_')(LETTER | DIGIT | '$' | '_')*;

//Séparateurs
ESPACE: ' ' {skip();};
EOL: '\n';
RETOURCHARIOT: '\r';
TAB: '\t';

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

//Littéraux entiers
POSITIVE_DIGIT: '1' .. '9';
INT: '0' | POSITIVE_DIGIT DIGIT*;

//Littéraux flottants
NUM: DIGIT+;
SIGN: ('+' | '-' |);
EXP: ('E' | 'e') SIGN NUM;
DEC: NUM '.' NUM;
FLOATDEC: (DEC + DEC EXP)('F' | 'f' | );
DIGITHEX: ('0' .. '9') | ('A' .. 'F') | ('a' .. 'f');
NUMHEX: DIGITHEX+;
FLOATHEX: ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f' |);
FLOAT: FLOATDEC | FLOATHEX;

//Chaines de caractère
STRING_CAR: ~('"' | '\n' | '\\');
STRING: '"' (STRING_CAR | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING: '"' (STRING_CAR | EOL | '\\"' | '\\\\')* '"';

//Commentaires
COMMENT: '/*' .*? '*/' {skip();};

//Inclusion de fichier
FILENAME:  (LETTER | DIGIT | '.' | '-' | '_')+;
INCLUDE: '#include' (' ')* '"' FILENAME '"'; //ACTION A RAJOUTERPAGE 55 POLY

