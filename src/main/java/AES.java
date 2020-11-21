public class AES {

    byte[][] keyWords = new byte[44][4]; // 44 words of 32(4bytes) bits
    byte[] originalKey;

    public AES(byte[] originalKey) {
        this.originalKey = originalKey;
        generateSubKeys(originalKey);
    }

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
    public byte[] encrypt(byte[] state) {
        byte[] tmp = state;

        tmp = addKey(tmp,0);

        // first round
        tmp = subBytes(tmp);
        tmp = shiftRows(tmp);
        tmp = mixColumns(tmp);
        tmp = addKey(tmp, 1);

        // rounds 2 - 9
        for (int i = 2; i < 10; i++) {
            tmp = subBytes(tmp);
            tmp = shiftRows(tmp);
            tmp = mixColumns(tmp);
            tmp = addKey(tmp, 2);
        }

        // last round
        tmp = subBytes(tmp);
        tmp = shiftRows(tmp);
        tmp = addKey(tmp, 10);

        return tmp;
    }

    private void generateSubKeys(byte[] keyInput) {
        // copy original key to first 4 words:
        int j = 0;
        for (int i = 0; i < 4; i++) {
            for (int k = 0; k < 4; k++) {
                keyWords[i][k] = keyInput[j];
            }
        }

        for (int round = 1; round <= 10; round++) {
            keyWords[4*round] = xorWords(keyWords[4*round-4], g(keyWords[4*round-1], round));
            keyWords[4*round+1] = xorWords(keyWords[4*round], keyWords[4*round-3]);
            keyWords[4*round+2] = xorWords(keyWords[4*round+1], keyWords[4*round-2]);
            keyWords[4*round+2] = xorWords(keyWords[4*round+2], keyWords[4*round-1]);
        }
    }

    public byte[] xorWords(byte[] word1, byte[] word2) {
        if (word1.length == word2.length){
            byte[] tmp = new byte[word1.length];
            for (int i = 0; i < word1.length; i++) {
                tmp[i] = (byte)(word1[i] ^ word2[i]);
            }
            return tmp;
        } else {
            return null;
        }
    }

    public byte[] g(byte[] word, int round) {
        byte[] tmp = shiftArrayLeft(word, 1);
        for (int i = 0; i < 4; i++) {
            tmp[i] = SBox.translate(tmp[i]);
        }

        // round coefficient added to first element
        byte RC = Utils.polynomialModuloDivision((byte)(0b1 << (round-1)));
        tmp[0] ^= RC;

        return tmp;
    }

    public byte[] addKey(byte[] state, int round){
        // round = 1
        byte[] tmp = new byte[state.length];
        int start = round*4;
        int end = start + 4; // not inclusive
        int k = 0;
        for (int i = start; i < end; i++) { // iterate over words
            for (int j = 0; j < 4; j++) { // iterate over bytes in words
                tmp[k] = (byte) (state[k] ^ keyWords[i][j]);
                k++;
            }
        }
        return tmp;
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
