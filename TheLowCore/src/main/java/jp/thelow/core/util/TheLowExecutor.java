package jp.thelow.core.util;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import jp.thelow.core.TheLowCoreMain;

public class TheLowExecutor {

  /**
   * 指定された作業を指定Tick後に実行する。もしdelayTickが0の場合は即時実行される
   *
   * @param delayTick
   * @param runnable
   */
  public static void executeLater(double delayTick, Runnable runnable) {
    if (delayTick == 0) {
      runnable.run();
    } else {
      new BukkitRunnable() {
        @Override
        public void run() {
          runnable.run();
        }
      }.runTaskLater(TheLowCoreMain.plugin, (long) delayTick);
    }
  }

  /**
   * メソッドを実行し戻り値を取得する。もしExceptionが発生した場合はdefaultValueを返す
   *
   * @param supplier
   * @param defaultValue
   * @return
   */
  public static <T> T getObjectIgnoreException(Supplier<T> supplier, T defaultValue) {
    try {
      return supplier.get();
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * メソッドを実行する。もしExceptionが発生した場合はexceptionConsumerを実行する
   *
   * @param runnable
   * @param exceptionConsumer
   */
  public static void executeIgnoreException(Runnable runnable, Consumer<Exception> exceptionConsumer) {
    try {
      runnable.run();
    } catch (Exception e) {
      exceptionConsumer.accept(e);
    }
  }

  /**
   * メソッドを実行し戻り値を取得する。もしExceptionが発生した場合はdefaultValueを返す
   *
   * @param supplier
   * @param defaultValue
   * @return
   */
  public static double getDoubleIgnoreException(DoubleSupplier supplier, double defaultValue) {
    try {
      return supplier.getAsDouble();
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * メソッドを実行し戻り値を取得する。もしExceptionが発生した場合はdefaultValueを返す
   *
   * @param supplier
   * @param defaultValue
   * @return
   */
  public static int getIntIgnoreException(IntSupplier supplier, int defaultValue) {
    try {
      return supplier.getAsInt();
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * メソッドを実行し戻り値を取得する。もしExceptionが発生した場合はdefaultValueを返す
   *
   * @param supplier
   * @param defaultValue
   * @return
   */
  public static boolean getBooleanIgnoreException(BooleanSupplier supplier, boolean defaultValue) {
    try {
      return supplier.getAsBoolean();
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * 条件に満たす場合のみオブジェクトを取得する。満たさない場合はnullをかえす。
   *
   * @param bool 条件
   * @param object 取得するオブジェクトを提供するインスタンス
   * @return オブジェクト or null
   */
  public static <T> T getObjectIfTrue(boolean bool, Supplier<T> object) {
    if (bool) { return object.get(); }
    return null;
  }

  /**
   * 条件に満たす場合のみオブジェクトを取得する。満たさない場合は-1をかえす。
   *
   * @param bool 条件
   * @param object 取得するオブジェクトを提供するインスタンス
   * @return オブジェクト or null
   */
  public static int getIntIfTrue(boolean bool, IntSupplier object) {
    if (bool) { return object.getAsInt(); }
    return -1;
  }

  /**
   * 非同期でタスクを実行する。
   *
   * @param asyncFunc 非同期タスク
   * @param syncFunc 同期タスク
   */
  public static <T> void execAsync(Supplier<T> asyncFunc, Consumer<T> syncFunc) {
    new BukkitRunnable() {
      @Override
      public void run() {
        T result = asyncFunc.get();
        Bukkit.getScheduler().callSyncMethod(TheLowCoreMain.plugin, () -> {
          syncFunc.accept(result);
          return null;
        });
      }

    }.runTaskAsynchronously(TheLowCoreMain.plugin);
  }
}
