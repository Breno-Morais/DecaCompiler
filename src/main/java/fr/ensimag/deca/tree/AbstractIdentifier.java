package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;

import java.util.Objects;

/**
 *
 * @author gl25
 * @date 01/01/2024
 */
public abstract class AbstractIdentifier extends AbstractLValue {

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a class definition.
     */
    public abstract ClassDefinition getClassDefinition();

    public abstract Definition getDefinition();

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    public abstract FieldDefinition getFieldDefinition();

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a method definition.
     */
    public abstract MethodDefinition getMethodDefinition();

    public abstract SymbolTable.Symbol getName();

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a ExpDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    public abstract ExpDefinition getExpDefinition();

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    public abstract VariableDefinition getVariableDefinition();

    public abstract void setDefinition(Definition definition);



    /**
     * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
     * @param compiler contains "env_types" attribute
     * @return the type corresponding to this identifier
     *         (corresponds to the "type" attribute)
     */
    public abstract Type verifyType(DecacCompiler compiler) throws ContextualError;

    @Override
    public String toString() {
        return getName().getName();
    }

    public DAddr getAddress() {
        if(getDefinition().isExpression())
            return getExpDefinition().getOperand();
        else throw new DecacInternalError("Cannot get address of a class or type");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractIdentifier that = (AbstractIdentifier) o;
        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public void codeGenIfBranch(DecacCompiler compiler, boolean expected, Label ifLabel, Label elseLabel) {
        codeGenIfBranch(compiler, expected, ifLabel);
        if(expected)
                compiler.addInstruction(new BRA(elseLabel));
    }

    @Override
    public void codeGenIfBranch(DecacCompiler compiler, boolean expected, Label ifLabel) {
        this.codeGen(compiler, 0);
        compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.R0));
        compiler.addInstruction((expected) ? new BNE(ifLabel) : new BEQ(ifLabel));
    }
}
