package jp.dsm.explore.recipe.common;

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
        return switch (this){
            case BlockMatch bm -> {
                Block block = ForgeRegistries.BLOCKS.getValue(bm.blockId());
                yield  block != null ? Set.of(block) : Collections.emptySet();
            }
            case TagMatch tm -> ForgeRegistries.BLOCKS.getValues()
                    .stream()
                    .filter(b -> b.defaultBlockState().is(TagKey.create(Registries.BLOCK, tm.tagId())))
                    .collect(Collectors.toUnmodifiableSet());
        };
    }

    record BlockMatch(ResourceLocation blockId) implements BlockPredicate{
        @Override
        public boolean matches(Block block) {
            return Objects.equals(ForgeRegistries.BLOCKS.getKey(block), blockId);
        }
    }

    record TagMatch(TagKey<Block> tag) implements BlockPredicate{

        public TagMatch(ResourceLocation tagId){
            this(TagKey.create(Registries.BLOCK, tagId));
        }

        @Override
        public boolean matches(Block block) {
            return block.defaultBlockState().is(tag);
        }

        public ResourceLocation tagId() {
            return tag.location();
        }
    }
}
