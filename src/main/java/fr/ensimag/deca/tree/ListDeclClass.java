package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.MethodName;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author gl25
 * @date 01/01/2024
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    private HashMap<AbstractIdentifier, AbstractDeclClass> classHashMap = new HashMap<>();

    @Override
    public void add(AbstractDeclClass i) {
        super.add(i);
        classHashMap.put(i.getName(), i);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    public void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verifyListClass ListDeclClass : start");
        for (AbstractDeclClass c: getList())
            c.verifyClass(compiler);
        LOG.debug("verifyListClass ListDeclClass : end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verifyListClassMembers ListDeclClass : start");
        for (AbstractDeclClass c: getList())
            c.verifyClassMembers(compiler);
        LOG.debug("verifyListClassMembers ListDeclClass : end");
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verifyListClassBody ListDeclClass : start");
        for(AbstractDeclClass c: getList())
            c.verifyClassBody(compiler);
        LOG.debug("verifyListClassBody ListDeclClass : end");
    }

    public AbstractDeclClass getClassDecl(AbstractIdentifier identifier) {
        return classHashMap.get(identifier);
    }

    // Inherent the methods of the parent and don't add the Overridden
    public void updateMethodNames(AbstractDeclClass declClass) {
        List<MethodName> methodNames = declClass.getMethodNames();

        if(declClass.getSuperclass().toString().equals("Object")) {
            if(methodNames.stream()
                    .noneMatch(childMethod -> childMethod.getName().equals("equals"))) {
                methodNames.add(new MethodName("Object", "equals"));
            }
        } else {
            AbstractDeclClass superClass = getClassDecl(declClass.getSuperclass());

            for (MethodName superMethod : superClass.getMethodNames()) {
                boolean exists = methodNames.stream()
                        .anyMatch(childMethod -> childMethod.getName().equals(superMethod.getName()));

                if (!exists) {
                    methodNames.add(superMethod);
                }
            }
        }
    }
}
