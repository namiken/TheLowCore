package jp.thelow.core.util.config;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import jp.thelow.core.TheLowCoreMain;

public class ConfigExecuter {
  /** 設定オブジェクト情報 */
  private static Map<String, TheLowFileConfigInterface> configList = new HashMap<>();

  /**
   * 設定情報を登録する。
   *
   * @param config 設定情報
   */
  public static void registReload(TheLowFileConfigInterface config) {
    if (configList.containsKey(config.getName())) { return; }
    configList.put(config.getName(), config);
  }

  /**
   * 全設定情報を更新する。
   */
  public static void reloadConfig() {
    // config.ymlを更新
    JavaPlugin plugin = TheLowCoreMain.getPlugin();
    plugin.reloadConfig();

    // 全ての設定情報を更新
    configList.entrySet().forEach(e -> e.getValue().reloadConfig());
  }

  public static FileConfiguration getConfig() {
    return TheLowCoreMain.getPlugin().getConfig();
  }

  /**
   * 設定情報を表示する。
   *
   * @param sender 表示先
   */
  public static void showInfo(CommandSender sender) {
    for (TheLowFileConfigInterface config : configList.values()) {
      sender.sendMessage("===" + config.getName() + "===");
      sender.sendMessage(config.toString());
    }
  }
}
