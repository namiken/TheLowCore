package jp.thelow.core.util.config;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * 設定オブジェクトを作成する。このインタフェースの実装はシングルトンで実装することが望ましい。
 */
public interface TheLowConfigInterface extends TheLowFileConfigInterface {
  /**
   * configのreload処理を行う。
   *
   * @param config FileConfiguration
   */
  void reloadConfig(FileConfiguration config);

  @Override
  default void reloadConfig() {
    reloadConfig(ConfigExecuter.getConfig());
  }
}
