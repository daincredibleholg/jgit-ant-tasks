package se.steinhauer.tools.ant.git;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;

import java.io.File;

public class GitCloneTask extends GitRemoteTask {

    private String uri;
    private String destination;
    private String branch = Constants.HEAD;

    @Override
    public void execute() throws BuildException {
        log("Cloning repository " + uri);

        CloneCommand cloneCommand = Git.cloneRepository();
        try {
            if (isCredentialsValid()) {
                 cloneCommand.setCredentialsProvider(getDefaultCredentialsProvider());
            }
            cloneCommand.setURI(uri).setDirectory(new File(destination)).setBranch(branch);
            cloneCommand.call().getRepository().close();
        } catch (Exception e) {
            log("Could not clone repository: " + e, e, Project.MSG_ERR);
            throw new BuildException("Could not clone repository: " + e.getMessage(), e);
        }
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

}
