package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;

import java.util.List;

public abstract class AbstractMethodBody extends Tree {
    public abstract List<GPRegister> getRegisters();
    public abstract void codeGenMethod(DecacCompiler compiler);
}
