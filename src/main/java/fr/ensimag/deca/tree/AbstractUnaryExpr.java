package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import org.apache.commons.lang.Validate;

/**
 * Unary expression.
 *
 * @author gl25
 * @date 01/01/2024
 */
public abstract class AbstractUnaryExpr extends AbstractExpr {

    public AbstractExpr getOperand() {
        return operand;
    }
    private AbstractExpr operand;
    public AbstractUnaryExpr(AbstractExpr operand) {
        Validate.notNull(operand);
        this.operand = operand;
    }
    public void codeGenPrintExpr(DecacCompiler compiler){};

    protected abstract String getOperatorName();
  
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        getOperand().decompile(s);
        s.print(")");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        operand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        operand.prettyPrint(s, prefix, true);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        getOperand().codeGenInst(compiler);
        addImaInstruction(compiler, Register.R2, Register.R2);
    }

    public abstract void addImaInstruction(DecacCompiler compiler, DVal value, GPRegister register);

    @Override
    protected void codeGen(DecacCompiler compiler, int registerNumber) {
        getOperand().codeGen(compiler, registerNumber);
        addImaInstruction(compiler, Register.getR(registerNumber), Register.getR(registerNumber));
    }
}
