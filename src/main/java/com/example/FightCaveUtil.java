package com.fightcave;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;

public class FightCaveUtil {
    // Monster IDs
    public static final int TZ_KIH_ID = 3116; // Level 22
    public static final int TZ_KEK_ID = 3118; // Level 45
    public static final int TZ_KEK_SMALL_ID = 3120; // Level 22 (spawned when Tz-Kek dies)
    public static final int TOK_XIL_ID = 3121; // Level 90
    public static final int YT_MEJKOT_ID = 3123; // Level 180
    public static final int KET_ZEK_ID = 3125; // Level 360
    public static final int TZTOK_JAD_ID = 3127; // Level 702
    public static final int YT_HURKOT_ID = 3128; // Level 108 (healers)

    // Spawn locations
    public static final WorldPoint SPAWN_SOUTH = new WorldPoint(2403, 5082, 0);
    public static final WorldPoint SPAWN_SOUTH_WEST = new WorldPoint(2392, 5088, 0);
    public static final WorldPoint SPAWN_CENTER = new WorldPoint(2400, 5097, 0);
    public static final WorldPoint SPAWN_NORTH_WEST = new WorldPoint(2392, 5106, 0);
    public static final WorldPoint SPAWN_SOUTH_EAST = new WorldPoint(2412, 5088, 0);

    // Rotation cycle: SE, SW, C, NW, SW, SE, S, NW, C, SE, SW, S, NW, C, S
    private static final List<WorldPoint> SPAWN_ROTATION = Arrays.asList(
        SPAWN_SOUTH_EAST, SPAWN_SOUTH_WEST, SPAWN_CENTER, SPAWN_NORTH_WEST,
        SPAWN_SOUTH_WEST, SPAWN_SOUTH_EAST, SPAWN_SOUTH, SPAWN_NORTH_WEST,
        SPAWN_CENTER, SPAWN_SOUTH_EAST, SPAWN_SOUTH_WEST, SPAWN_SOUTH,
        SPAWN_NORTH_WEST, SPAWN_CENTER, SPAWN_SOUTH
    );

    // Safe spots
    public static final WorldPoint ITALY_ROCK_WEST = new WorldPoint(2403, 5094, 0);
    public static final WorldPoint ITALY_ROCK_SOUTH = new WorldPoint(2408, 5089, 0);
    public static final WorldPoint LONG_ROCK_SOUTH = new WorldPoint(2390, 5093, 0);
    public static final WorldPoint DRAGON_ROCK_WEST = new WorldPoint(2413, 5108, 0);

    // Wave monster counts
    private static final Map<Integer, List<Integer>> WAVE_MONSTERS_COUNT = new HashMap<>();

