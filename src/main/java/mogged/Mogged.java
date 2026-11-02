package mogged;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Shared constants. The Forge entry point is mogged.forge.MoggedForge (one per Minecraft version family). */
public final class Mogged {
    public static final String MOD_ID = "mogged";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private Mogged() {
    }
}
