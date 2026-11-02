package mogged.forge;

import mogged.Mogged;
import mogged.forge.client.ForgeClient;
import mogged.platform.ModNetworking;
import mogged.server.SkinServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

/** The mod's entry point (26.x, Forge 62+: EventBus 7, each event has its own BUS). */
@Mod(Mogged.MOD_ID)
public final class MoggedForge {
    public MoggedForge(FMLJavaModLoadingContext context) {
        ModNetworking.registerPayloads();
        SkinServer.init();
        ServerStartingEvent.BUS.addListener(event -> SkinServer.onServerStarting(event.getServer()));
        ServerStoppedEvent.BUS.addListener(event -> SkinServer.onServerStopped());
        PlayerEvent.PlayerLoggedInEvent.BUS.addListener(event -> {
            if (event.getEntity() instanceof ServerPlayer player) {
                SkinServer.onJoin(player);
            }
        });
        PlayerEvent.PlayerLoggedOutEvent.BUS.addListener(event -> {
            if (event.getEntity() instanceof ServerPlayer player) {
                SkinServer.onLeave(player);
            }
        });
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ForgeClient.init();
        }
    }
}
