package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WINT;


/**
 * @author gl25
 * @date 01/01/2024
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "+";
    }
    
    @Override
    public void codeGenPrintExpr(DecacCompiler compiler) {
        //DVal r2 =
        compiler.addInstruction(new LOAD(3, Register.getR(2)));
        compiler.addInstruction(new LOAD(2, Register.getR(3)));
        compiler.addInstruction(new ADD(Register.getR(3), Register.getR(2)));
        compiler.addInstruction(new LOAD(Register.getR(2), Register.getR(1)));
        compiler.addInstruction(new WINT());
    }

    public Instruction getImaInstruction(DVal value, GPRegister register) {
        return new ADD(value, register);
    }
}
