package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl25
 * @date 01/01/2024
 */
public class Or extends AbstractOpBool {
    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }

    @Override
    protected void codeGen(DecacCompiler compiler, int registerNumber) {
        And equivalentAnd = new And(getLeftOperand(), getRightOperand());


    }
}
