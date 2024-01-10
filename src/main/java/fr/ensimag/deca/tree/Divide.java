package fr.ensimag.deca.tree;


import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
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

    @Override
    public Instruction getImaInstruction(DVal value, GPRegister register) {
        // TODO: Check if it's necessary no make the conversion here
        return new QUO(value, register);
    }
}
