package deanxbox.xaerosbiomefilter.render;

import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexConsumer;
import deanxbox.xaerosbiomefilter.XaerosBiomeFilterClient;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import org.joml.Matrix4f;
import xaero.lib.XaeroLib;
import xaero.map.graphics.CustomRenderTypes;
import xaero.map.region.texture.RegionTexture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public final class BiomeFilterRenderOverlay {
    private static final int TEXTURE_SIZE = 64;
    private static final Map<RegionTexture<?>, CachedMask> MASK_CACHE = Collections.synchronizedMap(new WeakHashMap<>());

    private BiomeFilterRenderOverlay() {
    }

    public static void clearCache() {
        MASK_CACHE.clear();
    }

    public static void renderFull(Matrix4f matrix, float x, float y, float width, float height, GpuTextureView textureView) {
        render(matrix, x, y, 0.0F, 0.0F, 1.0F, 1.0F, width, height, textureView);
    }

    public static void renderSubRect(
            Matrix4f matrix,
            float x,
            float y,
            float textureX1,
            float textureY1,
            float textureX2,
            float textureY2,
            float width,
            float height,
            GpuTextureView textureView
    ) {
        render(matrix, x, y, textureX1, textureY1, textureX2, textureY2, width, height, textureView);
    }

    private static void render(
            Matrix4f matrix,
            float x,
            float y,
            float textureX1,
            float textureY1,
            float textureX2,
            float textureY2,
            float width,
            float height,
            GpuTextureView textureView
    ) {
        if (!isActive() || width <= 0.0F || height <= 0.0F) {
            return;
        }

        RegionTexture<?> regionTexture = BiomeFilterRenderTextureRegistry.get(textureView);
        if (regionTexture == null) {
            return;
        }

        CachedMask mask = getMask(regionTexture);
        if (mask.runs().isEmpty()) {
            return;
        }

        float startPixelX = clampTexturePixel(Math.min(textureX1, textureX2) * TEXTURE_SIZE);
        float startPixelZ = clampTexturePixel(Math.min(textureY1, textureY2) * TEXTURE_SIZE);
        float endPixelX = clampTexturePixel(Math.max(textureX1, textureX2) * TEXTURE_SIZE);
        float endPixelZ = clampTexturePixel(Math.max(textureY1, textureY2) * TEXTURE_SIZE);
        if (endPixelX <= startPixelX || endPixelZ <= startPixelZ) {
            return;
        }

        VertexConsumer overlayBuffer = XaeroLib.INSTANCE.getClient().getBufferProvider().getBuffer(CustomRenderTypes.MAP_COLOR_OVERLAY);
        float pixelWidth = width / (endPixelX - startPixelX);
        float pixelHeight = height / (endPixelZ - startPixelZ);

        for (Run run : mask.runs()) {
            if (run.z() + 1.0F <= startPixelZ || run.z() >= endPixelZ || run.x2() <= startPixelX || run.x1() >= endPixelX) {
                continue;
            }

            float clippedX1 = Math.max(run.x1(), startPixelX);
            float clippedX2 = Math.min(run.x2(), endPixelX);
            float clippedZ1 = Math.max(run.z(), startPixelZ);
            float clippedZ2 = Math.min(run.z() + 1.0F, endPixelZ);
            fillBlack(
                    matrix,
                    overlayBuffer,
                    x + (clippedX1 - startPixelX) * pixelWidth,
                    y + (clippedZ1 - startPixelZ) * pixelHeight,
                    x + (clippedX2 - startPixelX) * pixelWidth,
                    y + (clippedZ2 - startPixelZ) * pixelHeight
            );
        }
    }

    private static CachedMask getMask(RegionTexture<?> regionTexture) {
        int revision = XaerosBiomeFilterClient.filterRevision();
        int textureVersion = regionTexture.getTextureVersion();
        CachedMask cached = MASK_CACHE.get(regionTexture);
        if (cached != null && cached.revision() == revision && cached.textureVersion() == textureVersion) {
            return cached;
        }

        CachedMask rebuilt = buildMask(regionTexture, revision, textureVersion);
        MASK_CACHE.put(regionTexture, rebuilt);
        return rebuilt;
    }

    private static CachedMask buildMask(RegionTexture<?> regionTexture, int revision, int textureVersion) {
        List<Run> runs = new ArrayList<>();
        for (int z = 0; z < TEXTURE_SIZE; z++) {
            int runStart = -1;
            for (int x = 0; x <= TEXTURE_SIZE; x++) {
                boolean filtered = x < TEXTURE_SIZE && isFiltered(regionTexture, x, z);
                if (filtered && runStart == -1) {
                    runStart = x;
                } else if (!filtered && runStart != -1) {
                    runs.add(new Run(z, runStart, x));
                    runStart = -1;
                }
            }
        }
        return new CachedMask(revision, textureVersion, runs);
    }

    private static boolean isFiltered(RegionTexture<?> regionTexture, int x, int z) {
        ResourceKey<Biome> biome = regionTexture.getBiome(x, z);
        return biome != null && !XaerosBiomeFilterClient.filter().allows(biome.identifier().toString());
    }

    private static boolean isActive() {
        return XaerosBiomeFilterClient.config().isEnabled()
                && !XaerosBiomeFilterClient.config().getSelectedBiomes().isEmpty();
    }

    private static float clampTexturePixel(float value) {
        return Math.max(0.0F, Math.min(TEXTURE_SIZE, value));
    }

    private static void fillBlack(Matrix4f matrix, VertexConsumer buffer, float x1, float y1, float x2, float y2) {
        buffer.addVertex(matrix, x1, y2, 0.0F).setColor(0.0F, 0.0F, 0.0F, 1.0F);
        buffer.addVertex(matrix, x2, y2, 0.0F).setColor(0.0F, 0.0F, 0.0F, 1.0F);
        buffer.addVertex(matrix, x2, y1, 0.0F).setColor(0.0F, 0.0F, 0.0F, 1.0F);
        buffer.addVertex(matrix, x1, y1, 0.0F).setColor(0.0F, 0.0F, 0.0F, 1.0F);
    }

    private record CachedMask(int revision, int textureVersion, List<Run> runs) {
    }

    private record Run(int z, int x1, int x2) {
    }
}
