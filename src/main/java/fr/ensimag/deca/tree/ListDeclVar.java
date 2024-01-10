package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 * List of declarations (e.g. int x; float y,z).
 *
 * @author gl25
 * @date 01/01/2024
 */
public class ListDeclVar extends TreeList<AbstractDeclVar> {
    //private static final Logger LOG = Logger.getLogger(Program.class);

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclVar i : this.getList())
            i.decompile(s);
    }

    /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv
     *   its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to
     *      the "env_exp_r" attribute
     * @param currentClass
     *          corresponds to "class" attribute (null in the main bloc).
     */
    void verifyListDeclVariable(DecacCompiler compiler, EnvironmentExp localEnv,
                                ClassDefinition currentClass) throws ContextualError {
        //LOG.debug("Verify List Declaration : start");
        for(AbstractDeclVar d: getList()) {
            //LOG.debug("     ListDeclVar : il y a un truc");
            d.verifyDeclVar(compiler, localEnv, currentClass);
        }
        //LOG.debug("Verify List Declaration : end");

    }


}
