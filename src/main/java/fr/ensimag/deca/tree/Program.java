package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.MethodName;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl25
 * @date 01/01/2024
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    
    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verifyProgram Program: start");
        getClasses().verifyListClass(compiler);
//        getClasses().verifyListClassMembers(compiler);
//        getClasses().verifyListClassBody(compiler);
        getMain().verifyMain(compiler);
        LOG.debug("verifyProgram Program: end");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        /*
        // TODO: TSTO with the maximum size of the pile
        // TODO: Error handler
        compiler.addInstruction(new ADDSP(main.getNumGlobalVariables())); // TODO: Add size of method table

        // Create the Method Table
        compiler.addComment("--------------------------------------------------");
        compiler.addComment("      Construction des tables des methodes        ");
        compiler.addComment("--------------------------------------------------");
        compiler.addComment("Construction de la table des methodes de Object");
        compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(1, Register.GB)));
        compiler.addInstruction(new LOAD(new LabelOperand(new Label("code.Object.equals")), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(2, Register.GB)));

        int currentGB = 3;
        HashMap<String, Integer> methodTableOffsetMap = new HashMap<String, Integer>();
        methodTableOffsetMap.put("Object", 1);

        for(AbstractDeclClass declClass : getClasses().getList()) {
            compiler.addComment("Construction de la table des methodes de " + declClass.getName());
            // Inherent the classes of the superclass
            getClasses().updateMethodNames(declClass); // TODO: Check the order
            // Update the address of the method table of the class so the children can access it
            methodTableOffsetMap.put(declClass.getName().toString(), currentGB);

            // Add the method table of the parent
            String superName = declClass.getSuperclass().toString();
            RegisterOffset superOffset = new RegisterOffset(methodTableOffsetMap.get(superName), Register.GB);
            compiler.addInstruction(new LEA(superOffset, Register.R0));
            RegisterOffset currentOffset = new RegisterOffset(currentGB, Register.GB);
            compiler.addInstruction(new STORE(Register.R0, currentOffset));

            // Add each method
            for(MethodName methodName : declClass.getMethodNames()) {
                currentGB++;

                LabelOperand codeLabel = new LabelOperand(methodName.toCodeLabel());
                compiler.addInstruction(new LOAD(codeLabel, Register.R0));
                compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(currentGB, Register.GB)));
            }

            currentGB++;
        }
         */

        // Generate the code of the fields and methods of the classes

        // Create Main Program
        compiler.addComment("Main program");
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());

        // Add The methods

    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
