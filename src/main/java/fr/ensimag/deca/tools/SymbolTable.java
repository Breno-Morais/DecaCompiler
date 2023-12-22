package fr.ensimag.deca.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * Manage unique symbols.
 * 
 * A Symbol contains the same information as a String, but the SymbolTable
 * ensures the uniqueness of a Symbol for a given String value. Therefore,
 * Symbol comparison can be done by comparing references, and the hashCode()
 * method of Symbols can be used to define efficient HashMap (no string
 * comparison or hashing required).
 * 
 * @author gl25
 * @date 01/01/2024
 */
public class SymbolTable {
    private Map<String, Symbol> map = new HashMap<String, Symbol>();

    /**
     * Create or reuse a symbol.
     * 
     * If a symbol already exists with the same name in this table, then return
     * this Symbol. Otherwise, create a new Symbol and add it to the table.
     */

    public Symbol create(String name) {
        // Vérifie si le symbole existe déjà dans la table
        if (map.containsKey(name)) {
            // Retourne le symbole existant
            return map.get(name);
        } else {
            // Crée un nouveau symbole, l'ajoute à la table et le retourne
            Symbol nouveau = new Symbol(name);
            map.put(name, nouveau);
            return nouveau;
        }
    }



    public static class Symbol {
        // Constructor is private, so that Symbol instances can only be created
        // through SymbolTable.create factory (which thus ensures uniqueness
        // of symbols).
        private Symbol(String name) {
            super();
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }

        private String name;
    }
}
