package se.steinhauer.tools.ant.git;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import org.apache.tools.ant.Task;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.OperationResult;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.util.StringUtils;
import se.steinhauer.tools.jgit.transport.JschKeyfileConfigSessionFactory;
import se.steinhauer.tools.jgit.transport.SshKeyfileCredentialsProvider;

/**
 * @author Sergey Bogutskiy
 */
public abstract class GitRemoteTask extends Task {

    private String username;
    private String password;
    private String keyfilePath;
    private String passphrase;

    protected boolean isUserPasswordCredentialsValid() {
        // the credentials are valid as log both is set
        boolean valid = !StringUtils.isEmptyOrNull(username) && password != null;

        if (StringUtils.isEmptyOrNull(keyfilePath) || StringUtils.isEmptyOrNull(passphrase)) {
            // just give a warning that both is configured...
            log("Found non-empty username and password but also keyfile or passphrase are set.");
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
            log("Using username and password credentials.");
        } else if (isKeyfileCredentialsValid()) {
            // create new credentials provider
            cp = new SshKeyfileCredentialsProvider(username, keyfilePath, passphrase);
            log("Using keyfile.");

            // set everything needed on JSch
            initKeyfileJschConfig();
        }

        return cp;
    }

    protected void initKeyfileJschConfig() {
        JschKeyfileConfigSessionFactory jschConfigSessionFactory = new JschKeyfileConfigSessionFactory();

        JSch jsch = new JSch();
        try {
            jsch.addIdentity(keyfilePath);
            // TODO check this...
            jsch.setKnownHosts(".ssh/known_hosts");
        } catch (JSchException e) {
            e.printStackTrace();
        }

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
