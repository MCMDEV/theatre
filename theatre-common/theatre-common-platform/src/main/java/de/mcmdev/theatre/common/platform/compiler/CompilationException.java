package de.mcmdev.theatre.common.platform.compiler;

public class CompilationException extends Exception {

    public CompilationException(String message) {
        super(message);
    }

    public CompilationException(Throwable cause) {
        super(cause);
    }
}
