package jp.dsm.explore.recipe.common;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

public class BlockPredicateCodec {
    public static final Codec<BlockPredicate> CODEC = Codec.either(
            ResourceLocation.CODEC.fieldOf("block").codec().xmap(
                    BlockPredicate.BlockMatch::new,
                    BlockPredicate.BlockMatch::blockId
            ),
            ResourceLocation.CODEC.fieldOf("tag").codec().xmap(
                    BlockPredicate.TagMatch::new,
                    BlockPredicate.TagMatch::tagId
            )
    ).xmap(
            either -> either.map(b -> b, t -> t),
            pred -> (pred instanceof BlockPredicate.BlockMatch b) ? Either.left(b) : Either.right((BlockPredicate.TagMatch) pred)
    );
}
