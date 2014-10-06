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

package se.steinhauer.tools.ant.git;

import org.apache.tools.ant.Task;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.OperationResult;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.util.StringUtils;
import se.steinhauer.tools.ant.JschLoggerForAnt;
import se.steinhauer.tools.jgit.transport.JschKeyfileConfigSessionFactory;
import se.steinhauer.tools.jgit.transport.SshKeyfileCredentialsProvider;

/**
 * This is the base class implementation.
 *
 * In addition to the great work of Sergey, I've added
 * SSH keyfile support.
 *
 * @author Sergey Bogutskiy, Holger Steinhauer
 */
public abstract class GitRemoteTask extends Task {

    /**
     * Username on the SSH host
     */
    private String username;
    /**
     * If username and password is used to authenticate, this
     * member holds the password.
     */
    private String password;
    /**
     * Path to the SSH keyfile, if needed.
     */
    private String keyfilePath;
    /**
     * Keyfile's passphrase
     */
    private String passphrase;

    /**
     * Checks if username and password are set. This information
     * can be used to decide if SSH authentication via username
     * and password is wanted.
     *
     * @return True, if username is neither null nor empty and password is set.
     */
    protected boolean isUserPasswordCredentialsValid() {
        // the credentials are valid as log both is set
        boolean valid = !StringUtils.isEmptyOrNull(username) && password != null;

        if (StringUtils.isEmptyOrNull(keyfilePath) || StringUtils.isEmptyOrNull(passphrase)) {
            // just give a warning that both is configured...
            log("Found non-empty username and password but also keyfile or passphrase are set.");
        }

        return valid;
    }

    /**
     * Checks if username and keyfile are set. If so, it is very likely we
     * have to use SSH key authentication.
     * This method does <strong>NOT</strong> check, if a passphrase is set.
     * Beside it is always a good idea to secure your keyfile with a
     * passphrase, it is not striclty needed. So the passphrase is optional.
     *
     * @return True, if username and keyfilePath are neither null nor empty.
     */
    protected boolean isKeyfileCredentialsValid() {
        return !StringUtils.isEmptyOrNull(username) && !StringUtils.isEmptyOrNull(keyfilePath);
    }

    /**
     * Checks if credentials for authentication via username/password <strong>OR</strong>
     * username/keyfile are set.
     *
     * @return
     *      True, if {@link #isKeyfileCredentialsValid()} or {@link #isUserPasswordCredentialsValid()}
     *      return true.
     */
    protected boolean isCredentialsValid() {
        return isUserPasswordCredentialsValid() || isKeyfileCredentialsValid();
    }

    /**
     * This method will initiate the needed steps for authentication, if
     * any is needed.
     *
     * If neither username/password nor username/keyfile are configured, nothing is
     * to do and the result will be null.
     *
     * In the case username and password are available, the {@link UsernamePasswordCredentialsProvider} will
     * be initiated with the credentials and returned. This will be also the case, if a keyfile is specified!
     *
     * The last check will be the look on username and keyfile. If they are set an instance of
     * {@link SshKeyfileCredentialsProvider} will be created and returned. In addition,
     * {@link #initKeyfileJschConfig()} is called and with it, the Jsch session stuff will be configured
     * to use the keyfile and, if available, passphrase.
     *
     * @return Depending on the above described logic, a valid CredentialProvider or null.
     */
    protected CredentialsProvider getDefaultCredentialsProvider() {
        CredentialsProvider cp = null;

        if (isUserPasswordCredentialsValid()) {
            cp = new UsernamePasswordCredentialsProvider(username, password);
            log("Using username and password credentials.");
        } else if (isKeyfileCredentialsValid()) {
            // create new credentials provider
            cp = new SshKeyfileCredentialsProvider(username, passphrase);
            initKeyfileJschConfig();
            log("Using keyfile.");
        }

        return cp;
    }

    /**
     * Once we have detected SSH key authentication, we have to configure
     * the {@link SshSessionFactory} to use our {@link JschKeyfileConfigSessionFactory}.
     * The latter one will be initiated in this method and the keyfile will be configured.
     */
    protected void initKeyfileJschConfig() {
        JschKeyfileConfigSessionFactory jschConfigSessionFactory = new JschKeyfileConfigSessionFactory(keyfilePath);
        jschConfigSessionFactory.setLogger(new JschLoggerForAnt(this));
        SshSessionFactory.setInstance(jschConfigSessionFactory);
    }

    protected void logResults(Iterable<? extends OperationResult> results) {
        log("Result: ");
        for (OperationResult result : results) {
            log(result.getMessages());
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setKeyfilePath (String keyfilePath) {
        this.keyfilePath = keyfilePath;
    }

    public void setPassphrase (String passphrase) {
        this.passphrase = passphrase;
    }
}
