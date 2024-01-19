package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;

import java.util.List;

public abstract class AbstractMethodBody extends Tree {
    public abstract List<GPRegister> getRegisters();
    public abstract void codeGenMethod(DecacCompiler compiler);

    public abstract void verifyClassBody(DecacCompiler compiler, EnvironmentExp env_exp, EnvironmentExp env_exp_params, ClassDefinition classe, AbstractIdentifier type) throws ContextualError;
}
