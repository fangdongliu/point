package cn.fdongl.point.auth.util;

import java.util.UUID;

/**
 * @ClassName IdGen
 * @Description ID生成
 * @Author zm
 * @Date 2019/9/7 8:46
 * @Version 1.0
 **/
public class IdGen {
    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     * 全局时空唯一
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
