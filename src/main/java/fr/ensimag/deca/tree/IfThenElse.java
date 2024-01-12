package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Label;
import org.apache.commons.lang.Validate;

/**
 * Full if/else if/else statement.
 *
 * @author gl25
 * @date 01/01/2024
 */
public class IfThenElse extends AbstractInst {
    private final AbstractExpr condition; 
    private final ListInst thenBranch;
    private ListInst elseBranch;

    private static int countIfLabels = 0;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        condition.verifyCondition(compiler, localEnv, currentClass);
        for (AbstractInst instruction : thenBranch.getList()){
            instruction.verifyInst(compiler, localEnv, currentClass, returnType);
        }
        for (AbstractInst instruction : elseBranch.getList()){
            instruction.verifyInst(compiler, localEnv, currentClass, returnType);
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        compiler.addComment("Beginning of IF ELSE");
        // Create code for the condition
        Label ifLabel = new Label("E_Sinon." + countIfLabels);
        Label elseLabel = new Label("E_Sinon." + countIfLabels + 1);
        Label finLabel = new Label("E_Fin." + countIfLabels);
        countIfLabels += 2;




        // Create if branch
        compiler.addLabel(ifLabel);
        thenBranch.codeGenListInst(compiler);

        // Create else branch
        compiler.addLabel(elseLabel);
        elseBranch.codeGenListInst(compiler);

        compiler.addLabel(finLabel);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("if(");
        condition.decompile(s);
        s.println("){");
        s.indent();
        thenBranch.decompile(s);
        s.unindent();
        s.print("} else {");
        s.indent();
        elseBranch.decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
