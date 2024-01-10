package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

public class New extends AbstractUnaryExpr {
    private static final Logger LOG = Logger.getLogger(Identifier.class);

    public New(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        getOperand().decompile(s);
        s.print("()");
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verifyExpr New : start");
        LOG.debug("verifyExpr New : end");
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected String getOperatorName() {
        return "new";
    }
}
