package deanxbox.xaerosbiomefilter.mixin;

import com.mojang.blaze3d.textures.GpuTextureView;
import deanxbox.xaerosbiomefilter.render.BiomeFilterRenderOverlay;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.map.graphics.renderer.multitexture.MultiTextureRenderTypeRenderer;
import xaero.map.gui.GuiMap;

@Mixin(value = GuiMap.class, remap = false)
public abstract class GuiMapBiomeFilterRenderMixin {
    @Inject(method = "renderTexturedModalRectWithLighting3", at = @At("RETURN"))
    private static void xaerosBiomeFilter$renderFullBiomeOverlay(
            Matrix4f matrix,
            float x,
            float y,
            float width,
            float height,
            GpuTextureView texture,
            boolean hasLight,
            MultiTextureRenderTypeRenderer renderer,
            CallbackInfo callbackInfo
    ) {
        BiomeFilterRenderOverlay.renderFull(matrix, x, y, width, height, texture);
    }

    @Inject(method = "renderTexturedModalSubRectWithLighting", at = @At("RETURN"))
    private static void xaerosBiomeFilter$renderSubRectBiomeOverlay(
            Matrix4f matrix,
            float x,
            float y,
            float textureX1,
            float textureY1,
            float textureX2,
            float textureY2,
            float width,
            float height,
            GpuTextureView texture,
            boolean hasLight,
            MultiTextureRenderTypeRenderer renderer,
            CallbackInfo callbackInfo
    ) {
        BiomeFilterRenderOverlay.renderSubRect(matrix, x, y, textureX1, textureY1, textureX2, textureY2, width, height, texture);
    }
}
