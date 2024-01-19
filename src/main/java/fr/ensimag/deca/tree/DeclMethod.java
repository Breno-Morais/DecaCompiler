package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class DeclMethod extends AbstractDeclMethod {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    private AbstractIdentifier type;
    private AbstractIdentifier name;
    private ListDeclParam parameters;
    private AbstractMethodBody methodBody;

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier name,
                      ListDeclParam parameters, AbstractMethodBody methodBody) {
        this.type = type;
        this.name = name;
        this.parameters = parameters;
        this.methodBody = methodBody;
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyClassMembers(DecacCompiler compiler, ClassDefinition superClass, ClassDefinition classe, int index) throws ContextualError {   //TODO manque des paramètres : env_types, super, class
        LOG.debug("verifyClassMembers DeclMethod: start");
        EnvironmentType env_types = compiler.environmentType;
        //Signature signature = parameters.verifyMachin(compiler);
        //TODO il faut faire d'abord ListParam et Param avec une fonction qui renvoie la Signature
        Signature signature = new Signature();
        parameters.verifyListClassMembers(compiler, signature);

        MethodDefinition methodDefinition = new MethodDefinition(type.verifyType(compiler), getLocation(), signature, index);
        try{
            classe.getMembers().declare(name.getName(), methodDefinition);
            classe.incNumberOfMethods();
            //incrémenter le nb de méthodes (pareil pour les field)
        }catch (EnvironmentExp.DoubleDefException e){
            throw new ContextualError("Error, field already declared in DeclMethod", this.getLocation());
        }

        //verifier si la méthode est déjà déclarée dans env_types. Alors il faut vérifier que elle a le même prototype que celle enregistrée

        LOG.debug("verifyClassMembers DeclMethod: end");
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyClassBody(DecacCompiler compiler, ClassDefinition superClass, ClassDefinition classe) throws ContextualError {    //TODO manque des paramètres : env_types, super, class
        LOG.debug("verifyClassBody DeclMethod: start");


        parameters.verifyListClassBody(compiler);

        LOG.debug("verifyClassBody DeclMethod: end");
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        name.decompile(s);
        s.print("(");
        parameters.decompile(s);
        s.print(")");
        methodBody.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, false);
        parameters.prettyPrint(s, prefix, false);
        methodBody.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        parameters.iter(f);
    }

    public AbstractIdentifier getName() {
        return name;
    }
}
