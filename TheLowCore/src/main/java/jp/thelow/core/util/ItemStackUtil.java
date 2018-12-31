package jp.thelow.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import net.minecraft.server.v1_12_R1.NBTBase;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class ItemStackUtil {

  private static final ItemStack ITEM_AIR = new ItemStack(Material.AIR);

  public static ItemMeta getItemMeta(ItemStack item) {
    if (item == null) { return null; }

    ItemMeta itemMeta = item.getItemMeta();
    if (itemMeta == null) { return null; }

    return itemMeta;
  }

  public static List<String> getLore(ItemStack item) {
    if (item == null) { return new ArrayList<>(); }

    ItemMeta itemMeta = getItemMeta(item);
    if (itemMeta == null) { return new ArrayList<>(); }

    List<String> lore = itemMeta.getLore();
    if (lore == null) { return new ArrayList<>(); }

    return lore;
  }

  public static List<String> getLore(ItemMeta meta) {
    if (meta == null) { return new ArrayList<>(); }

    List<String> lore = meta.getLore();
    if (lore == null) { return new ArrayList<>(); }

    return lore;
  }

  public static void setDispName(ItemStack item, String name) {
    ItemMeta itemMeta = getItemMeta(item);
    if (itemMeta == null) { return; }

    itemMeta.setDisplayName(name);
    item.setItemMeta(itemMeta);
  }

  public static String getName(ItemStack item) {
    ItemMeta itemMeta = getItemMeta(item);
    if (itemMeta == null) { return ""; }

    String name = itemMeta.getDisplayName();
    if (name == null) {
      return "";
    } else {
      return name;
    }
  }

  public static void setLore(ItemStack item, List<String> lore) {
    ItemMeta itemMeta = getItemMeta(item);
    if (itemMeta == null) { return; }
    itemMeta.setLore(lore);
    item.setItemMeta(itemMeta);
  }

  public static void addLore(ItemStack item, String... lore) {
    ItemMeta itemMeta = getItemMeta(item);
    if (itemMeta == null) { return; }

    List<String> lore2 = getLore(itemMeta);
    lore2.addAll(Arrays.asList(lore));

    itemMeta.setLore(lore2);

    item.setItemMeta(itemMeta);
  }

  /**
   * アイテムがnullまたはAIRならTRUE
   *
   * @param item
   * @return
   */
  public static boolean isEmpty(ItemStack item) {
    return item == null || item.getType() == Material.AIR;
  }

  /**
   * アイテムがnullまたはAIRでないならTRUE
   *
   * @param item
   * @return
   */
  public static boolean isNotEmpty(ItemStack item) {
    return !isEmpty(item);
  }

  /**
   * unsafeなエンチャントならTRUE
   *
   * @param itemMeta
   * @return
   */
  public static boolean isUnsafeEnchant(EnchantmentStorageMeta itemMeta) {
    Map<Enchantment, Integer> storedEnchants = itemMeta.getStoredEnchants();

    for (Entry<Enchantment, Integer> e : storedEnchants.entrySet()) {
      Enchantment ench = e.getKey();
      if (ench.getMaxLevel() < e.getValue() || ench.getStartLevel() > e.getValue()) { return true; }
    }
    return false;
  }

  public static String getId(ItemStack item) {
    if (isEmpty(item)) { return null; }

    // nbt tagから取得
    return getNBTTag(item, "thelow_item_id");
  }

  @SuppressWarnings("deprecation")
  public static byte getData(ItemStack item) {
    if (item == null) { return 0; }

    MaterialData data = item.getData();
    if (data == null) { return 0; }

    return data.getData();
  }

  /**
   * アイテムの個数を1つ減少させる
   *
   * @param item
   * @return
   */
  public static ItemStack getDecremented(ItemStack item) {
    if (item == null) { return ITEM_AIR; }

    if (item.getAmount() == 1) {
      return ITEM_AIR;
    } else if (item.getAmount() > 1) {
      item.setAmount(item.getAmount() - 1);
      return item;
    } else {
      return ITEM_AIR;
    }
  }

  /**
   * 手に持っているアイテムを1つ消費する
   *
   * @param player
   */
  public static void consumeItemInHand(Player player, boolean isMainHand) {

    PlayerInventory inventory = player.getInventory();

    ItemStack targetItem = isMainHand ? inventory.getItemInMainHand() : inventory.getItemInOffHand();
    if (targetItem == null) { return; }
    // 消費させる
    if (targetItem.getAmount() <= 1) {
      targetItem = null;
    } else {
      targetItem.setAmount(targetItem.getAmount() - 1);
    }
    ItemStack finalItemStack = targetItem;

    //アイテムを設定
    TheLowExecutor.executeLater(1, () -> {
      // 空中でクリックした際に消費されないバグを避けるために1ticv後に実行
      if (isMainHand) {
        inventory.setItemInMainHand(finalItemStack);
      } else {
        inventory.setItemInOffHand(finalItemStack);
      }
    });
  }

  /**
   * 指定したNBTTagがセットされていればTRUE
   */
  public static boolean hasNbtTag(ItemStack item, String name) {
    net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
    // NBTTagがないならFALSE
    if (nmsStack.getTag() == null) { return false; }
    // セットされているか確認する
    return nmsStack.getTag().hasKey(name);
  }

  /**
   * NTBTagをセットする
   *
   * @param item
   * @param name
   * @param value
   */
  public static void setNBTTag(ItemStack item, String name, String value) {
    net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
    if (nmsStack == null) {
      DungeonLogger.error("CraftItemStack is null:" + item);
      return;
    }
    if (nmsStack.getTag() == null) {
      nmsStack.setTag(new NBTTagCompound());
    }
    nmsStack.getTag().setString(name, value);
    item.setItemMeta(CraftItemStack.getItemMeta(nmsStack));
  }

  /**
   * NTBTagを取得する
   *
   * @param item
   * @param name
   */
  public static String getNBTTag(ItemStack item, String name) {
    net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
    if (nmsStack == null) { return null; }
    if (nmsStack.getTag() == null) { return null; }

    // NBTタグが不正な時はnullを返す
    String string = nmsStack.getTag().getString(name);
    if (string == null || string.isEmpty()) {
      return null;
    } else {
      return string;
    }
  }

  /**
   * 指定されたKeyに紐づくNBTTagを保持しているかどうか調べる。
   *
   * @param item アイテム
   * @param key key
   * @return 保持している場合はtrue
   */
  public static boolean hasNBTTagData(ItemStack item, String key) {
    net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
    if (nmsStack == null) { return false; }
    if (nmsStack.getTag() == null) { return false; }

    NBTTagCompound compound = nmsStack.getTag();
    return compound.c().contains(key);
  }

  /**
   * NbtTag情報をString化して取得する。
   *
   * @param item アイテム
   * @return NBTTag情報
   */
  public static String getNbtTagData(ItemStack item) {
    net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
    if (nmsStack == null) { return null; }
    if (nmsStack.getTag() == null) { return null; }

    NBTTagCompound compound = nmsStack.getTag();
    StringBuilder builder = new StringBuilder();
    setNbtElement(null, compound, builder, 0);
    return builder.toString();
  }

  public static void setNbtElement(String key, NBTBase compound, StringBuilder builder, int dept) {
    if (dept >= 10) {
      builder.append(compound.toString());
      return;
    }
    if (compound instanceof NBTTagCompound) {
      builder.append(IntStream.range(0, dept).mapToObj(i -> "  ").collect(Collectors.joining()) + key + ":");
      for (String subkey : ((NBTTagCompound) compound).c()) {
        NBTBase nbtBase = ((NBTTagCompound) compound).get(subkey);
        setNbtElement(subkey, nbtBase, builder, dept + 1);
      }
    } else {
      builder.append(IntStream.range(0, dept).mapToObj(i -> "  ").collect(Collectors.joining()) + key + ":"
          + compound.toString() + "@"
          + compound.getTypeId() + "\n");
    }
  }

  /**
   * NTBTagをセットする
   *
   * @param item
   * @param name
   * @param value
   */
  public static void setNBTTag(ItemStack item, String name, short value) {
    net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
    if (nmsStack.getTag() == null) {
      nmsStack.setTag(new NBTTagCompound());
    }
    nmsStack.getTag().setShort(name, value);
    item.setItemMeta(CraftItemStack.getItemMeta(nmsStack));
  }

  /**
   * NTBTagを取得する
   *
   * @param item
   * @param name
   */
  public static short getNBTTagShort(ItemStack item, String name) {
    net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
    if (nmsStack == null) { return 0; }
    if (nmsStack.getTag() == null) { return 0; }
    return nmsStack.getTag().getShort(name);
  }

  /**
   * NTBTagをセットする
   *
   * @param item
   * @param name
   * @param value
   */
  public static void setNBTTag(ItemStack item, String name, long value) {
    net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
    if (nmsStack.getTag() == null) {
      nmsStack.setTag(new NBTTagCompound());
    }
    nmsStack.getTag().setLong(name, value);
    item.setItemMeta(CraftItemStack.getItemMeta(nmsStack));
  }

  /**
   * NTBTagを取得する
   *
   * @param item
   * @param name
   */
  public static long getNBTTagLong(ItemStack item, String name) {
    net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
    if (nmsStack == null) { return 0; }
    if (nmsStack.getTag() == null) { return 0; }
    return nmsStack.getTag().getLong(name);
  }

  /**
   * NTBTagをセットする
   *
   * @param item
   * @param name
   * @param value
   */
  public static void setNBTTag(ItemStack item, String name, double value) {
    net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
    if (nmsStack.getTag() == null) {
      nmsStack.setTag(new NBTTagCompound());
    }
    nmsStack.getTag().setDouble(name, value);
    item.setItemMeta(CraftItemStack.getItemMeta(nmsStack));
  }

  /**
   * NTBTagを削除する
   *
   * @param item
   * @param name
   */
  public static void removeNBTTag(ItemStack item, String name) {
    net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
    if (nmsStack.getTag() == null) {
      nmsStack.setTag(new NBTTagCompound());
    }
    nmsStack.getTag().remove(name);
    item.setItemMeta(CraftItemStack.getItemMeta(nmsStack));
  }

  /**
   * NTBTagを取得する
   *
   * @param item
   * @param name
   */
  public static double getNBTTagDouble(ItemStack item, String name) {
    net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
    if (nmsStack == null) { return 0; }
    if (nmsStack.getTag() == null) { return 0; }
    return nmsStack.getTag().getDouble(name);
  }

  /**
   * インベントリから指定したCustomアイテムを指定した数量削除する
   *
   * @param inv
   * @param itemId
   * @param deleteAmount 数量
   */
  public static void removeCustomItem(Inventory inv, String itemId, int deleteAmount) {
    for (int i = 0; i < inv.getSize(); i++) {
      ItemStack itemStack = inv.getItem(i);
      // IDを比較する
      String id = ItemStackUtil.getId(itemStack);
      if (!itemId.equals(id)) {
        continue;
      }

      if (deleteAmount <= 0) { return; }

      int itemAmount = itemStack.getAmount();
      // 個数が同じ場合は削除する
      if (deleteAmount >= itemAmount) {
        inv.clear(i);
      } else if (deleteAmount < itemAmount) {
        itemStack.setAmount(itemAmount - deleteAmount);
      }
      // 消費した分を削除する
      deleteAmount -= itemAmount;
    }
  }

  /**
   * 指定したIDのアイテムが指定した個数持っていた場合はTRUE
   *
   * @param inv
   * @param itemId
   * @param amount
   * @return
   */
  public static boolean containsCustomItem(Inventory inv, String itemId, int amount) {
    ItemStack[] items = inv.getContents();
    for (int i = 0; i < items.length; i++) {
      // IDを比較する
      String id = ItemStackUtil.getId(items[i]);
      if (!itemId.equals(id)) {
        continue;
      }

      if (amount <= 0) { return true; }

      int itemAmount = items[i].getAmount();
      // 持っているアイテムの個数の方が多い場合はTRUE
      if (amount <= itemAmount) {
        return true;
      } else if (amount > itemAmount) {
        amount -= itemAmount;
      }
    }
    return false;
  }

  /**
   * 指定されたItemIDのアイテムがいくつあるか確認する。
   *
   * @param inv インベントリ
   * @param itemId アイテムID
   * @return 所持している個数
   */
  public static int countItem(Inventory inv, String itemId) {
    if (inv == null || itemId == null) { return 0; }

    int count = 0;
    for (ItemStack itemStack : inv) {
      if (itemId.equals(getId(itemStack))) {
        count += itemStack.getAmount();
      }
    }
    return count;
  }

  /**
   * 指定されたItemのカスタムIDが一致すれかどうかを調べる。どちらもnullの場合はfalseをかえす。
   *
   * @param item1 item1
   * @param item2 item2
   * @return 一致した場合はtrue
   */
  public static boolean equalsId(ItemStack item1, ItemStack item2) {
    String id1 = getId(item1);
    // idが存在しない場合はfalse
    if (id1 == null) { return false; }

    String id2 = getId(item2);
    return id1.equals(id2);
  }

  public static ItemStack getAir() {
    return ITEM_AIR;
  }
}
