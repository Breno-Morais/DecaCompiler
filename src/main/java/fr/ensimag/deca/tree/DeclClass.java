package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.MethodName;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import org.apache.commons.lang.StringUtils;
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

    private static EnvironmentExp env_exp = new EnvironmentExp(null);

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        //verifier le nom des classes et la hiérarchie de classes
        LOG.debug("verifyClass DeclClass: start");
        //on ajoute que le nom des classes dans l'environnement
        LOG.debug("name = " + name.prettyPrint());
        LOG.debug("superclass.name = " + superclass.getName());
        if (superclass.getName().toString() == "Object"){
            superclass.setDefinition(compiler.environmentType.OBJECT.getDefinition());
        } else {
            superclass.setDefinition(compiler.environmentType.get(superclass.getName()));
        }
        EnvironmentType env_types = compiler.environmentType;
        TypeDefinition type_def = compiler.environmentType.get(superclass.getName());
        if(!superclass.getName().toString().equals("Object") && type_def==null){  //!superclass.getClassDefinition().isClass()
            throw new ContextualError("superclass is not a Class in DeclClass", getLocation());
        }
        ClassType classType = new ClassType(name.getName(), getLocation(), superclass.getClassDefinition());
        ClassDefinition classDefinition = new ClassDefinition(classType, getLocation(), superclass.getClassDefinition());
//        EnvironmentExp env_local =  classDefinition.getMembers();

        try{
            name.setDefinition(classDefinition);
            env_types.declareClass(name.getName(), classType.getDefinition());
        }catch (EnvironmentType.DoubleDefException e){
            throw new ContextualError("Error, class already declared in DeclClass", this.getLocation());
        }
        LOG.debug("verifyClass DeclClass: end");
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        LOG.debug("verifyClassMembers DeclClass: start");
        EnvironmentType env_types = compiler.environmentType;   //on récupère l'environnement des types

        listField.verifyListClassMembers(compiler, superclass.getClassDefinition(), name.getClassDefinition());
        listMethod.verifyListClassMembers(compiler, superclass.getClassDefinition(), name.getClassDefinition());

        LOG.debug("verifyClassMembers DeclClass: end");
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verifyClassBody DeclClass: start");

        listField.verifyListClassBody(compiler, superclass.getClassDefinition(), name.getClassDefinition());
        listMethod.verifyListClassBody(compiler, superclass.getClassDefinition(), name.getClassDefinition());

        LOG.debug("verifyClassBody DeclClass: end");
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

    @Override
    public void addClassMethod(DecacCompiler compiler) {
        compiler.addComment("--------------------------------------------------");
        String strClassName = "Class " + getName();
        compiler.addComment(StringUtils.center(strClassName, 50 - strClassName.length()));
        compiler.addComment("--------------------------------------------------");

        addInit(compiler);
        addMethods(compiler);
    }

    private void addInit(DecacCompiler compiler) {
        compiler.addComment("---------- Initialisation des champs de " + getName());
        compiler.addLabel(new Label("init." + getName()));
        // TODO: add the pile stuff

        listField.codeGenListField(compiler);

        compiler.addInstruction(new RTS());
    }

    private void addMethods(DecacCompiler compiler) {
        for(AbstractDeclMethod declMethod : listMethod.getList()) {
            declMethod.codeGenMethod(compiler, getName().toString());
        }
    }
}
