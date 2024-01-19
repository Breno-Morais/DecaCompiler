package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
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
        insts.codeGenListInst(compiler);

        compiler.addInstruction(new WSTR("Erreur : sortie de la methode sans return"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }
}
