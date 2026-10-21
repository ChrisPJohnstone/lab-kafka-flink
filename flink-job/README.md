Following https://nightlies.apache.org/flink/flink-docs-release-2.3/docs/getting-started/table_api/#what-youll-build

- Download skeleton package
  ```sh
  mvn archetype:generate \
      -DarchetypeGroupId=org.apache.flink \
      -DarchetypeArtifactId=flink-walkthrough-table-java \
      -DarchetypeVersion=2.3.0 \
      -DgroupId=spendreport \
      -DartifactId=spendreport \
      -Dversion=0.1 \
      -Dpackage=spendreport \
      -DinteractiveMode=false
  ```
- Create jar
  ```sh
  mvn package
  ```
- Upload job
  ```sh
  podman cp flink-job/spendreport/target/spendreport-0.1.jar flink-jobmanager:/tmp/spendreport.jar
  ```
- Execute job
  ```sh
  podman exec -it flink-jobmanager flink run -c spendreport.SpendReport /tmp/spendreport.jar
  ```
