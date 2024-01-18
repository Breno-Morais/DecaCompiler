package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

public class DeclField extends AbstractDeclField {
    private AbstractVisibility visibility;
    private AbstractIdentifier type;
    private AbstractIdentifier field;
    private AbstractInitialization initialization;

    public DeclField(AbstractVisibility visibility, AbstractIdentifier type,
                     AbstractIdentifier field, AbstractInitialization initialization) {
        this.visibility = visibility;
        this.type = type;
        this.field = field;
        // field.setDefinition(new FieldDefinition());
        this.initialization = initialization;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        visibility.decompile(s);
        s.print(" ");
        type.decompile(s);
        s.print(" ");
        field.decompile(s);
        initialization.decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        visibility.prettyPrint(s, prefix, false);
        type.prettyPrint(s, prefix, false);
        field.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }

    @Override
    public AbstractVisibility getVisibility() {
        return visibility;
    }

    @Override
    public AbstractIdentifier getType() {
        return type;
    }

    @Override
    public AbstractIdentifier getField() {
        return field;
    }

    @Override
    public AbstractInitialization getInitialization() {
        return initialization;
    }
}
