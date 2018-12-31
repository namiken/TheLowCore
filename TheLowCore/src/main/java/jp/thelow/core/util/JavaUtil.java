package jp.thelow.core.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.Duration;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.reflect.ClassPath;

public class JavaUtil {
  public static Set<Class<?>> getInterface(Class<?> clazz) {
    return new InterfaceGetter(clazz).getInterfaceList();
  }

  private static Random rnd = new Random();

  public static boolean isRandomTrue(int percent) {
    // 確率が0以下なら失敗
    if (percent <= 0) { return false; }
    // 確率が100以上なら成功
    if (percent >= 100) { return true; }

    if (rnd.nextInt(100) < percent) { return true; }
    return false;
  }

  public static boolean isRandomTrue(int percent, Random random) {
    // 確率が0以下なら失敗
    if (percent <= 0) { return false; }
    // 確率が100以上なら成功
    if (percent >= 100) { return true; }

    if (random.nextInt(100) < percent) { return true; }
    return false;
  }

  public static double round(double val, int digits) {
    double pow = Math.pow(10, digits);
    return Math.round(pow * val) / pow;
  }

  public static int getInt(String data, int other) {
    if (data == null) { return other; }
    try {
      return Integer.parseInt(data.trim());
    } catch (Exception e) {
      return (int) getDouble(data, other);
    }
  }

  public static long getLong(String data, long other) {
    if (data == null) { return other; }
    try {
      return Long.parseLong(data.trim());
    } catch (Exception e) {
      return (long) getDouble(data, other);
    }
  }

  public static float getFloat(String data, float other) {
    if (data == null) { return other; }
    try {
      return Float.parseFloat(data);
    } catch (Exception e) {
      return other;
    }
  }

  public static short getShort(String data, short other) {
    if (data == null) { return other; }
    try {
      return Short.parseShort(data);
    } catch (Exception e) {
      return other;
    }
  }

  public static boolean getBoolean(String data, boolean other) {
    if (data == null) { return other; }
    if ("true".equalsIgnoreCase(data)) {
      return true;
    } else if ("false".equalsIgnoreCase(data)) { return false; }
    return other;
  }

  public static double getDouble(String deta, double other) {
    try {
      return Double.parseDouble(deta);
    } catch (Exception e) {
      return other;
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> List<T> getInstanceList(Class<?> labelClass, Class<T> superClass) {
    // スキルを登録する
    try {
      return (List<T>) ClassPath.from(labelClass.getClassLoader())
          .getTopLevelClassesRecursive(labelClass.getPackage().getName())
          .stream()
          .map(info -> info.load())
          .filter(c -> !c.isInterface())
          .filter(c -> !Modifier.isAbstract(c.getModifiers()))
          .filter(superClass::isAssignableFrom)
          .map(JavaUtil::getNewInstance).collect(Collectors.toList());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * newInatanceメソッドの例外を無視したメソッド
   *
   * @param clazz
   * @return
   */
  public static <T> T getNewInstance(Class<T> clazz) {
    try {
      return clazz.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      // 起こりえない
      e.printStackTrace();
      return null;
    }
  }

  /**
   * targetがmin(含まない)とmax(含まない)の間に入っていればTRUE
   *
   * @param target
   * @param min
   * @param max
   * @return
   */
  public static boolean isIn(double target, double min, double max) {
    if (min > max) { throw new RuntimeException("min bigger than max!! min:" + min + ", max:" + max); }
    return target > min && target < max;
  }

  /**
   * targetがmin(含む)とmax(含む)の間に入っていればTRUE
   *
   * @param target
   * @param min
   * @param max
   * @return
   */
  public static boolean isInClose(double target, double min, double max) {
    // minのほうが多い場合は入れ替える
    if (min > max) {
      double temp = min;
      min = max;
      max = temp;
    }
    return target >= min && target <= max;
  }

  static TimeZone timeZone = TimeZone.getTimeZone("Asia/Tokyo");

  public static long getJapanTimeInMillis() {
    Calendar cal1 = Calendar.getInstance(timeZone);
    long timeInMillis = cal1.getTimeInMillis();
    return timeInMillis;
  }

  @SuppressWarnings("unchecked")
  public static <T> T getField(Class<?> clazz, String fieldName, Object targetInstance) {
    try {
      Field field = clazz.getDeclaredField(fieldName);
      field.setAccessible(true);
      return (T) field.get(targetInstance);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Privateなメソッドに値を挿入する
   *
   * @param target_object
   * @param field_name
   * @param value
   * @throws Exception
   */
  public static void setPrivateField(Object target_object, String field_name, Object value) {
    try {
      Class<?> c = target_object.getClass();
      Field fld = c.getDeclaredField(field_name);
      fld.setAccessible(true);
      fld.set(target_object, value);
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  /**
   * Privateなメソッドに値を挿入する
   *
   * @param target_object
   * @param field_name
   * @param value
   * @throws Exception
   */
  public static void setPrivateField(Class<?> targetClass, Object target_object, String field_name, Object value) {
    try {
      Field fld = targetClass.getDeclaredField(field_name);
      fld.setAccessible(true);
      fld.set(target_object, value);
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  /**
   * NullならnullValueを返す。nullじゃないならそのままvalueを返す
   *
   * @param value
   * @param nullValue
   * @return
   */
  public static <T> T getNull(T value, T nullValue) {
    if (value == null) { return nullValue; }
    return value;
  }

  /**
   * 指定されたSupplierから値を取得する。もしエラーが起きればdefaultValueから取得する
   *
   * @param supplier
   * @param defaultValue
   * @return
   */
  public static <T> T getValueOrDefaultWhenThrow(Supplier<T> supplier, T defaultValue) {
    try {
      return supplier.get();
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * メッセージ付きでExceptionを投げる
   *
   * @param message
   */
  public static void throwException(String message) {
    throw new RuntimeException(message);
  }

  /**
   * valがnullまたは空文字ならTRUE
   */
  public static boolean isEmpty(String val) {
    return val == null || val.isEmpty();
  }

  /**
   * valがnullまたは空文字ならTRUE
   */
  public static String getDefault(String val, String defaultString) {
    if (isEmpty(val)) { return defaultString; }

    return val;
  }
}

class InterfaceGetter {
  private Set<Class<?>> interfaceList = new HashSet<>();

  private Class<?> clazz;

  public InterfaceGetter(Class<?> clazz) {
    this.clazz = clazz;
  }

  private void search(Class<?> clazz) {
    for (Class<?> inter : clazz.getInterfaces()) {
      // すでに探索済みなら何もしない
      if (interfaceList.contains(inter)) {
        continue;
      }
      interfaceList.add(inter);
      search(inter);
    }
    Class<?> superclass = clazz.getSuperclass();
    if (superclass == null) { return; }
    search(superclass);
  }

  public Set<Class<?>> getInterfaceList() {
    search(this.clazz);
    return this.interfaceList;
  }

  public String formatDuration(long duration) {
    Duration ofMillis = Duration.ofMillis(duration);
    StringBuilder builder = new StringBuilder();
    if (ofMillis.toHours() > 0) {
      builder.append(ofMillis.toHours());
      builder.append("時間");
    }

    if (ofMillis.toMinutes() > 0) {
      builder.append(ofMillis.toMinutes() % 60);
      builder.append("分");
    }

    builder.append(ofMillis.getSeconds() % 60);
    builder.append("秒");
    return builder.toString();
  }
}
