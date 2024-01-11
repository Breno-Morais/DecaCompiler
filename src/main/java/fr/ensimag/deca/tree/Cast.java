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
        s.print(")(");
        getOperand().decompile(s);
        s.print(")");
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verifyExpr Cast : start");
        Type T1 = this.getType();
        LOG.debug(T1);
        Type T2 = getOperand().verifyExpr(compiler, localEnv, currentClass);
        //TODO comment récupérer les types T1 et T2
        if(T1.sameType(T2)){
            throw new ContextualError("(cast) useless in Cast : same type", getLocation());
        }
        if(T1.isVoid() || T2.isVoid()){
            throw new ContextualError("a Void cannot be cast", getLocation());
        }
        if(T1.isFloat() && T2.isInt() || T2.isFloat() && T1.isInt()){
            LOG.debug("verifyExpr Cast : end");
            return T1;
        }
        throw new ContextualError("type incompatible in Cast", getLocation());
    }

    @Override
    protected String getOperatorName() {
        return "cast to " + type.getName();
    }
}
