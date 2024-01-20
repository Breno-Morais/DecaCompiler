package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

public class InstanceOf extends AbstractOpExactCmp {

    public InstanceOf(AbstractExpr leftOperand, AbstractIdentifier rightOperand) {
        super(leftOperand, rightOperand);
    }

    public AbstractExpr getObject() {
        return getLeftOperand();
    }

    public AbstractIdentifier getClassName() {
        return (AbstractIdentifier) getRightOperand();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        getLeftOperand().decompile(s);
        s.print(" instanceof ");
        getRightOperand().decompile(s);
        s.print(")");
    }

    @Override
    protected String getOperatorName() {
        return "instanceof";
    }

    @Override
    public void compareCondition(DecacCompiler compiler, Label E) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        getObject().codeGenInst(compiler);
        codeGenInstanceOf(compiler, Register.getR(2));
    }

    // TODO: InstanceOf
    public void codeGenInstanceOf(DecacCompiler compiler, GPRegister register) {
        // If its checking that it is an object, return true
        if(getClassName().toString().equals("object"))
            compiler.addInstruction(new LOAD(1, register));
        else {
            Label instanceTrue = new Label("INSTANCE_true");
            Label instanceFalse = new Label("INSTANCE_false");
            Label instanceLoop = new Label("INSTANCE_start_loop");
            Label instanceEnd = new Label("INSTANCE_fin");

            // Check if the object is null
            getObject().codeGen(compiler, register.getNumber());
            compiler.addInstruction(new CMP(new NullOperand(), register));
            compiler.addInstruction(new BEQ(instanceFalse));

            // Get the address in the heap,
            compiler.addInstruction(new LOAD(new RegisterOffset(0, register), register));
            // Get method table of the class
            compiler.addInstruction(new LOAD(new RegisterOffset(0, register), register)); // Maybe a LEA

            // Get the address of the method table of object
            compiler.addInstruction(new LOAD(getClassName().getClassDefinition().getMethodTableAddress(), Register.R1)); // Maybe a LEA

            // Add label of the while loop
            compiler.addLabel(instanceLoop);

            // Check the address of the method table is false
            compiler.addInstruction(new CMP(new NullOperand(), register));
            compiler.addInstruction(new BEQ(instanceFalse));

            // If the method tables are equal, than true
            compiler.addInstruction(new CMP(Register.R1, register));
            compiler.addInstruction(new BEQ(instanceTrue));

            // Go one level deeper, go to the begging of the while loop
            compiler.addInstruction(new LOAD(new RegisterOffset(0, register), register));
            compiler.addInstruction(new BRA(instanceLoop));

            // Put the label of returning true
            compiler.addLabel(instanceTrue);
            compiler.addInstruction(new LOAD(1, register));
            compiler.addInstruction(new BRA(instanceEnd));

            // Put the label of returning false
            compiler.addLabel(instanceFalse);
            compiler.addInstruction(new LOAD(0, register));

            // Put the label of end
            compiler.addLabel(instanceEnd);
        }
    }

    @Override
    protected void codeGenBool(DecacCompiler compiler, int registerNumber, boolean not) {
        // TODO
    }
}
