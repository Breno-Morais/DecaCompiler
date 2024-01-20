package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MethodBody extends AbstractMethodBody {
    private ListDeclVar declVariables;
    private ListInst insts;

    public MethodBody(ListDeclVar listVar, ListInst listInst) {
        this.declVariables = listVar;
        this.insts = listInst;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("{");
        declVariables.decompile(s);
        insts.decompile(s);
        s.print("}");
        s.println();
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }

    @Override
    public List<GPRegister> getRegisters() {
        List<GPRegister> regsUsed = new LinkedList<>();

        for(AbstractDeclVar declVar : declVariables.getList()) {
            regsUsed.addAll(declVar.getInitialization().getExpression().getRegisters(2));

            // Don't know if is faster doing only once with an enormous list
            regsUsed = regsUsed.stream().distinct().collect(Collectors.toList());
        }

        for(AbstractInst inst : insts.getList()) {
            regsUsed.addAll(inst.getRegisters(2));

            // Don't know if is faster doing only once with an enormous list
            regsUsed = regsUsed.stream().distinct().collect(Collectors.toList());
        }

        return regsUsed;
    }

    @Override
    public void codeGenMethod(DecacCompiler compiler) {
        Return possibleReturn = null;

        if(insts.getList().get(insts.size() - 1) instanceof Return) {
            possibleReturn = (Return) insts.getList().get(insts.size() - 1);
            insts.getModifiableList().remove(insts.size() - 1);
        }

        insts.codeGenListInst(compiler);

        // Return logic
        Label endMethodLabel = new Label("fin." + getDeclMethod().getClassDefinition().getType() + "." + getDeclMethod().getName());
        if(possibleReturn == null) {
            compiler.addInstruction(new BRA(endMethodLabel));
        } else {
            possibleReturn.setEndMethod(endMethodLabel);
            possibleReturn.codeGenInst(compiler);
        }

        compiler.addInstruction(new WSTR("Erreur : sortie de la methode sans return"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }

    @Override
    public void verifyClassBody(DecacCompiler compiler, EnvironmentExp env_exp, EnvironmentExp env_exp_params, ClassDefinition classe, AbstractIdentifier type) throws ContextualError {
        if (declVariables != null){
            //LOG.debug("     Main : declVariables not null");
            declVariables.verifyListDeclVariable(compiler, env_exp_params, classe);
        } else {
            throw new ContextualError("Liste des declarations de variables est null", getLocation());
        }

        if (insts != null){
            insts.verifyListInst(compiler, env_exp_params, classe, type.getType());
        } else {
            throw new ContextualError("Liste des declarations des instructionx null", getLocation());
        }
    }
}
