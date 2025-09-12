/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.commands.monkey.events.base;

import android.app.IActivityManager;
import android.view.IWindowManager;

import com.android.commands.monkey.events.MonkeyEvent;
import com.android.commands.monkey.utils.Logger;

/**
 * monkey throttle event
 */
public class MonkeyThrottleEvent extends MonkeyEvent {
    /* private */ long mThrottle;

    public MonkeyThrottleEvent(long throttle) {
        super(MonkeyEvent.EVENT_TYPE_THROTTLE);
        mThrottle = throttle;
    }

    /**
     * 注入monkey事件的方法
     *
     * @return MonkeyEvent.INJECT_SUCCESS 当事件成功注入时返回
     * @return MonkeyEvent.INJECT_FAIL 当线程在睡眠过程中被中断时返回
     */
    @Override
    public int injectEvent(IWindowManager iwm, IActivityManager iam, int verbose) {
        long sleep = mThrottle / 1000;
        if (verbose > 1 && mThrottle > 0) {
            Logger.println("点击频率 参数： " + mThrottle + "，将执行休眠" + sleep + "秒");
        }
        try {
            Thread.sleep(mThrottle);
        } catch (InterruptedException e1) {
            Logger.warningPrintln("Monkey interrupted in sleep.");
            return MonkeyEvent.INJECT_FAIL;
        }

        return MonkeyEvent.INJECT_SUCCESS;
    }
}
