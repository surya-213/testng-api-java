package utils;

import exceptions.UtilException;
import java.util.concurrent.TimeUnit;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class PauseUtil {

  /**
   * Pauses the current thread for a specified duration based on the provided time unit and value.
   *
   * @param timeUnit   The {@link TimeUnit} in which the sleep duration is specified (e.g., seconds,
   *                   milliseconds).
   * @param timerValue The amount of time to sleep, in the units provided by {@code timeUnit}.
   * @throws UtilException if the thread is interrupted during sleep, wrapping the original
   *                       exception.
   */
  public static void forDuration(TimeUnit timeUnit, int timerValue) {
    try {
      timeUnit.sleep(timerValue);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UtilException(e.getMessage(), e);
    }
  }
}
