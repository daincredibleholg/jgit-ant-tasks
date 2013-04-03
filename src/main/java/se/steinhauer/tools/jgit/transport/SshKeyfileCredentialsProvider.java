package se.steinhauer.tools.jgit.transport;

import org.eclipse.jgit.errors.UnsupportedCredentialItem;
import org.eclipse.jgit.transport.CredentialItem;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.URIish;

/**
 * Created with IntelliJ IDEA.
 * User: hsteinhauer
 * Date: 03.04.13
 * Time: 17:43
 * To change this template use File | Settings | File Templates.
 */
public class SshKeyfileCredentialsProvider extends CredentialsProvider {

    private String username;
    private String keyfilePath;
    private char[] passphrase;


    @Override
    public boolean isInteractive() {
        return false;
    }

    @Override
    public boolean supports(CredentialItem... items) {
        boolean supported = true;

        if (items != null && items.length > 1 && items.length < 4) {
            for (CredentialItem i : items) {
                if (i instanceof CredentialItem.Username)
                    continue;

                else if (i instanceof CredentialItem.Password)
                    continue;

                else if (i instanceof CredentialItem.StringType) {
                    continue;
                }
                else
                    supported = false;
            }
        } else {
            supported = false;
        }

        return supported;
    }

    @Override
    public boolean get(URIish uri, CredentialItem... items) throws UnsupportedCredentialItem {
        for (CredentialItem i : items) {
            if (i instanceof CredentialItem.Username) {
                ((CredentialItem.Username) i).setValue(username);
                continue;
            }
            if (i instanceof CredentialItem.Password) {
                ((CredentialItem.Password) i).setValue(passphrase);
                continue;
            }
            if (i instanceof PathCredentialItem) {
                ((PathCredentialItem) i).setValue(keyfilePath);
                continue;
            }
            throw new UnsupportedCredentialItem(uri, i.getClass().getName()
                    + ":" + i.getPromptText());
        }
        return true;
    }
}
