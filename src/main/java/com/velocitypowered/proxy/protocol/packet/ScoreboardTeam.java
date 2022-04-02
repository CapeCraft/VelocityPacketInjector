package com.velocitypowered.proxy.protocol.packet;

import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.proxy.connection.MinecraftSessionHandler;
import com.velocitypowered.proxy.protocol.MinecraftPacket;
import com.velocitypowered.proxy.protocol.ProtocolUtils;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * A missing Velocity packet
 */
public class ScoreboardTeam implements MinecraftPacket {
    public static final byte ADD = (byte) 0;
    public static final byte REMOVE = (byte) 1;
    public static final byte UPDATE = (byte) 2;
    public static final byte ADD_PLAYER = (byte) 3;
    public static final byte REMOVE_PLAYER = (byte) 4;
    private String name;
    private byte mode;
    private String displayName;
    private String prefix;
    private String suffix;
    private String nameTagVisibility;
    private String collisionRule;
    private int color;
    private byte friendlyFire;
    private List<String> players;

    public ScoreboardTeam() {
    }

    public ScoreboardTeam(String name, byte mode, String displayName, String prefix,
                          String suffix, String nameTagVisibility, String collisionRule,
                          int color, byte friendlyFire, List<String> players) {
        this.name = name;
        this.mode = mode;
        this.displayName = displayName;
        this.prefix = prefix;
        this.suffix = suffix;
        this.nameTagVisibility = nameTagVisibility;
        this.collisionRule = collisionRule;
        this.color = color;
        this.friendlyFire = friendlyFire;
        this.players = players;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getMode() {
        return mode;
    }

    public void setMode(byte mode) {
        this.mode = mode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getNameTagVisibility() {
        return nameTagVisibility;
    }

    public void setNameTagVisibility(String nameTagVisibility) {
        this.nameTagVisibility = nameTagVisibility;
    }

    public String getCollisionRule() {
        return collisionRule;
    }

    public void setCollisionRule(String collisionRule) {
        this.collisionRule = collisionRule;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public byte getFriendlyFire() {
        return friendlyFire;
    }

    public void setFriendlyFire(byte friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    @Override
    public void decode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion version) {
        name = ProtocolUtils.readString(buf);
        mode = buf.readByte();
        if (mode == ADD || mode == UPDATE) {
            displayName = ProtocolUtils.readString(buf);
            if (version.compareTo(ProtocolVersion.MINECRAFT_1_12_2) <= 0) {
                prefix = ProtocolUtils.readString(buf);
                suffix = ProtocolUtils.readString(buf);
            }
            friendlyFire = buf.readByte();
            if (version.compareTo(ProtocolVersion.MINECRAFT_1_8) >= 0) {
                nameTagVisibility = ProtocolUtils.readString(buf);
                if (version.compareTo(ProtocolVersion.MINECRAFT_1_9) >= 0) {
                    collisionRule = ProtocolUtils.readString(buf);
                }
                color = version.compareTo(ProtocolVersion.MINECRAFT_1_13) >= 0 ?
                        ProtocolUtils.readVarInt(buf) : buf.readByte();
                if (version.compareTo(ProtocolVersion.MINECRAFT_1_13) >= 0) {
                    prefix = ProtocolUtils.readString(buf);
                    suffix = ProtocolUtils.readString(buf);
                }
            }
        }
        if (mode == ADD || mode == ADD_PLAYER || mode == REMOVE_PLAYER) {
            int size = version.compareTo(ProtocolVersion.MINECRAFT_1_8) >= 0 ?
                    ProtocolUtils.readVarInt(buf) : buf.readShort();
            players = readPlayers(buf, size);
        }
    }

    @Override
    public void encode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion version) {
        ProtocolUtils.writeString(buf, name);
        buf.writeByte(mode);
        if (mode == ADD || mode == UPDATE) {
            ProtocolUtils.writeString(buf, displayName);
            if (version.compareTo(ProtocolVersion.MINECRAFT_1_12_2) <= 0) {
                ProtocolUtils.writeString(buf, prefix);
                ProtocolUtils.writeString(buf, suffix);
            }
            buf.writeByte(friendlyFire);
            if (version.compareTo(ProtocolVersion.MINECRAFT_1_8) >= 0) {
                ProtocolUtils.writeString(buf, nameTagVisibility);
                if (version.compareTo(ProtocolVersion.MINECRAFT_1_9) >= 0) {
                    ProtocolUtils.writeString(buf, collisionRule);
                }
                if (version.compareTo(ProtocolVersion.MINECRAFT_1_13) >= 0) {
                    ProtocolUtils.writeVarInt(buf, color);
                    ProtocolUtils.writeString(buf, prefix);
                    ProtocolUtils.writeString(buf, suffix);
                } else {
                    buf.writeByte(color);
                }
            }
        }
        if (mode == ADD || mode == ADD_PLAYER || mode == REMOVE_PLAYER) {
            if (version.compareTo(ProtocolVersion.MINECRAFT_1_8) >= 0) {
                ProtocolUtils.writeVarInt(buf, players.size());
            } else {
                buf.writeShort(players.size());
            }
            for (String player : players) {
                ProtocolUtils.writeString(buf, player);
            }
        }
    }

    @Override
    public boolean handle(MinecraftSessionHandler handler) {
        return false;
    }

    private static List<String> readPlayers(ByteBuf buf, int size) {
        List<String> players = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            players.add(ProtocolUtils.readString(buf));
        }
        return players;
    }
}