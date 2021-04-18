package dev.wwst.admintools3.modules;

import dev.wwst.admintools3.AdminTools3;
import dev.wwst.admintools3.util.ItemBuilder;
import dev.wwst.admintools3.util.XMaterial;
import dev.wwst.admintools3.util.PlayerDataStorage;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WarnModule extends Module implements Listener {

    private final List<UUID> warnedPlayers;
    private final PlayerDataStorage pds;
    private final String invthingName;
    private final Inventory WarnReasonSelector;

    public WarnModule() {
        super(false, true, "warn", XMaterial.SPONGE);
        useDefaultMessageKeyFormat = false;
        pds = new PlayerDataStorage("warns.yml");
        warnedPlayers = pds.getAllData();

        invthingName = msg.getMessage("gui.WarnReasonSelector.invthingName");
        WarnReasonSelector = Bukkit.createInventory(null, InventoryType.HOPPER, invthingName);
        WarnReasonSelector.setItem(0,new ItemBuilder(XMaterial.IRON_PICKAXE, msg.getMessage("gui.WarnReasonSelector.hacking")).build());
        WarnReasonSelector.setItem(1,new ItemBuilder(XMaterial.BEACON, msg.getMessage("gui.WarnReasonSelector.toxicity")).build());
        WarnReasonSelector.setItem(2,new ItemBuilder(XMaterial.WOODEN_SWORD, msg.getMessage("gui.WarnReasonSelector.disobedience")).build());
        

        Bukkit.getPluginManager().registerEvents(this,AdminTools3.getInstance());
    }

    //pplayer.sendMessage(ChatColor.RED + "You have been warned for [reason]. Current number of strikes: [number].");
    @Override
    public boolean execute(Player player, Player other, World world) {

        if(other.hasPermission("admintools3.bypass.warn")) {
            player.sendMessage(msg.getMessageAndReplace("module.warn..bypass",true,player, other.getName()));
            return false;
        }

        if(!super.execute(player, other, world)) {
            return false;
        }

        /*
        if(warnedPlayers.contains(other.getUniqueId())) {
            warnedPlayers.remove(other.getUniqueId());
            pds.getConfig().set(other.getUniqueId().toString(),false);
            other.sendMessage(msg.getMessage("module.mute.message.toggleOff",true,other));
            if(!other.getName().equals(player.getName())) {
                other.sendMessage(msg.getMessageAndReplace("module.mute.message.toggledOffByOther",true, player,player.getName()));
                player.sendMessage(msg.getMessageAndReplace("module.mute.message.toggledOffForOther",true, player,other.getName()));
            }
        } else {
            warnedPlayers.add(other.getUniqueId());
            pds.getConfig().set(other.getUniqueId().toString(),true);
            other.sendMessage(msg.getMessage("module.mute.message.toggleOn",true));
            if(!other.getName().equals(player.getName())) {
                other.sendMessage(msg.getMessageAndReplace("module.mute.message.toggledOnByOther", true, player,player.getName()));
                player.sendMessage(msg.getMessageAndReplace("module.mute.message.toggledOnForOther",true, player,other.getName()));
            }
        } //this is from the mute command, will need to update it to count warns, compare warns. if the amount of max warns in
        // the configuration matches or is above the amount of warns a player has
        // it will remove all of the warns, then tempban/kick a player with the message, you have been warned 
        //too many times, please stop and rejoin later. 
        */
        other.openInventory(WarnReasonSelector);
        return true;
    }

    @EventHandler
    public void click(InventoryClickEvent event) {
        if(!invthingName.equals(event.getView().getTitle())) return;
        event.setCancelled(true);
        if(!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if(event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) return;

        switch(event.getCurrentItem().getType()) {
            case IRON_PICKAXE:
                //player.sendMessage(msg.getMessage("module.gm.message.survival",true,player))
                player.sendMessage(ChatColor.RED + "You have been warned for hacking!");;
                break;
            case BEACON:
                player.sendMessage(ChatColor.RED + "You have been warned for toxicity!");;
                break;
            case WOODEN_SWORD:
                player.sendMessage(ChatColor.RED + "You have been warned for disobedience!");;
                break;
            default:
                break;
        }
        player.closeInventory();
    }
    

}
