package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.MethodName;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;
import fr.ensimag.ima.pseudocode.Label;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl25
 * @date 01/01/2024
 */
public class DeclClass extends AbstractDeclClass {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    private AbstractIdentifier name;
    private AbstractIdentifier superclass;
    private ListDeclField listField;
    private ListDeclMethod listMethod;
    private List<MethodName> methodNames;

    public DeclClass(AbstractIdentifier name, AbstractIdentifier superclass, ListDeclField listField,
                     ListDeclMethod listMethod) {
        this.name = name;
        this.superclass = superclass;
        this.listField = listField;
        this.listMethod = listMethod;

        this.methodNames = new LinkedList<MethodName>();
        for(AbstractDeclMethod absDeclMethod : listMethod.getList()) {
            DeclMethod declMethod = (DeclMethod) absDeclMethod;

            methodNames.add(new MethodName(name.toString(), declMethod.getName().toString()));
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class ");
        name.decompile(s);
        s.print(" extends ");
        superclass.decompile(s);
        s.print(" {");
        listField.decompile(s);
        listMethod.decompile(s);
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("superclass name : " + superclass.getName().toString());
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s, prefix, false);
        superclass.prettyPrint(s, prefix, false);
        listField.prettyPrint(s, prefix, false);
        listMethod.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        listField.iter(f);
        listMethod.iter(f);
    }

    @Override
    public List<Label> getMethodLabels() {
        List<Label> list = new LinkedList<Label>();
        for(MethodName methodName : methodNames) {
            list.add(methodName.toLabel());
        }

        return list;
    }

    @Override
    public List<MethodName> getMethodNames() {
        return methodNames;
    }

    @Override
    public AbstractIdentifier getName() {
        return name;
    }

    @Override
    public AbstractIdentifier getSuperclass() {
        return superclass;
    }
}
