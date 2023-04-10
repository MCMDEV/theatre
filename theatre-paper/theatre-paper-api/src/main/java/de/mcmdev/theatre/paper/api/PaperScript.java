package de.mcmdev.theatre.paper.api;

import de.mcmdev.theatre.common.api.Script;
import lombok.SneakyThrows;

public abstract class PaperScript implements Script {

    protected final PaperContext paperContext;

    public PaperScript(PaperContext paperContext) {
        this.paperContext = paperContext;
    }

    @Override
    public void enable() {
        onEnable();
    }

    @Override
    @SneakyThrows
    public void disable() {
        this.paperContext.disable();
        onDisable();
    }

    public abstract void onEnable();

    public abstract void onDisable();
}