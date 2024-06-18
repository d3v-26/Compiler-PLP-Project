
# Compact Compiler


### Overview
MyCompiler is a simple yet powerful compiler built using Java and ASM (Abstract Syntax Tree). It translates a custom programming language into executable bytecode. This project aims to provide a basic understanding of how compilers work, from lexical analysis to code generation.

### Features
- Lexical Analysis: Tokenizes the input source code.
- Syntax Analysis: Parses tokens into an Abstract Syntax Tree (AST).
- Semantic Analysis: Ensures semantic correctness of the AST.
- Code Generation: Converts the AST into executable bytecode using ASM.
- Error Handling: Provides meaningful error messages for syntax and semantic errors.
## Installation

Clone the repository:

```bash
  git clone https://github.com/d3v-26/Compiler-PLP-Project.git
  cd Compiler-PLP-Project
```

Import the `Compiler-PLP-Project` Folder into Any Java Editor of your choice.

Navigate to `CodeGenTests2.java` file inside `main/src/edu/ufl/cise/plpfa22/` folder.

You can find various test cases on each piece of code being compiled using `compile` function.

You can also define Custom test of the following signature 


```bash
    @DisplayName("Apt Name")
	@Test
	public void customTestFunction(TestInfo testInfo) throws Exception {
		String input = """
				## Your Custom Code
				""";
		
		String shortClassName = "prog";
		String JVMpackageName = "edu/ufl/cise/plpfa22";
		List<GenClass> classes = compile(input, shortClassName, JVMpackageName);		
		Object[] args = new Object[1];  
		String className = "edu.ufl.cise.plpfa22.prog";
		loadClassesAndRunMain(classes, className);
		
	}
```

Run all the test cases following the addition.
## Authors

- [Dev Patel](https://www.github.com/d3v-26)
- [Dharmam Savani](https://github.com/s-dharmam)


## Acknowledgements

 - [ASM Library](https://en.wikipedia.org/wiki/ObjectWeb_ASM) for bytecode manipulation
 - Inspiration from various compiler construction books and resources.