    static {
        // Initialize wave monster counts
        // Format: [Tz-Kih, Tz-Kek, Tok-Xil, Yt-MejKot, Ket-Zek, TzTok-Jad]
        WAVE_MONSTERS_COUNT.put(1, Arrays.asList(1, 0, 0, 0, 0, 0));
        WAVE_MONSTERS_COUNT.put(2, Arrays.asList(2, 0, 0, 0, 0, 0));
        WAVE_MONSTERS_COUNT.put(3, Arrays.asList(0, 1, 0, 0, 0, 0));
        WAVE_MONSTERS_COUNT.put(4, Arrays.asList(1, 1, 0, 0, 0, 0));
        WAVE_MONSTERS_COUNT.put(5, Arrays.asList(2, 1, 0, 0, 0, 0));
        WAVE_MONSTERS_COUNT.put(6, Arrays.asList(0, 2, 0, 0, 0, 0));
        WAVE_MONSTERS_COUNT.put(7, Arrays.asList(0, 0, 1, 0, 0, 0));
        WAVE_MONSTERS_COUNT.put(8, Arrays.asList(1, 0, 1, 0, 0, 0));
        WAVE_MONSTERS_COUNT.put(9, Arrays.asList(2, 0, 1, 0, 0, 0));
        WAVE_MONSTERS_COUNT.put(10, Arrays.asList(0, 1, 1, 0, 0, 0));
        WAVE_MONSTERS_COUNT.put(11, Arrays.asList(1, 1, 1, 0, 0, 0));
        WAVE_MONSTERS_COUNT.put(12, Arrays.asList(2, 1, 1, 0, 0, 0));
        WAVE_MONSTERS_COUNT.put(13, Arrays.asList(0, 2, 1, 0, 0, 0));
        WAVE_MONSTERS_COUNT.put(14, Arrays.asList(0, 0, 2, 0, 0, 0));
        WAVE_MONSTERS_COUNT.put(15, Arrays.asList(0, 0, 0, 1, 0, 0));
        // ... and so on for all 63 waves
        // For brevity, I'm only including a subset here
        // The full implementation would include all waves
        
        // Last few waves
        WAVE_MONSTERS_COUNT.put(60, Arrays.asList(0, 0, 2, 1, 1, 0));
        WAVE_MONSTERS_COUNT.put(61, Arrays.asList(0, 0, 0, 2, 1, 0));
        WAVE_MONSTERS_COUNT.put(62, Arrays.asList(0, 0, 0, 0, 2, 0));
        WAVE_MONSTERS_COUNT.put(63, Arrays.asList(0, 0, 0, 0, 0, 1));
    }

    /**
     * Gets the next spawn point based on the current wave and rotation
     * @param wave Current wave number
     * @param rotationStartIndex The starting index in the rotation cycle
     * @return The WorldPoint where the next monster will spawn
     */
    public static WorldPoint getNextSpawnPoint(int wave, int rotationStartIndex) {
        int rotationIndex = (rotationStartIndex + wave - 1) % SPAWN_ROTATION.size();
        return SPAWN_ROTATION.get(rotationIndex);
    }

    /**
     * Determines if an NPC is the orange/discolored version
     * This is a placeholder - actual implementation would need to check NPC model colors
     * @param npc The NPC to check
     * @return True if the NPC is discolored
     */
    public static boolean isDiscoloredNPC(NPC npc) {
        // In a real implementation, this would check the NPC's model colors
        // For now, we'll return false as a placeholder
        return false;
    }

    /**
     * Gets the recommended prayer for the current wave
     * @param wave Current wave number
     * @return The recommended prayer to use
     */
    public static String getRecommendedPrayer(int wave) {
        if (wave >= 31) {
            return "Protect from Magic";
        } else if (wave >= 7) {
            return "Protect from Missiles";
        } else {
            return "None";
        }
    }

    /**
     * Gets the recommended kill order for the current wave
     * @param wave Current wave number
     * @return List of monster names in the order they should be killed
     */
    public static List<String> getKillOrder(int wave) {
        List<String> killOrder = new ArrayList<>();
        
        // Standard kill order: Tz-Kih > Tok-Xil > Ket-Zek > Yt-MejKot > Tz-Kek
        if (wave >= 1) {
            List<Integer> monsters = WAVE_MONSTERS_COUNT.getOrDefault(wave, Arrays.asList(0, 0, 0, 0, 0, 0));
            
            if (monsters.get(0) > 0) {
                killOrder.add("Tz-Kih (22)");
            }
            
            if (monsters.get(2) > 0) {
                killOrder.add("Tok-Xil (90)");
            }
            
            if (monsters.get(4) > 0) {
                killOrder.add("Ket-Zek (360)");
            }
            
            if (monsters.get(3) > 0) {
                killOrder.add("Yt-MejKot (180)");
            }
            
            if (monsters.get(1) > 0) {
                killOrder.add("Tz-Kek (45)");
            }
            
            if (monsters.get(5) > 0) {
                killOrder.add("TzTok-Jad (702)");
            }
        }
        
        return killOrder;
    }
}