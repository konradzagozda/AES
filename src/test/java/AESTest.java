import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AESTest {
    AES aes;

    @BeforeEach
    void init() {
        this.aes = new AES();
    }

    @Test
    void shiftRowsTest() {
        byte[] state = new byte[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        byte[] expected = new byte[]{0,5,10,15,4,9,14,3,8,13,2,7,12,1,6,11};
        byte[] shifted = aes.shiftRows(state);
        System.out.println(Arrays.toString(shifted));
        assertArrayEquals(expected, shifted);
    }

    @Test
    void shiftArrayLeftTest() {
        byte[] state = new byte[]{0,1,2,3};
        byte[] shifted = aes.shiftArrayLeft(state,1);
        byte[] expected = new byte[]{1,2,3,0};
        System.out.println(Arrays.toString(shifted));
        assertArrayEquals(expected, shifted);

        state = new byte[]{0,1,2,3};
        shifted = aes.shiftArrayLeft(state,2);
        expected = new byte[]{2,3,0,1};
        System.out.println(Arrays.toString(shifted));
        assertArrayEquals(expected, shifted);

        state = new byte[]{0,1,2,3};
        shifted = aes.shiftArrayLeft(state,3);
        expected = new byte[]{3,0,1,2};
        System.out.println(Arrays.toString(shifted));
        assertArrayEquals(expected, shifted);
    }

}