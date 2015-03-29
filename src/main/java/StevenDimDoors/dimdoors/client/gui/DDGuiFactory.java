package StevenDimDoors.dimdoors.client.gui;

import StevenDimDoors.dimdoors.config.DDProperties;
import StevenDimDoors.dimdoors.config.DDProperties.ConfigCategory;
import StevenDimDoors.dimdoors.mod_pocketDim;
import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

/**
 *
 * @author EvilLivesHere
 */
public class DDGuiFactory implements IModGuiFactory {

    public static class ConfigGuiScreen extends GuiConfig {

        public ConfigGuiScreen(GuiScreen parent) {
            super(parent, getConfigElements(), mod_pocketDim.modid, true, false, I18n.format("gui.dimdoors_config.title"));
        }

        private static List<IConfigElement> getConfigElements() {
            ArrayList<IConfigElement> list = new ArrayList<IConfigElement>();
            list.add(new DummyConfigElement.DummyCategoryElement(I18n.format("gui.dimdoors_config.general.title"), "gui.dimdoors_config.general", GeneralEntry.class));
            list.add(new DummyConfigElement.DummyCategoryElement(I18n.format("gui.dimdoors_config.loot.title"), "gui.dimdoors_config.loot", LootEntry.class));
            list.add(new DummyConfigElement.DummyCategoryElement(I18n.format("gui.dimdoors_config.crafting.title"), "gui.dimdoors_config.crafting", CraftingEntry.class));
            return list;
        }

        public static class CraftingEntry extends GuiConfigEntries.CategoryEntry {

            public CraftingEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
                super(owningScreen, owningEntryList, prop);
            }

            @Override
            protected GuiScreen buildChildScreen() {
                // This GuiConfig object specifies the configID of the object and as such will force-save when it is closed. The parent
                // GuiConfig object's entryList will also be refreshed to reflect the changes.
                return new GuiConfig(this.owningScreen,
                        DDProperties.instance().createConfigElementForCategory(ConfigCategory.CATEGORY_CRAFTING).getChildElements(),
                        this.owningScreen.modID, ConfigCategory.CATEGORY_CRAFTING.id, this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
                        this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
                        this.owningScreen.title,
                        I18n.format("gui.dimdoors_config.crafting.title"));
            }
        }

        public static class GeneralEntry extends GuiConfigEntries.CategoryEntry {

            public GeneralEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
                super(owningScreen, owningEntryList, prop);
            }

            @Override
            protected GuiScreen buildChildScreen() {
                // This GuiConfig object specifies the configID of the object and as such will force-save when it is closed. The parent
                // GuiConfig object's entryList will also be refreshed to reflect the changes.
                return new GuiConfig(this.owningScreen,
                        DDProperties.instance().createConfigElementForCategory(ConfigCategory.CATEGORY_GENERAL).getChildElements(),
                        this.owningScreen.modID, ConfigCategory.CATEGORY_CRAFTING.id, this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
                        this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
                        this.owningScreen.title,
                        I18n.format("gui.dimdoors_config.general.title"));
            }
        }

        public static class LootEntry extends GuiConfigEntries.CategoryEntry {

            public LootEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
                super(owningScreen, owningEntryList, prop);
            }

            @Override
            protected GuiScreen buildChildScreen() {
                // This GuiConfig object specifies the configID of the object and as such will force-save when it is closed. The parent
                // GuiConfig object's entryList will also be refreshed to reflect the changes.
                return new GuiConfig(this.owningScreen,
                        DDProperties.instance().createConfigElementForCategory(ConfigCategory.CATEGORY_LOOT).getChildElements(),
                        this.owningScreen.modID, ConfigCategory.CATEGORY_LOOT.id, this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
                        this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
                        this.owningScreen.title,
                        I18n.format("gui.dimdoors_config.loot.title"));
            }
        }
    }

    @Override
    public void initialize(Minecraft minecraftInstance) {
    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return ConfigGuiScreen.class;
    }

    @Override
    public Set<IModGuiFactory.RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public IModGuiFactory.RuntimeOptionGuiHandler getHandlerFor(IModGuiFactory.RuntimeOptionCategoryElement element) {
        return null;
    }
}
