package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;

import java.io.PrintStream;
import java.util.List;

public class MethodCall extends AbstractExpr {
    private AbstractExpr obj;
    private AbstractIdentifier meth;
    private ListExpr param;

    public MethodCall(AbstractExpr obj, AbstractIdentifier meth, ListExpr param) {
        this.obj = obj;
        this.meth = meth;
        this.param = param;
    }
    @Override
    public void decompile(IndentPrintStream s) {
        if (obj != null) {
            obj.decompile(s);
            s.print(".");
        }
        meth.decompile(s);
        s.print("(");
        param.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        obj.prettyPrint(s, prefix, false);
        meth.prettyPrint(s, prefix, false);
        param.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        param.iter(f);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        // Vérifie que l'objet sur lequel est appelée la méthode est une classe
        Type typeObj = this.obj.verifyExpr(compiler, localEnv, currentClass);
        if (!typeObj.isClass()) {
            throw new ContextualError("Object must be a class in MethodCall", getLocation());
        }

        // Vérifie que la classe possède la méthode
        ClassType classType = (ClassType) typeObj;
        SymbolTable.Symbol methodName = this.meth.getName();
        ExpDefinition methodDef = classType.getDefinition().getMembers().get(methodName);
        if (methodDef == null || !(methodDef instanceof MethodDefinition)){
            throw new ContextualError("The method '" + methodName.toString() + "' doesn't exist in MethodCall", getLocation());
        }
        MethodDefinition methodDefOk = (MethodDefinition) methodDef;

        //Vérifie les paramètres de la méthode
        Signature signature = methodDefOk.getSignature();
        signature.verifyParameters(compiler, this.param.getList(), getLocation(), localEnv, currentClass);

        //SetType
        Type returnType = signature.getReturnType();
        setType(returnType);
        return returnType;
    }
}
