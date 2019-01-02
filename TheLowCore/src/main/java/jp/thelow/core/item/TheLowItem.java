package jp.thelow.core.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import jp.thelow.core.common.holder.SpecificValue;

public interface TheLowItem {

  /**
   * アイテムIDを取得する。
   *
   * @return アイテムID
   */
  String getItemId();

  /**
   * アイテム名を取得する。
   *
   * @return アイテム名
   */
  String getName();

  /**
   * デフォルトのSpecificValueを取得する。
   *
   * @return SpecificValue
   */
  SpecificValue getDefaultSpecificValue();

  /**
   * ItemStackに対応するSpecificValueを取得する。
   *
   * @param itemStack itemStack
   * @return SpecificValue
   */
  SpecificValue getItemStackSpecificValue(ItemStack itemStack);

  /**
   * 新しいアイテムを作成する。
   *
   * @param p 持ち主
   * @param specificValue アイテムの固有情報
   * @return ItemStack
   */
  ItemStack getNewItem(Player p, SpecificValue specificValue);
}
