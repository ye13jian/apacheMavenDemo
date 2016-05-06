package com.jianye.ant.util;

import java.io.Closeable;

public class CloseIoUtil {
   
    /***
     * 关闭IO流
     * 
     * @param cls
     */
    public static void closeAll(Closeable... cls) {
 
        if (cls != null) {
            for (Closeable cl : cls) {
                try {
                    if (cl != null)
                        cl.close();
                } catch (Exception e) {
 
                } finally {
                    cl = null;
                }
            }
        }
    }
}
