package jp.thelow.core.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import jp.thelow.core.common.holder.SpecificValue;
import jp.thelow.core.item.constant.ItemCategory;

public interface TheLowItem {

  /**
   * アイテムIDを取得する。
   *
   * @return アイテムID
   */
  String getItemId();

  /**
   * SpecificValueを取得する。
   *
   * @return SpecificValue
   */
  SpecificValue getDefaultSpecificValue();

  /**
   * アイテムカテゴリを取得する。
   *
   * @return アイテムカテゴリ
   */
  ItemCategory getItemCategory();

  /**
   * 新しいアイテムを作成する。
   *
   * @param p 持ち主
   * @param specificValue アイテムの固有情報
   * @return ItemStack
   */
  ItemStack getNewItem(Player p, SpecificValue specificValue);
}
