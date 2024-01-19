package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl25
 * @date 01/01/2024
 */
public class And extends AbstractOpBool {
    private static int andCount = 0;

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    @Override
    public void codeGenBranch(DecacCompiler compiler) {
        compiler.addComment("Beginning of AND");
        Label skipUnevaluated = new Label("And_Fin." + andCount);
        andCount++;

        // TODO: Refactor all of this mess
        if(getExpectedBool()) {
            // Check first operand
            getLeftOperand().codeGenIfBranch(compiler, false, skipUnevaluated);
            // Check second operand
            getRightOperand().codeGenIfBranch(compiler, true, getE(), skipUnevaluated);
        } else {
            // Check first operand
            getLeftOperand().codeGenIfBranch(compiler, false, getE());
            // Check second operand
            getRightOperand().codeGenIfBranch(compiler, false, getE());
        }

        compiler.addLabel(skipUnevaluated);
    }

    @Override
    public void addImaInstruction(DecacCompiler compiler, DVal value, GPRegister register) {
        // Create the label to the code that sets the boolean result
        Label isTrue = new Label("AND_bool." + andCount);
        Label isFalse = new Label("AND_not_bool." + andCount);
        Label end = new Label("AND_bool_fin." + andCount);
        this.setExpectedBool(false);
        this.setE(isFalse);
        andCount++;

        // Generate the code to resolve the operands (They'll branch to isFalse if they are false)
        codeGenBranch(compiler);

        compiler.addInstruction(new BRA(isTrue));

        compiler.addLabel(isTrue);
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), register));
        compiler.addInstruction(new BRA(end));

        compiler.addLabel(isFalse);
        compiler.addInstruction(new LOAD(new ImmediateInteger(0), register));

        compiler.addLabel(end);
    }
}

