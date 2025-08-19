package jp.dsm.explore.recipe.crash_recipe;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
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
import net.minecraft.world.level.block.state.BlockState;

import java.io.InputStream;
import java.util.Map;

public class CrashRecipeManager extends SimpleJsonResourceReloadListener<CrashRecipe> {

    private final Long2ObjectMap<CrashRecipe> index = new Long2ObjectOpenHashMap<>();

    public CrashRecipeManager(HolderLookup.Provider provider) {
        super(provider, CrashRecipe.CODEC, ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ModCore.MODID, "crash_recipes")));
    }

    private static final int ITEM_ANY = -1;

    public static long packKey(int itemId, int blockStateId){
        return ((long) itemId << 32) ^ (blockStateId & 0xFFFFFFFFL);
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
                        ModCore.LOGGER.info("[DSM][CrashRecipeManager] Loaded recipe: {}", rl);
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

            var tools = r.hammer().expandToItems();
            var blocks = r.target().expandToBlocks();

            if (tools.isEmpty()){
                for (Block b : blocks){
                    int bId = Block.getId(b.defaultBlockState());
                    index.putIfAbsent(packKey(ITEM_ANY, bId), r);
                }
            } else {
                for (Item i : tools){
                    int iId = Item.getId(i);
                    for (Block b : blocks){
                        int bId = Block.getId(b.defaultBlockState());
                        index.putIfAbsent(packKey(iId, bId), r);
                    }
                }
            }
        }

        ModCore.LOGGER.info("[DSM][CrashRecipeManager] Loaded {} recipes", index.size());
    }

    public CrashRecipe find(Item tool, BlockState blockState){
        int iId = Item.getId(tool);
        int bId = Block.getId(blockState);

        CrashRecipe r = index.get(packKey(iId, bId));
        if (r != null)return r;
        return index.get(packKey(ITEM_ANY, bId));
    }
}
