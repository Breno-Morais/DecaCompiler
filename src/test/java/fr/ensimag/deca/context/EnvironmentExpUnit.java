package fr.ensimag.deca.context;


public class EnvironmentExpUnit {
    static void assertTrue(boolean c) {
        if (c) {
            System.out.println("ok");
        } else {
            throw new RuntimeException();
        }
    }
}
