package com.fightcave;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("fightcave")
public interface FightCaveConfig extends Config {
    @ConfigItem(
        keyName = "showWaveInfo",
        name = "Show wave information",
        description = "Displays information about the current wave",
        position = 0
    )
    default boolean showWaveInfo() {
        return true;
    }

    @ConfigItem(
        keyName = "showSafeSpots",
        name = "Show safe spots",
        description = "Highlights safe spots in the Fight Cave",
        position = 1
    )
    default boolean showSafeSpots() {
        return true;
    }

    @ConfigItem(
        keyName = "showPrayerHelp",
        name = "Show prayer helper",
        description = "Displays prayer switching helper for TzTok-Jad",
        position = 2
    )
    default boolean showPrayerHelp() {
        return true;
    }

    @ConfigItem(
        keyName = "showHealerInfo",
        name = "Show healer info",
        description = "Displays information about TzTok-Jad's healers",
        position = 3
    )
    default boolean showHealerInfo() {
        return true;
    }

    @ConfigItem(
        keyName = "showNextSpawnPoint",
        name = "Show next spawn point",
        description = "Highlights the next spawn point based on colored monsters",
        position = 4
    )
    default boolean showNextSpawnPoint() {
        return true;
    }

    @ConfigItem(
        keyName = "highlightJadSpawn",
        name = "Highlight Jad spawn",
        description = "Highlights where TzTok-Jad will spawn (based on the orange Ket-Zek)",
        position = 5
    )
    default boolean highlightJadSpawn() {
        return true;
    }
}