package deanxbox.xaerosbiomefilter.mixin;

import deanxbox.xaerosbiomefilter.render.BiomeFilterRenderTextureRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xaero.lib.client.graphics.GpuTextureAndView;
import xaero.map.region.texture.RegionTexture;

@Mixin(value = RegionTexture.class, remap = false)
public abstract class RegionTextureRenderRegistryMixin {
    @Shadow
    protected GpuTextureAndView glColorTexture;

    @Inject(method = "getGlColorTexture", at = @At("RETURN"))
    private void xaerosBiomeFilter$registerRenderedTexture(CallbackInfoReturnable<GpuTextureAndView> callbackInfo) {
        BiomeFilterRenderTextureRegistry.register(callbackInfo.getReturnValue(), (RegionTexture<?>) (Object) this);
    }

    @Inject(method = "deleteTexturesAndBuffers", at = @At("HEAD"))
    private void xaerosBiomeFilter$unregisterDeletedTexture(CallbackInfo callbackInfo) {
        BiomeFilterRenderTextureRegistry.unregister(glColorTexture);
    }
}
