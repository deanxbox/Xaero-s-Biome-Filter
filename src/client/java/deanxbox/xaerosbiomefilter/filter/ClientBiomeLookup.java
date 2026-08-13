package deanxbox.xaerosbiomefilter.filter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

public final class ClientBiomeLookup {
    private ClientBiomeLookup() {
    }

    public static Optional<String> biomeIdAt(int worldX, int worldZ) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level == null || minecraft.player == null) {
            return Optional.empty();
        }
        int y = minecraft.player.getBlockY();
        return level.getBiome(new BlockPos(worldX, y, worldZ))
                .unwrapKey()
                .map(ResourceKey<Biome>::identifier)
                .map(Object::toString);
    }
}
