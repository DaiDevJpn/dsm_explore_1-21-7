package jp.dsm.explore.recipe.common;

import jp.dsm.explore.core.ModCore;
import jp.dsm.explore.recipe.crash_recipe.CrashRecipeManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = ModCore.MODID)
public class ReloadListenerRegister {

    private static CrashRecipeManager MANAGER;

    @SubscribeEvent
    public static void register(AddReloadListenerEvent e){
        MANAGER = new CrashRecipeManager(e.getRegistries());
        e.addListener(MANAGER);
    }

    public static CrashRecipeManager getManager() {
        return MANAGER;
    }
}
