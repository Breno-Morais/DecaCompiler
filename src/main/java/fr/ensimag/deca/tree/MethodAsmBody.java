package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;

import java.io.PrintStream;
import java.util.List;

public class MethodAsmBody extends AbstractMethodBody {
    private StringLiteral code;

    public MethodAsmBody(StringLiteral code) {
        this.code = code;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("asm(");
        code.decompile(s);
        s.print(");");
        s.println();
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {

    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }

    @Override
    public List<GPRegister> getRegisters() {
        //TODO
        return null;
    }

    @Override
    public void codeGenMethod(DecacCompiler compiler) {
        //TODO
    }
}
