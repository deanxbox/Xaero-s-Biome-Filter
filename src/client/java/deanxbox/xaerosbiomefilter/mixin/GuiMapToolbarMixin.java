package deanxbox.xaerosbiomefilter.mixin;

import deanxbox.xaerosbiomefilter.XaerosBiomeFilterClient;
import deanxbox.xaerosbiomefilter.ui.BiomeFilterScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.lib.client.gui.widget.Tooltip;
import xaero.map.gui.GuiMap;
import xaero.map.gui.GuiTexturedButton;

@Mixin(value = GuiMap.class, remap = false)
public abstract class GuiMapToolbarMixin extends Screen {
    @Unique
    private static final Identifier XAEROS_BIOME_FILTER$ICON = Identifier.fromNamespaceAndPath(XaerosBiomeFilterClient.MOD_ID, "gui/biome_filter.png");

    @Shadow
    private Button zoomInButton;

    private GuiMapToolbarMixin() {
        super(Component.empty());
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void xaerosBiomeFilter$addToolbarButton(CallbackInfo callbackInfo) {
        int x = zoomInButton.getX();
        int y = zoomInButton.getY() - 20;
        addRenderableWidget(new GuiTexturedButton(
                x,
                y,
                20,
                20,
                0,
                0,
                16,
                16,
                XAEROS_BIOME_FILTER$ICON,
                button -> Minecraft.getInstance().setScreenAndShow(new BiomeFilterScreen((Screen) (Object) this)),
                () -> new Tooltip(Component.translatable("text.xaeros-biome-filter.tooltip")),
                16,
                16
        ));
    }
}
