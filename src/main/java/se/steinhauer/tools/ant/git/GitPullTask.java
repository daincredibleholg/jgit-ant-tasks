package se.steinhauer.tools.ant.git;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;

import java.io.File;

/**
 * @author Sergey Bogutskiy
 */
public class GitPullTask extends GitRemoteTask {

    private String repository;

    @Override
    public void execute() throws BuildException {
        // credentials
        try {
            log("Start git pull... ");
            Git git = Git.open(new File(repository));
            PullCommand pullCommand = git.pull();

            if (isCredentialsValid()) {
                pullCommand.setCredentialsProvider(getDefaultCredentialsProvider());
            }

            pullCommand.call();
            log("End git pull.");
        } catch (Exception e) {
            log(e, Project.MSG_ERR);
            throw new BuildException("Could not pull repository: " + e.getMessage(), e);
        }
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

}