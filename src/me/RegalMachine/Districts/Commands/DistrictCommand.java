package me.RegalMachine.Districts.Commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import me.RegalMachine.Districts.Main;
import me.RegalMachine.Districts.Players.Wizard;
import me.RegalMachine.Districts.Players.WizardBag;
import me.RegalMachine.Districts.Protection.District;
import me.RegalMachine.Districts.Protection.DistrictBag;
import me.RegalMachine.Districts.Protection.DistrictGateway;
import me.RegalMachine.Districts.util.Lang;
import me.RegalMachine.Districts.util.Permissions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class DistrictCommand implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		Wizard wizard = WizardBag.getWizard(sender);
		
		if(label.equalsIgnoreCase("district")){
			if(args.length > 0){
				//All possible arguments after /district go here
				if(args[0].equalsIgnoreCase("claim")){
					claim(sender, cmd,label, args, wizard);
				}else if(args[0].equalsIgnoreCase("claims")){
					claims(sender, cmd,label, args, wizard);
				}else if(args[0].equalsIgnoreCase("unclaim")){
					unclaim(sender, cmd, label, args, wizard);
				}else if(args[0].equalsIgnoreCase("trust")){
					trust(sender, cmd,label, args, wizard);
				}else if(args[0].equalsIgnoreCase("trustall")){
					trustall(sender, cmd,label, args, wizard);
				}else if(args[0].equalsIgnoreCase("untrust")){
					distrust(sender, cmd, label, args, wizard);
				}else if(args[0].equalsIgnoreCase("untrustall")){
					distrustAll(sender, cmd, label, args, wizard);
				}else if(args[0].equalsIgnoreCase("info")){
					info(sender, cmd, label, args, wizard);
				}else if(args[0].equalsIgnoreCase("admin") || args[0].equalsIgnoreCase("a")){
					if(!wizard.getPlayer().hasPermission(Permissions.admin)){
						wizard.getPlayer().sendMessage(Lang.NO_PERMS);
						return true;
					}
					//TODO add the tree for the admin commands here.
					//is there enough args for there to be a 2nd argument;
					if(args.length > 1){
						if(args[1].equalsIgnoreCase("unclaim")){
							unclaimAdmin(sender, cmd, label, args, wizard);
						}else if(args[1].equalsIgnoreCase("give")){
							giveAdmin(sender, cmd, label, args, wizard);
						}else if(args[1].equalsIgnoreCase("take")){
							takeAdmin(sender, cmd, label, args, wizard);
						}else{
							wizard.getPlayer().sendMessage(Lang.DISTRICTS + "Unknown Admin argument: " + args[1]);
						}
					}else{
						wizard.getPlayer().sendMessage(Lang.DISTRICTS + "/district admin [unclaim/add/remove]");
					}
				}else{
					wizard.getPlayer().sendMessage(Lang.DISTRICTS + "No such argument: " + ChatColor.RED + args[0]);
				}
			}else{
				wizard.getPlayer().sendMessage(Lang.DISTRICTS + "No Arguments! '/District help'");
			}
			return true;
		}else{
			return false;
		}
		
	}
	
	private void claims(CommandSender sender, Command cmd, String label,
			String[] args, Wizard wizard) {
		wizard.getPlayer().sendMessage(Lang.DISTRICTS + "Information about you and your claims:"); 
		
		wizard.getPlayer().sendMessage(ChatColor.GOLD + ">>" + ChatColor.GRAY + "Districts: " + wizard.districtsOwned().size());
		wizard.getPlayer().sendMessage(ChatColor.GOLD + ">>" + ChatColor.GRAY + "You've claimed " +  DistrictGateway.getBlocksClaimed(wizard) + " out of " + (Permissions.getClaimableBlocks(wizard) + wizard.getBlockBoost()) + " blocks.");
		
	}

	private void takeAdmin(CommandSender sender, Command cmd, String label,
			String[] args, Wizard wizard) {
		if(args.length < 4){
			wizard.getPlayer().sendMessage(Lang.DISTRICTS + "Not Enough Arguments! /district admin take [PlayerName] [Number]");
		}else{
			//Get the player we are giving things to.
			for(Player p: Bukkit.getOnlinePlayers()){
				if(p.getName().equalsIgnoreCase(args[2])){
					Scanner scan = new Scanner(args[3]);
					if(scan.hasNextInt()){
						int boost = scan.nextInt();
						//Commence with the removal of blocks!
						WizardBag.getWizard(p).blockBoost(-boost);
						wizard.getPlayer().sendMessage(Lang.DISTRICTS + p.getName() + " now has  " + boost + " less claims.");
					}else{
						wizard.getPlayer().sendMessage(Lang.DISTRICTS + args[3] + " was not recognised as an integer.");
					}
					scan.close();
					return;
				}
			}
			
			List<Player> players = new ArrayList<Player>();
			for(Player p: Bukkit.getOnlinePlayers()){
				if(p.getName().contains(args[2].subSequence(0, args[2].length()-1))){
					players.add(p);
				}
			}
			
			String message = Lang.DISTRICTS + "Unrecognised Player Name. Did you mean: ";
			for(Player p: players){
				message = message + p.getName() + ", ";
			}
			message = message.substring(0, message.length() -2);
			
			wizard.getPlayer().sendMessage(message);
		}
		
	}

	private void giveAdmin(CommandSender sender, Command cmd, String label,
			String[] args, Wizard wizard) {
		//  /district admin give REGAL_MACHINE 400
		if(args.length < 4){
			wizard.getPlayer().sendMessage(Lang.DISTRICTS + "Not Enough Arguments! /district admin give [PlayerName] [Number]");
		}else{
			//Get the player we are giving things to.
			for(Player p: Bukkit.getOnlinePlayers()){
				if(p.getName().equalsIgnoreCase(args[2])){
					Scanner scan = new Scanner(args[3]);
					if(scan.hasNextInt()){
						int boost = scan.nextInt();
						//Commence with the rewarding of blocks!
						WizardBag.getWizard(p).blockBoost(boost);
						wizard.getPlayer().sendMessage(Lang.DISTRICTS + p.getName() + " was awarded " + boost + " claims.");
					}else{
						wizard.getPlayer().sendMessage(Lang.DISTRICTS + args[3] + " was not recognised as an integer.");
					}
					scan.close();
					return;
				}
			}
			
			List<Player> players = new ArrayList<Player>();
			for(Player p: Bukkit.getOnlinePlayers()){
				if(p.getName().contains(args[2].subSequence(0, args[2].length()-1))){
					players.add(p);
				}
			}
			
			String message = Lang.DISTRICTS + "Unrecognised Player Name. Did you mean: ";
			for(Player p: players){
				message = message + p.getName() + ", ";
			}
			message = message.substring(0, message.length() -2);
			
			wizard.getPlayer().sendMessage(message);
			
			
		}
	}

	@SuppressWarnings("deprecation")
	private void unclaimAdmin(CommandSender sender, Command cmd, String label,
			String[] args, Wizard wizard) {
		ApplicableRegionSet appset = Main.guard.worldGuard().getRegionManager(wizard.getPlayer().getLocation().getWorld()).getApplicableRegions(wizard.getPlayer().getLocation());
		Iterator<ProtectedRegion> itr = appset.iterator();
		ArrayList<ProtectedRegion> districtRegions = new ArrayList<ProtectedRegion>();
		while(itr.hasNext()){
			ProtectedRegion region = itr.next();
			if(DistrictBag.districts.containsKey(UUID.fromString(region.getId()))){
				districtRegions.add(region);
			}
		}
		
		if(districtRegions.size() > 0){
			ProtectedRegion region = districtRegions.get(0);
			DefaultDomain members = region.getMembers();
			for(String name: members.getPlayers()){
				Player p = Bukkit.getOfflinePlayer(name).getPlayer();
				Wizard w = WizardBag.getWizard(p);
				w.distrustIn(DistrictBag.getDistrict(region.getId()));
			}
			wizard.removeDistrict(DistrictBag.getDistrict(region.getId()));
			DistrictBag.getDistrict(region.getId()).remove();
			DistrictBag.remove(DistrictBag.getDistrict(region.getId()));
			
			wizard.getPlayer().sendMessage(Lang.DISTRICTS + "District force unclaimed.");
		}else{
			wizard.getPlayer().sendMessage(Lang.DISTRICTS + "You must be in a district to unclaim one!");
		}
		
	}

	private void info(CommandSender sender, Command cmd, String label,
			String[] args, Wizard wizard) {
		ApplicableRegionSet appset = Main.guard.worldGuard().getRegionManager(wizard.getPlayer().getLocation().getWorld()).getApplicableRegions(wizard.getPlayer().getLocation());
		Iterator<ProtectedRegion> itr = appset.iterator();
		ArrayList<ProtectedRegion> districtRegions = new ArrayList<ProtectedRegion>();
		while(itr.hasNext()){
			ProtectedRegion region = itr.next();
			if(DistrictBag.districts.containsKey(UUID.fromString(region.getId()))){
				districtRegions.add(region);
			}
		}
		
		if(districtRegions.size() > 0){
			District d = DistrictBag.getDistrict(districtRegions.get(0).getId());
			String owner = d.getOwner().getPlayer().getName();
			String members = "";
			for(String id: d.getTrusted()){
				members = members + Bukkit.getOfflinePlayer(UUID.fromString(id)).getName() + ", ";
			}
			members = members.substring(0, members.length()-2);
			int radius = d.getRadius();
			
			wizard.getPlayer().sendMessage(new String[]{Lang.DISTRICTS + "Information about " + owner + "'s district",
					ChatColor.GRAY + "Location: Radius - " + radius + "  X - " + d.getCenter().getBlockX() + "  Z - " + d.getCenter().getBlockZ(),
					ChatColor.GRAY + "Members: " + members});
			
			
		}else{
			wizard.getPlayer().sendMessage(Lang.DISTRICTS + "You are not in a district!");
		}
		
	}

	@SuppressWarnings("deprecation")
	private void unclaim(CommandSender sender, Command cmd, String label,
			String[] args, Wizard wizard){
		
		ApplicableRegionSet appset = Main.guard.worldGuard().getRegionManager(wizard.getPlayer().getLocation().getWorld()).getApplicableRegions(wizard.getPlayer().getLocation());
		Iterator<ProtectedRegion> itr = appset.iterator();
		ArrayList<ProtectedRegion> districtRegions = new ArrayList<ProtectedRegion>();
		while(itr.hasNext()){
			ProtectedRegion region = itr.next();
			if(DistrictBag.districts.containsKey(UUID.fromString(region.getId()))){
				districtRegions.add(region);
			}
		}
		
		if(districtRegions.size() > 0){
			
			ProtectedRegion region = districtRegions.get(0);
			if(!wizard.districtsOwned().contains(region.getId())){
				wizard.getPlayer().sendMessage(Lang.DISTRICTS + "This is not your District!");
				return;
			}
			
			//it IS their region, delete the region and the District. 
			DefaultDomain members = region.getMembers();
			for(String name: members.getPlayers()){
				Player p = Bukkit.getOfflinePlayer(name).getPlayer();
				Wizard w = WizardBag.getWizard(p);
				w.distrustIn(DistrictBag.getDistrict(region.getId()));
			}
			String uuid = region.getId();
			wizard.removeDistrict(DistrictBag.getDistrict(region.getId()));
			DistrictBag.getDistrict(region.getId()).remove();
			DistrictBag.remove(DistrictBag.getDistrict(region.getId()));
			
			if(DistrictBag.districts.containsKey(UUID.fromString(uuid))){
				wizard.getPlayer().sendMessage(Lang.DISTRICTS + "District no longer in District Bag.");
			}
			if(Main.districts.getConfig().contains("Districts." + uuid)){
				wizard.getPlayer().sendMessage(Lang.DISTRICTS + "District not removed from config!");
			}
			
			wizard.getPlayer().sendMessage(Lang.DISTRICTS + "District unclaimed.");
			
		}else{
			wizard.getPlayer().sendMessage(Lang.DISTRICTS + "You are not in one of your Districts!");
		}
		
		
		
	}
	
	private void distrustAll(CommandSender sender, Command cmd, String label,
			String[] args, Wizard wizard) {
		if(args.length < 2){
			
			if(!wizard.getPlayer().hasPermission(Permissions.basic)){
				wizard.getPlayer().sendMessage(Lang.NO_PERMS);
				return;
			}
			
			wizard.getPlayer().sendMessage(Lang.DISTRICTS + "Not enough arguments!");
			return;
		}
		
		//There is a player name argument.
		for(Player player: Bukkit.getOnlinePlayers()){
			if(player.getName().equalsIgnoreCase(args[1])){
				//disTrust This Player
				Wizard wizardBeingTrusted = WizardBag.getWizard(player);
				for(District d: wizard.districtObjectsOwned()){
					d.distrust(wizardBeingTrusted);
					wizardBeingTrusted.distrustIn(d);
					d.getRegion().getMembers().removePlayer(Main.guard.worldGuard().wrapPlayer(wizardBeingTrusted.getPlayer()));
					try {
						Main.guard.worldGuard().getRegionManager(d.getCenter().getWorld()).save();
					} catch (ProtectionDatabaseException e) {
						e.printStackTrace();
					}
				}
				wizard.getPlayer().sendMessage(Lang.DISTRICTS + args[1] + " is no longer trusted in any of your Districts!");
				return;
			}
		}
		wizard.getPlayer().sendMessage(Lang.DISTRICTS + args[1] + " does not exist! /distrustall requires you to type names exactly!");
		return;
		
	}

	private void distrust(CommandSender sender, Command cmd, String label,
			String[] args, Wizard wizard) {
		if(!wizard.getPlayer().hasPermission(Permissions.basic)){
			wizard.getPlayer().sendMessage(Lang.NO_PERMS);
			return;
		}
		if(args.length <= 1){
			wizard.getPlayer().sendMessage(Lang.DISTRICTS + "Not enough arguments!");
			return;
		}
		
		//Is the player even standing in their own region?
				ApplicableRegionSet appRegionSet = Main.guard.worldGuard().getRegionManager(wizard.getPlayer().getLocation().getWorld()).getApplicableRegions(wizard.getPlayer().getLocation());
				Iterator<ProtectedRegion> itr = appRegionSet.iterator();
				ArrayList<ProtectedRegion> districtRegions = new ArrayList<ProtectedRegion>();
				while(itr.hasNext()){
					ProtectedRegion region = itr.next();
					if(DistrictBag.districts.containsKey(UUID.fromString(region.getId()))){
						districtRegions.add(region);
					}
				}
				if(districtRegions.size() > 0){
					//Rely on the hope that districtRegions now has 1 region, a district. It Should.
					
					//Check to see if this region is owned by the wizard.
					ProtectedRegion region = districtRegions.get(0);
					if(!wizard.districtsOwned().contains(region.getId())){
						wizard.getPlayer().sendMessage(Lang.DISTRICTS + "This is not your District!");
						return;
					}
					
					//The district is owned by the player. commence with distrusting.
					ArrayList<Player> players = new ArrayList<Player>();
					for(Player player: Bukkit.getOnlinePlayers()){
						if(player.getDisplayName().contains(args[1].subSequence(0, args[1].length()-1))){
							players.add(player);
						}
					}
					
					if(players.size() == 1){
						//disTrust the player plugin side
						Player toBeunTrusted = players.get(0);
						Wizard toBeunTrust = WizardBag.getWizard(toBeunTrusted);
						District district = DistrictBag.getDistrict(region.getId());
						district.distrust(toBeunTrust);
						toBeunTrust.distrustIn(district);
						
						//remove the player to the members WG side.
						region.getMembers().removePlayer(Main.guard.worldGuard().wrapPlayer(toBeunTrusted));
						try {
							Main.guard.worldGuard().getRegionManager(district.getCenter().getWorld()).save();
						} catch (ProtectionDatabaseException e) {
							
							e.printStackTrace();
						}
						wizard.getPlayer().sendMessage(Lang.DISTRICTS + "You have untrusted " + toBeunTrusted.getDisplayName() + " in this District!");
					}
					
					if(players.size() > 1){
						wizard.getPlayer().sendMessage(Lang.DISTRICTS + "There were multiple players with " + args[1] + " in there name. Use Tab next time!");
						String blah = "";
						for(Player p: players){
							blah = blah + p.getName() + ", ";
						}
						wizard.getPlayer().sendMessage(blah.substring(0, blah.length()-2));
					}else if(players.size() == 0){
						wizard.getPlayer().sendMessage(Lang.DISTRICTS + "Could not find any players named " + args[1] + ". Sorry!");
						return;
					}
				}else{
					wizard.getPlayer().sendMessage(Lang.DISTRICTS + "You are not in one of your Districts!");
				}
		
	}

	private void trustall(CommandSender sender, Command cmd, String label,
			String[] args, Wizard wizard) {
		if(!wizard.getPlayer().hasPermission(Permissions.basic)){
			wizard.getPlayer().sendMessage(Lang.NO_PERMS);
			return;
		}
		
		//First get the player argument.
		
		if(args.length < 2){
			wizard.getPlayer().sendMessage(Lang.DISTRICTS + "Not enough arguments!");
			return;
		}
		
		//There is a player name argument.
		for(Player player: Bukkit.getOnlinePlayers()){
			if(player.getName().equalsIgnoreCase(args[1])){
				//Trust This Player
				Wizard wizardBeingTrusted = WizardBag.getWizard(player);
				for(District d: wizard.districtObjectsOwned()){
					d.trust(wizardBeingTrusted);
					wizardBeingTrusted.trustIn(d);
					d.getRegion().getMembers().addPlayer(Main.guard.worldGuard().wrapPlayer(wizardBeingTrusted.getPlayer()));
					try {
						Main.guard.worldGuard().getRegionManager(d.getCenter().getWorld()).save();
					} catch (ProtectionDatabaseException e) {
						
						e.printStackTrace();
					}
				}
				wizard.getPlayer().sendMessage(Lang.DISTRICTS + args[1] + " is now trusted in all your Districts!");
				return;
			}
		}
		wizard.getPlayer().sendMessage(Lang.DISTRICTS + args[1] + " does not exist! /trustall requires you to type names exactly!");
		return;
	}

	private void trust(CommandSender sender, Command cmd, String label,
			String[] args, Wizard wizard) {
		
		if(!wizard.getPlayer().hasPermission(Permissions.basic)){
			wizard.getPlayer().sendMessage(Lang.NO_PERMS);
			return;
		}
		
		//Check to see if there is enough args to run this command
		if(args.length <= 1){
			wizard.getPlayer().sendMessage(Lang.DISTRICTS + "Not enough arguments!");
			return;
		}
		
		//Is the player even standing in their own region?
		ApplicableRegionSet appRegionSet = Main.guard.worldGuard().getRegionManager(wizard.getPlayer().getLocation().getWorld()).getApplicableRegions(wizard.getPlayer().getLocation());
		Iterator<ProtectedRegion> itr = appRegionSet.iterator();
		ArrayList<ProtectedRegion> districtRegions = new ArrayList<ProtectedRegion>();
		while(itr.hasNext()){
			ProtectedRegion region = itr.next();
			if(DistrictBag.districts.containsKey(UUID.fromString(region.getId()))){
				districtRegions.add(region);
			}
		}
		if(districtRegions.size() > 0){
			//Rely on the hope that districtRegions now has 1 region, a district. It Should.
			
			//Check to see if this region is owned by the wizard.
			ProtectedRegion region = districtRegions.get(0);
			if(!wizard.districtsOwned().contains(region.getId())){
				wizard.getPlayer().sendMessage(Lang.DISTRICTS + "This is not your District!");
				return;
			}
			
			//The district is owned by the player. commence with trusting.
			ArrayList<Player> players = new ArrayList<Player>();
			for(Player player: Bukkit.getOnlinePlayers()){
				if(player.getDisplayName().contains(args[1].subSequence(0, args[1].length()-1))){
					players.add(player);
				}
			}
			
			if(players.size() == 1){
				//Trust the player plugin side
				Player toBeTrusted = players.get(0);
				Wizard toBeTrust = WizardBag.getWizard(toBeTrusted);
				District district = DistrictBag.getDistrict(region.getId());
				district.trust(toBeTrust);
				toBeTrust.trustIn(district);
				
				//Add the player to the members WG side.
				region.getMembers().addPlayer(Main.guard.worldGuard().wrapPlayer(toBeTrusted));
				try {
					Main.guard.worldGuard().getRegionManager(district.getCenter().getWorld()).save();
				} catch (ProtectionDatabaseException e) {
					
					e.printStackTrace();
				}
				wizard.getPlayer().sendMessage(Lang.DISTRICTS + "You have trusted " + toBeTrusted.getDisplayName() + " in this District!");
			}
			
			if(players.size() > 1){
				wizard.getPlayer().sendMessage(Lang.DISTRICTS + "There were multiple players with " + args[1] + " in there name. Use Tab next time!");
				String blah = "";
				for(Player p: players){
					blah = blah + p.getName() + ",";
				}
				wizard.getPlayer().sendMessage(blah.substring(0, blah.length()-2));
			}else if(players.size() == 0){
				wizard.getPlayer().sendMessage(Lang.DISTRICTS + "Could not find any players named " + args[1] + ". Sorry!");
				return;
			}
		}else{
			wizard.getPlayer().sendMessage(Lang.DISTRICTS + "You are not in one of your Districts!");
		}
	}

	private void claim(CommandSender sender, Command cmd, String label,
			String[] args, Wizard wizard){
		if(!wizard.getPlayer().hasPermission(Permissions.basic)){
			wizard.getPlayer().sendMessage(Lang.NO_PERMS);
			return;
		}
		if(args.length > 1){
			Scanner scan = new Scanner(args[1]);
			if(scan.hasNextInt()){
				int radius = scan.nextInt();
				scan.close();
				//Claim said region.
				if(radius < 4){
					wizard.getPlayer().sendMessage(Lang.DISTRICTS + "You can not claim a District with a radius less than 4.");
					return;
				}
				if(DistrictGateway.canCreateDistrict(wizard, radius)){
					if(DistrictGateway.hasEnoughClaimBlocks(wizard, radius)){
					//create the district and stuff.
					DistrictBag.addDistrict(new District(wizard.getPlayer().getLocation(), radius, wizard));
					wizard.getPlayer().sendMessage(Lang.DISTRICTS + "District created!");
					}else{
						wizard.getPlayer().sendMessage(Lang.DISTRICTS + "You have gone over the number of blocks you can claim! District not claimed! You can claim " + DistrictGateway.claimsLeft(wizard) + " more block(s).");
					}
				}else{
					wizard.getPlayer().sendMessage(Lang.DISTRICTS + ChatColor.RED + "You can't create a district here!");
				}
				
			}else{
				wizard.getPlayer().sendMessage(Lang.DISTRICTS + scan.next() + " was not recognised as an integer.");
				scan.close();
			}
		}else{
			wizard.getPlayer().sendMessage(Lang.DISTRICTS + "Not enough arguments! '/district claim #'");
		}
		
	}
	
}
