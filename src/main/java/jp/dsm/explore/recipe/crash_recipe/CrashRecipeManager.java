package jp.dsm.explore.recipe.crash_recipe;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

public class CrashRecipeManager extends SimpleJsonResourceReloadListener {


    protected CrashRecipeManager(HolderLookup.Provider p_378826_, Codec p_361980_, ResourceKey p_376437_) {
        super(p_378826_, p_361980_, p_376437_);
    }

    @Override
    protected void apply(Object object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {

    }
}
