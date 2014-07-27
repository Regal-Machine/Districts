package me.RegalMachine.Districts.Protection;

import java.util.ArrayList;
import java.util.List;
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
	
	private ProtectedRegion regionn;
	
	private Wizard owner;
	private List<String> trusted; //Strings are UUID of Trusted Players
	
	public District(Location center, int radius, Wizard wizard){
		id = UUID.randomUUID();
		owner = wizard;
		trusted = new ArrayList<String>();
		this.center = center;
		//Here we make the region.
		ProtectedRegion region = new ProtectedCuboidRegion(id.toString(),
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
			e.printStackTrace();
		}
		
		regionn = manager.getRegion(id.toString());
		
		//Here we finish making the region
		
		//Do things to the Config
		Main.districts.getConfig().createSection("Districts." + id.toString());
		Main.districts.getConfig().addDefault("Districts." + id.toString() + ".ownerUUID", owner.getPlayer().getUniqueId().toString());
		Main.districts.getConfig().addDefault("Districts." + id.toString() + ".worldUUID", center.getWorld().getUID().toString());
		Main.districts.getConfig().addDefault("Districts." + id.toString() + ".xyz", center.toVector());
		Main.districts.getConfig().addDefault("Districts." + id.toString() + ".radius", radius);
		Main.districts.getConfig().addDefault("Districts." + id.toString() + ".trustedUUID", trusted);
		
		Main.districts.getConfig().options().copyDefaults(true);
		Main.districts.saveConfig();
		wizard.grantDistrict(this);
	}
	
	public District(String uuid){
		id = UUID.fromString(uuid);
		org.bukkit.util.Vector vector = Main.districts.getConfig().getVector("Districts." + id.toString() + ".xyz");
		center = new Location(Bukkit.getWorld(UUID.fromString(Main.districts.getConfig().getString("Districts." + id.toString() + ".worldUUID"))), vector.getX(), vector.getY(), vector.getZ());
		radius = Main.districts.getConfig().getInt(id.toString() + ".radius");
		owner = WizardBag.getWizard(Bukkit.getOfflinePlayer(UUID.fromString(Main.districts.getConfig().getString("Districts." + id.toString() + ".ownerUUID"))).getPlayer());
		trusted = Main.districts.getConfig().getStringList("Districts." + id.toString() + ".trustedUUID");
		
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
		org.bukkit.util.Vector vector = Main.districts.getConfig().getVector("Districts." + id.toString() + ".xyz");
		center = new Location(Bukkit.getWorld(UUID.fromString(Main.districts.getConfig().getString("Districts." + id.toString() + ".worldUUID"))), vector.getX(), vector.getY(), vector.getZ());
		radius = Main.districts.getConfig().getInt("Districts." + id.toString() + ".radius");
		owner = WizardBag.getWizard(Bukkit.getPlayer(UUID.fromString(Main.districts.getConfig().getString("Districts." + id.toString() + ".ownerUUID"))));
		trusted = Main.districts.getConfig().getStringList("Districts." + id.toString() + ".trustedUUID");
	}
	
	public void remove(){
		
		Main.districts.getConfig().set("Districts." + id.toString() + ".ownerUUID", null);
		Main.districts.getConfig().set("Districts." + id.toString() + ".worldUUID", null);
		Main.districts.getConfig().set("Districts." + id.toString() + ".xyz", null);
		Main.districts.getConfig().set("Districts." + id.toString() + ".radius", null);
		Main.districts.getConfig().set("Districts." + id.toString() + ".trustedUUID", null);
		
		Main.districts.getConfig().set("Districts." + id.toString(), null);
		
		Main.districts.saveConfig();
	}
	
	public void trust(Wizard wizard){
		trusted.add(wizard.getPlayer().getUniqueId().toString());
		Main.districts.getConfig().set("Districts." + id.toString() + ".trustedUUID", trusted);
		
		Main.districts.saveConfig();
		Main.districts.reloadConfig();
	}
	
	public void distrust(Wizard wizard){
		trusted.remove(wizard.getPlayer().getUniqueId().toString());
		Main.districts.getConfig().set("Districts." + id.toString() + ".trustedUUID", trusted);
		
		Main.districts.saveConfig();
		Main.districts.reloadConfig();
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
		return regionn;
	}
	
	public Wizard getOwner(){
		return owner;
	}
	
	public List<String> getTrusted(){
		return trusted;
	}
	
	@SuppressWarnings("unused")
	private District(){}
}
