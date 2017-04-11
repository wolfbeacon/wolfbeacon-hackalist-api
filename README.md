# WolfBeacon Hackalist API

An API built around [Hackalist](www.hackalist.org) data that lists all the Hackathons around you on the planet.

## Using the API

### Endpoint
`/hackalist-hackathons`

*Updated every 6 hours from [Hackalist](www.hackalist.org)*

##### Parameters Required:
* `start-date` (*yyyy-MM-dd*): Returns all the hackathons *after* this date. Eg: *start-date=2015-10-10*

* `end-date` (*yyyy-MM-dd*): Returns all the hackathons *before* this date. Eg: *end-date=2015-12-10*

* `sort-by`:
    1. `distance` (*sort-by*, *latitude*, longitude*): Sorts all the Hackathons in ascending radial order from the coordinates entered. Eg: *sort-by=distance&latitude=19.1231&longitude=45.1231*
        OR
    2. `date` (*sort-by*): Sorts all the Hackathons in ascending order of start-date. Eg: *sort-by=date*

##### Example Query: 

> curl -X GET "http://api.wolfbeacon.com/hackalist-hackathons?start-date=2016-01-01&end-date=2016-01-30&sort-by=distance&latitude=40.7127837&longitude=-74.00594130000002"


##### Result:
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


## Local Setup:

#### Dependencies: [Java 8](https://www.java.com/en/download/), [MySQL](http://dev.mysql.com/doc/refman/5.7/en/installing.html), [Maven](https://maven.apache.org/download.cgi).

* `git clone https://github.com/wolfbeacon/wolfbeacon-server`
* Rename `applications-test.properties` to `application.properties` and enter the configuration details as given
* `mvn dependency:resolve && mvn verify && mvn package`
* `cd target`
* `java -jar wolfbeacon-hackalist-api-1.0.jar`


## Running/Deploying with [Docker](https://docs.docker.com/engine/installation/)
* `sudo docker service start`
* `sudo docker build -t wolfbeacon-hackalist-api .`
* `sudo docker run -p 8080:80 wolfbeacon-hackalist-api`

## [Cycle](https://cycle.io) Settings

* Create Image -> Go to `Environment` (create one if not present) -> New Container -> set `JAVA_OPTS` as `-Xms1024m -Xmx1024m` under CONFIG in Environment Image -> Start
