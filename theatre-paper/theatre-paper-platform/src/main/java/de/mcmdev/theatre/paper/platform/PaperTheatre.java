package de.mcmdev.theatre.paper.platform;

import de.mcmdev.theatre.common.api.Script;
import de.mcmdev.theatre.common.platform.Theatre;
import de.mcmdev.theatre.common.platform.compiler.CompiledScript;
import de.mcmdev.theatre.common.platform.compiler.java.JavaCompiler;
import de.mcmdev.theatre.common.platform.compiler.kotlin.KotlinCompiler;
import de.mcmdev.theatre.common.platform.source.ScriptSource;
import de.mcmdev.theatre.common.platform.source.ScriptType;
import de.mcmdev.theatre.paper.api.PaperContext;
import de.mcmdev.theatre.paper.api.PaperScript;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.*;

public class PaperTheatre extends JavaPlugin {

    private File scriptsDirectory;
    private File compiledDirectory;
    private File librariesDirectory;
    private JavaCompiler javaCompiler;
    private KotlinCompiler kotlinCompiler;
    private Theatre theatre;

    private final Set<PaperScript> loadedScripts = new HashSet<>();

    @Override
    public void onLoad() {
        this.scriptsDirectory = new File(getDataFolder(), "./scripts/");
        this.compiledDirectory = new File(getDataFolder(), "./compiled/");
        this.librariesDirectory = new File(getDataFolder(), "./libraries/");
        if(!this.scriptsDirectory.exists()) {
            this.scriptsDirectory.mkdirs();
        }
        if(!this.compiledDirectory.exists())    {
            this.compiledDirectory.mkdirs();
        }
        if(!this.librariesDirectory.exists())   {
            this.librariesDirectory.mkdirs();
        }

        List<String> classpath = collectClasspath();

        this.javaCompiler = new JavaCompiler(this.compiledDirectory, classpath, ScriptType.JAVA);
        this.kotlinCompiler = new KotlinCompiler(this.compiledDirectory, classpath, ScriptType.KOTLIN);

        this.theatre = new Theatre(
                scriptsDirectory,
                compiledDirectory,
                javaCompiler,
                kotlinCompiler,
                Map.of(
                        ScriptType.JAVA, javaCompiler,
                        ScriptType.KOTLIN, kotlinCompiler
                ),
                new HashSet<>()
        );
    }

    @Override
    public void onEnable() {
        this.theatre.collectScripts();
        this.theatre.compileScripts();
        this.theatre.loadScriptClasses(getLibraryClassloader());

        Bukkit.getScheduler().runTask(this, () -> {
            loadScripts();
            enableScripts();
        });

        PluginCommand pluginCommand = getCommand("theatre");
        TheatreCommandExecutor commandExecutor = new TheatreCommandExecutor(this);
        pluginCommand.setExecutor(commandExecutor);
    }

    @Override
    public void onDisable() {
        disableScripts();
    }

    public void reload() {
        disableScripts();
        clearScripts();

        this.theatre.collectScripts();
        this.theatre.compileScripts();
        this.theatre.loadScriptClasses(getLibraryClassloader());

        loadScripts();
        enableScripts();
    }

    private void loadScripts() {
        for (ScriptSource scriptSource : this.theatre.getScriptSources()) {
            CompiledScript compiledScript = scriptSource.getCompiledScript();
            if(compiledScript == null) continue;
            Class<PaperScript> scriptClass = (Class<PaperScript>) compiledScript.getScriptClass();
            PaperContext paperContext = createContext(scriptClass);
            try {
                PaperScript paperScript = scriptClass.getConstructor(PaperContext.class).newInstance(paperContext);
                this.loadedScripts.add(paperScript);

                Bukkit.getLogger().info("Loaded script " + scriptClass.getSimpleName() + ".");
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                Bukkit.getLogger().info("Could not load script " + scriptClass.getName() + ".");
            }
        }
    }

    private void enableScripts()    {
        this.loadedScripts.forEach(Script::enable);
    }

    private void disableScripts()   {
        this.loadedScripts.forEach(Script::disable);
    }

    private void clearScripts() {
        this.loadedScripts.clear();
    }

    private PaperContext createContext(Class<PaperScript> scriptClass)  {
        return new PaperContext(this);
    }

    @SneakyThrows
    private List<String> collectClasspath()   {
        List<String> classpathFiles = new ArrayList<>();
        File pluginsFolder = Bukkit.getPluginsFolder();
        File librariesFolder = new File("libraries");
        if(librariesFolder.exists())    {
            Files.walk(librariesFolder.toPath(), 25).forEach(path -> classpathFiles.add(path.toAbsolutePath().toUri().getPath()));
        }
        classpathFiles.add(pluginsFolder.getAbsolutePath() + "/*");
        classpathFiles.add(librariesDirectory.getAbsolutePath() + "/*");
        return classpathFiles;
    }

    @SneakyThrows
    private ClassLoader getLibraryClassloader() {
        File[] files = librariesDirectory.listFiles();
        if(files == null) return new URLClassLoader(new URL[]{}, getClassLoader());
        URL[] urls = Arrays.stream(files)
                .map(file -> {
                    try {
                        return file.toURI().toURL();
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                }).toArray(URL[]::new);
        return new URLClassLoader(urls, getClassLoader());
    }
}
