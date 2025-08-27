package com.fightcave;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
    name = "Fight Cave Helper",
    description = "Helps with the Fight Cave minigame by showing wave information, prayer switching, and safe spots",
    tags = {"fight", "cave", "jad", "tzharr", "fire", "cape"}
)
public class FightCavePlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private FightCaveConfig config;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private FightCaveOverlay overlay;

    @Inject
    private FightCaveWaveOverlay waveOverlay;

    @Inject
    private FightCaveJadOverlay jadOverlay;

    private boolean inFightCave;
    private int currentWave;

    @Override
    protected void startUp() throws Exception {
        inFightCave = false;
        currentWave = -1;
        overlayManager.add(overlay);
        overlayManager.add(waveOverlay);
        overlayManager.add(jadOverlay);
    }

    @Override
    protected void shutDown() throws Exception {
        inFightCave = false;
        currentWave = -1;
        overlayManager.remove(overlay);
        overlayManager.remove(waveOverlay);
        overlayManager.remove(jadOverlay);
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOADING) {
            inFightCave = false;
            currentWave = -1;
        }
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        if (event.getType() != ChatMessageType.GAMEMESSAGE) {
            return;
        }

        String message = event.getMessage();
        
        // Check if player is in Fight Cave
        if (message.contains("Wave:")) {
            inFightCave = true;
            try {
                currentWave = Integer.parseInt(message.replace("Wave:", "").trim());
                log.debug("Fight Cave wave: {}", currentWave);
            } catch (NumberFormatException e) {
                log.warn("Error parsing wave number", e);
            }
        }
    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned event) {
        if (!inFightCave) {
            return;
        }

        NPC npc = event.getNpc();
        
        // Check for TzTok-Jad spawn
        if (npc.getName() != null && npc.getName().equals("TzTok-Jad")) {
            log.debug("TzTok-Jad spawned");
        }
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged event) {
        if (!inFightCave || currentWave != 63) {
            return;
        }

        if (!(event.getActor() instanceof NPC)) {
            return;
        }

        NPC npc = (NPC) event.getActor();
        
        // Check if the animation is from TzTok-Jad
        if (npc.getName() != null && npc.getName().equals("TzTok-Jad")) {
            int animation = npc.getAnimation();
            // Handle Jad animations for prayer switching
            // Magic animation: 2656
            // Range animation: 2652
            if (animation == 2656) {
                log.debug("Jad magic attack");
            } else if (animation == 2652) {
                log.debug("Jad range attack");
            }
        }
    }

    public boolean isInFightCave() {
        return inFightCave;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    @Provides
    FightCaveConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(FightCaveConfig.class);
    }
}