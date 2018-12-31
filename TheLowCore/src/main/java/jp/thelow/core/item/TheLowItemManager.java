package jp.thelow.core.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.inventory.ItemStack;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import jp.thelow.core.util.ItemStackUtil;
import jp.thelow.core.util.JavaUtil;

@SuppressWarnings("unchecked")
public enum TheLowItemManager {
  INSNTACE;

  /** IDがキーのマップ */
  private Map<String, TheLowItem> idItemMap = new HashMap<>();

  /** クラスとIDがキーのマップ */
  private Table<Class<? extends TheLowItem>, String, TheLowItem> classIdMap = HashBasedTable.create();

  public static TheLowItemManager getInstance() {
    return INSNTACE;
  }

  /**
   *
   * アイテムを登録する。
   *
   * @param item アイテム
   */
  public void register(TheLowItem item) {
    //自身を登録
    registerInner(item.getClass(), item);

    //インターフェイスを登録
    Set<Class<?>> interfaces = JavaUtil.getInterface(item.getClass());
    for (Class<?> clazz : interfaces) {
      if (clazz.isAssignableFrom(TheLowItem.class) || !clazz.equals(TheLowItem.class)) {
        registerInner(clazz, item);
      }
    }
  }

  private void registerInner(Class<?> clazz, TheLowItem item) {
    idItemMap.put(item.getItemId(), item);
    classIdMap.put((Class<? extends TheLowItem>) clazz, item.getItemId(), item);
  }

  /**
   * IDからアイテムを取得する。
   *
   * @param id id
   * @return アイテム
   */
  public <T extends TheLowItem> T findById(String id) {
    return (T) idItemMap.get(id);
  }

  /**
   * IDからアイテムを取得する。
   *
   * @param itemStack itemStack
   * @return アイテム
   */
  public <T extends TheLowItem> T findByItemStack(ItemStack itemStack) {
    return findById(ItemStackUtil.getId(itemStack));
  }

  /**
   * クラスとIDからアイテムを取得する。
   *
   * @param id アイテムID
   * @param clazz 実装クラスまたは、インターフェース
   * @return アイテム
   */
  public <T extends TheLowItem> T findImplementalItem(String id, Class<T> clazz) {
    return (T) classIdMap.get(clazz, id);
  }

  /**
   * クラスとItemStackからアイテムを取得する。
   *
   * @param itemStack itemStack
   * @param clazz 実装クラスまたは、インターフェース
   * @return アイテム
   */
  public <T extends TheLowItem> T findImplementalItem(ItemStack itemStack, Class<T> clazz) {
    return findImplementalItem(ItemStackUtil.getId(itemStack), clazz);
  }
}
