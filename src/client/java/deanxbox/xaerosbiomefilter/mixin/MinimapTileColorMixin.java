package deanxbox.xaerosbiomefilter.mixin;

import deanxbox.xaerosbiomefilter.XaerosBiomeFilterClient;
import deanxbox.xaerosbiomefilter.filter.ClientBiomeLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.minimap.region.MinimapTile;

@Mixin(value = MinimapTile.class, remap = false)
public abstract class MinimapTileColorMixin {
    @Shadow
    private byte[][][] red;
    @Shadow
    private byte[][][] green;
    @Shadow
    private byte[][][] blue;

    @Shadow
    public abstract int getX();

    @Shadow
    public abstract int getZ();

    @Inject(method = "setRGB", at = @At("HEAD"), cancellable = true)
    private void xaerosBiomeFilter$blackOutUnselectedBiome(int level, int localX, int localZ, int redValue, int greenValue, int blueValue, CallbackInfo callbackInfo) {
        int worldX = getX() * 16 + localX;
        int worldZ = getZ() * 16 + localZ;
        boolean allowed = ClientBiomeLookup.biomeIdAt(worldX, worldZ)
                .map(XaerosBiomeFilterClient.filter()::allows)
                .orElse(true);
        if (allowed) {
            return;
        }
        red[level][localX][localZ] = 0;
        green[level][localX][localZ] = 0;
        blue[level][localX][localZ] = 0;
        callbackInfo.cancel();
    }
}
