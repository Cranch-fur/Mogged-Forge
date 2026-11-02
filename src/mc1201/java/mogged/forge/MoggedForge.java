package mogged.forge;

import mogged.Mogged;
import mogged.forge.client.ForgeClient;
import mogged.platform.ModNetworking;
import mogged.server.SkinServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

/** The mod's entry point (1.20.1, Forge 47). */
@Mod(Mogged.MOD_ID)
public final class MoggedForge {
    public MoggedForge(FMLJavaModLoadingContext context) {
        ModNetworking.registerPayloads();
        SkinServer.init();
        MinecraftForge.EVENT_BUS.addListener(MoggedForge::onServerStarting);
        MinecraftForge.EVENT_BUS.addListener(MoggedForge::onServerStopped);
        MinecraftForge.EVENT_BUS.addListener(MoggedForge::onLoggedIn);
        MinecraftForge.EVENT_BUS.addListener(MoggedForge::onLoggedOut);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ForgeClient.init(context.getModEventBus());
        }
    }

    private static void onServerStarting(ServerStartingEvent event) {
        SkinServer.onServerStarting(event.getServer());
    }

    private static void onServerStopped(ServerStoppedEvent event) {
        SkinServer.onServerStopped();
    }

    private static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SkinServer.onJoin(player);
        }
    }

    private static void onLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SkinServer.onLeave(player);
        }
    }
}
