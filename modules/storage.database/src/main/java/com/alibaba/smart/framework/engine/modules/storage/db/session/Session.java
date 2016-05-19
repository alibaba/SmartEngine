package com.alibaba.smart.framework.engine.modules.storage.db.session;
public interface Session {
  
  void flush();

  void close();
}
