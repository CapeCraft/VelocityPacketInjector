package com.github.NEZNAMY;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;

@Plugin(id = "velocitypacketinjector",
        name = "VelocityPacketInjector",
        version = "1.1.0",
        description = "Adds missing scoreboard packets to velocity",
        authors = {"NEZNAMY", "Nova"})
public class VelocityPacketInjector {
    private final Logger logger;
    private final VelocityPacketRegistry packetRegistry;

    @Inject
    public VelocityPacketInjector(Logger logger) {
        this.logger = logger;
        this.packetRegistry = new VelocityPacketRegistry();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("Registering missing scoreboard packets...");
        if (packetRegistry.registerPackets()) {
            logger.info("Successfully registered scoreboard packets!");
        } else {
            logger.error("Failed to register packets!");
        }
    }
}
