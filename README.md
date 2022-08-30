Phases of the Compiler
======================

Lexer
----------------
* Converts Stream of Characters to Stream of Tokens

Parser
------
* Converts Stream of Tokens to AST (Abstract Syntax Tree)

Semantic Analysis
-----------------
* Type Checking
* Analyze AST

Code Generation
---------------
* Traverse AST to generate code in target language, Java Byte Code
