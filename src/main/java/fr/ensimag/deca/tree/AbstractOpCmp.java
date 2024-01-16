package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import org.apache.log4j.Logger;

/**
 *
 * @author gl25
 * @date 01/01/2024
 */
public abstract class AbstractOpCmp extends AbstractBranchable {
    private static final Logger LOG = Logger.getLogger(AbstractExpr.class);

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verifyExpr AbstractOpCmp : start");
        //On veut vérifier que les deux éléments sont des types Arith
        Type leftType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type rightType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if(!this.areBothArith(leftType, rightType)){
            throw new ContextualError("one operator is not an Arith type in AbstractOpCmp", getLocation());
        }
        this.setType(compiler.environmentType.BOOLEAN);
        if(leftType.sameType((rightType))){
            LOG.debug("verifyExpr AbstractOpCmp : end");
            return compiler.environmentType.BOOLEAN;
        }
        if(leftType.isFloat() || rightType.isFloat()){
            if (leftType.isFloat()){
                ConvFloat conv_float = new ConvFloat(getRightOperand());
                conv_float.verifyExpr(compiler, localEnv, currentClass);
                setRightOperand(conv_float);
            } else {
                ConvFloat conv_float = new ConvFloat(getLeftOperand());
                conv_float.verifyExpr(compiler, localEnv, currentClass);
                setLeftOperand(conv_float);
            }
            LOG.debug("verifyExpr AbstractOpCmp : end");
            return compiler.environmentType.BOOLEAN;
        }
        LOG.debug("verifyExpr AbstractOpCmp : end");
        throw new ContextualError("Type error in AbstractOpCmp", getLocation());
    }

    @Override
    public void codeGenBranch(DecacCompiler compiler) {
        codeGen(compiler, 0);
        compareCondition(compiler, getE());
    }

    @Override
    public Instruction getImaInstruction(DVal value, GPRegister register) {
        return new CMP(value, register);
    }

    // Compare the condition and branch to the label E if true
    public abstract void compareCondition(DecacCompiler compiler, Label E);
}
