package com.fightcave;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class FightCaveWaveOverlay extends OverlayPanel {
    private final FightCavePlugin plugin;
    private final FightCaveConfig config;

    // Wave information for all 63 waves
    private static final Map<Integer, String> WAVE_MONSTERS = new HashMap<>();

    static {
        // Initialize wave information
        WAVE_MONSTERS.put(1, "1x Tz-Kih (22)");
        WAVE_MONSTERS.put(2, "2x Tz-Kih (22)");
        WAVE_MONSTERS.put(3, "1x Tz-Kek (45)");
        WAVE_MONSTERS.put(4, "1x Tz-Kek (45), 1x Tz-Kih (22)");
        WAVE_MONSTERS.put(5, "1x Tz-Kek (45), 2x Tz-Kih (22)");
        WAVE_MONSTERS.put(6, "2x Tz-Kek (45)");
        WAVE_MONSTERS.put(7, "1x Tok-Xil (90)");
        WAVE_MONSTERS.put(8, "1x Tok-Xil (90), 1x Tz-Kih (22)");
        WAVE_MONSTERS.put(9, "1x Tok-Xil (90), 2x Tz-Kih (22)");
        WAVE_MONSTERS.put(10, "1x Tok-Xil (90), 1x Tz-Kek (45)");
        WAVE_MONSTERS.put(11, "1x Tok-Xil (90), 1x Tz-Kek (45), 1x Tz-Kih (22)");
        WAVE_MONSTERS.put(12, "1x Tok-Xil (90), 1x Tz-Kek (45), 2x Tz-Kih (22)");
        WAVE_MONSTERS.put(13, "1x Tok-Xil (90), 2x Tz-Kek (45)");
        WAVE_MONSTERS.put(14, "2x Tok-Xil (90)");
        WAVE_MONSTERS.put(15, "1x Yt-MejKot (180)");
        WAVE_MONSTERS.put(16, "1x Yt-MejKot (180), 1x Tz-Kih (22)");
        WAVE_MONSTERS.put(17, "1x Yt-MejKot (180), 2x Tz-Kih (22)");
        WAVE_MONSTERS.put(18, "1x Yt-MejKot (180), 1x Tz-Kek (45)");
        WAVE_MONSTERS.put(19, "1x Yt-MejKot (180), 1x Tz-Kek (45), 1x Tz-Kih (22)");
        WAVE_MONSTERS.put(20, "1x Yt-MejKot (180), 1x Tz-Kek (45), 2x Tz-Kih (22)");
        WAVE_MONSTERS.put(21, "1x Yt-MejKot (180), 2x Tz-Kek (45)");
        WAVE_MONSTERS.put(22, "1x Yt-MejKot (180), 1x Tok-Xil (90)");
        WAVE_MONSTERS.put(23, "1x Yt-MejKot (180), 1x Tok-Xil (90), 1x Tz-Kih (22)");
        WAVE_MONSTERS.put(24, "1x Yt-MejKot (180), 1x Tok-Xil (90), 2x Tz-Kih (22)");
        WAVE_MONSTERS.put(25, "1x Yt-MejKot (180), 1x Tok-Xil (90), 1x Tz-Kek (45)");
        WAVE_MONSTERS.put(26, "1x Yt-MejKot (180), 1x Tok-Xil (90), 1x Tz-Kek (45), 1x Tz-Kih (22)");
        WAVE_MONSTERS.put(27, "1x Yt-MejKot (180), 1x Tok-Xil (90), 1x Tz-Kek (45), 2x Tz-Kih (22)");
        WAVE_MONSTERS.put(28, "1x Yt-MejKot (180), 1x Tok-Xil (90), 2x Tz-Kek (45)");
        WAVE_MONSTERS.put(29, "1x Yt-MejKot (180), 2x Tok-Xil (90)");
        WAVE_MONSTERS.put(30, "2x Yt-MejKot (180)");
        WAVE_MONSTERS.put(31, "1x Ket-Zek (360)");
        WAVE_MONSTERS.put(32, "1x Ket-Zek (360), 1x Tz-Kih (22)");
        WAVE_MONSTERS.put(33, "1x Ket-Zek (360), 2x Tz-Kih (22)");
        WAVE_MONSTERS.put(34, "1x Ket-Zek (360), 1x Tz-Kek (45)");
        WAVE_MONSTERS.put(35, "1x Ket-Zek (360), 1x Tz-Kek (45), 1x Tz-Kih (22)");
        WAVE_MONSTERS.put(36, "1x Ket-Zek (360), 1x Tz-Kek (45), 2x Tz-Kih (22)");
        WAVE_MONSTERS.put(37, "1x Ket-Zek (360), 2x Tz-Kek (45)");
        WAVE_MONSTERS.put(38, "1x Ket-Zek (360), 1x Tok-Xil (90)");
        WAVE_MONSTERS.put(39, "1x Ket-Zek (360), 1x Tok-Xil (90), 1x Tz-Kih (22)");
        WAVE_MONSTERS.put(40, "1x Ket-Zek (360), 1x Tok-Xil (90), 2x Tz-Kih (22)");
        WAVE_MONSTERS.put(41, "1x Ket-Zek (360), 1x Tok-Xil (90), 1x Tz-Kek (45)");
        WAVE_MONSTERS.put(42, "1x Ket-Zek (360), 1x Tok-Xil (90), 1x Tz-Kek (45), 1x Tz-Kih (22)");
        WAVE_MONSTERS.put(43, "1x Ket-Zek (360), 1x Tok-Xil (90), 1x Tz-Kek (45), 2x Tz-Kih (22)");
        WAVE_MONSTERS.put(44, "1x Ket-Zek (360), 1x Tok-Xil (90), 2x Tz-Kek (45)");
        WAVE_MONSTERS.put(45, "1x Ket-Zek (360), 2x Tok-Xil (90)");
        WAVE_MONSTERS.put(46, "1x Ket-Zek (360), 1x Yt-MejKot (180)");
        WAVE_MONSTERS.put(47, "1x Ket-Zek (360), 1x Yt-MejKot (180), 1x Tz-Kih (22)");
        WAVE_MONSTERS.put(48, "1x Ket-Zek (360), 1x Yt-MejKot (180), 2x Tz-Kih (22)");
        WAVE_MONSTERS.put(49, "1x Ket-Zek (360), 1x Yt-MejKot (180), 1x Tz-Kek (45)");
        WAVE_MONSTERS.put(50, "1x Ket-Zek (360), 1x Yt-MejKot (180), 1x Tz-Kek (45), 1x Tz-Kih (22)");
        WAVE_MONSTERS.put(51, "1x Ket-Zek (360), 1x Yt-MejKot (180), 1x Tz-Kek (45), 2x Tz-Kih (22)");
        WAVE_MONSTERS.put(52, "1x Ket-Zek (360), 1x Yt-MejKot (180), 2x Tz-Kek (45)");
        WAVE_MONSTERS.put(53, "1x Ket-Zek (360), 1x Yt-MejKot (180), 1x Tok-Xil (90)");
        WAVE_MONSTERS.put(54, "1x Ket-Zek (360), 1x Yt-MejKot (180), 1x Tok-Xil (90), 1x Tz-Kih (22)");
        WAVE_MONSTERS.put(55, "1x Ket-Zek (360), 1x Yt-MejKot (180), 1x Tok-Xil (90), 2x Tz-Kih (22)");
        WAVE_MONSTERS.put(56, "1x Ket-Zek (360), 1x Yt-MejKot (180), 1x Tok-Xil (90), 1x Tz-Kek (45)");
        WAVE_MONSTERS.put(57, "1x Ket-Zek (360), 1x Yt-MejKot (180), 1x Tok-Xil (90), 1x Tz-Kek (45), 1x Tz-Kih (22)");
        WAVE_MONSTERS.put(58, "1x Ket-Zek (360), 1x Yt-MejKot (180), 1x Tok-Xil (90), 1x Tz-Kek (45), 2x Tz-Kih (22)");
        WAVE_MONSTERS.put(59, "1x Ket-Zek (360), 1x Yt-MejKot (180), 1x Tok-Xil (90), 2x Tz-Kek (45)");
        WAVE_MONSTERS.put(60, "1x Ket-Zek (360), 1x Yt-MejKot (180), 2x Tok-Xil (90)");
        WAVE_MONSTERS.put(61, "1x Ket-Zek (360), 2x Yt-MejKot (180)");
        WAVE_MONSTERS.put(62, "2x Ket-Zek (360)");
        WAVE_MONSTERS.put(63, "1x TzTok-Jad (702), 4x Yt-HurKot (108)");
    }

    @Inject
    private FightCaveWaveOverlay(FightCavePlugin plugin, FightCaveConfig config) {
        super();
        setPosition(OverlayPosition.TOP_LEFT);
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!plugin.isInFightCave() || !config.showWaveInfo()) {
            return null;
        }

        int currentWave = plugin.getCurrentWave();
        if (currentWave <= 0 || currentWave > 63) {
            return null;
        }

        panelComponent.getChildren().clear();

        // Add title
        panelComponent.getChildren().add(TitleComponent.builder()
            .text("Fight Cave - Wave " + currentWave)
            .color(Color.GREEN)
            .build());

        // Add current wave monsters
        panelComponent.getChildren().add(LineComponent.builder()
            .left("Current:")
            .right(WAVE_MONSTERS.get(currentWave))
            .build());

        // Add next wave monsters if not on the final wave
        if (currentWave < 63) {
            panelComponent.getChildren().add(LineComponent.builder()
                .left("Next:")
                .right(WAVE_MONSTERS.get(currentWave + 1))
                .build());
        }

        // Add special notes for certain waves
        if (currentWave == 30) {
            panelComponent.getChildren().add(LineComponent.builder()
                .left("Note:")
                .right("Activate Protect from Magic for next wave!")
                .leftColor(Color.RED)
                .rightColor(Color.RED)
                .build());
        } else if (currentWave == 62) {
            panelComponent.getChildren().add(LineComponent.builder()
                .left("Note:")
                .right("Find orange Ket-Zek for Jad spawn!")
                .leftColor(Color.RED)
                .rightColor(Color.RED)
                .build());
        } else if (currentWave == 63) {
            panelComponent.getChildren().add(LineComponent.builder()
                .left("Note:")
                .right("Watch for Jad's animations!")
                .leftColor(Color.RED)
                .rightColor(Color.RED)
                .build());
        }

        // Add killing order reminder
        panelComponent.getChildren().add(LineComponent.builder()
            .left("Kill order:")
            .right("22 > 90 > 360 > 180 > 45")
            .build());

        return super.render(graphics);
    }
}