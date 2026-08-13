package deanxbox.xaerosbiomefilter.ui;

import deanxbox.xaerosbiomefilter.XaerosBiomeFilterClient;
import deanxbox.xaerosbiomefilter.config.BiomeFilterMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public final class BiomeFilterScreen extends Screen {
    private static final int BIOMES_PER_PAGE = 8;

    private final Screen parent;
    private final List<Button> biomeListButtons = new ArrayList<>();
    private EditBox searchBox;
    private Button enabledButton;
    private Button modeButton;
    private String search = "";
    private int page;
    private List<BiomeEntry> filteredBiomes = List.of();

    public BiomeFilterScreen(Screen parent) {
        super(Component.translatable("text.xaeros-biome-filter.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        this.searchBox = new EditBox(this.font, centerX - 150, 38, 300, 20, Component.translatable("text.xaeros-biome-filter.search"));
        this.searchBox.setMaxLength(128);
        this.searchBox.setValue(search);
        this.searchBox.setHint(Component.translatable("text.xaeros-biome-filter.search"));
        this.searchBox.setResponder(value -> {
            search = value;
            page = 0;
            refreshBiomeListButtons();
        });
        addRenderableWidget(this.searchBox);

        this.enabledButton = Button.builder(enabledMessage(), button -> {
            XaerosBiomeFilterClient.config().setEnabled(!XaerosBiomeFilterClient.config().isEnabled());
            XaerosBiomeFilterClient.refreshFilter();
            button.setMessage(enabledMessage());
        }).bounds(centerX - 150, 64, 145, 20).build();
        addRenderableWidget(this.enabledButton);

        this.modeButton = Button.builder(modeMessage(), button -> {
            XaerosBiomeFilterClient.config().setMode(XaerosBiomeFilterClient.config().getMode().next());
            XaerosBiomeFilterClient.refreshFilter();
            button.setMessage(modeMessage());
        }).bounds(centerX + 5, 64, 145, 20).build();
        addRenderableWidget(this.modeButton);

        addRenderableWidget(Button.builder(Component.translatable("text.xaeros-biome-filter.clear"), button -> {
            XaerosBiomeFilterClient.config().clearSelectedBiomes();
            XaerosBiomeFilterClient.refreshFilter();
            refreshBiomeListButtons();
        }).bounds(centerX - 150, 88, 300, 20).build());

        addRenderableWidget(Button.builder(Component.translatable("text.xaeros-biome-filter.save"), button -> {
            XaerosBiomeFilterClient.saveConfig();
            Minecraft.getInstance().setScreenAndShow(parent);
        }).bounds(centerX - 102, this.height - 28, 100, 20).build());

        addRenderableWidget(Button.builder(Component.translatable("gui.back"), button -> Minecraft.getInstance().setScreenAndShow(parent))
                .bounds(centerX + 2, this.height - 28, 100, 20).build());

        refreshBiomeListButtons();
    }

    private void refreshBiomeListButtons() {
        for (Button button : biomeListButtons) {
            removeWidget(button);
        }
        biomeListButtons.clear();

        rebuildBiomeList();

        int centerX = this.width / 2;
        int y = 114;
        int start = page * BIOMES_PER_PAGE;
        int end = Math.min(filteredBiomes.size(), start + BIOMES_PER_PAGE);
        for (int index = start; index < end; index++) {
            BiomeEntry biome = filteredBiomes.get(index);
            addBiomeListButton(Button.builder(biomeMessage(biome), button -> {
                boolean nextSelected = !XaerosBiomeFilterClient.config().isSelected(biome.id());
                XaerosBiomeFilterClient.config().setSelected(biome.id(), nextSelected);
                XaerosBiomeFilterClient.refreshFilter();
                button.setMessage(biomeMessage(biome));
            }).bounds(centerX - 150, y, 300, 20).build());
            y += 22;
        }

        int maxPage = maxPage();
        Button previous = Button.builder(Component.literal("<"), button -> {
            page = Math.max(0, page - 1);
            refreshBiomeListButtons();
        }).bounds(centerX - 150, this.height - 52, 40, 20).build();
        previous.active = page > 0;
        addBiomeListButton(previous);

        Button next = Button.builder(Component.literal(">"), button -> {
            page = Math.min(maxPage(), page + 1);
            refreshBiomeListButtons();
        }).bounds(centerX + 110, this.height - 52, 40, 20).build();
        next.active = page < maxPage;
        addBiomeListButton(next);
    }

    private void addBiomeListButton(Button button) {
        biomeListButtons.add(button);
        addRenderableWidget(button);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        graphics.centeredText(this.font, this.title, this.width / 2, 14, 0xFFFFFF);
        graphics.centeredText(this.font, Component.translatable("text.xaeros-biome-filter.selected_count", XaerosBiomeFilterClient.config().getSelectedBiomes().size()), this.width / 2, this.height - 74, 0xA0A0A0);
        graphics.centeredText(this.font, Component.translatable("text.xaeros-biome-filter.page", page + 1, maxPage() + 1), this.width / 2, this.height - 48, 0xA0A0A0);
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreenAndShow(parent);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    private Component enabledMessage() {
        Component value = Component.translatable(XaerosBiomeFilterClient.config().isEnabled()
                ? "text.xaeros-biome-filter.on"
                : "text.xaeros-biome-filter.off");
        return Component.translatable("text.xaeros-biome-filter.enabled", value);
    }

    private Component modeMessage() {
        return Component.translatable(
                XaerosBiomeFilterClient.config().getMode() == BiomeFilterMode.SHOW_SELECTED
                        ? "text.xaeros-biome-filter.mode.show_selected"
                        : "text.xaeros-biome-filter.mode.hide_selected"
        );
    }

    private Component biomeMessage(BiomeEntry biome) {
        return Component.literal((XaerosBiomeFilterClient.config().isSelected(biome.id()) ? "[x] " : "[ ] ") + biome.displayName());
    }

    private void rebuildBiomeList() {
        String query = search.toLowerCase(Locale.ROOT).trim();
        List<BiomeEntry> biomes = new ArrayList<>();
        if (Minecraft.getInstance().level != null) {
            Registry<Biome> biomeRegistry = Minecraft.getInstance().level.registryAccess().lookupOrThrow(Registries.BIOME);
            for (Identifier id : biomeRegistry.keySet()) {
                String biomeId = id.toString();
                String displayName = BiomeNameFormatter.displayName(id);
                if (query.isEmpty()
                        || displayName.toLowerCase(Locale.ROOT).contains(query)
                        || biomeId.toLowerCase(Locale.ROOT).contains(query)) {
                    biomes.add(new BiomeEntry(biomeId, displayName));
                }
            }
        }
        biomes.sort(Comparator.comparing(BiomeEntry::displayName, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(BiomeEntry::id));
        this.filteredBiomes = biomes;
        this.page = Math.min(this.page, maxPage());
    }

    private int maxPage() {
        if (filteredBiomes.isEmpty()) {
            return 0;
        }
        return (filteredBiomes.size() - 1) / BIOMES_PER_PAGE;
    }

    private record BiomeEntry(String id, String displayName) {
    }
}
