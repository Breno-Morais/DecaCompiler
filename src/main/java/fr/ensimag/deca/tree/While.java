package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import org.apache.commons.lang.Validate;

/**
 *
 * @author gl25
 * @date 01/01/2024
 */
public class While extends AbstractInst {
    private static int whileCount = 0;

    private AbstractExpr condition;
    private ListInst body;

    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getBody() {
        return body;
    }

    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label startCodeLabel = new Label("E_Debut." + whileCount);
        Label condLabel = new Label("E_Cond." + whileCount);
        whileCount++;

        compiler.addInstruction(new BRA(condLabel));

        compiler.addLabel(startCodeLabel);
        body.codeGenListInst(compiler);

        compiler.addLabel(condLabel);
        if(condition instanceof BooleanLiteral) {
            BooleanLiteral conditionValue = (BooleanLiteral) condition;
            if (conditionValue.getValue())
                compiler.addInstruction(new BRA(startCodeLabel));

        } else if (condition instanceof AbstractIdentifier) {
            AbstractIdentifier conditionIdentifier = (AbstractIdentifier) condition;

            conditionIdentifier.codeGen(compiler, 0);
            compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.R0));
            compiler.addInstruction(new BNE(startCodeLabel));
        } else if (condition instanceof AbstractBranchable) {
            AbstractBranchable conditionBranchable = (AbstractBranchable) condition;
            conditionBranchable.setE(startCodeLabel);
            conditionBranchable.setExpectedBool(true);
            conditionBranchable.codeGenBranch(compiler);
        } else if (condition instanceof Not) {
            if (((Not) condition).getOperand() instanceof BooleanLiteral) {
                BooleanLiteral conditionValue = (BooleanLiteral) ((Not) condition).getOperand();
                if (!conditionValue.getValue())
                    compiler.addInstruction(new BRA(startCodeLabel));

            } else if (((Not) condition).getOperand() instanceof AbstractBranchable) {
                AbstractBranchable conditionBranchable = (AbstractBranchable) condition;
                conditionBranchable.setE(startCodeLabel);
                conditionBranchable.setExpectedBool(false);
                conditionBranchable.codeGenBranch(compiler);

            } else if (((Not) condition).getOperand() instanceof AbstractIdentifier) {
                AbstractIdentifier conditionIdentifier = (AbstractIdentifier) condition;

                conditionIdentifier.codeGen(compiler, 0);
                compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.R0));
                compiler.addInstruction(new BEQ(startCodeLabel));
            }
        }
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        condition.verifyExpr(compiler, localEnv, currentClass);
        for(AbstractInst instruction : body.getList()){
            instruction.verifyInst(compiler, localEnv, currentClass, returnType);
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("while (");
        getCondition().decompile(s);
        s.println(") {");
        s.indent();
        getBody().decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }
}
