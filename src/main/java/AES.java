public class AES {

    byte[][] key; // 44 column wide key


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
        this.key = this.generateKey(key);


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

    private byte[] encrypt(byte[] blok) {
        //todo
        return new byte[1];
    }

    private byte[][] generateKey(byte[] key) {
        //todo
        return new byte[1][1];
    }

}
