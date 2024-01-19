package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.log4j.Logger;

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
    public void verifyClassMembers(DecacCompiler compiler, ClassDefinition superClass, ClassDefinition classe, int index) throws ContextualError {
        LOG.debug("verifyClassMembers DeclMethod: start");
        Signature signature = new Signature();
        parameters.verifyListClassMembers(compiler, signature);
        MethodDefinition methodDefinition = new MethodDefinition(type.verifyType(compiler), getLocation(), signature, index);
        name.setDefinition(methodDefinition);
        methodDefinition.getSignature().setReturnType(methodDefinition.getType());
        try{
            classe.getMembers().declare(name.getName(), methodDefinition);
            classe.incNumberOfMethods();
        }catch (EnvironmentExp.DoubleDefException e){
            throw new ContextualError("Error, field already declared in DeclMethod", this.getLocation());
        }

//        MethodDefinition superMethodDefinition = (MethodDefinition) superClass.getMembers().get(name.getName());
//        Type superType = superMethodDefinition.getType();
//        Signature superSignature = superMethodDefinition.getSignature();
//        if(superMethodDefinition != null){
//            if(!superSignature.equals(signature) && !superType.isSubType(superType)){
//                throw new ContextualError("Method definition not compatible with the superClass in DeclMethod", getLocation());
//            }
//        }

        name.setDefinition(methodDefinition);
        LOG.debug("verifyClassMembers DeclMethod: end");
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyClassBody(DecacCompiler compiler, EnvironmentExp env_exp, ClassDefinition classe) throws ContextualError {
        LOG.debug("verifyClassBody DeclMethod: start");
        //type est le type de retour
        MethodDefinition methodDefinition = (MethodDefinition) classe.getMembers().get(name.getName());
        methodDefinition.getSignature().setReturnType(type.getType());  //enregistrement de la valeur de retour dans signature

        EnvironmentExp env_exp_params = parameters.verifyListClassBody(compiler, classe);
        methodBody.verifyClassBody(compiler, env_exp, env_exp_params, classe, name);
        //TODO : une declaration à ajouter ?
        LOG.debug("verifyClassBody DeclMethod: end");
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
        DecacCompiler blockCompiler = new DecacCompiler(compiler.getCompilerOptions(), compiler.getSource());

        // Before everything, it defines the address of the parameters
        for (int i = 0; i < parameters.size(); i++) {
            DAddr addr = new RegisterOffset(-3 - i, Register.LB);
            parameters.getList().get(i).getIdent().getExpDefinition().setOperand(addr);
        }

        compiler.addComment("---------- Code de la methode " + getName() + " dans la classe " + className);
        compiler.addLabel(new Label("code." + className + "." + getName()));

        // Get all the registers used
        List<GPRegister> regsUsed =  methodBody.getRegisters();

        // Register Saving
        blockCompiler.addComment("Sauvegarde des registres");
        for(GPRegister reg : regsUsed) {
            blockCompiler.addInstruction(new PUSH(reg));
            blockCompiler.addToStack(1);
        }

        // Main code
        methodBody.codeGenMethod(blockCompiler);

        // TODO: Error handling
        blockCompiler.addLabel(new Label("fin." + className + "." + getName()));
        // Register Restauration
        blockCompiler.addComment("Restauration des registres");
        for(GPRegister reg : regsUsed) {
            blockCompiler.addInstruction(new POP(reg));
            blockCompiler.removeFromStack(1);
        }

        blockCompiler.addLabel(new Label("fin." + className + "." + getName()));
        blockCompiler.addInstruction(new RTS());

        blockCompiler.addInstruction(new ADDSP(parameters.size())); // TODO: Put local variables instead
        blockCompiler.addFirst(new BOV(new Label("pile_pleine")));
        blockCompiler.addFirst(new TSTO(blockCompiler.getMaxStack()));

        compiler.append(blockCompiler);
    }
}
