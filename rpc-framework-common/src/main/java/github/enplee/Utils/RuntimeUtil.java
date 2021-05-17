package github.enplee.Utils;

/**
 *  @author: leezy
 *  @Date: 2021/5/17 17:26
 *  @Description: 获取当前运行环境参数
 */
public class RuntimeUtil {
    public static int cpus() {
        return Runtime.getRuntime().availableProcessors();
    }
}
