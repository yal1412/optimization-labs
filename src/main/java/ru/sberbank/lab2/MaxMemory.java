package ru.sberbank.lab2;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;

public class MaxMemory {

    /**
     * Задание: запускать код этого класса со следующими параметрами VM и
     * объяснить значение Max memory, которое выводится в консоль, для каждого случая.
     * Желательно использовать Java версии 8, если используется другая, то в каждом пункте, кроме пятого, нужно еще добавить параметр -XX:+UseConcMarkSweepGC
     * <p>
     * 1.  -Xmx512m
     * <p>
     * 2.  -Xmx512m -Xms512m
     * <p>
     * 3.  -Xmx512m -Xms512m -XX:+PrintGCDetails
     * <p>
     * 4.  -Xms512m -Xmx512m -XX:SurvivorRatio=100
     * <p>
     * 5.  -Xmx512m -XX:+UseG1GC
     */

    static String mb(long s) {
        return String.format("%d (%.2f M)", s, (double) s / (1024 * 1024));
    }

    public static void main(final String[] args) {
        printMaxMemory();
    }

    public static void printMaxMemory() {
        final long maxMemory = Runtime.getRuntime().maxMemory();

        System.out.printf("Max memory: %d MB\n", maxMemory / 1024 / 1024);

        System.out.println("Runtime max: " + mb(Runtime.getRuntime().maxMemory()));
        MemoryMXBean m = ManagementFactory.getMemoryMXBean();

        System.out.println("Non-heap: " + mb(m.getNonHeapMemoryUsage().getMax()));
        System.out.println("Heap: " + mb(m.getHeapMemoryUsage().getMax()));

        for (MemoryPoolMXBean mp : ManagementFactory.getMemoryPoolMXBeans()) {
            System.out.println("Pool: " + mp.getName() +
                    " (type " + mp.getType() + ")" +
                    " = " + mb(mp.getUsage().getMax()));
        }
    }
}