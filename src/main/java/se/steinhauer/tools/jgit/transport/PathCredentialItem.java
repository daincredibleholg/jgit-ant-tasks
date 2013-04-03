package se.steinhauer.tools.jgit.transport;

import org.eclipse.jgit.transport.CredentialItem;

/**
 * Created with IntelliJ IDEA.
 * User: hsteinhauer
 * Date: 03.04.13
 * Time: 17:54
 * To change this template use File | Settings | File Templates.
 */
public class PathCredentialItem extends CredentialItem {

    private String value = "";

    public PathCredentialItem(String promptText) {
        this(promptText, false);
    }

    public PathCredentialItem (String promptText, boolean maskValue) {
        super(promptText, maskValue);
    }

    @Override
    public void clear() {
        this.value = "";
    }

    public String getValue() {
        return value;
    }

    public void setValue(String path) {
        this.value = path;
    }
}
