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

package se.steinhauer.tools.jgit.transport;

import org.eclipse.jgit.errors.UnsupportedCredentialItem;
import org.eclipse.jgit.transport.CredentialItem;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.URIish;

/**
 * This is an non-interactive Credentials Provider for SSH authentication
 * via keyfile. It expects an username and, optionally, a passphrase
 * for a keyfile.
 *
 * @author: Holger Steinhauer
 * @since: 2013-04-03
 */
public class SshKeyfileCredentialsProvider extends CredentialsProvider {

    private String username;
    private String passphrase = null;

    public SshKeyfileCredentialsProvider(String username, String keyfilePath) {
        this.username = username;
        this.passphrase = passphrase;
    }

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
                else if (i instanceof CredentialItem.StringType)
                    continue;
                else {
                    supported = false;
                    break;
                }
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
            } else if (i instanceof CredentialItem.StringType) {
                CredentialItem.StringType currentItem = (CredentialItem.StringType) i;
                if (currentItem.getPromptText().startsWith("Passphrase")) {
                    ((CredentialItem.StringType) i).setValue(passphrase);
                    continue;
                }

            }

            throw new UnsupportedCredentialItem(uri, i.getClass().getName()
                        + ":" + i.getPromptText());
        }
        return true;
    }
}
