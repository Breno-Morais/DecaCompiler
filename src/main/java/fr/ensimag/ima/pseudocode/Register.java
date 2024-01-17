package fr.ensimag.ima.pseudocode;

/**
 * Register operand (including special registers like SP).
 * 
 * @author Ensimag
 * @date 01/01/2024
 */
public class Register extends DVal {
    private String name;
    protected Register(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Global Base register
     */
    public static final Register GB = new Register("GB");
    /**
     * Local Base register
     */
    public static final Register LB = new Register("LB");
    /**
     * Stack Pointer
     */
    public static final Register SP = new Register("SP");
    /**
     * General Purpose Registers. Array is private because Java arrays cannot be
     * made immutable, use getR(i) to access it.
     */
    private static final GPRegister[] R = initRegisters();
    /**
     * General Purpose Registers (The first 4 are guaranteed)
     */
    public static GPRegister getR(int i) {
        if(i <= maxRegister)
            return R[i];
        else
            return null;
    }
    /**
     * Convenience shortcut for R[0]
     */
    public static final GPRegister R0 = R[0];
    /**
     * Convenience shortcut for R[1]
     */
    public static final GPRegister R1 = R[1];
    /**
     * Convenience shortcut for R[2]
     */
    public static final GPRegister R2 = R[2];
    /**
     * Convenience shortcut for R[3]
     */
    public static final GPRegister R3 = R[3];

    static private GPRegister[] initRegisters() {
        GPRegister [] res = new GPRegister[16];
        for (int i = 0; i <= 15; i++) {
            res[i] = new GPRegister("R" + i, i);
        }
        return res;
    }

    /**
     * Maximum number of register possible to use
     */
    private static int maxRegister = 16;

    public void setMaxRegister(int X) {
        if(X >= 4 && X <= 16)
            maxRegister = X;
    }

    public int getMaxRegister() {
        return maxRegister;
    }
}
