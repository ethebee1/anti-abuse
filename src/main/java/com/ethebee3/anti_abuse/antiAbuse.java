package com.ethebee3.anti_abuse;

import io.papermc.lib.PaperLib;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;


/**
 * Created by Levi Muniz on 7/29/20.
 *
 * @author Copyright (c) Levi Muniz. All Rights Reserved.
 */
@SuppressWarnings("deprecation")
public class antiAbuse extends JavaPlugin {

  @Override
  public void onEnable() {
    PaperLib.suggestPaper(this);

    saveDefaultConfig();
    PluginManager pm = this.getServer().getPluginManager();
    AntiAbuseListener AALU = new AntiAbuseListener(this);
    pm.registerEvents(AALU, this);
  }
}
