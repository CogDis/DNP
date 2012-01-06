package com.precipicegames.zeryl.dnp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 *
 * @author Zeryl
 */
public class DNP extends JavaPlugin {
    
    PermissionManager permissions;

    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();

        PluginDescriptionFile pdf = this.getDescription();
        System.out.println(pdf.getName() + " is now enabled.");
    }

    public void onDisable() {
        PluginDescriptionFile pdf = this.getDescription();
        System.out.println(pdf.getName() + " is now disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx") && (this.permissions == null)) {
            this.permissions = PermissionsEx.getPermissionManager();
        }
        
        if(this.permissions == null) {
            sender.sendMessage(ChatColor.RED + "PermissionsEx not found.  You cannot use this!");
            return true;
        }
        
        if (cmd.getName().equalsIgnoreCase("dnp")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if(!player.hasPermission("com.precipicegames.dnp"))
                    return true;

                if (args.length > 0) {
                    if(args.length == 1) {
                        sender.sendMessage("Must include a player on all commands.");
                        return true;
                    }
                    
                    String check = args[1];
                    PermissionUser puser = this.permissions.getUser(check);
                    if(puser == null) {
                        player.sendMessage("Cannot find player " + check);
                    }
                    
                    if(args[0].equalsIgnoreCase("status")) {
                        if(puser.getOption("donotpromote").equalsIgnoreCase("true")) {
                            player.sendMessage("User is currently " + ChatColor.RED + "DNP");
                            String dnper = puser.getOption("donotpromote.setby");
                            if(!dnper.isEmpty())
                                player.sendMessage("DNP set by " + dnper);
                            return true;
                        } else {
                            player.sendMessage("No DNP currently set");
                        }
                    }
                    
                    if(args[0].equalsIgnoreCase("toggle")) {
                        String cur = puser.getOption("donotpromote");
                        String dnper = "";
                        
                        if(cur.equalsIgnoreCase("true")) {
                            cur = "";
                        } else {
                            cur = "true";
                            dnper = player.getName();
                        }
                        
                        puser.setOption("donotpromote", cur);
                        puser.setOption("donotpromote.setby", dnper);
                        
                        sender.sendMessage("DNP is now " + (cur.equalsIgnoreCase("") ? "unset" : "set") + " for player " + check);                        
                    }
                    
                    if(args[0].equalsIgnoreCase("set")) {
                        puser.setOption("donotpromote", "true");
                        puser.setOption("donotpromote.setby", player.getName());
                        
                        sender.sendMessage("DNP is now set for player " + check);
                    }
                    
                    if(args[0].equalsIgnoreCase("clear")) {
                        puser.setOption("donotpromote", "");
                        puser.setOption("donotpromote.setby", "");
                        
                        sender.sendMessage("DNP is now unset for player " + check);                        
                    }                   
                } else {
                    return false;
                }
            }
        }
        return true;
    }
}