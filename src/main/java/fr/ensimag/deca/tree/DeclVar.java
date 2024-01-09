package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * @author gl25
 * @date 01/01/2024
 */
public class DeclVar extends AbstractDeclVar {

    private static final Logger LOG = Logger.getLogger(Main.class);
    
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify DeclVal: start");
        EnvironmentType env_Type = compiler.environmentType;
        TypeDefinition type_Def = env_Type.defOfType(this.type.getName());

        if(type_Def == null || type_Def.getType().isVoid()){   //ici, on vérifie si le type que on utilise existe et qu'il n'est pas void
            throw new ContextualError("Erreur de type", getLocation());
        }

        try{
            localEnv.declare(this.varName.getName(), new VariableDefinition(type_Def.getType(), this.getLocation()));
        }catch(EnvironmentExp.DoubleDefException e){
            throw new ContextualError("Erreur, le type est déjà déclaré", this.getLocation());
        }

        initialization.verifyInitialization(compiler, type_Def.getType(), localEnv, currentClass);

        LOG.debug("verify DeclVal: end");
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        varName.decompile(s);
        initialization.decompile(s);
        s.println(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
