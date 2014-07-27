package me.RegalMachine.Districts.util;

import org.bukkit.permissions.Permission;

public class BlockNumberPermission {
	
	
	Permission perm;
	String permName;
	int blockAmount;
	
	public BlockNumberPermission(String name, int amount){
		permName = name;
		blockAmount = amount;
		perm = new Permission(permName);
	}
	
	public Permission getPermission(){
		return perm;
	}
	
	public int getBlocksAllowed(){
		return blockAmount;
	}
	
	public String getPermString(){
		return permName;
	}
	
	
	
	
	
	
	
	@SuppressWarnings("unused")
	private BlockNumberPermission() {}
}
