/*
 * Java-based distributed database like Mysql
 */

package io.jsql.hazelcast.test;

import java.io.StringReader;
import java.util.Scanner;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: jiang
 * \* Date: 2017/8/24 0024
 * \* Time: 14:16
 * \
 */
public class Stringtest {
    public static void main(String[] args) {
        String s2 = "fdfdfd||dfasfsd a fasdf || fasfsf2||";
        Scanner scanner = new Scanner(new StringReader(s2));
        scanner.useDelimiter("\\|\\|");
        while (scanner.hasNext()) {
            String s = scanner.next();
            if (s.length() > 0) {
                System.out.println(s);
            }
        }
    }
}
