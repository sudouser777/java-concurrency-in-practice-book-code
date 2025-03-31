package chapter05;

import java.util.Vector;

/**
 * SafeVectorHelpers
 * <p/>
 * Compound actions on Vector using client-side locking
 *
 * @author Brian Goetz and Tim Peierls
 */
public class SafeVectorHelpers {
    public static Object getLast(Vector list) {
        synchronized (list) {
            int lastIndex = list.size() - 1;
            return list.get(lastIndex);
        }
    }

    public static void deleteLast(Vector list) {
        synchronized (list) {
            int lastIndex = list.size() - 1;
            list.remove(lastIndex);
        }
    }

    public void maThrowException(Vector list) {
        for (int i = 0; i < list.size(); i++) {
            doSomething(list.get(i));
        }
    }

    public void withClientSideLocking(Vector list) {
        synchronized (list) {
            for (int i = 0; i < list.size(); i++) {
                doSomething(list.get(i));
            }
        }
    }


    private void doSomething(Object obj) {
    }
}