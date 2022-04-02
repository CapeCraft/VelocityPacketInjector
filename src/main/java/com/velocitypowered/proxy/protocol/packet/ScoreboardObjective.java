package com.velocitypowered.proxy.protocol.packet;

import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.proxy.connection.MinecraftSessionHandler;
import com.velocitypowered.proxy.protocol.MinecraftPacket;
import com.velocitypowered.proxy.protocol.ProtocolUtils;
import io.netty.buffer.ByteBuf;

/**
 * A missing Velocity packet
 */
public class ScoreboardObjective implements MinecraftPacket {
    public static final byte ADD = (byte) 0;
    public static final byte REMOVE = (byte) 1;
    public static final byte CHANGE = (byte) 2;
    private String name;
    private String value;
    private HealthDisplay type;
    private byte action;

    public ScoreboardObjective() {
    }

    public ScoreboardObjective(String name, String value, HealthDisplay type, byte action) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public HealthDisplay getType() {
        return type;
    }

    public void setType(HealthDisplay type) {
        this.type = type;
    }

    public byte getAction() {
        return action;
    }

    public void setAction(byte action) {
        this.action = action;
    }

    @Override
    public void decode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion version) {
        name = ProtocolUtils.readString(buf);
        if (version.compareTo(ProtocolVersion.MINECRAFT_1_7_6) <= 0) {
            value = ProtocolUtils.readString(buf);
        }
        action = buf.readByte();
        if (version.compareTo(ProtocolVersion.MINECRAFT_1_8) >= 0 && action != REMOVE) {
            value = ProtocolUtils.readString(buf);
            if (version.compareTo(ProtocolVersion.MINECRAFT_1_13) >= 0) {
                type = HealthDisplay.values()[ProtocolUtils.readVarInt(buf)];
            } else {
                type = HealthDisplay.valueOf(ProtocolUtils.readString(buf).toUpperCase());
            }
        }
    }

    @Override
    public void encode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion version) {
        ProtocolUtils.writeString(buf, name);
        if (version.compareTo(ProtocolVersion.MINECRAFT_1_7_6) <= 0) {
            ProtocolUtils.writeString(buf, value);
        }
        buf.writeByte(action);
        if (version.compareTo(ProtocolVersion.MINECRAFT_1_8) >= 0 && action != REMOVE) {
            ProtocolUtils.writeString(buf, value);
            if (version.compareTo(ProtocolVersion.MINECRAFT_1_13) >= 0) {
                ProtocolUtils.writeVarInt(buf, type.ordinal());
            } else {
                ProtocolUtils.writeString(buf, type.name().toUpperCase());
            }
        }
    }

    @Override
    public boolean handle(MinecraftSessionHandler handler) {
        return false;
    }

    public enum HealthDisplay {
        INTEGER,
        HEARTS
    }
}