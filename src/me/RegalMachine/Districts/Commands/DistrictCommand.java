package me.RegalMachine.Districts.Commands;

import java.util.Scanner;

import me.RegalMachine.Districts.Players.Wizard;
import me.RegalMachine.Districts.Players.WizardBag;
import me.RegalMachine.Districts.Protection.District;
import me.RegalMachine.Districts.Protection.DistrictBag;
import me.RegalMachine.Districts.Protection.DistrictGateway;
import me.RegalMachine.Districts.util.Lang;
import me.RegalMachine.Districts.util.Permissions;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DistrictCommand implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		Wizard wizard = WizardBag.getWizard(sender);
		
		if(label.equalsIgnoreCase("district")){
			if(args.length > 0){
				//All possible arguments after /district go here
				if(args[0].equalsIgnoreCase("claim")){
					if(!wizard.getPlayer().hasPermission(Permissions.basic)){
						wizard.getPlayer().sendMessage(Lang.NO_PERMS);
						return true;
					}
					if(args.length > 1){
						Scanner scan = new Scanner(args[1]);
						if(scan.hasNextInt()){
							int radius = scan.nextInt();
							scan.close();
							//Claim said region.
							if(DistrictGateway.canCreateDistrict(wizard, radius)){
								//create the district and stuff.
								DistrictBag.addDistrict(new District(wizard.getPlayer().getLocation(), radius, wizard));
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
				}else if(args[0].equalsIgnoreCase("remove")){
					
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
	
}
