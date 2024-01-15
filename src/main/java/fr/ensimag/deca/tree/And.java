package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl25
 * @date 01/01/2024
 */
public class And extends AbstractOpBool {
    private static int andCount = 0;

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    @Override
    public void codeGenBranch(DecacCompiler compiler) {
        compiler.addComment("Beginning of AND");

        // TODO: Refactor all of this mess
        if(getExpectedBool()) {
            Label skipUnevaluated = new Label("E_Fin." + andCount);
            andCount++;

            // Check first operand
            if (getLeftOperand() instanceof AbstractBranchable) {
                AbstractBranchable leftBranchable = (AbstractBranchable) getLeftOperand();
                leftBranchable.setE(skipUnevaluated);
                leftBranchable.setExpectedBool(false);
                leftBranchable.codeGenBranch(compiler);

            } else if (getLeftOperand() instanceof BooleanLiteral) {
                BooleanLiteral leftValue = (BooleanLiteral) getLeftOperand();
                if (!leftValue.getValue()) // If the first value is false, don't generate the code of the second part
                    return;

            } else if (getLeftOperand() instanceof Not) {
                if (((Not) getLeftOperand()).getOperand() instanceof BooleanLiteral) {
                    BooleanLiteral leftValue = (BooleanLiteral) ((Not) getLeftOperand()).getOperand();
                    if (leftValue.getValue()) // If the first value is false, don't generate the code of the second part
                        return;

                } else if (((Not) getLeftOperand()).getOperand() instanceof AbstractBranchable) {
                    AbstractBranchable leftBranchable = (AbstractBranchable) ((Not) getLeftOperand()).getOperand();
                    leftBranchable.setE(skipUnevaluated);
                    leftBranchable.setExpectedBool(true);
                    leftBranchable.codeGenBranch(compiler);

                } else if (((Not) getLeftOperand()).getOperand() instanceof AbstractIdentifier) {
                    AbstractIdentifier leftIdentifier = (AbstractIdentifier) ((Not) getLeftOperand()).getOperand();

                    leftIdentifier.codeGen(compiler, 0);
                    compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.R0));
                    compiler.addInstruction(new BNE(skipUnevaluated));
                }

            } else if (getLeftOperand() instanceof AbstractIdentifier) {
                AbstractIdentifier leftIdentifier = (AbstractIdentifier) getLeftOperand();

                leftIdentifier.codeGen(compiler, 0);
                compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.R0));
                compiler.addInstruction(new BEQ(skipUnevaluated));
            } else
                throw new DecacInternalError("Left Operand not a boolean");

            // Check second operand
            if (getRightOperand() instanceof AbstractBranchable) {
                AbstractBranchable rightBranchable = (AbstractBranchable) getRightOperand();
                rightBranchable.setE(getE());
                rightBranchable.setExpectedBool(true);
                rightBranchable.codeGenBranch(compiler);
            } else if (getRightOperand() instanceof BooleanLiteral) {
                BooleanLiteral rightValue = (BooleanLiteral) getRightOperand();
                if (rightValue.getValue())
                    compiler.addInstruction(new BRA(getE()));
            } else if (getRightOperand() instanceof Not) {
                if (((Not) getRightOperand()).getOperand() instanceof BooleanLiteral) {
                    BooleanLiteral rightValue = (BooleanLiteral) ((Not) getRightOperand()).getOperand();
                    if (!rightValue.getValue())
                        compiler.addInstruction(new BRA(getE()));
                } else if (((Not) getRightOperand()).getOperand() instanceof AbstractBranchable) {
                    AbstractBranchable rightBranchable = (AbstractBranchable) ((Not) getRightOperand()).getOperand();
                    rightBranchable.setE(getE());
                    rightBranchable.setExpectedBool(false);
                    rightBranchable.codeGenBranch(compiler);

                } else if (((Not) getRightOperand()).getOperand() instanceof AbstractIdentifier) {
                    AbstractIdentifier rightIdentifier = (AbstractIdentifier) ((Not) getRightOperand()).getOperand();

                    rightIdentifier.codeGen(compiler, 0);
                    compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.R0));
                    compiler.addInstruction(new BEQ(getE()));
                }
            } else if (getRightOperand() instanceof AbstractIdentifier) {
                AbstractIdentifier rightIdentifier = (AbstractIdentifier) getRightOperand();

                rightIdentifier.codeGen(compiler, 0);
                compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.R0));
                compiler.addInstruction(new BNE(getE()));
            } else
                throw new DecacInternalError("Right Operand not a boolean");

            compiler.addLabel(skipUnevaluated);
        } else {
            // Check first operand
            if (getLeftOperand() instanceof AbstractBranchable) {
                AbstractBranchable leftBranchable = (AbstractBranchable) getLeftOperand();
                leftBranchable.setE(getE());
                leftBranchable.setExpectedBool(false);
                leftBranchable.codeGenBranch(compiler);
            } else if (getLeftOperand() instanceof BooleanLiteral) {
                BooleanLiteral leftValue = (BooleanLiteral) getLeftOperand();
                if (!leftValue.getValue()) // If the first value is false, don't generate the code of the second part
                    return;
            } else if (getLeftOperand() instanceof Not) {
                if (((Not) getLeftOperand()).getOperand() instanceof BooleanLiteral) {
                    BooleanLiteral leftValue = (BooleanLiteral) ((Not) getLeftOperand()).getOperand();
                    if (leftValue.getValue()) // If the first value is false, don't generate the code of the second part
                        return;
                } else if (((Not) getLeftOperand()).getOperand() instanceof AbstractBranchable) {
                    AbstractBranchable leftBranchable = (AbstractBranchable) ((Not) getLeftOperand()).getOperand();
                    leftBranchable.setE(getE());
                    leftBranchable.setExpectedBool(true);
                    leftBranchable.codeGenBranch(compiler);

                } else if (((Not) getLeftOperand()).getOperand() instanceof AbstractIdentifier) {
                    AbstractIdentifier leftIdentifier = (AbstractIdentifier) ((Not) getLeftOperand()).getOperand();

                    leftIdentifier.codeGen(compiler, 0);
                    compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.R0));
                    compiler.addInstruction(new BNE(getE()));
                }

            } else if (getLeftOperand() instanceof AbstractIdentifier) {
                AbstractIdentifier leftIdentifier = (AbstractIdentifier) getLeftOperand();

                leftIdentifier.codeGen(compiler, 0);
                compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.R0));
                compiler.addInstruction(new BEQ(getE()));
            } else
                throw new DecacInternalError("Left Operand not a boolean");

            // Check second operand
            if (getRightOperand() instanceof AbstractBranchable) {
                AbstractBranchable rightBranchable = (AbstractBranchable) getRightOperand();
                rightBranchable.setE(getE());
                rightBranchable.setExpectedBool(false);
                rightBranchable.codeGenBranch(compiler);
            } else if (getRightOperand() instanceof BooleanLiteral) {
                BooleanLiteral rightValue = (BooleanLiteral) getRightOperand();
                if (rightValue.getValue())
                    compiler.addInstruction(new BRA(getE()));

            } else if (getRightOperand() instanceof Not) {
                if (((Not) getLeftOperand()).getOperand() instanceof BooleanLiteral) {
                    BooleanLiteral rightValue = (BooleanLiteral) ((Not) getRightOperand()).getOperand();
                    if (!rightValue.getValue())
                        compiler.addInstruction(new BRA(getE()));

                } else if (((Not) getLeftOperand()).getOperand() instanceof AbstractBranchable) {
                    AbstractBranchable rightBranchable = (AbstractBranchable) ((Not) getRightOperand()).getOperand();
                    rightBranchable.setE(getE());
                    rightBranchable.setExpectedBool(true);
                    rightBranchable.codeGenBranch(compiler);

                } else if (((Not) getLeftOperand()).getOperand() instanceof AbstractIdentifier) {
                    AbstractIdentifier rightIdentifier = (AbstractIdentifier) ((Not) getRightOperand()).getOperand();

                    rightIdentifier.codeGen(compiler, 0);
                    compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.R0));
                    compiler.addInstruction(new BNE(getE()));
                }

            } else if (getRightOperand() instanceof AbstractIdentifier) {
                AbstractIdentifier rightIdentifier = (AbstractIdentifier) getRightOperand();

                rightIdentifier.codeGen(compiler, 0);
                compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.R0));
                compiler.addInstruction(new BEQ(getE()));
            } else
                throw new DecacInternalError("Right Operand not a boolean");
        }
    }

    @Override
    public Instruction getImaInstruction(DVal value, GPRegister register) {
        throw new UnsupportedOperationException("Not applicable");
    }
}

