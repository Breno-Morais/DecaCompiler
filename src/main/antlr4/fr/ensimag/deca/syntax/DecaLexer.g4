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
LETTER: ( 'a' .. 'z' | 'A'..'Z');
DIGIT: '0' .. '9';
IDENT: (LETTER | '$' | '_')(LETTER | DIGIT | '$' | '_')*;
POSITIVE_DIGIT: '1' .. '9';
INT: '0' | POSITIVE_DIGIT DIGIT*;
NUM: DIGIT+;
SIGN: '+' | '-' | ;
EXP: ('E' | 'e') SIGN NUM;
DEC: NUM '.' NUM;
FLOATDEC: (DEC + DEC EXP)('F' | 'f' | );
DIGITHEX: ('0' .. '9') | ('A' .. 'F') | ('a' .. 'f');
NUMHEX: DIGITHEX+;
FLOATHEX: ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f' | );
FLOAT: FLOATDEC | FLOATHEX;
COMMENT: '/*' .*? '*/' {skip();};
ESPACE: ' ' {skip();};
EOL: '\n';
RETOURCHARIOT: '\r';
TAB: '\t';
FILENAME:  (LETTER | DIGIT | '.' | '-' | '_')+;
INCLUDE: '#include' (' ')* '"' FILENAME '"'; //ACTION A RAJOUTERPAGE 55 POLY
//DUMMY_TOKEN: .; // A FAIRE : Règle bidon qui reconnait tous les caractères.
                // A FAIRE : Il faut la supprimer et la remplacer par les vraies règles.
