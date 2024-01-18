package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;

public abstract class AbstractDeclField extends Tree {
    public abstract AbstractVisibility getVisibility();

    public abstract AbstractIdentifier getType();

    public abstract AbstractIdentifier getField();

    public abstract AbstractInitialization getInitialization();
}
