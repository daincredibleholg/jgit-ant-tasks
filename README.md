JGit Ant Tasks (advanced)
==============
This project is based on the great work of Sergey Bogutskiy. It can be found
here: https://github.com/SergeyBoguckiy/jgit-ant-tasks.

The main advantage of this implementation is to provide (SSH) authentication
to the Git ant tasks. Sergey added username/password authentication. I introduced
keyfile based authentication - with or without passphrase.


Usage
=====
Add the JAR file to your ant classpath [Ant Setup Documentation](http://ant.apache.org/manual/install.html#classpath "Classpath part of Ant setup documentation")
 and include the task defintion in your build.xml like this:

 ```
 	<taskdef resource="se/steinhauer/tools/ant/git/git-ant-tasks.properties">
 		<classpath refid="ant.git.advanced" />
 	</taskdef>
 ```

Afterwards you have the following ant tasks available:
[ TODO ]

Maven
=======
Another thing I did was making the project a Maven project. Beside the "nicer" dependency management you can use Maven
or [Ivy](http://ant.apache.org/ivy "Ivy Project page")  to get this and all needed dependencies for you.

How it works
----------
[ TODO ]