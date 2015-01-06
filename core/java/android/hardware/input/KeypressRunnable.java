/*
 * Copyright (C) 2014 VanirAOSP && The Android Open Source Project
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

package android.hardware.input;

import android.os.SystemClock;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;

import java.util.ArrayList;


/**
 * used internally by InputManager
 * @hide
 */
public class KeypressRunnable implements Runnable
{
    private final int keyCode;
    private final int keyFlags;
    private KeyEvent keyEvent;
    public KeypressRunnable(final int code, final int flags) {
        keyCode = code;
        keyFlags = flags;
    }

    @Override
    public void run() {
        final long now = SystemClock.uptimeMillis();
        if (keyEvent == null) {
            // null ==> down
            keyEvent = new KeyEvent(now, now, KeyEvent.ACTION_DOWN,
                keyCode, 0, 0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0,
                keyFlags, InputDevice.SOURCE_KEYBOARD);
            InputManager.getInstance().injectInputEvent(keyEvent, InputManager.INJECT_INPUT_EVENT_MODE_ASYNC);
        } else {
            // not null ==> up, then reset to null
            InputManager.getInstance().injectInputEvent(KeyEvent.changeAction(
                    KeyEvent.changeTimeRepeat(
                            keyEvent, now, keyEvent.getRepeatCount()),
                    KeyEvent.ACTION_UP), InputManager.INJECT_INPUT_EVENT_MODE_ASYNC);
            keyEvent = null;
        }
    }
}
