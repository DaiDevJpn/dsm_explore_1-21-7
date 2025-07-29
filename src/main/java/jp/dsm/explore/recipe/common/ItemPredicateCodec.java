package jp.dsm.explore.recipe.common;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

public class ItemPredicateCodec {
    public static final Codec<ItemPredicate> CODEC = Codec.either(
            ResourceLocation.CODEC.fieldOf("item").codec().xmap(
                    ItemPredicate.ItemMatch::new,
                    ItemPredicate.ItemMatch::itemId
            ),
            ResourceLocation.CODEC.fieldOf("tag").codec().xmap(
                    ItemPredicate.TagMatch::new,
                    ItemPredicate.TagMatch::tagId
            )
    ).xmap(
            either -> either.map(b -> b, t -> t),
            pred -> (pred instanceof ItemPredicate.ItemMatch i) ? Either.left(i) : Either.right((ItemPredicate.TagMatch) pred)
    );
}
