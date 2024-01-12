package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import org.apache.log4j.Logger;

/**
 *
 * @author gl25
 * @date 01/01/2024
 */
public class Modulo extends AbstractOpArith {
    private static final Logger LOG = Logger.getLogger(Identifier.class);

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verifyExpr Modulo : start");
        Type leftType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type rightType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        //both types must be INT
        if(leftType.isInt() && rightType.isInt()) {
            this.setType(leftType);
            LOG.debug("verifyExpr Modulo : end");
            return leftType;
        }
        throw new ContextualError("an INT was expected in Modulo", getLocation());
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }
}
