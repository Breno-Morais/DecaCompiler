package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;

public abstract class AbstractDeclParam extends Tree {
    public abstract void verifyClassMembers(DecacCompiler compiler, Signature signature) throws ContextualError;

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public abstract void verifyClassBody(DecacCompiler compiler) throws ContextualError;
}
