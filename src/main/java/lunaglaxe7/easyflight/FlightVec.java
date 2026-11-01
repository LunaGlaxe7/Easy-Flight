package lunaglaxe7.easyflight;

import net.minecraft.util.Mth;

public record FlightVec(float vx, float vy, float vz) {
    public static FlightVec from(float xRot, float yRot){
        float rxr = xRot * Mth.DEG_TO_RAD;
        float ryr = yRot * Mth.DEG_TO_RAD;
        float cp = Mth.cos(rxr);

        float dx = -Mth.sin(ryr) * cp;
        float dy = -Mth.sin(rxr);
        float dz = Mth.cos(ryr) * cp;
        float boost = 1 + 2f * Mth.clamp(Mth.abs(dy), 0.7f, 1);
        return new FlightVec(
                dx * boost, dy * boost, dz * boost
        );
    }
}
