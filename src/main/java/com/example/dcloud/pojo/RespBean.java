package com.example.dcloud.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {
    private Integer code;
    private String message;
    private Object obj;

    /**
     * 返回成功结果，无对象参数
     * @param message
     * @return
     */

    public static RespBean success(String message){
        return new RespBean(200,message, null);
    }

    /**
     * 返回成功结果，有对象参数
     * @param message
     * @param obj
     * @return
     */
    public static RespBean success(String message,Object obj){
        return new RespBean(200,message, obj);
    }

    /**
     * 返回失败结果，无对象参数
     * @param message
     * @return
     */
    public static RespBean error(String message){
        return new RespBean(500,message, null);
    }

    /**
     * 返回失败结果，有对象参数
     * @param message
     * @param obj
     * @return
     */
    public static RespBean error(String message,Object obj){
        return new RespBean(500,message, obj);
    }
}
