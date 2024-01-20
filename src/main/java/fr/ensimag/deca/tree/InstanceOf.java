package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

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
        codeGenInstanceOf(compiler);
    }

    // TODO: InstanceOf
    public void codeGenInstanceOf(DecacCompiler compiler) {
        // If its checking that it is an object, return true
        if(compiler.environmentType.defOfType(getClassName().getName()).getType().sameType(compiler.environmentType.OBJECT))
            compiler.addInstruction(new LOAD(1, Register.R2));
        else {
            // Check if the object is null

            // Get the address in the heap, get method table of the class

            // Add label of the while loop

            // Check the address of the method table is false

            // Get the address of the method table of object, if the method table is equal, than true

            // Go one level deeper, go to the begging of the while loop

            // Put the label of returning true

            // Put the label of returning false

            // Put the label of end
        }
    }

    @Override
    protected void codeGenBool(DecacCompiler compiler, int registerNumber, boolean not) {
        // TODO
    }
}
