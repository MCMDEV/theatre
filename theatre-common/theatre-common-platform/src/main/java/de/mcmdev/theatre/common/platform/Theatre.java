package de.mcmdev.theatre.common.platform;

import de.mcmdev.theatre.common.api.Script;
import de.mcmdev.theatre.common.platform.compiler.ScriptCompiler;
import de.mcmdev.theatre.common.platform.compiler.java.JavaCompiler;
import de.mcmdev.theatre.common.platform.compiler.kotlin.KotlinCompiler;
import de.mcmdev.theatre.common.platform.source.ScriptSource;
import de.mcmdev.theatre.common.platform.source.ScriptType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public class Theatre {

    private final File scriptsDirectory;
    private final File compiledDirectory;
    private final JavaCompiler javaCompiler;
    private final KotlinCompiler kotlinCompiler;
    private final Map<ScriptType, ScriptCompiler> compilerMap;
    private final Set<ScriptSource> scriptSources;

    public void collectScripts()  {
        scriptSources.clear();

        for (File file : scriptsDirectory.listFiles()) {
            if(file.isDirectory()) continue;
            String extension = file.getName().split("\\.", 2)[1];
            for (ScriptType scriptType : ScriptType.values()) {
                if(scriptType.getFileExtension().equals(extension)) {
                    ScriptSource scriptSource = new ScriptSource(file, scriptType);
                    scriptSources.add(scriptSource);
                }
            }
        }

        System.out.println("Loaded " + scriptSources.size() + " scripts.");
    }

    public void compileScripts()    {
        // Delete all compiled scripts
        File[] compiledScriptFiles = compiledDirectory.listFiles();
        if(compiledScriptFiles != null) {
            for (File file : compiledScriptFiles) {
                if(!file.delete())  {
                    throw new IllegalStateException("Could not delete script file " + file.getName() + ".");
                }
            }
        }

        // Compile all scripts
        for (ScriptSource scriptSource : getScriptSources()) {
            ScriptCompiler compiler = getCompiler(scriptSource.getScriptType());
            scriptSource.compileAndSave(compiler);
        }
    }

    @SneakyThrows
    public void loadScriptClasses(ClassLoader parentClassLoader)   {
        URL url = compiledDirectory.toURI().toURL();
        try(URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url}, parentClassLoader)) {
            for (File file : compiledDirectory.listFiles()) {
                String fileName = file.getName().split("\\.", 2)[0];
                Class<? extends Script> scriptClass = (Class<? extends Script>) urlClassLoader.loadClass(fileName);

                scriptSources.stream()
                        .filter(scriptSource -> scriptSource.getCompiledScript().getName().equals(fileName))
                        .findFirst()
                        .ifPresent(scriptSource -> {
                            scriptSource.getCompiledScript().loadClass(scriptClass);
                        });
            }
        }
    }

    private ScriptCompiler getCompiler(ScriptType scriptType)   {
        return compilerMap.get(scriptType);
    }


}
