import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDequeTest {
    @Test
    public void addFirstTest() {
        ArrayDeque<Integer> arrd1 = new ArrayDeque<>();

        arrd1.addFirst(0);
        assertThat(arrd1.toList()).containsExactly(0).inOrder();

        arrd1.addFirst(1);
        assertThat(arrd1.toList()).containsExactly(1, 0).inOrder();

        arrd1.addFirst(2);
        assertThat(arrd1.toList()).containsExactly(2, 1, 0).inOrder();
    }

    @Test
    public void addLast() {
        ArrayDeque<Integer> arrd1 = new ArrayDeque<>();

        arrd1.addLast(0);
        assertThat(arrd1.toList()).containsExactly(0).inOrder();

        arrd1.addLast(1);
        assertThat(arrd1.toList()).containsExactly(0, 1).inOrder();

        arrd1.addLast(2);
        assertThat(arrd1.toList()).containsExactly(0, 1, 2).inOrder();
    }

    @Test
    public void addFirstAndAddLastTest() {
        ArrayDeque<Integer> arrd1 = new ArrayDeque<>();

        arrd1.addFirst(0);
        arrd1.addLast(2);
        arrd1.addFirst(3);
        arrd1.addLast(1);
        arrd1.addFirst(4);
        arrd1.addLast(5);

        assertThat(arrd1.toList()).containsExactly(4, 3, 0, 2, 1, 5).inOrder();
    }

    @Test
    public void removeFirstAndLastTest() {
        ArrayDeque<Integer> arrd1 = new ArrayDeque<>();

        arrd1.addFirst(0);
        arrd1.addLast(2);
        arrd1.addFirst(3);
        arrd1.addLast(1);
        arrd1.removeFirst();
        assertThat(arrd1.toList()).containsExactly(0, 2, 1).inOrder();

        arrd1.removeLast();
        assertThat(arrd1.toList()).containsExactly(0, 2).inOrder();
    }

    @Test
    public void sizeTest() {
        ArrayDeque<Integer> arrd1 = new ArrayDeque<>();
        arrd1.addFirst(0);
        arrd1.addLast(2);
        arrd1.addFirst(3);
        assertThat(arrd1.size()).isEqualTo(3);

        arrd1.removeFirst();
        assertThat(arrd1.size()).isEqualTo(2);

        arrd1.removeLast();
        assertThat(arrd1.size()).isEqualTo(1);
    }

    @Test
    public void isEmptyTest() {
        ArrayDeque<Integer> arrd1 = new ArrayDeque<>();

        assertThat(arrd1.isEmpty()).isTrue();

        arrd1.addFirst(0);
        assertThat(arrd1.isEmpty()).isFalse();

        arrd1.removeLast();
        assertThat(arrd1.isEmpty()).isTrue();
    }

    @Test
    public void getTest() {
        ArrayDeque<Integer> arrd1 = new ArrayDeque<>();
        arrd1.addFirst(8);
        arrd1.addLast(4);
        arrd1.addFirst(87);
        arrd1.addLast(1);
        arrd1.addFirst(5);
        arrd1.addLast(9);

        assertThat(arrd1.get(-76)).isEqualTo(null);
        assertThat(arrd1.get(8)).isEqualTo(null);
        assertThat(arrd1.get(0)).isEqualTo(5);
        assertThat(arrd1.get(3)).isEqualTo(8);

        arrd1.removeLast();
        assertThat(arrd1.get(arrd1.size() - 1)).isEqualTo(1);
    }

    @Test
    public void resizeTest() {
        ArrayDeque<Integer> arrd1 = new ArrayDeque<>();

        arrd1.addFirst(0);
        arrd1.addFirst(1);
        arrd1.addFirst(2);
        arrd1.addLast(3);
        arrd1.addFirst(4);
        arrd1.addFirst(6);
        arrd1.addFirst(6);
        arrd1.addFirst(7);
        arrd1.addFirst(8);

        assertThat(arrd1.toList()).containsExactly(8, 7, 6, 5, 4, 2, 1, 0, 3).inOrder();
        assertThat(arrd1.size()).isEqualTo(9);
        assertThat(arrd1.get(0)).isEqualTo(8);
        assertThat(arrd1.get(8)).isEqualTo(3);
        assertThat(arrd1.get(7)).isEqualTo(6);

        arrd1.addLast(9);
        arrd1.addLast(10);
        arrd1.removeFirst();
        arrd1.removeLast();
        assertThat(arrd1.toList()).containsExactly(7, 6, 5, 4, 2, 1, 0, 3, 9).inOrder();
        assertThat(arrd1.get(0)).isEqualTo(7);
        assertThat(arrd1.get(arrd1.size() - 1)).isEqualTo(9);


        ArrayDeque<Integer> arrd2 = new ArrayDeque<>();

        arrd2.addLast(0);
        arrd2.addLast(1);
        arrd2.addFirst(3);
        arrd2.addFirst(4);
        arrd2.addFirst(5);
        arrd2.addFirst(6);
        arrd2.addFirst(7);
        arrd2.addLast(8);

        arrd2.addLast(9);

        assertThat(arrd2.toList()).containsExactly(7, 6, 5, 4, 3, 0, 1, 8, 9).inOrder();
        assertThat(arrd2.size()).isEqualTo(9);
        assertThat(arrd2.get(0)).isEqualTo(7);
        assertThat(arrd2.get(9)).isEqualTo(9);
        assertThat(arrd2.get(4)).isEqualTo(3);

        assertThat(arrd2.removeFirst()).isEqualTo(7);
        assertThat(arrd2.removeFirst()).isEqualTo(6);
        assertThat(arrd2.toList()).containsExactly(5, 4, 3, 0, 1, 8, 9).inOrder();
    }
}
