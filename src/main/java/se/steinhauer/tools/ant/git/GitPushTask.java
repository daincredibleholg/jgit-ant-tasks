package se.steinhauer.tools.ant.git;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.transport.PushResult;

import java.io.File;

public class GitPushTask extends GitRemoteTask {

    private String repository;

    @Override
    public void execute() throws BuildException {
        try {

            Git git = Git.open(new File(repository));
            log("Start git push...");
            PushCommand pushCommand = git.push();
            if (isCredentialsValid()) {
                pushCommand.setCredentialsProvider(getDefaultCredentialsProvider());
            }
            Iterable<PushResult> pushResults = pushCommand.setForce(true).setPushAll().call();
            logResults(pushResults);
            log("End git push.");

        } catch (Exception e) {
            log(e, Project.MSG_ERR);
            throw new BuildException("Could not push repository: " + e.getMessage(), e);
        }
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }
}
