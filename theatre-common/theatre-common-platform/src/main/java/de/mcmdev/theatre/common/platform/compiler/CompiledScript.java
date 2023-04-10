package de.mcmdev.theatre.common.platform.compiler;

import de.mcmdev.theatre.common.api.Script;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CompiledScript {

    private final String name;
    private final long compiledOn;
    private Class<? extends Script> scriptClass;

    public void loadClass(Class<? extends Script> scriptClass)  {
        if(this.scriptClass != null)    {
            throw new IllegalStateException("Script class is already set. Please recompile.");
        }

        this.scriptClass = scriptClass;
    }

}
