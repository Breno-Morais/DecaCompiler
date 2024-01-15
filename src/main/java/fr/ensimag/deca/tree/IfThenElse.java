package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Full if/else if/else statement.
 *
 * @author gl25
 * @date 01/01/2024
 */
public class IfThenElse extends AbstractInst {
    private static final Logger LOG = Logger.getLogger(Identifier.class);
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
        LOG.debug("verifyInst IfThenElse : start");
        condition.verifyCondition(compiler, localEnv, currentClass);
        for (AbstractInst instruction : thenBranch.getList()){
            LOG.debug("for de then");
            instruction.verifyInst(compiler, localEnv, currentClass, returnType);
        }
        for (AbstractInst instruction : elseBranch.getList()){
            LOG.debug("for de else");
            instruction.verifyInst(compiler, localEnv, currentClass, returnType);
        }
        LOG.debug("verifyInst IfThenElse : end");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        compiler.addComment("Beginning of IF ELSE");
        // Create code for the condition
        Label ifLabel = new Label("E_Sinon." + countIfLabels);
        Label elseLabel = new Label("E_Sinon." + countIfLabels + 1);
        Label finLabel = new Label("E_Fin." + countIfLabels);
        countIfLabels += 2;

        if(condition instanceof BooleanLiteral) {
            BooleanLiteral conditionValue = (BooleanLiteral) condition;
            if (conditionValue.getValue())
                compiler.addInstruction(new BRA(ifLabel));

        } else if (condition instanceof Not) {
            if (((Not) condition).getOperand() instanceof BooleanLiteral) {
                BooleanLiteral conditionValue = (BooleanLiteral) ((Not) condition).getOperand();
                if (!conditionValue.getValue())
                    compiler.addInstruction(new BRA(ifLabel));

            } else if (((Not) condition).getOperand() instanceof AbstractBranchable) {
                AbstractBranchable conditionBranchable = (AbstractBranchable) condition;
                conditionBranchable.setE(ifLabel);
                conditionBranchable.setExpectedBool(false);
                conditionBranchable.codeGenBranch(compiler);

            } else if (((Not) condition).getOperand() instanceof AbstractIdentifier) {
                AbstractIdentifier conditionIdentifier = (AbstractIdentifier) condition;

                conditionIdentifier.codeGen(compiler, 0);
                compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.R0));
                compiler.addInstruction(new BEQ(ifLabel));
            }
        } else {
            if (condition instanceof AbstractIdentifier) {
                AbstractIdentifier conditionIdentifier = (AbstractIdentifier) condition;

                conditionIdentifier.codeGen(compiler, 0);
                compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.R0));
                compiler.addInstruction(new BNE(ifLabel));
            } else if (condition instanceof AbstractBranchable) {
                AbstractBranchable conditionBranchable = (AbstractBranchable) condition;
                conditionBranchable.setE(ifLabel);
                conditionBranchable.setExpectedBool(true);
                conditionBranchable.codeGenBranch(compiler);
            }

            compiler.addInstruction(new BRA(elseLabel));
        }

        // Create if branch
        compiler.addLabel(ifLabel);
        thenBranch.codeGenListInst(compiler);
        compiler.addInstruction(new BRA(finLabel));

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
        if (elseBranch.isEmpty()){
            s.print("}");
        } else {
            s.println("} else {");
            s.indent();
            elseBranch.decompile(s);
            s.unindent();
            s.print("}");
        }

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
