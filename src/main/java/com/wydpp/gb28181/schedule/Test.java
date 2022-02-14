package com.wydpp.gb28181.schedule;

import com.wydpp.controller.model.HiPotQueue;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.table.TableStringConverter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Test {

    private final static ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 200,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(5));

    private static boolean boon;

    @AllArgsConstructor
    public static class TestThread extends Thread {

        private LinkedBlockingDeque<String> queues;

        @Override
        public void run() {
            while (!boon) {
                String take;
                try {
                    take = queues.take();
                    log.info("接受到消息take={}",take);
                    // 模拟实际的处理事情
//                        Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public void run(){
        List<TestThread> testThreads = new ArrayList<>();
        LinkedBlockingDeque<String> queues = new LinkedBlockingDeque<>(100);

        for (int i = 0; i < 5; i++) {
            TestThread testThread = new TestThread(queues);
            executor.execute(testThread);
            testThreads.add(testThread);
        }

        for (int i = 0; i < 10000; i++) {
            try {
                queues.put(i+"");
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
        boon = true;

        /*for (TestThread testThread : testThreads) {
            log.info("开始调用interrupt");
            testThread.interrupt();
        }*/
    }


    public static void main(String[] args) {
        Test test = new Test();
        for (int i = 0; i < 100; i++) {
            new Thread(test::run).start();
        }
    }

}
