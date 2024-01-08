package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;

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
        s.print("class { ... A FAIRE ... }");
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
}
