# Install jar to local repository

Assuming your project looks along these lines like this:
```
project-root
- src
- target
- pom.xml
```
Add another directory called “lib”
```
project-root
- lib
- src
- target
- pom.xml
```

Then use the maven deploy plugin’s deploy-file goal (execute on one line):
```
mvn deploy:deploy-file -Durl=file://{path_to_local_repo} -Dfile={local_jar} -DgroupId={group_id} -DartifactId={artifact_id} -Dpackaging=jar -Dversion={version}
```

Update our project pom.xml with a new repository entry:
```xml
<repositories>
  <repository>
    <id>mylib_local</id>
    <name>mylib</name>
    <url>file:${project.basedir}/lib</url>
  </repository>
</repositories>
```

And the dependency:
```xml
<dependency>  
    <groupId>{group_id}</groupId>
    <artifactId>{artifact_id}</artifactId>
    <version>{version}</version>
</dependency>
```
