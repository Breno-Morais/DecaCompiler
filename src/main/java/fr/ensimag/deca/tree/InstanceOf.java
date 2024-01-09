package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

public class InstanceOf extends AbstractOpExactCmp {

    public InstanceOf(AbstractExpr leftOperand, AbstractIdentifier rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        getLeftOperand().decompile(s);
        s.print(" instanceof ");
        getRightOperand().decompile(s);
        s.print(")");
    }

    @Override
    protected String getOperatorName() {
        return "instanceof";
    }
}
