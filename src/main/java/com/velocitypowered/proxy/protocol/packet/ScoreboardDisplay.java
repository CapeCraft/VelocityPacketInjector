package com.velocitypowered.proxy.protocol.packet;

import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.proxy.connection.MinecraftSessionHandler;
import com.velocitypowered.proxy.protocol.MinecraftPacket;
import com.velocitypowered.proxy.protocol.ProtocolUtils;

import io.netty.buffer.ByteBuf;

/**
 * A missing Velocity packet
 */
public class ScoreboardDisplay implements MinecraftPacket {
	private byte position;
	private String name;

	public ScoreboardDisplay() {
	}

	public ScoreboardDisplay(byte position, String name) {
		this.position = position;
		this.name = name;
	}

	public byte getPosition() {
		return position;
	}

	public void setPosition(byte position) {
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void decode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion version) {
		position = buf.readByte();
		name = ProtocolUtils.readString(buf);
	}

	@Override
	public void encode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion version) {
		buf.writeByte(position);
		ProtocolUtils.writeString(buf, name);
	}

	@Override
	public boolean handle(MinecraftSessionHandler handler) {
		return false;
	}
}