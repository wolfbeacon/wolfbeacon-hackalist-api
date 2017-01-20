# WolfBeacon Server

The Java/Spring-Boot/MySQL Backend & API powering [WolfBeacon](http://wolfbeacon.com)

##Using the API

##### An [Auth0](https://auth0.com) JWT Authentication Token ID is required, with the Client ID set as `cMr82TS37R0DPqLSnC0gCWn2vV3qHK7A`. You can generate one at [api.wolfbeacon.com](api.wolfbeacon.com)

###Hackathon Endpoint
`/v1/hackathon/list`

*Updated every 6 hours from [Hackalist](www.hackalist.org)*

#####Parameters:
* `start-date` (*yyyy-MM-dd*): Returns all the hackathons *after* this date. Eg: *start-date=2015-10-10*

* `end-date` (*yyyy-MM-dd*): Returns all the hackathons *before* this date. Eg: *end-date=2015-12-10*

* `sort-by` (*Date, Distance*): Returns all the results sorted according to this parameter (All hackathons returned in *datewise ascending order by default)*

 * `distance` (*latitude, longitude*): *sort-by=distance&latitude=19.1231&longitude=45.1231`*

#####Example Query: 

> curl -X GET -H "Authorization: Bearer YOUR_TOKEN" -H "Cache-Control: no-cache" "http://api.wolfbeacon.com/v1/hackathon/list?start-date=2016-01-01&end-date=2016-01-30&sort-by=distance&latitude=40.7127837&longitude=-74.00594130000002"


#####Result:
```
{
  ...
    {
    "id": 20160916552226390,
    "title": "Hack the North",
    "eventLink": "https://hackthenorth.com/",
    "startDate": "2016-09-15",
    "endDate": "2016-09-17",
    "lastUpdatedDate": "2016-11-13T00:00:00.000+0000",
    "year": 2016,
    "location": "Waterloo, ON, Canada",
    "host": "Hack the North",
    "length": 36,
    "size": "1000",
    "travel": true,
    "prize": true,
    "highSchoolers": true,
    "cost": "free",
    "facebookLink": "https://www.facebook.com/hackthenorth",
    "twitterLink": "https://twitter.com/hackthenorth",
    "googlePlusLink": "",
    "imageLink": "https://scontent.xx.fbcdn.net/v/t1.0-1/p100x100/13501875_1423460301013883_426092165374437510_n.png?oh=23ff635c68eee2faa74b376adc7982fd&oe=58CE1F88",
    "latitude": 43.4642578,
    "longitude": -80.5204096,
    "notes": ""
    },
  ...
}
```


##Setup:

####Dependencies: [Java 8](https://www.java.com/en/download/), [MySQL](http://dev.mysql.com/doc/refman/5.7/en/installing.html), [Maven](https://maven.apache.org/download.cgi).

* `git clone https://github.com/wolfbeacon/wolfbeacon-server`
* Create/Get the `applications.properties and auth0.properties file` like from [here](https://auth0.com/docs/quickstart/backend/java-spring-security/00-intro) and place them in `src/main/resources`
* `mvn spring-boot:run` (If it shows you an error, run `App.java` directly from withing your IDE)


#####[WolfBeacon Server Credentials](https://www.dropbox.com/s/8hk1z0yg015dk9w/wolfbeacon-new-server-credentials.tar.gz?dl=0) (Only Available to privileged members. Contact Wolfbeacon admin for info)


## Build and Deploying with Docker

* Install Docker from from https://docs.docker.com/engine/installation/

* Include in Maven Plugin (already included)
```
<!-- Maven Docker Plugin-->
<plugin>
  <groupId>com.spotify</groupId>
  <artifactId>docker-maven-plugin</artifactId>
  <version>0.4.11</version>
  <configuration>
    <serverId>docker-hub</serverId>
    <imageName>${docker.image.prefix}/${project.artifactId}</imageName>
    <dockerDirectory>${basedir}</dockerDirectory>
    <resources>
      <resource>
        <targetPath>/</targetPath>
        <directory>${project.build.directory}</directory>
        <include>${project.build.finalName}.jar</include>
      </resource>
    </resources>
  </configuration>
</plugin>
```


* Packaging, Building and Pushing Docker Image with Maven:
    `sudo mvn package docker:build  (optional: -DpushImage)`

* LOCAL: Running Docker Image with local host MySQL Instance
    `sudo docker run -p 8080:8080 -t --net=host wolfbeacon1/wolfbeacon-server`

* PROD: Running Docker Image with prod MySQL instance(dont?)
    `sudo docker run -p 8080:8080 wolfbeacon1/wolfbeacon-server`

* Show Docker process
    `sudo docker ps`

* Kill Docker process
    `docker stop <id>`

* To push to a private Docker image registry that requires authentication, you can put your credentials as follows in your Maven's global `settings.xml` (`/usr/share/maven/conf/settings.xml`)file as part of the <servers></servers> block
```
<server>
  <id>docker-hub</id>
  <username>foo</username>
  <password>secret-password</password>
  <configuration>
    <email>foo@foo.bar</email>
  </configuration>
</server>
```

## [Cycle](https://cycle.io) Settings

* Create Image -> Go to `Environment` (create one if not present) -> New Container -> set `JAVA_OPTS` as `-Xms512m -Xmx1024m` under CONFIG in Environment Image -> Start
