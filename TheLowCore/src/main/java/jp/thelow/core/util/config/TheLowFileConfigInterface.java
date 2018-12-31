package jp.thelow.core.util.config;

public interface TheLowFileConfigInterface {
  /**
   * configのreload処理を行う。
   *
   * @param config FileConfiguration
   */
  void reloadConfig();

  /**
   * config名を取得する。
   *
   * @return config名
   */
  String getName();
}
