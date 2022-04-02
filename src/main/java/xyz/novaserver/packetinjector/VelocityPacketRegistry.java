package xyz.novaserver.packetinjector;

import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.proxy.protocol.StateRegistry;
import com.velocitypowered.proxy.protocol.StateRegistry.PacketMapping;
import com.velocitypowered.proxy.protocol.StateRegistry.PacketRegistry;
import com.velocitypowered.proxy.protocol.packet.ScoreboardDisplay;
import com.velocitypowered.proxy.protocol.packet.ScoreboardObjective;
import com.velocitypowered.proxy.protocol.packet.ScoreboardSetScore;
import com.velocitypowered.proxy.protocol.packet.ScoreboardTeam;

import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * Util to register scoreboard packets which are missing on velocity
 */
public class VelocityPacketRegistry {

    // Packet id mapping method
    private Method map;

    /**
     * Registers missing velocity packets
     * @return true if registration was successful, false if not
     */
    public boolean registerPackets() {
        try {
            Method register = PacketRegistry.class.getDeclaredMethod("register", Class.class, Supplier.class, PacketMapping[].class);
            register.setAccessible(true);

            map = StateRegistry.class.getDeclaredMethod("map", int.class, ProtocolVersion.class, boolean.class);
            map.setAccessible(true);

            register.invoke(StateRegistry.PLAY.clientbound, ScoreboardDisplay.class, (Supplier<ScoreboardDisplay>) ScoreboardDisplay::new,
                    new PacketMapping[] {
                            map(0x3D, ProtocolVersion.MINECRAFT_1_7_2, true),
                            map(0x38, ProtocolVersion.MINECRAFT_1_9, true),
                            map(0x3A, ProtocolVersion.MINECRAFT_1_12, true),
                            map(0x3B, ProtocolVersion.MINECRAFT_1_12_1, true),
                            map(0x3E, ProtocolVersion.MINECRAFT_1_13, true),
                            map(0x42, ProtocolVersion.MINECRAFT_1_14, true),
                            map(0x43, ProtocolVersion.MINECRAFT_1_15, true),
                            map(0x4C, ProtocolVersion.MINECRAFT_1_17, true)
                    });
            register.invoke(StateRegistry.PLAY.clientbound, ScoreboardObjective.class, (Supplier<ScoreboardObjective>) ScoreboardObjective::new,
                    new PacketMapping[] {
                            map(0x3B, ProtocolVersion.MINECRAFT_1_7_2, true),
                            map(0x3F, ProtocolVersion.MINECRAFT_1_9, true),
                            map(0x41, ProtocolVersion.MINECRAFT_1_12, true),
                            map(0x42, ProtocolVersion.MINECRAFT_1_12_1, true),
                            map(0x45, ProtocolVersion.MINECRAFT_1_13, true),
                            map(0x49, ProtocolVersion.MINECRAFT_1_14, true),
                            map(0x4A, ProtocolVersion.MINECRAFT_1_15, true),
                            map(0x53, ProtocolVersion.MINECRAFT_1_17, true)
                    });
            register.invoke(StateRegistry.PLAY.clientbound, ScoreboardSetScore.class, (Supplier<ScoreboardSetScore>) ScoreboardSetScore::new,
                    new PacketMapping[] {
                            map(0x3C, ProtocolVersion.MINECRAFT_1_7_2, true),
                            map(0x42, ProtocolVersion.MINECRAFT_1_9, true),
                            map(0x44, ProtocolVersion.MINECRAFT_1_12, true),
                            map(0x45, ProtocolVersion.MINECRAFT_1_12_1, true),
                            map(0x48, ProtocolVersion.MINECRAFT_1_13, true),
                            map(0x4C, ProtocolVersion.MINECRAFT_1_14, true),
                            map(0x4D, ProtocolVersion.MINECRAFT_1_15, true),
                            map(0x56, ProtocolVersion.MINECRAFT_1_17, true)
                    });
            register.invoke(StateRegistry.PLAY.clientbound, ScoreboardTeam.class, (Supplier<ScoreboardTeam>) ScoreboardTeam::new,
                    new PacketMapping[] {
                            map(0x3E, ProtocolVersion.MINECRAFT_1_7_2, true),
                            map(0x41, ProtocolVersion.MINECRAFT_1_9, true),
                            map(0x43, ProtocolVersion.MINECRAFT_1_12, true),
                            map(0x44, ProtocolVersion.MINECRAFT_1_12_1, true),
                            map(0x47, ProtocolVersion.MINECRAFT_1_13, true),
                            map(0x4B, ProtocolVersion.MINECRAFT_1_14, true),
                            map(0x4C, ProtocolVersion.MINECRAFT_1_15, true),
                            map(0x55, ProtocolVersion.MINECRAFT_1_17, true)
                    });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Calls map method and returns the output packet mapping
     * @param id - packet id
     * @param version - protocol version
     * @param encodeOnly - disables packet decoding
     * @return result from map method
     * @throws ReflectiveOperationException - if reflection fails
     */
    private PacketMapping map(int id, ProtocolVersion version, boolean encodeOnly) throws ReflectiveOperationException {
        return (PacketMapping) map.invoke(null, id, version, encodeOnly);
    }
}