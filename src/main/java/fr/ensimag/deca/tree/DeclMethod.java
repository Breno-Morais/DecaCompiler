package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import org.apache.log4j.Logger;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.RTS;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public void codeGenMethod(DecacCompiler compiler, String className) {
        // Before everything, it defines the address of the parameters
        for (int i = 0; i < parameters.size(); i++) {
            DAddr addr = new RegisterOffset(-3 - i, Register.LB);
            parameters.getList().get(i).getIdent().getExpDefinition().setOperand(addr);
        }

        compiler.addComment("---------- Code de la methode " + getName() + " dans la classe " + className);
        compiler.addLabel(new Label("code." + className + "." + getName()));
        // TODO: Add pile shenanigans
        compiler.addInstruction(new ADDSP(parameters.size()));

        // Get all the registers used
        List<GPRegister> regsUsed =  methodBody.getRegisters();

        // Register Saving
        compiler.addComment("Sauvegarde des registres");
        for(GPRegister reg : regsUsed) {
            compiler.addInstruction(new PUSH(reg));
            compiler.addToStack(1);
        }

        // Main code
        methodBody.codeGenMethod(compiler);

        // TODO: Error handling
        compiler.addLabel(new Label("fin." + className + "." + getName()));
        // Register Restauration
        compiler.addComment("Restauration des registres");
        for(GPRegister reg : regsUsed) {
            compiler.addInstruction(new POP(reg));
            compiler.removeFromStack(1);
        }

        compiler.addLabel(new Label("fin." + className + "." + getName()));
        compiler.addInstruction(new RTS());
    }
}
