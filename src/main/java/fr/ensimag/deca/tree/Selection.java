package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class Selection extends AbstractLValue {
    private static final Logger LOG = Logger.getLogger(Identifier.class);
    private AbstractExpr obj;
    private AbstractIdentifier field;

    public void codeGenPrintExpr(DecacCompiler compiler){};


    public Selection(AbstractExpr obj, AbstractIdentifier field) {
        this.obj = obj;
        this.field = field;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verifyExpr Selection : start");
        LOG.debug("verifyExpr Selection : end");
        throw new UnsupportedOperationException("not yet implemented");

    }

    @Override
    public void decompile(IndentPrintStream s) {
        obj.decompile(s);
        s.print(".");
        field.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        obj.prettyPrint(s, prefix, false);
        field.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }
}
