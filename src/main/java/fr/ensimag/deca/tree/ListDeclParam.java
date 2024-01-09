package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclParam extends TreeList<AbstractDeclParam> {
    @Override
    public void decompile(IndentPrintStream s) {
        String delim = "";
        for (AbstractDeclParam i : this.getList()) {
            s.print(delim);
            i.decompile(s);
            delim = ",";
        }
    }
}
