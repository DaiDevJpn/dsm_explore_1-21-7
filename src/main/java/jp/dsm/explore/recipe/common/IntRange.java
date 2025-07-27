package jp.dsm.explore.recipe.common;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record IntRange(int min, int max) {
    public static final Codec<IntRange> BASIC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("min").forGetter(IntRange::min),
            Codec.INT.fieldOf("max").forGetter(IntRange::max)
    ).apply(instance, IntRange::new));

    public static final Codec<IntRange> CODEC = Codec.either(
            Codec.INT,
            BASIC
    ).xmap(
            either -> either.map(
                    integer -> new IntRange(integer, integer),
                    intRange -> intRange
            ),
            intRange -> intRange.min() == intRange.max()
                ? Either.left(intRange.min())
                : Either.right(intRange)
    );
}


