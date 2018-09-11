package no.kristiania.pgr200.common;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class Spinner {
    private static String[] spinner = new String[]{
            "⠙",
            "⠋",
            "⠹",
            "⠸",
            "⠼",
            "⠴",
            "⠦",
            "⠧",
            "⠇",
            "⠏"
    };
    private static boolean isSpinning = false;
    private static String text = null;
    private static int index = 0;

    public static void spin(Runnable runnable) {
        spin(runnable, null);
    }

    public static void spin(Runnable runnable, String text) {
        Spinner.text = text;
        SpinnerTask task1 = new SpinnerTask();
        Thread thread1 = new Thread(task1);
        thread1.start();
        runnable.run();
        thread1.interrupt();
    }

    private static class SpinnerTask implements Runnable {

        @Override
        public void run () {
            try {
                System.out.print("\u001B[?25l");
                while (true) {
                    for (String spin: Spinner.spinner) {
                        System.out.write(("\r" + spin + (Spinner.text != null ? " " + Spinner.text : "")).getBytes());
                        if (Thread.currentThread().isInterrupted()) {
                            return;
                        }
                        try {
                            TimeUnit.MILLISECONDS.sleep(80);
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                }
            } catch (IOException e) {
            } finally {
                System.out.print("\u001B[?25h");
            }
        }
    }
}
