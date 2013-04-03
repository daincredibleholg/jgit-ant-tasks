package se.steinhauer.tools.ant.git;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.OperationResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.util.StringUtils;

/**
 * @author Sergey Bogutskiy
 */
public abstract class GitRemoteTask extends Task {

    protected String username;
    protected String password;
    protected String keyfilePath;
    protected String passphrase;

    protected boolean isUserPasswordCredentialsValid() {
        // the credentials are valid as log both is set
        boolean valid = !StringUtils.isEmptyOrNull(username) && password != null;

        if (StringUtils.isEmptyOrNull(keyfilePath) || StringUtils.isEmptyOrNull(passphrase)) {
            // just give a warning that both is configured...
            log("Found non-empty username and password but also keyfile or passphrase are set.", Project.MSG_DEBUG);
        }

        return valid;
    }

    protected boolean isKeyfileCredentialsValid() {
        boolean valid = !StringUtils.isEmptyOrNull(username) && !StringUtils.isEmptyOrNull(keyfilePath);
        return valid;
    }

    protected boolean isCredentialsValid() {
        return !StringUtils.isEmptyOrNull(username) && password != null;
    }

    protected CredentialsProvider getDefaultCredentialsProvider() {
        CredentialsProvider cp = null;

        if (isUserPasswordCredentialsValid()) {
            cp = new UsernamePasswordCredentialsProvider(username, password);
            log("Using username and password credentials.", Project.MSG_DEBUG);
        } else if (isKeyfileCredentialsValid()) {
            cp = null;
            log("Using username and password credentials.", Project.MSG_DEBUG);
        }

        return cp;
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
