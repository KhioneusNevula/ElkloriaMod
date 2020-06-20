package com.gm910.elkloriamod.api.networking.messages;

import com.gm910.elkloriamod.api.networking.TaskEvent;
import com.gm910.elkloriamod.api.util.ModReflect;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;

/**
 * ALL MUST HAVE A NOARGS CONSTRUCTOR OR A SINGLE-STRING CONSTRUCTOR accepting the data it stores itself as
 * @author borah
 *
 */
public abstract class ModTask implements Runnable {
	
	private static final String separator = ((char)1)+"}}{{"+ ((char)0);
	
	/**
	 * Used to check logical sides, mostly
	 */
	private World worldRef;

	/**
	 * Player that sent the event, or null if from server
	 */
	private ServerPlayerEntity sender = null;
	
	public abstract void run();
	
	public TaskEvent createEvent() {
		return new TaskEvent(this, null);
	}
	
	public static ModTask getFromString(String str) {
		Class<? extends ModTask> clazz = ModTask.class;
		String classstring = str.split(separator)[0];
		String datastring = str.split(separator)[1];
		try {
			clazz = (Class<? extends ModTask>) Class.forName(classstring);
		} catch (ClassNotFoundException e) {
			return null;
		}
		ModTask tasque = ModReflect.construct(clazz);
		if (tasque == null) ModReflect.construct(clazz, datastring);
		if (tasque != null) {
			tasque.read(datastring);
		}
		
		return tasque;
	}
	
	public static String writeToString(ModTask towrite) {
		return towrite.$write();
	}
	
	public final String $write() {
		String s = "";
		s += this.getClass().getName();
		s += separator;
		String toW = this.write();
		if (toW.isEmpty()) toW = "null";
		s += toW;
		return s;
	}
	
	public String write() {
		return "";
	}
	
	protected void read(String str) {
		
	}
	
	public World getWorldRef() {
		return worldRef;
	}
	
	public void setWorldRef(World worldRef) {
		this.worldRef = worldRef;
	}
	
	public static String getSeparator() {
		return separator;
	}
	
	public ServerPlayerEntity getSender() {
		return sender;
	}
	
	public void setSender(ServerPlayerEntity sender) {
		this.sender = sender;
	}
	
	/**
	 * Whether it can be called on the logical client, default true
	 * @return
	 */
	public boolean isLClient() {
		return true;
	}
	
	/**
	 * Whether it can be called on logical server, default true
	 * @return
	 */
	public boolean isLServer() {
		return true;
	}
	
}
