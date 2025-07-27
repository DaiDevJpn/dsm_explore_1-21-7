package jp.dsm.explore.recipe.common;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public sealed interface BlockPredicate permits BlockPredicate.BlockMatch, BlockPredicate.TagMatch{
    boolean matches(Block block);

    default Set<Block> expandToBlocks(){
        if (this instanceof BlockMatch bm){
            Block block = ForgeRegistries.BLOCKS.getValue(bm.blockId());
            return block != null ? Set.of(block) : Collections.emptySet();
        }else if (this instanceof TagMatch tm){
            TagKey<Block> tag = TagKey.create(Registries.BLOCK, tm.tagId());
            return ForgeRegistries.BLOCKS.getValues()
                    .stream()
                    .filter(b -> b.defaultBlockState().is(tag))
                    .collect(Collectors.toUnmodifiableSet());
        }
        return Set.of();
    }

    record BlockMatch(ResourceLocation blockId) implements BlockPredicate{
        @Override
        public boolean matches(Block block) {
            return Objects.equals(ForgeRegistries.BLOCKS.getKey(block), blockId);
        }
    }

    record TagMatch(ResourceLocation tagId) implements BlockPredicate{
        @Override
        public boolean matches(Block block) {
            return block.defaultBlockState().is(TagKey.create(Registries.BLOCK, tagId));
        }
    }
}
