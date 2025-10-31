package com.xsasakihaise.hellasforms.diagnostics;

/**
 * Enumeration of logging flags used to identify which logical subsystem
 * produced a message. Each flag renders as an upper-case token so that log
 * statements can be filtered easily when debugging issues reported by users.
 */
public enum LogFlag {
    CORE("CORE"),
    API("API"),
    CONFIG("CONFIG"),
    COMMANDS("COMMANDS"),
    ITEMS("ITEMS"),
    BATTLES("BATTLES"),
    LISTENERS("LISTENERS"),
    DIAGNOSTICS("DIAGNOSTICS");

    private final String token;

    LogFlag(String token) {
        this.token = token;
    }

    /**
     * Returns the formatted flag that will be appended to the debug prefix in
     * log statements.
     */
    public String format() {
        return "[" + token + "]";
    }

    @Override
    public String toString() {
        return format();
    }
}
