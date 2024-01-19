package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

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
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGen(DecacCompiler compiler, int registerNumber) {
        GPRegister register = Register.R2;
        if(registerNumber > 2)
            compiler.addInstruction(new PUSH(Register.R2));

        // Add to the pile register the number of parameters + 1
        int frameSize = param.size() + 1;
        compiler.addInstruction(new ADDSP(frameSize));

        // Stores the method table in the pile
        if(obj instanceof Identifier) { // Add selection later if possible
            DAddr objAddr = ((Identifier) obj).getExpDefinition().getOperand();
            compiler.addInstruction(new LOAD(objAddr, Register.R2));
            compiler.addInstruction(new STORE(Register.R2, new RegisterOffset(0, Register.SP)));
        }

        // Store the parameters
        for (int i = param.size(); i > 0; i++) {
            param.getList().get(i - 1).codeGen(compiler, 2);

            compiler.addInstruction(new STORE(Register.R2, new RegisterOffset(-i, Register.SP)));
        }
        
        // Loads the address of the method table
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), Register.R2));
        /* TODO: Error Handler
        compiler.addInstruction(new CMP(new NullOperand(), Register.R2));
        compiler.addInstruction(new BEQ(new Label("dereferencement_null")));
        */
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.R2), Register.R2));

        // BSR to the table
        compiler.addInstruction(new BSR(new RegisterOffset(meth.getMethodDefinition().getIndex(), Register.R2)));

        if(registerNumber > 2)
            compiler.addInstruction(new POP(Register.R2));

        compiler.addInstruction(new LOAD(Register.R0, Register.getR(registerNumber)));
        compiler.addInstruction(new SUBSP(frameSize));
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        codeGen(compiler, 2);
    }
}
