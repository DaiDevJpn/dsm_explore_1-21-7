package jp.dsm.explore.recipe.common;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class ItemPredicateCodec {
    private record Raw(Optional<ResourceLocation> item, Optional<ResourceLocation> tag, boolean any){}

    public static final Codec<ItemPredicate> CODEC = RecordCodecBuilder.<Raw>create(i -> i.group(
            ResourceLocation.CODEC.optionalFieldOf("item").forGetter(Raw::item),
            ResourceLocation.CODEC.optionalFieldOf("tag").forGetter(Raw::tag),
            Codec.BOOL.optionalFieldOf("any", false).forGetter(Raw::any)
            ).apply(i, Raw::new))
            .flatXmap(
                    raw -> {
                        int count = (raw.item.isPresent() ? 1 : 0) + (raw.tag.isPresent() ? 1 : 0) + (raw.any ? 1 : 0);
                        if (count != 1) {
                            return DataResult.error(() -> "Exactly one of 'item', 'tag', or 'any' must be set");
                        }
                        if (raw.any) return DataResult.success(new ItemPredicate.Any());
                        if (raw.item.isPresent()) return DataResult.success(new ItemPredicate.ItemMatch(raw.item.get()));
                        return DataResult.success(new ItemPredicate.TagMatch(raw.tag.get()));
                    },
                    pred -> {
                        if (pred instanceof ItemPredicate.Any) {
                            return DataResult.success(new Raw(Optional.empty(), Optional.empty(), true));
                        } else if (pred instanceof ItemPredicate.ItemMatch im) {
                            return DataResult.success(new Raw(Optional.of(im.itemId()), Optional.empty(), false));
                        } else if (pred instanceof ItemPredicate.TagMatch tm) {
                            return DataResult.success(new Raw(Optional.empty(), Optional.of(tm.tagId()), false));
                        }
                        return DataResult.error(() -> "Unknown ItemPredicate impl: " + pred.getClass());
                    }
            );
}
