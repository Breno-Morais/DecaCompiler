package fr.ensimag.deca.codegen;

import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.ima.pseudocode.Label;

public class MethodName {
    private String curclass;
    private String name;

    public MethodName(String curclass, String name) {
        this.curclass = curclass;
        this.name = name;
    }

    public Label toLabel() {
        return new Label(getCurclass() + "." + getName());
    }

    public String getCurclass() {
        return curclass;
    }

    public String getName() {
        return name;
    }

    public Label toCodeLabel() {
        return new Label("code." + getCurclass() + "." + getName());
    }
}
