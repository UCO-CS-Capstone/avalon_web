package edu.uco.avalon;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

/**
 * Hashes and verifies a given password
 * Load using PasswordHash.getInstance()
 */
public class PasswordHash {

    private static int iterations = 2;
    private static int memory = 65536;
    private static int parallelism = 1;

    private Argon2 argon2;

    /**
     * Constructor is private so that it cannot be called
     * with "new" keyword elsewhere
     */
    private PasswordHash() {
        this.argon2 = Argon2Factory.create();
    }

    /**
     * Lazy-loads and holds the PasswordHash instance
     */
    private static class PasswordHashHolder {
        private static final PasswordHash INSTANCE = new PasswordHash();
    }

    /**
     * Gets the PasswordHash singleton instance
     * @return PasswordHash Instance
     */
    public static PasswordHash getInstance() {
        return PasswordHashHolder.INSTANCE;
    }

    /**
     * Hashes a given password with sane configuration
     * @param password String to hash
     * @return The encoded password
     */
    public String hash(String password) {
        return this.argon2.hash(iterations, memory, parallelism, password);
    }

    /**
     * Verifies that a given password and hash match
     * @param hashed   The hashed password
     * @param unhashed The unhashed password
     * @return Whether they matched or not
     */
    public boolean verify(String hashed, String unhashed) {
        return this.argon2.verify(hashed, unhashed);
    }

    public static int getIterations() {
        return iterations;
    }

    public static int getMemory() {
        return memory;
    }

    public static int getParallelism() {
        return parallelism;
    }

    public Argon2 getArgon2() {
        return argon2;
    }
}