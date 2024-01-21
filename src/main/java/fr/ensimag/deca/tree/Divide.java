package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.QUO;

/**
 *
 * @author gl25
 * @date 01/01/2024
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "/";
    }

    // TODO: Fix Divide
    @Override
    public void addImaInstruction(DecacCompiler compiler, DVal value, GPRegister register) {
        if(getLeftOperand().getType().isFloat() || getRightOperand().getType().isFloat())
            compiler.addInstruction(new DIV(value, register));
        else
            compiler.addInstruction(new QUO(value, register));
    }
}
