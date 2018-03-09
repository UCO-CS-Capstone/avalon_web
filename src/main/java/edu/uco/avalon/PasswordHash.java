package edu.uco.avalon;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class PasswordHash {

    private int iterations = 2;
    private int memory = 65536;
    private int parallelism = 1;
    private Argon2 argon2;

    PasswordHash() {
        this.argon2 = Argon2Factory.create();
    }

    private static class PasswordHashHolder {
        private static final PasswordHash INSTANCE = new PasswordHash();
    }

    public static PasswordHash getInstance() {
        return PasswordHashHolder.INSTANCE;
    }

    public String hash(String password) {
        return this.argon2.hash(this.iterations, this.memory, this.parallelism, password);
    }

    public boolean verify(String hashed, String unhashed) {
        return this.argon2.verify(hashed, unhashed);
    }
}