/*
 * Copyright (c) 2014, george thomas
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE CRYPTIX FOUNDATION LIMITED AND
 * CONTRIBUTORS ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE CRYPTIX FOUNDATION LIMITED OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package se.steinhauer.tools.ant;

import java.util.HashMap;
import java.util.Map;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import com.jcraft.jsch.Logger;

/**
 * An implementation of JSch's {@link Logger} that writes to Ant's logger.
 *
 * @author george thomas
 * @since 2014-10-05
 */
public class JschLoggerForAnt implements Logger {

    @SuppressWarnings("serial")
    private static final Map<Integer, Integer> levelMapper = new HashMap<Integer, Integer>() {{
        put(Logger.DEBUG, Project.MSG_VERBOSE);
        /*
         * unfortunately, because JSch uses the Logger.INFO level to log all
         * SSH communication, we map it to Project.MSG_VERBOSE to eliminate
         * noise from the output
         */
        put(Logger.INFO, Project.MSG_VERBOSE);
        put(Logger.WARN, Project.MSG_WARN);
        put(Logger.ERROR, Project.MSG_ERR);
        put(Logger.FATAL, Project.MSG_ERR);
    }};

    private Task task = null;

    public JschLoggerForAnt(Task task) {
        this.task = task;
    }

    /**
     * @see com.jcraft.jsch.Logger#isEnabled(int)
     */
    @Override
    public boolean isEnabled(int level) {
        return true;
    }

    /**
     * @see com.jcraft.jsch.Logger#log(int, java.lang.String)
     */
    @Override
    public void log(int level, String message) {
        task.getProject().log(message, levelMapper.get(level));
    }
}
