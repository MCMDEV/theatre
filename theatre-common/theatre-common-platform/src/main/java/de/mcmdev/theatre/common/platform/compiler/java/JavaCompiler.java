package de.mcmdev.theatre.common.platform.compiler.java;

import de.mcmdev.theatre.common.platform.compiler.CompilationException;
import de.mcmdev.theatre.common.platform.compiler.CompiledScript;
import de.mcmdev.theatre.common.platform.compiler.ScriptCompiler;
import de.mcmdev.theatre.common.platform.source.ScriptSource;
import de.mcmdev.theatre.common.platform.source.ScriptType;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JavaCompiler extends ScriptCompiler {

    public JavaCompiler(File compiledDirectory, List<String> classpath, ScriptType scriptType) {
        super(compiledDirectory, classpath, scriptType);
    }

    @Override
    public CompiledScript compile(ScriptSource scriptSource) throws CompilationException {
        String scriptName = scriptSource.getFile().getName().split("\\.", 2)[0];

        String absolutePath = scriptSource.getFile().getAbsolutePath();
        try {
            Process process = new ProcessBuilder("javac", "-d", compiledDirectory.getAbsolutePath(), "-cp", buildClasspathString(), absolutePath).start();
            int statusCode = process.waitFor();

            if(statusCode != 0) {
                try(BufferedReader errorReader = process.errorReader()) {
                    String errorString = errorReader.lines().collect(Collectors.joining("\n"));
                    throw new CompilationException(errorString);
                }
            }

            return new CompiledScript(scriptName, System.currentTimeMillis());
        } catch (IOException | InterruptedException e) {
            throw new CompilationException(e);
        }
    }
}
