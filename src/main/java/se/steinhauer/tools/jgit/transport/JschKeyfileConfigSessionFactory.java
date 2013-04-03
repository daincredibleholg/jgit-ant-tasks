package se.steinhauer.tools.jgit.transport;

import com.jcraft.jsch.Session;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;

/**
 * Created with IntelliJ IDEA.
 * User: hsteinhauer
 * Date: 03.04.13
 * Time: 19:06
 * To change this template use File | Settings | File Templates.
 */
public class JschKeyfileConfigSessionFactory extends JschConfigSessionFactory {
    @Override
    protected void configure(OpenSshConfig.Host hc, Session session) {
        session.setConfig("StrictHostKeyChecking", "yes");
    }
}
