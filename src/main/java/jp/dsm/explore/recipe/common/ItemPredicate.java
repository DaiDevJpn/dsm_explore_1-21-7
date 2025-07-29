package jp.dsm.explore.recipe.common;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public sealed interface ItemPredicate permits ItemPredicate.ItemMatch, ItemPredicate.TagMatch{
    boolean matches(Item item);

    default Set<Item> expandToItems(){
        return switch (this){
          case ItemMatch im -> {
              Item item = ForgeRegistries.ITEMS.getValue(im.itemId());
              yield  item != null ? Set.of(item) : Collections.emptySet();
          }
          case TagMatch tm -> ForgeRegistries.ITEMS.tags()
                  .getTag(TagKey.create(Registries.ITEM, tm.tagId()))
                  .stream()
                  .collect(Collectors.toUnmodifiableSet());
        };
    }

    record ItemMatch(ResourceLocation itemId) implements ItemPredicate {
        @Override
        public boolean matches(Item item) {
            return Objects.equals(ForgeRegistries.ITEMS.getKey(item), itemId);
        }
    }

    record TagMatch(TagKey<Item> tag) implements ItemPredicate {
        public TagMatch(ResourceLocation tagId){
            this(TagKey.create(Registries.ITEM, Objects.requireNonNull(tagId, "tagId must be nonNull")));
        }
        @Override
        public boolean matches(Item item) {
            return ForgeRegistries.ITEMS.tags()
                    .getTag(tag)
                    .contains(item);
        }

        public ResourceLocation tagId() {
            return tag.location();
        }
    }
}
