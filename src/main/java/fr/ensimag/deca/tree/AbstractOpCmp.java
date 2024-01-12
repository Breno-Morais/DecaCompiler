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
public abstract class AbstractOpCmp extends AbstractBinaryExpr {
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
            throw new ContextualError("one Operator is not an Arith type", getLocation());

        }
        //this.setType(compiler.environmentType.BOOLEAN);
        LOG.debug("verifyExpr AbstractOpCmp : end");
        return compiler.environmentType.BOOLEAN;
    }


}
