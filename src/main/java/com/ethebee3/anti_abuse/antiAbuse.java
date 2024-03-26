package com.ethebee3.anti_abuse;

import com.ethebee3.anti_abuse.antiAbuseListener;
import io.papermc.lib.PaperLib;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;


@SuppressWarnings("deprecation")
public class antiAbuse extends JavaPlugin {

  @Override
  public void onEnable() {
    PaperLib.suggestPaper(this);

    saveDefaultConfig();
    PluginManager pm = this.getServer().getPluginManager();
    antiAbuseListener AALU = new antiAbuseListener(this);
    pm.registerEvents(AALU, this);
  }
}
