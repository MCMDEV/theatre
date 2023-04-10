package de.mcmdev.theatre.common.platform.source;

import lombok.Getter;

@Getter
public enum ScriptType {

    JAVA("java"),
    KOTLIN("kt");

    private final String fileExtension;

    ScriptType(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
