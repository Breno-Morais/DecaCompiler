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
public class Or extends AbstractOpBool {
    private static int orCount = 0;
    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }

    @Override
    public void codeGenBranch(DecacCompiler compiler) {
        compiler.addComment("Beginning of OR");

        // TODO: Refactor all of this mess ( NOT can be recursive :| )
        if(getExpectedBool()) {
            // Left Operand
            getLeftOperand().codeGenIfBranch(compiler, true, getE());
            // Right Operand
            getRightOperand().codeGenIfBranch(compiler, true, getE());

        } else {
            Label skipUnevaluated = new Label("OR_Fin." + orCount);
            orCount++;

            // Check first operand
            getLeftOperand().codeGenIfBranch(compiler, true, skipUnevaluated);
            // Check second operand
            getRightOperand().codeGenIfBranch(compiler, false, getE());

            compiler.addLabel(skipUnevaluated);
        }
    }

    @Override
    public void addImaInstruction(DecacCompiler compiler, DVal value, GPRegister register) {
        // Create the label to the code that sets the boolean result
        Label isTrue = new Label("OR_bool." + orCount);
        Label isFalse = new Label("OR_not_bool." + orCount);
        Label end = new Label("OR_bool_fin." + orCount);
        this.setExpectedBool(true);
        this.setE(isTrue);
        orCount++;

        // Generate the code to resolve the operands (They'll branch to isTrue if they are true)
        codeGenBranch(compiler);

        compiler.addInstruction(new BRA(isFalse));

        compiler.addLabel(isTrue);
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), register));
        compiler.addInstruction(new BRA(end));

        compiler.addLabel(isFalse);
        compiler.addInstruction(new LOAD(new ImmediateInteger(0), register));

        compiler.addLabel(end);
    }
}
