package com.alibaba.smart.framework.engine.test;

import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.exception.LockException;
import com.alibaba.smart.framework.engine.model.assembly.IdBasedElement;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * Created by 高海军 帝奇 74394 on 2017 January  18:03.
 */
@Service
public class FileLockStrategy implements LockStrategy {

    private  static volatile RandomAccessFile raf;
    private  static volatile FileLock lock;

    static {

        try {
            raf = new RandomAccessFile("smart-engine.lock", "rw");
        } catch (FileNotFoundException e) {
            throw new EngineException(e);
        }

    }

    @Override
    public void tryLock(String processInstanceId, ExecutionContext context) throws LockException {

        FileChannel fileChannel = raf.getChannel();

        try {
            System.out.println(" ----trying lock---- "+context.getExecutionInstance()+" ----|||||---- ");
            lock = fileChannel.tryLock();
            System.out.println(" ----locked ---- "+context.getExecutionInstance()+" ----|||||---- "+lock.toString());
        } catch (IOException e) {
            throw new EngineException(e);
        }


        System.out.println(context.getExecutionInstance()+" has got the lock");

    }

    @Override
    public void unLock(String processInstanceId, ExecutionContext context) throws LockException {
        try {
            lock.release();
            System.out.println(" ----release---- "+context.getExecutionInstance()+" ----|||||---- "+lock.toString());

        } catch (IOException e) {
            throw new EngineException(e);
        }
    }


}
