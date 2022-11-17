/**  This code is provided for solely for use of students in the course COP5556 Programming Language Principles at the 
 * University of Florida during the Fall Semester 2022 as part of the course project.  No other use is authorized. 
 */

package edu.ufl.cise.plpfa22;

/**
 * This class contains several static methods useful when developing and testing
 * the code generation part of our compiler.
 * 
 */
import java.io.PrintWriter;
import java.io.StringWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.TraceClassVisitor;

import edu.ufl.cise.plpfa22.ast.Types.Type;

public class CodeGenUtils{

	/**
	 * Converts the provided byte array
	 * in a human readable format and returns as a String.
	 * 
	 * @param bytecode
	 */
	public static String bytecodeToString(byte[] bytecode) {
		int flags = ClassReader.SKIP_DEBUG;
		ClassReader cr;
		cr = new ClassReader(bytecode);
		StringWriter out = new StringWriter();
		cr.accept(new TraceClassVisitor(new PrintWriter(out)), flags);
		return out.toString();
	}
	
	/**
	 * Loader for dynamically generated classes.
	 * Instantiated by getInstance.
	 *
	 */
	public static class DynamicClassLoader extends ClassLoader {
		public DynamicClassLoader(ClassLoader parent) {
			super(parent);
		}

		public Class<?> define(String className, byte[] bytecode) {
			return super.defineClass(className, bytecode, 0, bytecode.length);
		}
	};
	
	/**
	 * Use for debugging only.
	 * Generates code to print the given String followed by ; to the standard output to allow observation of execution of generated program
	 * during development.
	 * 
	 * @param mv
	 * @param message
	 */
	public static void genDebugPrint(MethodVisitor mv, String message) {
		mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn(message + ";");
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);
	}

	/**
	 * Use for debugging only
	 * Generates code to print the value on top of the stack to the standard output without consuming it.
	 * Requires stack not empty
     *
	 * @param mv
	 * @param type
	 */
	public static void genDebugPrintTOS(MethodVisitor mv, Type type) {
			mv.visitInsn(Opcodes.DUP);
			mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			mv.visitInsn(Opcodes.SWAP);
			if (type.equals(Type.NUMBER)) {
				mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(I)V", false);
			}	
			else if (type.equals(Type.BOOLEAN)) {
				mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Z)V", false);
			}
			else if (type.equals(Type.STRING)) {
				mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);
			}
			else
			mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			mv.visitLdcInsn(";\n");
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);				
	}
}
