package mogged.forge.client;

import mogged.client.MoggedClient;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

/** Client events (1.20.1, Forge 47). Only loaded on the physical client. */
public final class ForgeClient {
    private static KeyMapping toggleKey;

    private ForgeClient() {
    }

    public static void init(IEventBus modBus) {
        toggleKey = MoggedClient.init();
        modBus.addListener(ForgeClient::onRegisterKeys);
        MinecraftForge.EVENT_BUS.addListener(ForgeClient::onClientTick);
        MinecraftForge.EVENT_BUS.addListener(ForgeClient::onLoggingIn);
        MinecraftForge.EVENT_BUS.addListener(ForgeClient::onLoggingOut);
        // Lowest priority: after other mods have added their buttons, as the Fabric version's late phase
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, ForgeClient::onScreenInit);
    }

    private static void onRegisterKeys(RegisterKeyMappingsEvent event) {
        event.register(toggleKey);
    }

    private static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            MoggedClient.onEndTick();
        }
    }

    private static void onLoggingIn(ClientPlayerNetworkEvent.LoggingIn event) {
        MoggedClient.onJoin();
    }

    private static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        MoggedClient.onDisconnect();
    }

    private static void onScreenInit(ScreenEvent.Init.Post event) {
        MoggedClient.onScreenInit(event.getScreen(), event.getListenersList(), event::addListener);
    }
}
