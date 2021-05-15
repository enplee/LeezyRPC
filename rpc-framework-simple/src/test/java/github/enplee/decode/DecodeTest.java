package github.enplee.decode;

import github.enplee.enums.CompressTypeEnum;
import org.junit.Test;

public class DecodeTest {

    @Test
    void decodeTest(){
        byte type = (byte)0x01;
        final String name = CompressTypeEnum.getName(type);
    }
}
