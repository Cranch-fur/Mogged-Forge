package mogged.platform;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import mogged.net.SkinRemove;
import mogged.net.SkinState;
import mogged.net.TexChunk;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

/**
 * The networking layer (Forge 52+: ChannelBuilder/SimpleChannel). One optional channel carries the three
 * messages both ways; a client or server without the mod is accepted, it just never gets them.
 */
public final class ModNetworking {
    private static final int PROTOCOL = 1;
    public static final SimpleChannel CHANNEL = ChannelBuilder.named(Ids.mod("main"))
            .networkProtocolVersion(PROTOCOL)
            .optional()
            .simpleChannel();

    private static BiConsumer<ServerPlayer, TexChunk> serverChunk = (player, message) -> {
    };
    private static BiConsumer<ServerPlayer, SkinState> serverState = (player, message) -> {
    };
    // Set by the client (ClientNetworking); a dedicated server never sets them
    private static Consumer<TexChunk> clientChunk = message -> {
    };
    private static Consumer<SkinState> clientState = message -> {
    };
    private static Consumer<SkinRemove> clientRemove = message -> {
    };

    private ModNetworking() {
    }

    public static void registerPayloads() {
        CHANNEL.messageBuilder(TexChunk.class, 0)
                .encoder(TexChunk::write)
                .decoder(TexChunk::read)
                .consumerMainThread((message, context) -> {
                    ServerPlayer sender = context.getSender();
                    if (sender != null) {
                        serverChunk.accept(sender, message);
                    } else {
                        clientChunk.accept(message);
                    }
                })
                .add();
        CHANNEL.messageBuilder(SkinState.class, 1)
                .encoder(SkinState::write)
                .decoder(SkinState::read)
                .consumerMainThread((message, context) -> {
                    ServerPlayer sender = context.getSender();
                    if (sender != null) {
                        serverState.accept(sender, message);
                    } else {
                        clientState.accept(message);
                    }
                })
                .add();
        CHANNEL.messageBuilder(SkinRemove.class, 2)
                .encoder(SkinRemove::write)
                .decoder(SkinRemove::read)
                .consumerMainThread((message, context) -> {
                    if (context.getSender() == null) {
                        clientRemove.accept(message);
                    }
                })
                .add();
        CHANNEL.build();
    }

    public static void registerServerReceivers(BiConsumer<ServerPlayer, TexChunk> onChunk, BiConsumer<ServerPlayer, SkinState> onState) {
        serverChunk = onChunk;
        serverState = onState;
    }

    /** Where messages from the server go on the client. */
    public static void setClientReceivers(Consumer<TexChunk> onChunk, Consumer<SkinState> onState, Consumer<SkinRemove> onRemove) {
        clientChunk = onChunk;
        clientState = onState;
        clientRemove = onRemove;
    }

    /** True if the other side of {@code connection} has the mod. */
    public static boolean present(Connection connection) {
        // The singleplayer/LAN host plays over an in-memory connection: same game, so it has the mod
        return connection != null && (connection.isMemoryConnection() || CHANNEL.isRemotePresent(connection));
    }

    /** True if the player's client has the mod. */
    public static boolean hasMod(ServerPlayer player) {
        return player.connection != null && present(player.connection.getConnection());
    }

    public static void send(ServerPlayer player, Object message) {
        CHANNEL.send(message, PacketDistributor.PLAYER.with(player));
    }
}
