package StevenDimDoors.dimdoors.client.gui;

import com.google.common.collect.ImmutableSet;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.IModGuiFactory;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

/**
 *
 * @author EvilLivesHere
 */
public class DDGuiFactory implements IModGuiFactory {

    public static class ConfigGuiScreen extends GuiScreen {

        private GuiScreen parent;

        public ConfigGuiScreen(GuiScreen parent) {
            this.parent = parent;
        }

        /**
         * Adds the buttons (and other controls) to the screen in question.
         */
        @SuppressWarnings("unchecked")
        @Override
        public void initGui() {
            this.buttonList.add(new GuiButton(1, this.width / 2 - 75, this.height - 38, I18n.format("gui.done")));
        }

        @Override
        protected void actionPerformed(GuiButton par1GuiButton) {
            if (par1GuiButton.enabled && par1GuiButton.id == 1) {
                FMLClientHandler.instance().showGuiScreen(parent);
            }
        }

        /**
         * Draws the screen and all the components in it.
         */
        @Override
        public void drawScreen(int par1, int par2, float par3) {
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, "Dimensional Doors test config screen", this.width / 2, 40, 0xFFFFFF);
            super.drawScreen(par1, par2, par3);
        }

    }

    @Override
    public void initialize(Minecraft minecraftInstance) {
    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return ConfigGuiScreen.class;
    }

    private static final Set<IModGuiFactory.RuntimeOptionCategoryElement> fmlCategories = ImmutableSet.of(new IModGuiFactory.RuntimeOptionCategoryElement("HELP", "FML"));

    @Override
    public Set<IModGuiFactory.RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return fmlCategories;
    }

    @Override
    public IModGuiFactory.RuntimeOptionGuiHandler getHandlerFor(IModGuiFactory.RuntimeOptionCategoryElement element) {
        return new IModGuiFactory.RuntimeOptionGuiHandler() {
            @Override
            public void paint(int x, int y, int w, int h) {
                // TODO Auto-generated method stub
            }

            @Override
            public void close() {
            }

            @Override
            public void addWidgets(List<Gui> widgets, int x, int y, int w, int h) {
                widgets.add(new GuiButton(100, x + 10, y + 10, "HELLO"));
            }

            @Override
            public void actionCallback(int actionId) {
                // TODO Auto-generated method stub
            }
        };
    }
}
