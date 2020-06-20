package com.gm910.elkloriamod.api.networking.messages;

import java.util.function.Supplier;

import com.gm910.elkloriamod.api.networking.TaskEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class Networking {

	public static void sendToServer(StringMessage msg) {
		ModChannels.INSTANCE.sendToServer(msg);
	}
	
	public static void sendTo(StringMessage msg, ServerPlayerEntity play) {
		ModChannels.INSTANCE.send(PacketDistributor.PLAYER.with(() -> play), msg);
	}
	

	public static void sendTo(StringMessage msg, Chunk play) {
		ModChannels.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> play), msg);
	}
	
	public static class StringMessage {

		private String data = "";
		
		public StringMessage(String data) {
			this.data = data;
		}
		
		public StringMessage(ModTask data) {
			this.data = ModTask.writeToString(data);
		}
		
		public void encode(PacketBuffer buf) {
			buf.writeString(data);
		}
		
		public void handle(Supplier<NetworkEvent.Context> context) {
			NetworkEvent.Context ctxt = context.get();
			LogicalSide from = ctxt.getDirection().getOriginationSide();
			LogicalSide to = ctxt.getDirection().getReceptionSide();
			
			ModTask tasque = ModTask.getFromString(data);
			if (tasque == null) {
				
			} else {
				if (tasque.isLClient() && to.isClient() || tasque.isLServer() && to.isServer()) {
					TaskEvent e = tasque.createEvent();
					if (e.getSource() != null) {
						
						e.getTask().setWorldRef(e.getSource().getServerWorld());
						e.getTask().setSender(e.getSource());
					} else {
						e.getTask().setWorldRef(Minecraft.getInstance().world);
						
					}
					e.setSource(ctxt.getSender());
					ctxt.enqueueWork(() -> {
						MinecraftForge.EVENT_BUS.post(e);
					});
				} else {
					String s = "Suppressed task " + ModTask.writeToString(tasque) + " because it was sent to wrong side";
					System.out.println(s);
					ctxt.enqueueWork(() -> {
						System.out.println(s);
					});
				}
			}
	    }
		
		public static StringMessage fromBuffer(PacketBuffer buf) {
			return new StringMessage(buf.readString());
		}
		
	}
}
