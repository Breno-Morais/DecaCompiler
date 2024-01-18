package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

public class ListDeclField extends TreeList<AbstractDeclField>{
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler, ClassDefinition superClass, ClassDefinition classe) throws ContextualError {
        LOG.debug("verifyListClassMembers ListDeclField: start");
        int index = 0;
        for (AbstractDeclField c : getList()){   //TODO il y le calcul de (env_expr = env_expr + env_exp) à faire
            c.verifyClassMembers(compiler, superClass, classe, index);
            index++;
        }
        LOG.debug("verifyListClassMembers ListDeclField: end");
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler, ClassDefinition superClass, ClassDefinition classe) throws ContextualError {
        LOG.debug("verifyListClassBody ListDeclField: start");
        for (AbstractDeclField c: getList())
            c.verifyClassBody(compiler, superClass, classe);
        LOG.debug("verifyListClassBody ListDeclField: end");
    }
}
