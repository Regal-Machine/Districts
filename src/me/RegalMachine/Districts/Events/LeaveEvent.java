package me.RegalMachine.Districts.Events;

import java.util.List;
import java.util.UUID;

import me.RegalMachine.Districts.Main;
import me.RegalMachine.Districts.Players.WizardBag;
import me.RegalMachine.Districts.Protection.District;
import me.RegalMachine.Districts.Protection.DistrictBag;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class LeaveEvent implements Listener{
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onLeave(PlayerQuitEvent e){
		WizardBag.clearWizard(e.getPlayer());
		final String uuid = e.getPlayer().getUniqueId().toString();
		new BukkitRunnable() {
			
			@Override
			public void run() {
				WizardBag.addWizard(uuid);
				List<String> uuids = WizardBag.getWizard(Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getPlayer()).districtsOwned();
				for(String id: uuids){
					DistrictBag.addDistrict(new District(UUID.fromString(id)));
				}
			}
		}.runTaskLater(Main.instance, 20);
	}

}
