package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ListDeclField extends TreeList<AbstractDeclField>{
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verifyListClass ListDeclField: start");
        LOG.debug("verifyListClass ListDeclField: end");
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public void codeGenListField(DecacCompiler compiler) {
        // Get all the registers used
        List<GPRegister> regsUsed =  new LinkedList<GPRegister>();
        for(AbstractDeclField declVarAbs : this.getList()) {
            regsUsed.addAll(declVarAbs.getInitialization().getExpression().getRegisters(2));

            // Don't know if is faster doing only once with an enormous list
            regsUsed = regsUsed.stream().distinct().collect(Collectors.toList());
        }

        compiler.addComment("Sauvegarde des registres");
        for(GPRegister reg : regsUsed) {
            compiler.addInstruction(new PUSH(reg));
        }

        compiler.addComment("Initialisation des registres");
        // I'm loading "this" in R1 before because I belive nothing else modifies it except the read, may be changed later
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));

        int fieldCounter = 1;
        for(AbstractDeclField declVarAbs : this.getList()) {
            DAddr addr = new RegisterOffset(fieldCounter, Register.R1);

            if(declVarAbs.getInitialization() instanceof Initialization) {
                declVarAbs.getInitialization().getExpression().codeGenInst(compiler);

                compiler.addInstruction(new STORE(Register.R2, addr));
            } else if(declVarAbs.getInitialization() instanceof NoInitialization) { // To use in the initialization of fields
                compiler.addInstruction(new LOAD(declVarAbs.getField().getType().getDefaultValue(), Register.R0));
                compiler.addInstruction(new STORE(Register.R0, addr));
            }

            fieldCounter++;
        }

        compiler.addComment("Restauration des registres");
        for(GPRegister reg : regsUsed) {
            compiler.addInstruction(new POP(reg));
        }
    }
}
