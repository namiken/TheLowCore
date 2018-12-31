package jp.thelow.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;

import jp.thelow.core.TheLowCoreMain;

/**
 * <p>
 * DungeonCoreの大部分で使用されるロガーです。
 * </p>
 *
 * @version 2.0
 * @see LogLevel
 */
public final class DungeonLogger {

  // /** Logger */
  // private static Logger TRACE_LOGGER = LogManager.getLogManager().getLogger("TraceLogger");

  /**
   * ロガーで使用可能なログのレベルを表します。
   *
   * @since 2.0
   * @version 2.0
   * @author azure
   * @see DungeonLogger
   */
  public static enum LogLevel {
    /** 情報メッセージ */
    INFO,
    /** 警告メッセージ */
    WARNING,
    /** エラーメッセージ */
    ERROR,
    /** SEVEREメッセージ */
    SEVERE,
    /** デバッグ情報メッセージ */
    DEBUG,
    /** 開発情報メッセージ */
    DEVELOPMENT,
    /** トレースレベルのメッセージ */
    TRACE;
  }

  /**
   * <p>
   * 指定されたログ レベルでメッセージのログをとります。
   * </p>
   *
   * @param message 文字列のメッセージ
   * @param level ログのレベル
   */
  public static void log(String message, LogLevel level) {
    StringBuilder prefix = new StringBuilder("[The LoW");
    java.util.logging.Logger logger = Bukkit.getLogger();
    switch (level) {
    case INFO:
      prefix.append(" Info] ");
      logger.info(prefix.append(message).toString());
      break;
    case WARNING:
      prefix.append(" Warning] ");
      logger.warning(prefix.append(message).toString());
      break;
    case ERROR:
      prefix.append(" Error] ");
      logger.severe(prefix.append(message).toString());
      break;
    case SEVERE:
      prefix.append(" Severe] ");
      logger.severe(prefix.append(message).toString());
      break;
    case DEVELOPMENT:
      prefix.append(" Dev] ");
      logger.info(prefix.append(message).toString());
      break;
    case DEBUG:
      prefix.append(" Debug] ");
      logger.info(prefix.append(message).toString());
      break;
    case TRACE:
      prefix.append(" Trace] ");
      logger.info(prefix.append(message).toString());
      break;
    default:
      prefix.append("] ");
      logger.info(prefix.append(message).toString());
      break;
    }
  }

  /**
   * <p>
   * 情報メッセージのログをとります。
   * </p>
   *
   * @param message 文字列のメッセージ
   */
  public static void info(String message) {
    log(message, LogLevel.INFO);
  }

  /**
   * <p>
   * 警告メッセージのログをとります。
   * </p>
   *
   * @param message 文字列のメッセージ
   */
  public static void warning(String message) {
    log(message, LogLevel.WARNING);
  }

  /**
   * <p>
   * エラーメッセージのログをとります。
   * </p>
   * <p>
   * これは、SEVEREメッセージと同等です。
   * </p>
   *
   * @param message 文字列のメッセージ
   */
  public static void error(String message) {
    log(message, LogLevel.ERROR);
  }

  /**
   * <p>
   * SEVEREメッセージのログをとります。
   * </p>
   *
   * @param message 文字列のメッセージ
   */
  public static void severe(String message) {
    log(message, LogLevel.SEVERE);
  }

  /**
   * <p>
   * デバッグ情報メッセージのログをとります。
   * </p>
   * <p>
   * これは、情報メッセージと同等です。
   * </p>
   *
   * @param message 文字列のメッセージ
   */
  public static void debug(String message) {
    log(message, LogLevel.DEBUG);
  }

  /**
   * <p>
   * 開発情報メッセージのログをとります。
   * </p>
   * <p>
   * これは、情報メッセージと同等です。
   * </p>
   *
   * @param message 文字列のメッセージ
   */
  public static void development(String message) {
    log(message, LogLevel.DEVELOPMENT);
  }

  // ログファイルのファイル名
  private static File logFile = new File(TheLowCoreMain.getPlugin().getDataFolder().getAbsolutePath(),
      "logs\\log_" + DateTimeFormatter.ofPattern("uuuuMMdd_hhmmss").format(LocalDateTime.now()) + ".txt");

  private static PrintStream printStream = null;

  /**
   * <p>
   * Traceメッセージのログをとります。
   * </p>
   * <p>
   * これは、情報メッセージと同等です。
   * </p>
   *
   * @param message 文字列のメッセージ
   */
  public static void trace(String message) {
    if (printStream != null) {
      new RuntimeException(message).printStackTrace(printStream);
      log(message, LogLevel.TRACE);
    }
  }

  /**
   * <p>
   * Traceメッセージのログをとります。
   * </p>
   * <p>
   * これは、情報メッセージと同等です。
   * </p>
   *
   * @param message 文字列のメッセージ
   */
  public static void trace(Object... message) {
    if (printStream != null) {
      log(StringUtils.join(message), LogLevel.TRACE);
    }
  }

  public static void toggleTraceLog() {
    try {
      if (!logFile.exists()) {
        logFile.getParentFile().mkdirs();
        logFile.createNewFile();
      }

      if (printStream == null) {
        printStream = new PrintStream(new FileOutputStream(logFile, true));
      } else {
        printStream.flush();
        printStream.close();
        printStream = null;
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
