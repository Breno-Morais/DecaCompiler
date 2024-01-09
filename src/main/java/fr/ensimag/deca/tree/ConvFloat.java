package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl25
 * @date 01/01/2024
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        if(this.verifyExpr(compiler, localEnv, currentClass).isInt() || this.verifyExpr(compiler, localEnv, currentClass).isFloat())
            throw new ContextualError("a Arith type was expected in ConvFloat", getLocation());
        return compiler.environmentType.FLOAT;
    }

    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getOperand().decompile(s);
    }
}
