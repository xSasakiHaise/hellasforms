package hellasforms.pokeballs;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Lunar Ball capture multiplier:
 * Night-only, symmetric by distance from full moon.
 * Anchors: full=6.0×, ±1=3.0×, new=0.05×.
 * Formula: mult(d) = 6.0 * 0.5^(d^1.394), where d = min(phase, 8 - phase) in [0..4].
 */
public final class LunarBallLogic {
    private static final double P = 1.394;   // derived exponent
    private static final double PEAK = 6.0;  // mult(0)
    private static final double RATIO = 0.5; // mult(1)/mult(0) = 3.0/6.0

    public double getCatchMultiplier(World world, BlockPos pos /*, Object target, Object thrower */) {
        if (world == null) return 1.0;

        long tod = world.getDayTime() % 24000L;
        boolean isNight = (tod >= 13000L && tod < 23000L);
        if (!isNight) return 1.0;

        int phase = (int)((world.getDayTime() / 24000L) % 8L); // 0..7 (0=Full, 4=New)
        int d = Math.min(phase, 8 - phase);                    // 0..4 distance from full
        double mult = PEAK * Math.pow(RATIO, Math.pow(d, P));

        if (mult < 0.05) mult = 0.05; // hard floor at new moon per design
        if (mult > PEAK) mult = PEAK;
        return mult;
    }
}
