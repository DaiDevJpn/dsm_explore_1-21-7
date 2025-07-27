package jp.dsm.explore.recipe.common;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public record DropEntry(Item item, IntRange count, float chance) {
    public static final Codec<DropEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(DropEntry::item),
            IntRange.CODEC.fieldOf("count").forGetter(DropEntry::count),
            Codec.FLOAT.fieldOf("chance").orElse(1.0F).forGetter(DropEntry::chance)
    ).apply(instance, DropEntry::new));

    public ItemStack createStack(RandomSource rand){
        int min = count().min();
        int max = count().max();
        int amount = (min == max) ? min : rand.nextInt(min, max + 1);
        return new ItemStack(item, amount);
    }
}
