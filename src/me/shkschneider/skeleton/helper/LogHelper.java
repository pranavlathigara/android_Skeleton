/**
 * Copyright 2013 ShkSchneider
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.shkschneider.skeleton.helper;

import me.shkschneider.skeleton.SkeletonApplication;

@SuppressWarnings("unused")
public class LogHelper {

    public static final int VERBOSE = 10;
    public static final int DEBUG = 20;
    public static final int INFO = 30;
    public static final int WARN = 40;
    public static final int ERROR = 50;

    public static void log(final int state, final String msg) {
        final String tag = SkeletonApplication.TAG;

        // Uses StackTrace to build the log tag
        final StackTraceElement[] elements = new Throwable().getStackTrace();
        String callerClassName = "?";
        String callerMethodName = "?";
        if (elements.length >= 3) {
            callerClassName = elements[2].getClassName();
            callerClassName = callerClassName.substring(callerClassName.lastIndexOf('.') + 1);
            if (callerClassName.indexOf("$") > 0) {
                callerClassName = callerClassName.substring(0, callerClassName.indexOf("$"));
            }
            callerMethodName = elements[2].getMethodName();
            callerMethodName = callerMethodName.substring(callerMethodName.lastIndexOf('_') + 1);
            if (callerMethodName.equals("<init>")) {
                callerMethodName = callerClassName;
            }
        }

        final String stack = callerClassName + " " + callerMethodName + "()";
        switch (state) {
            case VERBOSE: android.util.Log.v(tag, "[" + stack + "] " + msg); break ;
            case DEBUG: android.util.Log.d(tag, "[" + stack + "] " + msg); break ;
            case INFO: android.util.Log.i(tag, "[" + stack + "] " + msg); break ;
            case WARN: android.util.Log.w(tag, "[" + stack + "] " + msg); break ;
            case ERROR: android.util.Log.e(tag, "[" + stack + "] " + msg); break ;
            default: break ;
        }
    }

    public static void v(final String msg) {
        log(VERBOSE, msg);
    }

    public static void d(final String msg) {
        log(DEBUG, msg);
    }

    public static void i(final String msg) {
        log(INFO, msg);
    }

    public static void w(final String msg) {
        log(WARN, msg);
    }

    public static void e(final String msg) {
        log(ERROR, msg);
    }

}
