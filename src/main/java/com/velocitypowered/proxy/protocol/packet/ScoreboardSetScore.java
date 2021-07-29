package com.velocitypowered.proxy.protocol.packet;

import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.proxy.connection.MinecraftSessionHandler;
import com.velocitypowered.proxy.protocol.MinecraftPacket;
import com.velocitypowered.proxy.protocol.ProtocolUtils;

import io.netty.buffer.ByteBuf;

/**
 * A missing Velocity packet
 */
public class ScoreboardSetScore implements MinecraftPacket {
	public static final byte CHANGE = (byte) 0;
	public static final byte REMOVE = (byte) 1;
	private String itemName;
	private byte action;
	private String scoreName;
	private int value;

	public ScoreboardSetScore() {
	}

	public ScoreboardSetScore(String itemName, byte action, String scoreName, int value) {
		this.itemName = itemName;
		this.action = action;
		this.scoreName = scoreName;
		this.value = value;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public byte getAction() {
		return action;
	}

	public void setAction(byte action) {
		this.action = action;
	}

	public String getScoreName() {
		return scoreName;
	}

	public void setScoreName(String scoreName) {
		this.scoreName = scoreName;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public void decode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion version) {
		itemName = ProtocolUtils.readString(buf);
		action = buf.readByte();
		if (version.compareTo(ProtocolVersion.MINECRAFT_1_8) >= 0) {
			scoreName = ProtocolUtils.readString(buf);
			if (action != REMOVE) {
				value = ProtocolUtils.readVarInt(buf);
			}
		} else if (action != REMOVE) {
			scoreName = ProtocolUtils.readString(buf);
			value = buf.readInt();
		}
	}

	@Override
	public void encode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion version) {
		ProtocolUtils.writeString(buf, itemName);
		buf.writeByte(action);
		if (version.compareTo(ProtocolVersion.MINECRAFT_1_8) >= 0) {
			ProtocolUtils.writeString(buf, scoreName);
			if (action != REMOVE) {
				ProtocolUtils.writeVarInt(buf, value);
			}
		} else if (action != REMOVE) {
			ProtocolUtils.writeString(buf, scoreName);
			buf.writeInt(value);
		}
	}

	@Override
	public boolean handle(MinecraftSessionHandler handler) {
		return false;
	}
}