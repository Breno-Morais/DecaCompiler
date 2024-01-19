package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class DeclField extends AbstractDeclField {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
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

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyClassMembers(DecacCompiler compiler, ClassDefinition superClass, ClassDefinition classe, int index) throws ContextualError {
        LOG.debug("verifyClassMembers DeclField: start");
        EnvironmentType env_types = compiler.environmentType;

        if(type.verifyType(compiler).isVoid()){
            throw new ContextualError("type is Void in DeclField", getLocation());
        }
        if(classe.getSuperClass().getMembers() == null){ //Vérifier que env_exp_super(name) est défini  //TODO pas sur que ça soit comme ça ??
            throw new ContextualError("env_exp_super(name) not defined in DeclField", getLocation());
        }
        Visibility visib = getStatusVisibility();
        //on veut vérifier que
        FieldDefinition fieldDefinition = new FieldDefinition(type.verifyType(compiler), getLocation(), visib, classe, index);
        try{
            classe.getMembers().declare(field.getName(), fieldDefinition);
            classe.incNumberOfFields();
        }catch (EnvironmentExp.DoubleDefException e){
            throw new ContextualError("Error, field already declared in DeclField", this.getLocation());
        }

        LOG.debug("verifyClassMembers DeclField: end");
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyClassBody(DecacCompiler compiler, ClassDefinition superClass, ClassDefinition classe) throws ContextualError {
        LOG.debug("verifyClassBody DeclField: start");

        LOG.debug("verifyClassBody DeclField: end");
        throw new UnsupportedOperationException("not yet implemented");
    }

    public Visibility getStatusVisibility(){
        if(this.visibility.isPublic()){
            return Visibility.PUBLIC;
        }
        else{
            return Visibility.PROTECTED;
        }
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
