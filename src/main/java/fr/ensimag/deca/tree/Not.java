package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.log4j.Logger;

/**
 *
 * @author gl25
 * @date 01/01/2024
 */
public class Not extends AbstractUnaryExpr {
    private static final Logger LOG = Logger.getLogger(Identifier.class);
    public Not(AbstractExpr operand) {
        super(operand);
    }
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verifyExpr Not : start");
        Type type = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        if(!type.isBoolean())
            throw new ContextualError("a BOOL was expected in Not", getLocation());
        this.setType(type);
        LOG.debug("verifyExpr Not : end");
        return type;
    }

    @Override
    protected String getOperatorName() {
        return "!";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(getOperatorName());
        getOperand().decompile(s);
    }

    @Override
    protected void codeGen(DecacCompiler compiler, int registerNumber) {
    }

    @Override
    public void codeGenIfBranch(DecacCompiler compiler, boolean expected, Label ifLabel, Label elseLabel) {
        getOperand().codeGenIfBranch(compiler, !expected, ifLabel, elseLabel);
    }
}
