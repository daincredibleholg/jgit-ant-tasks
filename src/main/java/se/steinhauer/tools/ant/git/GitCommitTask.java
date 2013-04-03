package se.steinhauer.tools.ant.git;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;

/**
 * @author Sergey Bogutskiy
 */
public class GitCommitTask extends Task {

    private String repository;
    private String username;
    private String email;
    private String message;

    @Override
    public void execute() throws BuildException {
        try {

            Git git = Git.open(new File(repository));

            log("Start git commit...");
            RevCommit callRes = git.commit().setCommitter(username, email).setMessage(message).call();
            logCommitResult(callRes);
            log("End git commit.");

        } catch (Exception e) {
            log(e, Project.MSG_ERR);
            throw new BuildException("Could not commit to repository: " + e.getMessage(), e);
        }
    }

    private void logCommitResult(RevCommit callRes) {
        log("Commit result: ");
        log(callRes.getFullMessage());
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
