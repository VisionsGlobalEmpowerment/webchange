# Troubleshooting

<details>
  <summary>Leiningen error</summary>
  
  ```
  /usr/bin/lein: line 125: /usr/lib/jvm/java-8-openjdk-amd64/bin/java: No such file or directory
  ```
  
  ### Fix 
  
  Open to edit file `/usr/bin/lein` and update the next piece of code:
  
  ```
  # NOTE(Debian): Force Java 8 usage for performance and compatibility
  # See https://salsa.debian.org/clojure-team/leiningen-clojure/issues/1
  JAVA_CMD=${JAVA_CMD:-"/usr/lib/jvm/___YOUR_JAVA_VERSION__/bin/java"}
  ```
</details>

<details>
  <summary>Java 8 install error</summary>
  
  ```
  Package 'oracle-java8-installer' has no installation candidate
  ```
  
  ### Fix 
  
  Completely uninstall Java and then install Java 8 manually.
  
  #### Uninstall:
  
  1. Remove all the Java related packages (Sun, Oracle, OpenJDK, IcedTea plugins, GIJ):
    
        ```
        dpkg-query -W -f='${binary:Package}\n' | grep -E -e '^(ia32-)?(sun|oracle)-java' -e '^openjdk-' -e '^icedtea' -e '^(default|gcj)-j(re|dk)' -e '^gcj-(.*)-j(re|dk)' -e '^java-common' | xargs sudo apt-get -y remove
        sudo apt-get -y autoremove
        ```

  2. Purge config files (careful. This command removed libsgutils2-2 and virtualbox config files too):
  
        ```
        dpkg -l | grep ^rc | awk '{print($2)}' | xargs sudo apt-get -y purge
        ```
  3. Remove Java config and cache directory:
  
        ```
        sudo bash -c 'ls -d /home/*/.java' | xargs sudo rm -rf
        ```
  
  4. Remove manually installed JVMs:
  
        ```
        sudo rm -rf /usr/lib/jvm/*
        ```
  
  5. Remove Java entries, if there is still any, from the alternatives:
  
        ```
        for g in ControlPanel java java_vm javaws jcontrol jexec keytool mozilla-javaplugin.so orbd pack200 policytool rmid rmiregistry servertool tnameserv unpack200 appletviewer apt extcheck HtmlConverter idlj jar jarsigner javac javadoc javah javap jconsole jdb jhat jinfo jmap jps jrunscript jsadebugd jstack jstat jstatd native2ascii rmic schemagen serialver wsgen wsimport xjc xulrunner-1.9-javaplugin.so; do sudo update-alternatives --remove-all $g; done
        ```
  
  6. Search for possible remaining Java directories:
  
        ```
        sudo updatedb
        sudo locate -b '\pack200'
        ```
  
  If the command above produces any output like 
  `/path/to/jre1.6.0_34/bin/pack200` 
  remove the directory that is parent of bin, like this: 
  `sudo rm -rf /path/to/jre1.6.0_34`.
  
  #### Install:
  
  1. Download the latest JAVA 8 SE development kit from [here](https://www.oracle.com/technetwork/java/javase/downloads/java-archive-javase8-2177648.html)
  
  2. Create jdk folder:
  
        ```
        $ sudo mkdir /opt/jdk
        ```
     
  3. Untar Java in your new folder:
  
        ```
        $ sudo tar -zxf jdk-8u5-linux-x64.tar.gz -C /opt/jdk
        ```

  4. Set oracle JDK as the default JVM by running those two instructions (or something around that depending on your configuration):
  
        ```
        $ sudo update-alternatives --install /usr/bin/java java /opt/jdk/jdk1.8.0_<YourVersion>/bin/java 100
        $ sudo update-alternatives --install /usr/bin/javac javac /opt/jdk/jdk1.8.0_<YourVersion>/bin/javac 100
        ```
     
  ####Check the result:
  
```
java -version
```
  
</details>

<details>
  <summary>Idea doesn't resolve functions</summary>
  
  ### Fix 
  
  Open file `./project.clj` and let Idea index it.
</details>

<details>
  <summary>All request to api are failed</summary>
  
  Error in console:
  
  ```
  [{:type java.lang.Exception
     :message Exception in :get-first-school
     :at [conman.core$try_query$fn__27855$fn__27856 invoke core.clj 32]}
    {:type java.lang.IllegalArgumentException
     :message db-spec mount.core.DerefableState@87873c9 is missing a required parameter
     :at [clojure.java.jdbc$get_connection invokeStatic jdbc.clj 427]}]
  ```
  
  ### Fix 
  
  That happens when db state is not defined. 
  
  Make sure that ypu enter site from `:3000` port (back app port, not figwheel server).
</details>
