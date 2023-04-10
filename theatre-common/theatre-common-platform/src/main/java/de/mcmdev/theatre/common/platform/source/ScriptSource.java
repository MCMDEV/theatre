package de.mcmdev.theatre.common.platform.source;

import de.mcmdev.theatre.common.platform.compiler.CompilationException;
import de.mcmdev.theatre.common.platform.compiler.CompiledScript;
import de.mcmdev.theatre.common.platform.compiler.ScriptCompiler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;

@Getter
@RequiredArgsConstructor
public class ScriptSource {

    private final File file;
    private final ScriptType scriptType;
    private CompiledScript compiledScript;

    public void compileAndSave(ScriptCompiler scriptCompiler)  {
        try {
            this.compiledScript = scriptCompiler.compile(this);
        }   catch (CompilationException exception)  {
            exception.printStackTrace();
        }
    }
}
