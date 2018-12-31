package jp.thelow.core.common.holder;

public interface SpecificValue {

  /**
   * 値を登録する。
   *
   * @param key キー
   * @param value 値
   */
  <T> void addElements(String key, T value);

  /**
   * 値を取得する。
   *
   * @param key キー
   * @return 値
   */
  <T> T getValue(String key);

  /**
   * キーが登録されているか確認する。
   *
   * @param key キー
   * @return 登録されている場合はtrue
   */
  boolean hasKey(String key);

  /**
   * 自身の複製を作成する。
   *
   * @return 複製
   */
  SpecificValue copy();
}
