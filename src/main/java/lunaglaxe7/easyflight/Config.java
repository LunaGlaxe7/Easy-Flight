package lunaglaxe7.easyflight;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static ModConfigSpec.IntValue GlidingSpeedBound = BUILDER.comment("Max speed bound when gliding")
                    .defineInRange("glidingspeedbound", 5 , 0, 20);

    static final ModConfigSpec SPEC = BUILDER.build();

}
