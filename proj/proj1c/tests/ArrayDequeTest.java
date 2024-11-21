import deque.ArrayDeque;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class ArrayDequeTest {
    @Test
    public void equalsTest() {
        ArrayDeque<Integer> arrd1 = new ArrayDeque<>();
        arrd1.addLast(1);
        arrd1.addLast(2);
        arrd1.addLast(3);

        ArrayDeque<Integer> arrd2 = new ArrayDeque<>();
        arrd2.addLast(1);
        arrd2.addLast(2);
        arrd2.addLast(3);

        assertThat(arrd1.equals(arrd1)).isTrue();
        assertThat(arrd1.equals(null)).isFalse();
        assertThat(arrd1.equals(arrd2)).isTrue();

        arrd2.removeFirst();

        assertThat(arrd2.equals(arrd1)).isFalse();
    }

    @Test
    public void iteratorTest() {
        ArrayDeque<Integer> arrd1 = new ArrayDeque<>();
        arrd1.addLast(1);
        arrd1.addLast(2);
        arrd1.addLast(3);

        int i = 0;
        for (int x : arrd1) {
            assertThat(x).isEqualTo(arrd1.get(i));
            i++;
        }
    }

    @Test
    public void toStringTest() {
        ArrayDeque<Integer> arrd1 = new ArrayDeque<>();
        arrd1.addLast(1);
        arrd1.addLast(2);
        arrd1.addLast(3);

        assertThat(arrd1).containsExactly(1, 2, 3).inOrder();
    }
}
