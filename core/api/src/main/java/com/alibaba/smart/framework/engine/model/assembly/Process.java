package com.alibaba.smart.framework.engine.model.assembly;

import java.util.List;

/**
 * Created by ettear on 16-4-12.
 */
public interface Process extends Activity {

    List<BaseElement> getElements();
}
