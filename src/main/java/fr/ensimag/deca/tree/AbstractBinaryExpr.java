package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
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
        GPRegister[][] allRegisters = Register.getUsableRegisters(2, registerNumber);
        pushRegisters(compiler, allRegisters[0]);

        // What a horrible code
        GPRegister firstReg = (allRegisters[1][0].getNumber() == registerNumber) ? allRegisters[1][0] : allRegisters[1][1];
        GPRegister secondReg = (allRegisters[1][1].getNumber() == registerNumber) ? allRegisters[1][0] : allRegisters[1][1];

        if(getLeftOperand() instanceof AbstractLiteral) {
            compiler.addInstruction(new LOAD(((AbstractLiteral) getLeftOperand()).getDValue(), firstReg));
        } else if(getLeftOperand() instanceof Identifier) {
            compiler.addInstruction(new LOAD(((Identifier) getLeftOperand()).getAddress(), firstReg));
        } else {
            getLeftOperand().codeGen(compiler, firstReg.getNumber());
        }

        DVal value = secondReg;

        if(getRightOperand() instanceof AbstractLiteral) {
            value = ((AbstractLiteral) getRightOperand()).getDValue();
        } else if(getRightOperand() instanceof Identifier) {
            value = ((Identifier) getRightOperand()).getAddress();
        } else {
            getRightOperand().codeGen(compiler, secondReg.getNumber());
        }

        addImaInstruction(compiler, value, firstReg);

        popRegisters(compiler, allRegisters[0]);
        //compiler.addComment("end of " + getOperatorName() + " operation");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        codeGen(compiler, 2);
    }

    private void pushRegisters(DecacCompiler compiler, GPRegister[] registers) {
        for (GPRegister register : registers) {
            if(register == null)
                break;
            compiler.addInstruction(new PUSH(register));
        }
    }

    private void popRegisters(DecacCompiler compiler, GPRegister[] registers) {
        for (GPRegister register : registers) {
            if(register == null)
                break;
            compiler.addInstruction(new POP(register));
        }
    }

    @Override
    public List<GPRegister> getRegisters(int registerNumber) {
        List<GPRegister> registers = new LinkedList<GPRegister>(getLeftOperand().getRegisters(registerNumber));
        registers.addAll(getRightOperand().getRegisters(registerNumber + 1));

        return registers.stream().distinct().collect(Collectors.toList());
    }
}
