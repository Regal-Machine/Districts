package me.RegalMachine.Districts.Protection;

import java.util.UUID;

import me.RegalMachine.Districts.Main;
import me.RegalMachine.Districts.Players.Wizard;
import me.RegalMachine.Districts.Players.WizardBag;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class District {
	
	private UUID id;
	private Location center;
	private int radius;
	
	private Wizard owner;
	
	public District(Location center, int radius, Wizard wizard){
		id = UUID.randomUUID();
		owner = wizard;
		//Here we make the region.
		ProtectedCuboidRegion region = new ProtectedCuboidRegion(id.toString(),
				new BlockVector(new Vector(center.getBlockX() - radius, 0, center.getBlockZ() - radius)),
				new BlockVector(new Vector(center.getBlockX() + radius, 127, center.getBlockZ() + radius)));
		
		DefaultDomain owners = new DefaultDomain();
		
		owners.addPlayer(Main.guard.worldGuard().wrapPlayer(wizard.getPlayer()));
		
		region.setOwners(owners);
		RegionManager manager = Main.guard.worldGuard().getRegionManager(wizard.getPlayer().getWorld());
		
		manager.addRegion(region);
		try {
			manager.save();
		} catch (ProtectionDatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Here we finish making the region
		
		//Do things to the Config
		Main.districts.getConfig().createSection(id.toString());
		Main.districts.getConfig().addDefault(id.toString() + ".ownerUUID", owner.getPlayer().getUniqueId().toString());
		Main.districts.getConfig().addDefault(id.toString() + ".worldUUID", center.getWorld().getUID().toString());
		Main.districts.getConfig().addDefault(id.toString() + ".xyz", center.toVector());
		Main.districts.getConfig().addDefault(id.toString() + ".radius", radius);
		
		Main.districts.getConfig().options().copyDefaults(true);
		Main.districts.saveConfig();
		wizard.grantDistrict(this);
	}
	
	public District(String uuid){
		id = UUID.fromString(uuid);
		org.bukkit.util.Vector vector = Main.districts.getConfig().getVector(id.toString() + ".xyz");
		center = new Location(Bukkit.getWorld(UUID.fromString(Main.districts.getConfig().getString(id.toString() + ".worldUUID"))), vector.getX(), vector.getY(), vector.getZ());
		radius = Main.districts.getConfig().getInt(id.toString() + ".radius");
		owner = WizardBag.getWizard(Bukkit.getOfflinePlayer(UUID.fromString(Main.districts.getConfig().getString(id.toString() + ".ownerUUID"))).getPlayer());
		
		/*ProtectedCuboidRegion region = new ProtectedCuboidRegion(id.toString(),
				new BlockVector(new Vector(center.getBlockX() - radius, 0, center.getBlockZ() - radius)),
				new BlockVector(new Vector(center.getBlockX() + radius, 127, center.getBlockZ() + radius)));
		
		DefaultDomain owners = region.getOwners();
		
		owners.addPlayer(Main.guard.worldGuard().wrapPlayer(owner.getPlayer()));
		
		region.setOwners(owners);
		RegionManager manager = Main.guard.worldGuard().getRegionManager(center.getWorld());
		
		manager.addRegion(region);
		*/
		
	}
	
	public District(UUID uid){
		id = uid;
		org.bukkit.util.Vector vector = Main.districts.getConfig().getVector(id.toString() + ".xyz");
		center = new Location(Bukkit.getWorld(UUID.fromString(Main.districts.getConfig().getString(id.toString() + ".worldUUID"))), vector.getX(), vector.getY(), vector.getZ());
		radius = Main.districts.getConfig().getInt(id.toString() + ".radius");
		owner = WizardBag.getWizard(Bukkit.getPlayer(UUID.fromString(Main.districts.getConfig().getString(id.toString() + ".ownerUUID"))));
	}
	
	public UUID getUUID(){
		return id;
	}
	
	public Location getCenter(){
		return center;
	}
	
	public int getRadius(){
		return radius;
	}
	
	public ProtectedRegion getRegion(){
		return Main.guard.worldGuard().getRegionManager(center.getWorld()).getRegion(id.toString());
	}
	
	public Wizard getOwner(){
		return owner;
	}
	
	@SuppressWarnings("unused")
	private District(){}
}
