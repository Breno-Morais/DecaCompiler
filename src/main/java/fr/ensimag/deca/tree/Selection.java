package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class Selection extends AbstractIdentifier {
    private static final Logger LOG = Logger.getLogger(Identifier.class);
    private AbstractExpr obj;
    private AbstractIdentifier field;

    private Symbol name;

    public Selection(AbstractExpr obj, AbstractIdentifier field, Symbol name) {
        this.obj = obj;
        this.field = field;
        this.name = name;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verifyExpr Selection : start");
        LOG.debug("verifyExpr Selection : end");
        throw new UnsupportedOperationException("not yet implemented");

    }

    @Override
    public void decompile(IndentPrintStream s) {
        obj.decompile(s);
        s.print(".");
        field.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        obj.prettyPrint(s, prefix, false);
        field.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }

    @Override
    public ClassDefinition getClassDefinition() {
        return field.getClassDefinition();
    }

    @Override
    public Definition getDefinition() {
        return field.getDefinition();
    }

    @Override
    public FieldDefinition getFieldDefinition() {
        return field.getFieldDefinition();
    }

    @Override
    public MethodDefinition getMethodDefinition() {
        return field.getMethodDefinition();
    }

    @Override
    public Symbol getName() {
        return name;
    }

    @Override
    public ExpDefinition getExpDefinition() {
        return field.getExpDefinition();
    }

    @Override
    public VariableDefinition getVariableDefinition() {
        return field.getVariableDefinition();
    }

    @Override
    public void setDefinition(Definition definition) {
        field.setDefinition(definition);
    }

    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        throw new ContextualError("Not yet implemented", getLocation());
    }

    @Override
    protected void codeGen(DecacCompiler compiler, int registerNumber) {
        // I'll assume that the obj can only be a This or a Identifier
        if(obj instanceof This) {
            /* TODO: Error Handler
            compiler.addInstruction(new CMP(new NullOperand(), ));
            compiler.addInstruction(new BEQ(new Label("dereferencement_null")));
             */
            LOAD
        }
        else if(obj instanceof AbstractIdentifier)
            compiler.addInstruction(new );
        else throw new DecacInternalError("Selection of impossible type");

        compiler.addInstruction(new LOAD(getAddress(), Register.getR(registerNumber)));
    }
}
