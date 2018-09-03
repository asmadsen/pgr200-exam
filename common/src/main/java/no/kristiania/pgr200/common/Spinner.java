package no.kristiania.pgr200.common;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class Spinner {
    private static Set<String> spinner = new TreeSet<>(Arrays.asList(
            "⠋",
            "⠙",
            "⠹",
            "⠸",
            "⠼",
            "⠴",
            "⠦",
            "⠧",
            "⠇",
            "⠏"));
    private static boolean isSpinning = false;
    private static String text = null;
    private static int index = 0;

    public static void start() {
        start(null);
    }

    private static void start(String text) {
        Spinner.isSpinning = true;
        Spinner.text = text;
        Spinner.run();
    }

    public static void spin(Runnable runnable) {
        spin(runnable, null);
    }

    public static void spin(Runnable runnable, String text) {
        System.out.println(String.join(" - ", Arrays.asList(
                "⠋",
                "⠙",
                "⠹",
                "⠸",
                "⠼",
                "⠴",
                "⠦",
                "⠧",
                "⠇",
                "⠏")));
        return;
        /*Spinner.text = text;
        SpinnerTask task1 = new SpinnerTask();
        Thread thread1 = new Thread(task1);
        thread1.start();
        runnable.run();
        thread1.interrupt();*/
    }

    private static class SpinnerTask implements Runnable {

        @Override
        public void run () {
            try {
                while (true) {
                    for (String spin: Spinner.spinner) {
                        System.out.write(("\r" + spin + (Spinner.text != null ? " " + Spinner.text : "")).getBytes());
                        if (Thread.currentThread().isInterrupted()) {
                            return;
                        }
                        try {
                            TimeUnit.MICROSECONDS.sleep(80);
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static void stop() {
        Spinner.isSpinning = false;
        Spinner.text = null;

    }

    private static void run() {
        try {
            while (Spinner.isSpinning) {
                for (String spin: Spinner.spinner) {
                    System.out.write(("\r" + spin).getBytes());
                    Thread.sleep(80);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
