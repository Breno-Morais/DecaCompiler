package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.log4j.Logger;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl25
 * @date 01/01/2024
 */
public class Assign extends AbstractBinaryExpr {
    private static final Logger LOG = Logger.getLogger(AbstractExpr.class);
    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verifyExpr Assign : start");
        Type leftOp = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        AbstractExpr expr = getRightOperand().verifyRValue(compiler, localEnv, currentClass, leftOp);
        LOG.debug("verifyExpr Assign : end");
        setType(expr.getType());
        return expr.getType();
    }

    @Override
    protected String getOperatorName() {
        return "=";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        AbstractIdentifier leftOperandId = (AbstractIdentifier) getLeftOperand();
        DAddr leftAddress = leftOperandId.getExpDefinition().getOperand();

        if(getLeftOperand() instanceof Identifier) {
            getRightOperand().codeGen(compiler, 2);
            compiler.addInstruction(new STORE(Register.R2, leftAddress));

        } else if(getLeftOperand() instanceof Selection) {
            compiler.addInstruction(new LOAD(leftOperandId.getAddress(), Register.R2));
            /* TODO: Error Handler
            compiler.addInstruction(new CMP(new NullOperand(), ));
            compiler.addInstruction(new BEQ(new Label("dereferencement_null")));
             */

            getRightOperand().codeGen(compiler, 3);
            compiler.addInstruction(new STORE(Register.R3, new RegisterOffset(leftOperandId.getFieldDefinition().getIndex(), Register.R2)));
        } else
            throw new DecacInternalError("Invalid left operand of assign operation");
    }

    @Override
    public void addImaInstruction(DecacCompiler compiler, DVal value, GPRegister register) {
        throw new UnsupportedOperationException("Not applicable");
    }

    public void decompile(IndentPrintStream s) {
        getLeftOperand().decompile(s);
        s.print(" " + getOperatorName() + " ");
        getRightOperand().decompile(s);
    }
}
