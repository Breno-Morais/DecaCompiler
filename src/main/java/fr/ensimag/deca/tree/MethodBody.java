package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

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
}
