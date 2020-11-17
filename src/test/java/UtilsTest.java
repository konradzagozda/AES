import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsTest {
    @Test
    void polynomialMultiplicationTest() {
        assertEquals(1, (Utils.polynomialMultiplication((byte) 1, (byte) 1)));
        assertEquals(0, (Utils.polynomialMultiplication((byte) 0, (byte) 0)));
        assertEquals(2, (Utils.polynomialMultiplication((byte) 2, (byte) 1)));
        assertEquals(4, (Utils.polynomialMultiplication((byte) 2, (byte) 2)));
        assertEquals(8, (Utils.polynomialMultiplication((byte) 2, (byte) 4)));
        assertEquals(8, (Utils.polynomialMultiplication((byte) 4, (byte) 2)));
        assertEquals(0b110011, Utils.polynomialMultiplication((byte) 0b110011, (byte) 1));
        assertEquals(0b100000001, Utils.polynomialMultiplication((byte) 0b110011, (byte) 0b1111));
    }

    @Test
    void polynomialDivisionTest() {
        assertEquals(0b11011, Utils.polynomialModuloDivision(0b100000000));
        assertEquals(0b11010, Utils.polynomialModuloDivision(0b100000001));
        assertEquals(0b101100, Utils.polynomialModuloDivision(0b1100000001));
    }

}