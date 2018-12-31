package jp.thelow.core;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;

public class TheLowCoreMain extends JavaPlugin {

  @Getter
  public static JavaPlugin plugin;

  @Override
  public void onEnable() {
    plugin = this;
  }
}
