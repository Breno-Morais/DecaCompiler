package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;

public abstract class AbstractBranchable extends AbstractBinaryExpr {
    private Label E; // Label for branching if true
    private boolean expectedBool; // Expected value for the expression to branch

    public AbstractBranchable(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    public Label getE() {
        return E;
    }

    public void setE(Label e) {
        E = e;
    }

    public boolean getExpectedBool() {
        return expectedBool;
    }

    public void setExpectedBool(boolean expectedBool) {
        this.expectedBool = expectedBool;
    }

    public abstract void codeGenBranch(DecacCompiler compiler);

    @Override
    public void codeGenIfBranch(DecacCompiler compiler, boolean expected, Label ifLabel, Label elseLabel) {
        codeGenIfBranch(compiler, expected, ifLabel);
        if(expected)
                compiler.addInstruction(new BRA(elseLabel));
    }

    @Override
    public void codeGenIfBranch(DecacCompiler compiler, boolean expected, Label ifLabel) {
        setE(ifLabel);
        setExpectedBool(expected);
        codeGenBranch(compiler);
    }
}
