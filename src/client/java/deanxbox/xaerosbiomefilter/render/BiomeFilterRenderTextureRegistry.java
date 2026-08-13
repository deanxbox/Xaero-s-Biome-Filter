package deanxbox.xaerosbiomefilter.render;

import com.mojang.blaze3d.textures.GpuTextureView;
import xaero.lib.client.graphics.GpuTextureAndView;
import xaero.map.region.texture.RegionTexture;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public final class BiomeFilterRenderTextureRegistry {
    private static final Map<GpuTextureView, RegionTexture<?>> REGION_TEXTURES_BY_VIEW = Collections.synchronizedMap(new WeakHashMap<>());

    private BiomeFilterRenderTextureRegistry() {
    }

    public static void register(GpuTextureAndView textureAndView, RegionTexture<?> regionTexture) {
        if (textureAndView != null && textureAndView.view != null) {
            REGION_TEXTURES_BY_VIEW.put(textureAndView.view, regionTexture);
        }
    }

    public static void unregister(GpuTextureAndView textureAndView) {
        if (textureAndView != null && textureAndView.view != null) {
            REGION_TEXTURES_BY_VIEW.remove(textureAndView.view);
        }
    }

    public static RegionTexture<?> get(GpuTextureView textureView) {
        return textureView == null ? null : REGION_TEXTURES_BY_VIEW.get(textureView);
    }
}
