package jp.dsm.explore.recipe.crash_recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import jp.dsm.explore.recipe.common.BlockPredicate;
import jp.dsm.explore.recipe.common.BlockPredicateCodec;
import jp.dsm.explore.recipe.common.DropEntry;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public record CrashRecipe(Item hammer, BlockPredicate target, DropEntry baseOutput, List<DropEntry> additionalOutputs) {
    public static final Codec<CrashRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ForgeRegistries.ITEMS.getCodec().fieldOf("hammer").forGetter(CrashRecipe::hammer),
            BlockPredicateCodec.CODEC.fieldOf("target").forGetter(CrashRecipe::target),
            DropEntry.CODEC.fieldOf("base").forGetter(CrashRecipe::baseOutput),
            DropEntry.CODEC.listOf().optionalFieldOf("additional", List.of()).forGetter(CrashRecipe::additionalOutputs)
    ).apply(instance, CrashRecipe::new));
}
