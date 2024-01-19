package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.log4j.Logger;

public class New extends AbstractUnaryExpr {
    private static final Logger LOG = Logger.getLogger(Identifier.class);

    public New(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        getOperand().decompile(s);
        s.print("()");
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verifyExpr New : start");
        if (!this.getType().isClass()){
            throw new ContextualError("The Object is not a Class in new", getLocation());
        }
        if (compiler.environmentType.get(this.getType().getName()) == null){
            throw new ContextualError("The Class doesn't exist in EnvType in New", getLocation());
        }
        LOG.debug("verifyExpr New : end");
        //Settype New
        Type typeNew = this.getType();
        setType(typeNew);
        return typeNew;
    }

    @Override
    protected String getOperatorName() {
        return "new";
    }

    @Override
    protected void codeGen(DecacCompiler compiler, int registerNumber) {
        int classSize = ((Identifier) getOperand()).getClassDefinition().getNumberOfFields() + 1;
        addImaInstruction(compiler, new ImmediateInteger(classSize), Register.getR(registerNumber));
    }

    @Override
    public void addImaInstruction(DecacCompiler compiler, DVal value, GPRegister register) {
        compiler.addInstruction(new NEW(value, register));
        // TODO: BOV tas_plein
        Identifier ident = (Identifier) getOperand();
        DAddr methodTableAddr = ident.getClassDefinition().getMethodTableAddress();
        compiler.addInstruction(new LEA(methodTableAddr, Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, register)));
        compiler.addInstruction(new PUSH(register));
        compiler.addInstruction(new BSR(new Label("init." + ident)));
        compiler.addInstruction(new POP(register));
    }
}
