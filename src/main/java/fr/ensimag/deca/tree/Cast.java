package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.INT;

import java.util.HashMap;

public class Cast extends AbstractUnaryExpr {
    private AbstractIdentifier type;

    public Cast(AbstractExpr operand, AbstractIdentifier type) {
        super(operand);
        this.type = type;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        type.decompile(s);
        s.print(") (");
        getOperand().decompile(s);
        s.print(")");
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        return null;
    }

    @Override
    protected String getOperatorName() {
        return "cast to " + type.getName();
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        getOperand().codeGenInst(compiler);
        if(this.getType().isInt())
            addImaInstruction(compiler, Register.R2, Register.R2);
    }

    @Override
    protected void codeGen(DecacCompiler compiler, int registerNumber) {
        getOperand().codeGen(compiler, registerNumber);
        if(this.getType().isInt())
            addImaInstruction(compiler, Register.getR(registerNumber), Register.getR(registerNumber));
    }

    @Override
    public void addImaInstruction(DecacCompiler compiler, DVal value, GPRegister register) {
            compiler.addInstruction(new INT(value, register));
    }
}
