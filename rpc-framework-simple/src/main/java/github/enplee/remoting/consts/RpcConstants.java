package github.enplee.remoting.consts;

/**
 *  @author: leezy
 *  @Date: 2021/5/14 21:31
 *  @Description: const val
 */
public class RpcConstants {

    public static final byte[] MAGIC_NUMBER = {(byte)'l',(byte)'r',(byte)'p',(byte)'c'};
    public static final int MAX_FRAME_LENGTH = 8 * 1024 * 1024;
    public static final int TOTAL_LENGTH = 16;
    public static final byte VERSION = 1;
}
