package com.ds.flink.core.faulttolerance.Correctness.functions;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;

/**
 * @ClassName: MapFunctionWithException
 * @Description: 触发异常
 * @author: ds-longju
 * @Date: 2022-09-05 10:59
 * @Version 1.0
 **/
public class MapFunctionWithException extends RichMapFunction<Tuple3<String, Long, Long>, Tuple3<String, Long, Long>> {

    private transient int indexOfSubTask;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        indexOfSubTask = getRuntimeContext().getIndexOfThisSubtask();
    }

    @Override
    public Tuple3<String, Long, Long> map(Tuple3<String, Long, Long> event) throws Exception {
        if (event.f1 % 10 == 0) {
            String msg = String.format("Bad data [%d]...", event.f1);
            // 抛出异常，作业根据 配置 的重启策略进行恢复，无重启策略作业直接退出。
            throw new RuntimeException(msg);
        }
        return new Tuple3<>(event.f0, event.f1, System.currentTimeMillis());
    }
}
