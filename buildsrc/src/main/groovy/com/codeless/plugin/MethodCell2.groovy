package com.codeless.plugin

/**
 * Created by nailperry on 2017/3/4.
 */

public class MethodCell2 {

/*    // 原方法名
    String diggerID   //根据注解生成ID。
    // 采集数据的方法名
    String agentName = "onTest"
    // 采集数据的方法描述
    String agentDesc="(Ljava/lang/Object;[Ljava/lang/Object;)V"*/

    // 原方法名
    String name
    // 原方法描述
    String desc

    String source

    // 采集数据的方法名
    String agentName
    // 采集数据的方法描述
    String agentDesc


    MethodCell2(String name, String desc, String source, String agentName, String agentDesc) {
        this.desc = desc;
        this.source = source;
        this.name = name;
        this.agentName = agentName
        this.agentDesc = agentDesc
    }

    public static MethodCell2 parse(String line){
        if(line == null){
            return null;
        }
        line = line.trim()
        if(line.startsWith("#")){
            return null;
        }
        if(line.isEmpty()){
            return null;
        }
        String[] words =  line.split("[, \t]");

        int fillIndex = 0;
        for(int i =0 ;i< words.length;i++){
            String w = words[i].trim();
            if(w.size() > 0){
                words[fillIndex++]=w
            }
        }

        return new MethodCell2(words[0],words[1],words[2],words[3],words[4])

    }


}