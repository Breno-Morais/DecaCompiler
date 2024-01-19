package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

public class ListDeclParam extends TreeList<AbstractDeclParam> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    @Override
    public void decompile(IndentPrintStream s) {
        String delim = "";
        for (AbstractDeclParam i : this.getList()) {
            s.print(delim);
            i.decompile(s);
            delim = ",";
        }
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler, Signature signature) throws ContextualError {
        LOG.debug("ListDeclParam ListDeclMethod: start");
        for (AbstractDeclParam c : getList()){
            c.verifyClassMembers(compiler, signature);
        }
        LOG.debug("ListDeclParam ListDeclMethod: end");
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        LOG.debug("ListDeclParam ListDeclMethod: start");
        for (AbstractDeclParam c: getList())
            c.verifyClassBody(compiler);
        LOG.debug("ListDeclParam ListDeclMethod: end");
    }
}
