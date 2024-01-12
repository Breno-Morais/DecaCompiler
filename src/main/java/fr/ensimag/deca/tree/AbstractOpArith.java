package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl25
 * @date 01/01/2024
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    public Type new_type(DecacCompiler compiler, Type leftType, Type rightType) throws ContextualError{
        //We want to check that the type is either Int or Float, and if it is neither, we throw an exception
        if(this.isArithType(leftType) || this.isArithType(rightType)){
            throw new ContextualError("The type is neither Int nor Float in AbstractOpArith", getLocation());
        }

        if(leftType.sameType((rightType))){
            return leftType;
        }
        if(leftType.isFloat() || rightType.isFloat()){
            return compiler.environmentType.FLOAT;
        }
        return leftType;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type leftType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type rightType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        return new_type(compiler, leftType, rightType);
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
