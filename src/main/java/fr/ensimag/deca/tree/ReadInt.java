package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.instructions.RINT;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 *
 * @author gl25
 * @date 01/01/2024
 */
public class ReadInt extends AbstractReadExpr {
    private static final Logger LOG = Logger.getLogger(AbstractExpr.class);
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //We want to check that the return type is an INT
//        if(!this.verifyExpr(compiler, localEnv, currentClass).isInt())
//            throw new UnsupportedOperationException("an INT was expected");
        LOG.debug("verifyExpr ReadFloat : start");
        this.setType(compiler.environmentType.INT);
        LOG.debug("verifyExpr ReadFloat : end");
        return this.getType();
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readInt()");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    protected Instruction getReadInstruction() {
        return new RINT();
    }
}
