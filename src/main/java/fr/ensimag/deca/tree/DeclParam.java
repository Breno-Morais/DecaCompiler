package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class DeclParam extends AbstractDeclParam {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    final private AbstractIdentifier type;
    final private AbstractIdentifier ident;

    public DeclParam(AbstractIdentifier type, AbstractIdentifier ident) {
        this.type = type;
        this.ident = ident;
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyClassMembers(DecacCompiler compiler, Signature signature) throws ContextualError {   //TODO manque des paramètres : env_types, super, class
        LOG.debug("verifyClassMembers DeclParam: start");
        if(type.verifyType(compiler).isVoid()){
            throw new ContextualError("type is Void in DeclParam", getLocation());
        }
        EnvironmentType env_type = compiler.environmentType;
        if(env_type.get(type.getName()) == null){
            throw new ContextualError("type is not declared in env_type in DeclParam", getLocation());
        }

        signature.add(type.verifyType(compiler));

        LOG.debug("verifyClassMembers DeclParam: end");
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyClassBody(DecacCompiler compiler) throws ContextualError {    //TODO manque des paramètres : env_types, super, class
        LOG.debug("verifyClassBody DeclParam: start");

        LOG.debug("verifyClassBody DeclParam: end");
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        ident.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        ident.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }
}
