package fr.ensimag.deca.context;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tree.Visibility;

import static fr.ensimag.deca.context.EnvironmentExpUnit.assertTrue;


public class TestEnvironmentExp {
    public static void main(String[] args) {
        //Test 1
        EnvironmentExp racine = new EnvironmentExp(null);
        assertTrue(racine != null);

        //Test 2
        EnvironmentExp premierFils = new EnvironmentExp(racine);
        assertTrue(premierFils != null);

        //Test 3
        SymbolTable symbolTable = new SymbolTable();
        SymbolTable.Symbol x = symbolTable.create("x");
        ExpDefinition res = racine.get(x);
        assertTrue(res == null);

        //Test 4
        IntType i = new IntType(x);
        Location l = new Location(0, 0, "filename");
        Visibility v = Visibility.PUBLIC;
        ClassDefinition cd = new ClassDefinition(new ClassType(x, l, null), l, null);
        FieldDefinition def = new FieldDefinition(i, l, v, cd, 0);
        try {
            racine.declare(x, def);
        } catch (EnvironmentExp.DoubleDefException err){
            err.printStackTrace();
        }
        assertTrue(true);

        //Test 5
        res = racine.get(x);
        assertTrue(res != null);

        //Test 6
        ExpDefinition res2 = premierFils.get(x);
        assertTrue(res2 != null);
    }
}
