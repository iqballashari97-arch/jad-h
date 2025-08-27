package com.fightcave;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

public class FightCaveOverlay extends Overlay {
    private final Client client;
    private final FightCavePlugin plugin;
    private final FightCaveConfig config;

    // Safe spot locations (Italy Rock, Long Rock, etc.)
    private static final WorldPoint ITALY_ROCK_WEST = new WorldPoint(2403, 5094, 0);
    private static final WorldPoint ITALY_ROCK_SOUTH = new WorldPoint(2408, 5089, 0);
    private static final WorldPoint LONG_ROCK_SOUTH = new WorldPoint(2390, 5093, 0);
    private static final WorldPoint DRAGON_ROCK_WEST = new WorldPoint(2413, 5108, 0);

    @Inject
    private FightCaveOverlay(Client client, FightCavePlugin plugin, FightCaveConfig config) {
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!plugin.isInFightCave()) {
            return null;
        }

        // Render safe spots if enabled
        if (config.showSafeSpots()) {
            renderSafeSpots(graphics);
        }

        // Render next spawn point if enabled
        if (config.showNextSpawnPoint()) {
            renderNextSpawnPoint(graphics);
        }

        // Highlight colored monsters for spawn prediction
        highlightColoredMonsters(graphics);

        return null;
    }

    private void renderSafeSpots(Graphics2D graphics) {
        // Italy Rock West
        highlightTile(graphics, ITALY_ROCK_WEST, new Color(0, 255, 0, 50), "Italy Rock (West)");
        
        // Italy Rock South
        highlightTile(graphics, ITALY_ROCK_SOUTH, new Color(0, 255, 0, 50), "Italy Rock (South)");
        
        // Long Rock South
        highlightTile(graphics, LONG_ROCK_SOUTH, new Color(0, 255, 0, 50), "Long Rock (South)");
        
        // Dragon Rock West
        highlightTile(graphics, DRAGON_ROCK_WEST, new Color(0, 255, 0, 50), "Dragon Rock (West)");
    }

    private void renderNextSpawnPoint(Graphics2D graphics) {
        // This would highlight the next spawn point based on the current wave
        // Implementation depends on the wave rotation logic
    }

    private void highlightColoredMonsters(Graphics2D graphics) {
        // Highlight the orange Ket-Zek in wave 62 to predict Jad spawn
        if (plugin.getCurrentWave() == 62 && config.highlightJadSpawn()) {
            for (NPC npc : client.getNpcs()) {
                if (npc.getName() == null) {
                    continue;
                }

                // Check for Ket-Zek
                if (npc.getName().equals("Ket-Zek")) {
                    NPCComposition composition = npc.getTransformedComposition();
                    if (composition != null) {
                        // Check if this is the orange Ket-Zek (would need to determine this)
                        // For now, we'll highlight all Ket-Zeks in wave 62
                        highlightNPC(graphics, npc, new Color(255, 165, 0, 100), "Jad will spawn here!");
                    }
                }
            }
        }
    }

    private void highlightTile(Graphics2D graphics, WorldPoint worldPoint, Color color, String text) {
        LocalPoint localPoint = LocalPoint.fromWorld(client, worldPoint);
        if (localPoint == null) {
            return;
        }

        Polygon poly = Perspective.getCanvasTilePoly(client, localPoint);
        if (poly != null) {
            OverlayUtil.renderPolygon(graphics, poly, color);
            
            Point canvasTextPoint = Perspective.getCanvasTextLocation(client, graphics, localPoint, text, 0);
            if (canvasTextPoint != null) {
                OverlayUtil.renderTextLocation(graphics, canvasTextPoint, text, Color.WHITE);
            }
        }
    }

    private void highlightNPC(Graphics2D graphics, NPC npc, Color color, String text) {
        Polygon poly = npc.getCanvasTilePoly();
        if (poly != null) {
            OverlayUtil.renderPolygon(graphics, poly, color);
            
            Point textLocation = npc.getCanvasTextLocation(graphics, text, 0);
            if (textLocation != null) {
                OverlayUtil.renderTextLocation(graphics, textLocation, text, Color.ORANGE);
            }
        }
    }
}