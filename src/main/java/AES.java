import java.util.Arrays;
public class AES {

    byte[][] subKeys; // 44 column wide key


    /**
     * Encrypt whole thing
     * @param message - bytes to encrypt
     * @param key     - 16 byte key
     * @return - encoded bytes
     */
    public byte[] encode(byte[] message, byte[] key){

        int wholeBlocksCount = message.length / 16;
        int charactersToEncodeCount;
        if (wholeBlocksCount == 0) {
            charactersToEncodeCount = 16;
        } else if (message.length % 16 != 0) {
            charactersToEncodeCount = (wholeBlocksCount + 1) * 16;
        } else {
            charactersToEncodeCount = wholeBlocksCount * 16;
        }

        byte[] result = new byte[charactersToEncodeCount];
        byte[] temp = new byte[charactersToEncodeCount];
        byte[] blok = new byte[16];
        this.subKeys = this.generateSubKeys(key);


        // rewrite message to temporary array + append 0s
        for (int i = 0; i < charactersToEncodeCount; ++i) {
            if (i < message.length) {
                temp[i] = message[i];
            } else {
                temp[i] = 0;
            }
        }

        // construct output array...
        int i = 0;
        while (i < temp.length) {
            for (int j = 0; j < 16; ++j) {
                blok[j] = temp[i++];
            }

            blok = this.encrypt(blok);
            System.arraycopy(blok, 0, result, i - 16, blok.length);
        }

        return result;
    }


    /**
     * @param state - 128 bit - 16 byte block
     * @return encrypted 16 block
     */
    private byte[] encrypt(byte[] state) {
        //todo
        return new byte[1];
    }

    private byte[][] generateSubKeys(byte[] keyInput) {
        // 11 subkeys thus 44 columns of key
        //todo
        return new byte[1][1];
    }


    private byte[] subBytes(byte[] state){
        byte[] tmp = new byte[state.length];
        for (int i = 0; i < state.length; i++) {
            tmp[i] = SBox.translate(state[i]);
        }
        return tmp;
    }

    public byte[] shiftRows(byte[] state){
        // create two dimensional array for easier shifting
        byte[][] tmp = new byte[4][4];
        int k = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tmp[j][i] = state[k];
                k++;
            }
        }

        shiftArrayLeft(tmp[1], 1);
        shiftArrayLeft(tmp[2], 2);
        shiftArrayLeft(tmp[3], 3);

        // create one dimensional array to return output
        byte[] newState = new byte[16];
        k = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                newState[k] = tmp[j][i];
                k++;
            }
        }

        return newState;
    }


    public byte[] shiftArrayLeft(byte[] array, int step){
        for(int i = 0; i < step; i++){
            int j;
            byte first;
            //Stores the last element of array
            first = array[0];

            for(j = 1; j < array.length; j++){
                //Shift element of array by one
                array[j-1] = array[j];
            }
            //Last element of array will be added to the start of array.
            array[array.length-1] = first;
        }
        return array;
    }


    public byte[] mixColumns(byte[] state) {
        //1. create columns

        byte[][] columns = new byte[4][4];
        int k = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                columns[i][j] = state[k];
                k++;
            }
        }
        //2. apply function to columns
        for (int i = 0; i < 4; i++) {
            columns[i] = multiplySingleColumn(columns[i]);
        }

        //4. create single dimension state output
        byte[] tmp = new byte[16];
        k = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tmp[k] = columns[i][j];
                k++;
            }
        }
        return tmp;

    }


    /**
     * @param column to mutliply by matrix
     * @return column multiplied by matrix:
     * 02 03 01 01
     * 01 02 03 01
     * 01 01 02 03
     * 03 01 01 02
     */
    public byte[] multiplySingleColumn(byte[] column) {
        byte[] c = new byte[4];
        c[0] = (byte)(Utils.fMul((byte) 0x02, column[0]) ^ Utils.fMul((byte) 0x03, column[1]) ^ column[2] ^ column[3]);
        c[1] = (byte)(column[0] ^ Utils.fMul((byte) 0x02, column[1]) ^ Utils.fMul((byte) 0x03, column[2]) ^ column[3]);
        c[2] = (byte)(column[0] ^ column[1] ^ Utils.fMul((byte) 0x02, column[2]) ^ Utils.fMul((byte) 0x03, column[3]));
        c[3] = (byte)(Utils.fMul((byte) 0x03, column[0]) ^ column[1] ^ column[2] ^ Utils.fMul((byte) 0x02, column[3]));
        return c;
    }

}
