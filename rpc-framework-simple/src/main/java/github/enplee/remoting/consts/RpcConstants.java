package github.enplee.remoting.consts;

import java.net.PortUnreachableException;

/**
 *  @author: leezy
 *  @Date: 2021/5/14 21:31
 *  @Description: const val
 */
public class RpcConstants {
    // message type
    public static final byte REQUEST_TYPE = 1;
    public static final byte RESPONCE_TYPE = 2;
    public static final byte HEARTBEAT_REQUEST_TYPE = 3;
    public static final byte HEARTBEAT_RESPONCE_TYPE = 4;
    // heartBeat messageBody
    public static final String PING = "PING";
    public static final String PONG = "PONG";
    // frame constants
    public static final byte[] MAGIC_NUMBER = {(byte)'l',(byte)'r',(byte)'p',(byte)'c'};
    public static final int MAX_FRAME_LENGTH = 8 * 1024 * 1024;
    public static final int MAX_HEAD_LENGTH = 16;
    public static final int TOTAL_LENGTH = 16;
    public static final byte VERSION = 1;
}
