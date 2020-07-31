package com.buried.point;

import android.os.SystemClock;
import android.os.Trace;

/*************************************
 *
 * 性能追踪工具。
 * @Author : raodongming
 * @Date : 10:12  2020/5/19
 * @Email : 11101037@bbktel.com
 * @title : 
 * @Company : www.vivo.com
 * @Description : 
 ************************************/
public class TraceUtils {

    private static final ThreadLocal<TraceCache> mLocal = new ThreadLocal();
    private static final String TAG = "TraceUtils";



    private static TracePathMsg gTracePathMsg = null;

    /**
     * 开始追踪耗时信息。线程安全，用于追踪方法。traceStart()可以嵌套在方法里面调用
     *
     * @param message
     */
    public static void traceStart(String message) {
        TraceCache traceCache = mLocal.get();
        if (traceCache == null) {
            traceCache = new TraceCache();
            mLocal.set(traceCache);
        }
        traceCache.put(message,SystemClock.uptimeMillis());
        Trace.beginSection(message);
    }

    /**
     * 结束追踪耗时信息。
     *
     * @param msg
     */
    public static void traceEnd(String msg) {

        traceEnd(msg, 80);
    }
    public static void traceEnd(String msg, long warnDuraiton) {
        traceEnd(msg, 80,null);

    }
    /**
     * 结束追踪耗时信息。
     *
     * @param msg
     * @param warnDuraiton 单位为毫秒 超过上报限制时间，就打印告警日志。
     */
    public static void traceEnd(String msg, long warnDuraiton,String extraMsg) {
        Trace.endSection();
        TraceCache traceCache = mLocal.get();
        TraceTagMsg traceMsg = traceCache.pop();
        StringBuffer sb = traceCache.sb;
        if (traceMsg == null) {
            sb.delete(0, sb.length());
            sb.append("duration =?,  startMsg=?,endMsg=");
            sb.append(msg);
            LogUtils.w(TAG, sb.toString());
        } else {
            long duration = SystemClock.uptimeMillis() - traceMsg.startTime;
            boolean debugLevel =  warnDuraiton > duration;
            if(debugLevel){
                return;
            }
            //性能告警，warn级别打印
            sb.delete(0, sb.length());
            sb.append("duration = ");
            sb.append(duration);
            if (traceMsg.msg.equals(msg)) {
                sb.append("ms,  msg=");
                sb.append(traceMsg.msg);
                if(extraMsg!= null){
                    sb.append(",extraMsg=");
                    sb.append(extraMsg);

                }
            } else {
                sb.append("ms,  startMsg=");
                sb.append(traceMsg.msg);
                sb.append(",endMsg=");
                sb.append(msg);
            }
            LogUtils.w(TAG, sb.toString());

        }

    }



    private static class TraceCache {
        //Stack<TraceStartMsg> mCache = new Stack<>();
        private TraceTagMsg[] mCache = new TraceTagMsg[10];
        private int top = -1;

        void put(String msg,long startTime){
            top++;
            if(top >= mCache.length){
                throw new IllegalStateException("mCache.size to small");
            }
            if(mCache[top] == null){
                mCache[top] = new TraceTagMsg();
            }
            mCache[top].msg = msg;
            mCache[top].startTime = startTime;
        }

        TraceTagMsg pop(){
            if(top > -1){
                int i = top--;
                if (i >= 0 && i < mCache.length){
                    return mCache[i];
                }
                return null;
            }
            return null;
        }

        StringBuffer sb = new StringBuffer();
    }

    private static class TraceTagMsg {
        String msg;
        long startTime;
    }

    private static class TracePathMsg {

        final TraceTagMsg[] pathList;
        int mVeryIndex = -1;

        TracePathMsg(String ...msg){
            pathList = new TraceTagMsg[msg.length];
            for(int i = 0; i < msg.length;i++){
                pathList[i] = new TraceTagMsg();
                pathList[i].msg = msg[i];
            }
        }

        void addToVerify(){
            int index = mVeryIndex + 1;
            TraceTagMsg msg = pathList[index];
            msg.startTime = SystemClock.uptimeMillis();
            mVeryIndex ++;
        }

        boolean isFinished(){
            return mVeryIndex + 1 >= pathList.length;
        }

        boolean isTheNext(String msg){
            int index = mVeryIndex + 1;
            if(msg.equals(pathList[index].msg)){
                return true;
            }
            return false;
        }

        void buildString(StringBuffer sb){
            if(isFinished()){
                sb.append("\npath finished: dur = " );
            }else{
                sb.append("\npath unfinished: dur = ");
            }

            if(mVeryIndex < 0){
                sb.append("?");
            }else{
                sb.append(pathList[mVeryIndex].startTime - pathList[0].startTime);
                sb.append("\n");
                sb.append("     msg=");
                sb.append(pathList[0].msg);
                sb.append(",time=");
                sb.append(pathList[0].startTime);
            }
            int size = Math.min(mVeryIndex,pathList.length -1);
            for(int i = 1; i<= size ;i++){
                sb.append("\n");
                sb.append("     msg=");
                sb.append(pathList[i].msg);
                sb.append(",dur=");
                sb.append(pathList[i].startTime - pathList[i -1].startTime);
                sb.append(",time=");
                sb.append(pathList[i].startTime);
            }



        }
    }
}
