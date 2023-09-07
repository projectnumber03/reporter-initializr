package ru.plorum.reporterinitializr;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;

public class IdGeneratorTest {

    @Test
    public void test() {
        LinkedList<String> result = new LinkedList<>();
        recursion(999999998, result);
        System.out.println(String.join("-", result));
    }

    public static void recursion(final long number, final LinkedList<String> parts) {
        if (number <= 0 && parts.size() > 2) return;
        parts.addFirst(String.format("%03d", number % 1000));
        recursion(number / 1000, parts);
    }

}
