package jp.thelow.core.common.holder;

import java.util.HashMap;
import java.util.Map;

public class MapSpecificValue implements SpecificValue {

  /** 値を格納 */
  private Map<String, Object> valueMap = new HashMap<>();

  /**
   * 値を登録する。
   *
   * @param key キー
   * @param value 値
   */
  @Override
  public <T> void addElements(String key, T value) {
    valueMap.put(key, value);
  }

  /**
   * 値を取得する。
   *
   * @param key キー
   * @return 値
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getValue(String key) {
    return (T) valueMap.get(key);
  }

  /**
   * キーが登録されているか確認する。
   *
   * @param key キー
   * @return 登録されている場合はtrue
   */
  @Override
  public boolean hasKey(String key) {
    return valueMap.containsKey(key);
  }

  @Override
  public SpecificValue copy() {
    MapSpecificValue specificValue = new MapSpecificValue();
    valueMap.entrySet().stream().forEach(e -> specificValue.addElements(e.getKey(), e.getValue()));
    return specificValue;
  }
}
