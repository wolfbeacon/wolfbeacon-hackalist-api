# WolfBeacon Hackalist API

An API Microservice built on top of [Hackalist.org](www.hackalist.org) data which lists all the Hackathons around you on the planet, sorted datewise and by radial distance on entering your location coordinates.

Written in [Kotlin](https://kotlinlang.org/), powered by [Spring-Boot](https://projects.spring.io/spring-boot/) and [H2 Database Engine](http://www.h2database.com/html/main.html). Configuration is written using the [Gradle Kotlin DSL Plugin](https://github.com/gradle/kotlin-dsl) which allows for Gradle configuration to be written in Kotlin itself. 

Currently in production use at WolfBeacon and publicly accessible. See usage docs at **https://api.wolfbeacon.com/docs/#api-Hackalist**

This API was previously written in Java and migrated to Kotlin as an experiment which worked out pretty well. See the [java branch](https://github.com/wolfbeacon/wolfbeacon-hackalist-api/tree/java) for the previous, stable code.

## Using the API

### Endpoint
`GET /v1/hackalist/hackathons/`
##### Updated every 12 hours from Hackalist with data present from 2017 onwards


#### Parameters Required:
* `start-date` (*yyyy-MM-dd*): Returns all the hackathons *after* this date. Eg: *start-date=2017-10-10*

* `end-date` (*yyyy-MM-dd*): Returns all the hackathons *before* this date. Eg: *end-date=2017-12-10*

* `sort-by`:
    1. `distance` (*sort-by*, *latitude*, *longitude*): Sorts all the Hackathons in ascending radial order from the coordinates entered. Eg: *sort-by=distance&latitude=19.1231&longitude=45.1231*
        OR
    2. `date` (*sort-by*): Sorts all the Hackathons in ascending order of start-date. Eg: *sort-by=date*

#### Example Query 1 (By date):

> curl -X GET "https://api.wolfbeacon.com/v1/hackalist/hackathons?start-date=2017-01-01&end-date=2017-01-30&sort-by=date"

#### Example Query 2 (By distance/coordinates): 

> curl -X GET "https://api.wolfbeacon.com/v1/hackalist/hackathons?start-date=2017-01-01&end-date=2017-01-30&sort-by=distance&latitude=40.7127837&longitude=-74.00594130000002"


#### Result:
```
{
  ...
    {
        "id": 201701201955420960,
        "title": "PennApps XV",
        "eventLink": "http://pennapps.com/",
        "startDate": "2017-01-20",
        "endDate": "2017-01-22",
        "lastUpdatedDate": "2017-12-01T03:26:31.244+0000",
        "year": 2017,
        "location": "Philadelphia, PA, United States",
        "host": "University of Pennsylvania",
        "length": 48,
        "size": "unknown",
        "travel": true,
        "prize": null,
        "highSchoolers": true,
        "cost": "free",
        "facebookLink": "https://www.facebook.com/pennapps/?fref=ts",
        "twitterLink": "https://twitter.com/pennapps",
        "googlePlusLink": "",
        "imageLink": "https://pbs.twimg.com/profile_images/930281893752442880/EFCW9AZJ_normal.jpg",
        "latitude": 39.9525839,
        "longitude": -75.1652215,
        "notes": ""
    },
  ...
}
```

#### Ping Endpoint
`/v1/hackalist/ping` returns *Ping Successful* with Response Code 200

## Local Setup

##### Dependencies: [Kotlin](https://kotlinlang.org/docs/tutorials/getting-started.html), [Gradle](https://gradle.org/install/)

* `git clone https://github.com/wolfbeacon/wolfbeacon-hackalist-api`
* Rename `applications-test.properties` to `application.properties` and complete configuration details - *GOOGLE_MAPS_API_KEY*, *START_YEAR*, *START_MONTH* and Database credentials.
* `./gradlew bootRun`


## Deploying with Docker
##### Note: Configure `application.properties` before deployment

* `sudo docker build -t wolfbeacon-hackalist-api .`
* `sudo docker run -p 8080:80 wolfbeacon-hackalist-api`

## AWS CodeBuild Env Variables

`HACKALIST_CONFIG_PATH` - src/resources/application.properties S3 path