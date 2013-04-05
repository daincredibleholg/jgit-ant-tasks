/*
 * Copyright (c) 2013, Holger Steinhauer
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of the FreeBSD Project.
 */

package se.steinhauer.tools.jgit.transport;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.util.FS;

/**
 * JSch based SSH session for keyfile based authentication.
 *
 * @author Holger Steinhauer
 * @since 2013-04-03
 */
public class JschKeyfileConfigSessionFactory extends JschConfigSessionFactory {

    private JSch _jsch = null;
    private String keyfilePath = null;

    /**
     * Signals whether to use strict host key checking or not.
     * If the check is enabled, any change of the host's SSH key
     * fingerprint will lead to a immediate interuption. But, on
     * the other hand, you will minimize the chance of
     * man-in-the-middle attacks.
     */
    private boolean strictHostKeyChecking = true;

    /**
     * Simply calls the super constructor.
     * Strict host key checking will be enabled.
     */
    public JschKeyfileConfigSessionFactory (String keyfilePath) {
        super();

        this.keyfilePath = keyfilePath;
    }

    /**
     * This constructor let you set the strict host key checking.
     *
     * @param strictHostKeyChecking
     *              En- or disables the strict host key checking
     */
    public JschKeyfileConfigSessionFactory (boolean strictHostKeyChecking) {
        super();
        this.strictHostKeyChecking = strictHostKeyChecking;
    }

    /**
     * Sets the strcith host key checking configuation to the
     * OpenSSH configuation, used by the super SSH session
     * implementation.
     *
     * @param hc {@link JschConfigSessionFactory#configure}
     * @param session  {@link JschConfigSessionFactory#configure}
     */
    @Override
    protected void configure(OpenSshConfig.Host hc, Session session) {
        session.setConfig("StrictHostKeyChecking", useStrictHostKeyChecking());
    }

    @Override
    protected Session createSession(OpenSshConfig.Host hc, String user, String host, int port, FS fs) throws JSchException {
        Session session = super.createSession(hc, user, host, port, fs);

        _jsch = getJSch(hc, fs);
        _jsch.addIdentity(keyfilePath);

        return session;
    }

    /**
     * Converts the {@link #strictHostKeyChecking} to "yes" or "no" string
     * representation that can be used e.g. in session config.
     *
     * @return "yes", unless @link #strictHostKeyChecking is false.
     */
    public String useStrictHostKeyChecking () {
        String strict = "yes";

        if (!strictHostKeyChecking) {
            strict = "no";
        }

        return  strict;
    }

    public boolean isStrictHostKeyChecking() {
        return strictHostKeyChecking;
    }

    public void setStrictHostKeyChecking(boolean strictHostKeyChecking) {
        this.strictHostKeyChecking = strictHostKeyChecking;
    }
}
