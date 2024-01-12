package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.STORE;

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
    protected void codeGen(DecacCompiler compiler, int registerNumber) {
        if(getExpectedBool()) {
            Label skipUnevaluated = new Label("E_Fin." + andCount);
            andCount++;

            // TODO: Add boolean literal treatment
            try {
                AbstractBranchable leftBranchable = (AbstractBranchable) getLeftOperand();
                leftBranchable.setE(skipUnevaluated);
                leftBranchable.setExpectedBool(false);
                leftBranchable.codeGen(compiler, 0);
            } catch (ClassCastException e) {
                throw new DecacInternalError("Left Operand not a boolean " + e);
            }

            try {
                AbstractBranchable rightBranchable = (AbstractBranchable) getRightOperand();
                rightBranchable.setE(getE());
                rightBranchable.setExpectedBool(true);
                rightBranchable.codeGen(compiler, 0);
            } catch (ClassCastException e) {
                throw new DecacInternalError("Right Operand not a boolean " + e);
            }

            compiler.addLabel(skipUnevaluated);
        } else {
            // TODO: Add boolean literal treatment
            try {
                AbstractBranchable leftBranchable = (AbstractBranchable) getLeftOperand();
                leftBranchable.setE(getE());
                leftBranchable.setExpectedBool(false);
                leftBranchable.codeGen(compiler, 0);
            } catch (ClassCastException e) {
                throw new DecacInternalError("Left Operand not a boolean " + e);
            }

            try {
                AbstractBranchable rightBranchable = (AbstractBranchable) getRightOperand();
                rightBranchable.setE(getE());
                rightBranchable.setExpectedBool(false);
                rightBranchable.codeGen(compiler, 0);
            } catch (ClassCastException e) {
                throw new DecacInternalError("Right Operand not a boolean " + e);
            }

        }
    }
}
