package mogged.forge.client;

import mogged.client.MoggedClient;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.Priority;

/** Client events (26.x, Forge 62+). Only loaded on the physical client. */
public final class ForgeClient {
    private ForgeClient() {
    }

    public static void init() {
        KeyMapping toggleKey = MoggedClient.init();
        RegisterKeyMappingsEvent.BUS.addListener(event -> event.register(toggleKey));
        TickEvent.ClientTickEvent.Post.BUS.addListener(event -> MoggedClient.onEndTick());
        ClientPlayerNetworkEvent.LoggingIn.BUS.addListener(event -> MoggedClient.onJoin());
        ClientPlayerNetworkEvent.LoggingOut.BUS.addListener(event -> MoggedClient.onDisconnect());
        // Lowest priority: after other mods have added their buttons, as the Fabric version's late phase
        ScreenEvent.Init.Post.BUS.addListener(Priority.LOWEST,
                event -> MoggedClient.onScreenInit(event.getScreen(), event.getListenersList(), event::addListener));
    }
}
