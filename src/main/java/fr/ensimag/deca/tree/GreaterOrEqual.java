package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BGE;

/**
 * Operator "x >= y"
 * 
 * @author gl25
 * @date 01/01/2024
 */
public class GreaterOrEqual extends AbstractOpIneq {
    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return ">=";
    }

    @Override
    public void compareCondition(DecacCompiler compiler, Label E) {
        compiler.addInstruction(new BGE(E));
    }
}
