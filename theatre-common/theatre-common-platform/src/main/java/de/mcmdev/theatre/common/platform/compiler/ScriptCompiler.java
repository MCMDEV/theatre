package de.mcmdev.theatre.common.platform.compiler;

import de.mcmdev.theatre.common.platform.source.ScriptSource;
import de.mcmdev.theatre.common.platform.source.ScriptType;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.List;

@RequiredArgsConstructor
public abstract class ScriptCompiler {

    protected final File compiledDirectory;
    protected final List<String> classpathFiles;
    protected final ScriptType scriptType;

    public abstract CompiledScript compile(ScriptSource scriptSource) throws CompilationException;

    public String buildClasspathString()    {
        return String.join(":", classpathFiles);
    }

}
