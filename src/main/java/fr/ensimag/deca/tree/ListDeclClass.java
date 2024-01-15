package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 *
 * @author gl25
 * @date 01/01/2024
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verifyListClass ListDeclClass : start");
        for (AbstractDeclClass c: getList())
            c.verifyClass(compiler);
        LOG.debug("verifyListClass ListDeclClass : end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verifyListClassMembers ListDeclClass : start");
        for (AbstractDeclClass c: getList())
            c.verifyClassMembers(compiler);
        LOG.debug("verifyListClassMembers ListDeclClass : end");
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verifyListClassBody ListDeclClass : start");
        for(AbstractDeclClass c: getList())
            c.verifyClassBody(compiler);
        LOG.debug("verifyListClassBody ListDeclClass : end");
    }

}
