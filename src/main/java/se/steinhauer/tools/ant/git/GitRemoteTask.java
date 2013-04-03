package se.steinhauer.tools.ant.git;

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

    protected boolean isCredentialsValid() {
        return !StringUtils.isEmptyOrNull(username) && password != null;
    }

    protected CredentialsProvider getDefaultCredentialsProvider() {
        if (isCredentialsValid()) {
            return new UsernamePasswordCredentialsProvider(username, password);
        } else {
            return null;
        }
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
}
