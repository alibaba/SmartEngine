package com.alibaba.smart.framework.engine.test;

import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.exception.LockException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;


@Service
public class SimpleFileLockStrategy implements LockStrategy {

    private  static volatile RandomAccessFile raf;
    private  static volatile FileLock lock;

    static {

        try {
            raf = new RandomAccessFile("smart-engine.lock", "rw");
        } catch (Exception e) {
            throw new EngineException(e);
        }

    }

    @Override
    public void tryLock(String processInstanceId, ExecutionContext context) throws LockException {

        FileChannel fileChannel = raf.getChannel();

        try {

                acquireLockRepeatedly(context, fileChannel);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    private void acquireLockRepeatedly(ExecutionContext context, FileChannel fileChannel) throws IOException {
        int i = 3;

        while (true){

            if(i <=0){
//                throw new RuntimeException("timeout");
                break;
            }

            try {
                if(null != context){
                    System.out.println(i+" times, "+Thread.currentThread()+" ----trying lock---- "+ context.getExecutionInstance()+" ----|||||---- ");
                }
                lock = fileChannel.lock();
                if(null != context) {

                    System.out.println(Thread.currentThread() + " ----locked ---- " + context.getExecutionInstance() + " ----|||||---- " + lock.toString());
                }
                if(lock.isValid()){
                    break;
                }
            }catch (OverlappingFileLockException e) {
                //ignore
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100));
            }

            i--;
        }

    }

    @Override
    public void unLock(String processInstanceId, ExecutionContext context) throws LockException {
        try {
            lock.release();
            if(null != context) {

                System.out.println(Thread.currentThread() + " ----release---- " + context.getExecutionInstance() + " ----|||||---- " + lock.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();

            throw new EngineException(e);
        }
    }


}
