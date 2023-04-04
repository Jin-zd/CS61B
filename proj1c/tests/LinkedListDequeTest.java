
import deque.LinkedListDeque;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
public class LinkedListDequeTest {
    @Test
    public void equalsTest() {
        LinkedListDeque<Integer> arrd1 = new LinkedListDeque<>();
        arrd1.addLast(1);
        arrd1.addLast(2);
        arrd1.addLast(3);

        LinkedListDeque<Integer> arrd2 = new LinkedListDeque<>();
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
        LinkedListDeque<Integer> arrd1 = new LinkedListDeque<>();
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
        LinkedListDeque<Integer> arrd1 = new LinkedListDeque<>();
        arrd1.addLast(1);
        arrd1.addLast(2);
        arrd1.addLast(3);

        assertThat(arrd1).containsExactly(1, 2, 3).inOrder();
    }
}
