package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.commons.lang.Validate;

/**
 * Binary expressions.
 *
 * @author gl25
 * @date 01/01/2024
 */
public abstract class AbstractBinaryExpr extends AbstractExpr {

    public AbstractExpr getLeftOperand() {
        return leftOperand;
    }

    public AbstractExpr getRightOperand() {
        return rightOperand;
    }

    protected void setLeftOperand(AbstractExpr leftOperand) {
        Validate.notNull(leftOperand);
        this.leftOperand = leftOperand;
    }

    protected void setRightOperand(AbstractExpr rightOperand) {
        Validate.notNull(rightOperand);
        this.rightOperand = rightOperand;
    }

    private AbstractExpr leftOperand;
    private AbstractExpr rightOperand;

    public AbstractBinaryExpr(AbstractExpr leftOperand,
            AbstractExpr rightOperand) {
        Validate.notNull(leftOperand, "left operand cannot be null");
        Validate.notNull(rightOperand, "right operand cannot be null");
        Validate.isTrue(leftOperand != rightOperand, "Sharing subtrees is forbidden");
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public void codeGenPrintExpr(DecacCompiler compiler){};




    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        getLeftOperand().decompile(s);
        s.print(" " + getOperatorName() + " ");
        getRightOperand().decompile(s);
        s.print(")");
    }

    abstract protected String getOperatorName();

    @Override
    protected void iterChildren(TreeFunction f) {
        leftOperand.iter(f);
        rightOperand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        leftOperand.prettyPrint(s, prefix, false);
        rightOperand.prettyPrint(s, prefix, true);
    }

    @Override
    protected void codeGen(DecacCompiler compiler, int registerNumber) {
        int nextRegisterNumber = registerNumber + 1;

        GPRegister firstReg = Register.getR(registerNumber);
        GPRegister secondReg = Register.getR(nextRegisterNumber); // TODO: Register Spilling

        if(getLeftOperand() instanceof AbstractLiteral) {
            compiler.addInstruction(new LOAD(((AbstractLiteral) getLeftOperand()).getDValue(), firstReg));
        } else if(getLeftOperand() instanceof Identifier) {
            compiler.addInstruction(new LOAD(((Identifier) getLeftOperand()).getAddress(), firstReg));
        } else {
            getLeftOperand().codeGen(compiler, registerNumber);
        }

        DVal value = secondReg;

        if(getRightOperand() instanceof AbstractLiteral) {
            value = ((AbstractLiteral) getRightOperand()).getDValue();
        } else if(getRightOperand() instanceof Identifier) {
            value = ((Identifier) getLeftOperand()).getAddress();
        } else {
            getRightOperand().codeGen(compiler, nextRegisterNumber);
        }

        compiler.addInstruction(getImaInstruction(value, firstReg));
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        codeGen(compiler, 2);
    }

    public abstract Instruction getImaInstruction(DVal value, GPRegister register);
}
