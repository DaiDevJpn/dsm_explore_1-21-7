package jp.dsm.explore.recipe.crash_recipe;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import jp.dsm.explore.core.ModCore;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class CrashRecipeManager extends SimpleJsonResourceReloadListener<CrashRecipe> {

    private final Int2ObjectMap<Int2ObjectMap<CrashRecipe>> index = new Int2ObjectOpenHashMap<>();

    public CrashRecipeManager(HolderLookup.Provider provider) {
        super(provider, CrashRecipe.CODEC, ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ModCore.MODID, "crash_recipes")));
    }


    @Override
    protected Map<ResourceLocation, CrashRecipe> prepare(ResourceManager rm, ProfilerFiller profiler) {
        Map<ResourceLocation, CrashRecipe> out = new java.util.HashMap<>();

        rm.listResources("crash_recipes", p -> p.getPath().endsWith(".json"))
                .forEach((rl, res) -> {
                    try (InputStream is = res.open()) {
                        // JSON データを読み込む
                        JsonElement jsonElement = GsonHelper.parse(new java.io.InputStreamReader(is));
                        // CrashRecipe.CODEC を使ってデシリアライズ
                        CrashRecipe recipe = CrashRecipe.CODEC.parse(JsonOps.INSTANCE, jsonElement)
                                .getOrThrow();
                        out.put(rl, recipe);
                        ModCore.LOGGER.info("[DSM] Loaded recipe: {}", rl);
                    } catch (Exception ex) {
                        ModCore.LOGGER.error("Error reading recipe from {}", rl, ex);
                    }
                });

        return out;
    }

    @Override
    protected void apply(Map<ResourceLocation, CrashRecipe> data, ResourceManager rm, ProfilerFiller profiler) {
        index.clear();
        for (CrashRecipe r : data.values()){
            int hammerId = Item.getId(r.hammer());
            Int2ObjectMap<CrashRecipe> byBlock = index.computeIfAbsent(
                    hammerId, k -> new Int2ObjectOpenHashMap<>()
            );

            for (Block b : r.target().expandToBlocks()){
                int blockId = Block.getId(b.defaultBlockState());
                byBlock.putIfAbsent(blockId, r);
            }
        }
        ModCore.LOGGER.info("[DSM]apply() hit, map size={}", data.size());
        data.forEach((id, r) -> ModCore.LOGGER.info("[DSM]key={} hammer={}", id, r.hammer()));
        ModCore.LOGGER.info("[DSM] Loaded {} recipes", index.size());
    }

    public CrashRecipe find(Item hammer, Block block){
        Int2ObjectMap<CrashRecipe> byBlock = index.get(Item.getId(hammer));
        if (byBlock == null)return null;
        return byBlock.get(Block.getId(block.defaultBlockState()));
    }
}
