package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

public class Cast extends AbstractUnaryExpr {
    private static final Logger LOG = Logger.getLogger(AbstractExpr.class);
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
        LOG.debug("verifyExpr Cast : start");
        //TODO
        LOG.debug("verifyExpr Cast : end");
        return null;
    }

    @Override
    protected String getOperatorName() {
        return "cast to " + type.getName();
    }
}
