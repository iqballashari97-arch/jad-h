package com.fightcave;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Prayer;
import net.runelite.api.SpriteID;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class FightCaveJadOverlay extends Overlay {
    private static final Color TITLE_COLOR = new Color(255, 146, 0);
    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 150);
    
    private final Client client;
    private final FightCavePlugin plugin;
    private final FightCaveConfig config;
    private final SpriteManager spriteManager;
    private final PanelComponent panelComponent = new PanelComponent();
    
    // Jad animation IDs
    private static final int JAD_MAGIC_ANIMATION = 2656;
    private static final int JAD_RANGE_ANIMATION = 2652;
    
    // Track Jad's last animation
    private int lastJadAnimation = -1;
    private long lastAnimationTime = 0;
    
    // Prayer icons
    private BufferedImage protectFromMagicImg;
    private BufferedImage protectFromRangedImg;
    
    @Inject
    private FightCaveJadOverlay(Client client, FightCavePlugin plugin, FightCaveConfig config, SpriteManager spriteManager) {
        setPosition(OverlayPosition.BOTTOM_RIGHT);
        setPriority(OverlayPriority.HIGH);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.spriteManager = spriteManager;
    }
    
    @Override
    public Dimension render(Graphics2D graphics) {
        if (!plugin.isInFightCave() || plugin.getCurrentWave() != 63 || !config.showPrayerHelp()) {
            return null;
        }
        
        // Check if we're fighting Jad
        boolean fightingJad = false;
        for (NPC npc : client.getNpcs()) {
            if (npc.getName() != null && npc.getName().equals("TzTok-Jad")) {
                fightingJad = true;
                
                // Check for Jad animations
                int animation = npc.getAnimation();
                if (animation == JAD_MAGIC_ANIMATION || animation == JAD_RANGE_ANIMATION) {
                    lastJadAnimation = animation;
                    lastAnimationTime = System.currentTimeMillis();
                }
                break;
            }
        }
        
        if (!fightingJad) {
            return null;
        }
        
        panelComponent.getChildren().clear();
        panelComponent.setBackgroundColor(BACKGROUND_COLOR);
        
        // Add title
        panelComponent.getChildren().add(TitleComponent.builder()
            .text("Jad Helper")
            .color(TITLE_COLOR)
            .build());
        
        // Load prayer icons if needed
        if (protectFromMagicImg == null) {
            protectFromMagicImg = spriteManager.getSprite(SpriteID.PRAYER_PROTECT_FROM_MAGIC, 0);
        }
        if (protectFromRangedImg == null) {
            protectFromRangedImg = spriteManager.getSprite(SpriteID.PRAYER_PROTECT_FROM_MISSILES, 0);
        }
        
        // Show prayer based on Jad's animation
        if (lastJadAnimation == JAD_MAGIC_ANIMATION) {
            panelComponent.getChildren().add(new ImageComponent(protectFromMagicImg));
            
            // Check if the player has the correct prayer active
            if (!client.isPrayerActive(Prayer.PROTECT_FROM_MAGIC)) {
                // Flash warning if the wrong prayer is active
                if (System.currentTimeMillis() % 1000 > 500) {
                    panelComponent.setBackgroundColor(new Color(255, 0, 0, 150));
                }
            }
        } else if (lastJadAnimation == JAD_RANGE_ANIMATION) {
            panelComponent.getChildren().add(new ImageComponent(protectFromRangedImg));
            
            // Check if the player has the correct prayer active
            if (!client.isPrayerActive(Prayer.PROTECT_FROM_MISSILES)) {
                // Flash warning if the wrong prayer is active
                if (System.currentTimeMillis() % 1000 > 500) {
                    panelComponent.setBackgroundColor(new Color(255, 0, 0, 150));
                }
            }
        } else {
            // No animation detected yet, show both prayers
            panelComponent.getChildren().add(new ImageComponent(protectFromRangedImg));
            panelComponent.getChildren().add(new ImageComponent(protectFromMagicImg));
        }
        
        // Reset animation after 4.8 seconds (Jad's attack speed)
        if (lastJadAnimation != -1 && System.currentTimeMillis() - lastAnimationTime > 4800) {
            lastJadAnimation = -1;
        }
        
        return panelComponent.render(graphics);
    }
}