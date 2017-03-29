# Yet Another Todo List

## Live demo site

[http://yatl.actframework.org](http://yatl.actframework.org)

## Background

The project is created to 

1. finish the [coding challenge](https://www.oschina.net/question/253880_2236467) I've raised to [@eechen](https://my.oschina.net/eechen).
2. as companion project for my [blog](https://my.oschina.net/greenlaw110/blog/869927) on applying [act-aaa plugin](https://github.com/actframework/act-aaa-plugin) in an [ActFramework](http://actframework.org) application which demonstrates on the following feature:

    * Login/SignUp user
    * Send welcome email and handle activation link
    * Authentication/Authorisation/Auditing with [Act-AAA](https://github.com/actframework/act-aaa-plugin)
    * The expressiveness of ActFramework. It takes only 261 lines of Java code to implement this project.

## Known issues

See https://git.oschina.net/greenlaw110/yatl/issues

## Requirement

1. It shall allow user to login/sign up
2. It shall send welcome letter to user once user signed up
3. It shall handle the request when user clicked the activation link in the welcome email. 
4. It shall redirect user to login form if user hasn't logged in yet
5. Once user logged in it shall display a list of TODO items associated with the user
6. Each todo item has a subject to be displayed
7. It shall allow logged in user to create new TODO item
8. It shall allow logged in user to update his TODO item only
 
### non-functional requirements

* No requirement on styling. Just keep it simple. 
* No requirement on database. Choose whatever you want. But data must be persistent
* No need to CSS just use standard HTML element is enough. List shall use `<li>` element. The body shall use `<textarea>` element
* Project must provide the build script, deploy script and README file.
 
## Platform and Database

You must have MongoDB, JDK7+ and the corresponding JCE installed in order to play with this project. You must install maven to build the application
 
## Build/Run/Deploy

To build and run the app locally

```bash
mvn clean compile exec:exec
```

This will start the app in actframework default port: 5460 

To build package for deployment

```bash
mvn clean packatge
```
 
This will generate a distribution package in `target/dist` dir. You can scp the file to your cloud server.

To run the distribution package, unzip it and type `./run`

## Note about Mail configuration

This app require SMTP configurations in order to send out welcome email. Once you are about to run the application, please add the following items into your `conf/common/mail.properties`:

```
mailer.smtp.username=${smtpUsername}
mailer.smtp.password=${smtpPassword}
mailer.smtp.host=${smtpHost}
mailer.smtp.port=${smtpPort}
```


## LOC statistics

```
luog@luog-Satellite-P50-A:~/p/greenlaw110/yatl$ loc src
--------------------------------------------------------------------------------
 Language             Files        Lines        Blank      Comment         Code
--------------------------------------------------------------------------------
 Java                    10          360           78           20          262
 HTML                     7          161           20            0          141
 XML                      2          133           21           20           92
 JavaScript               2           84            7            2           75
 YAML                     1           15            3            0           12
 Batch                    1            7            0            0            7
--------------------------------------------------------------------------------
 Total                   23          760          129           42          589
--------------------------------------------------------------------------------
```

## Benchmark

**Note** 

1. The environment is my local laptop: i7-4700MQ / 16GB RAM / 256 GB SSD / Linuxmint 18.1
1. All benchmarks are done using [wrk](https://github.com/wg/wrk) with 4 threads and 256 concurrent connections for 5s. For each benchmark I run a couple of times to warm up the JVM and pick up the best one. 
2. There are only one user signed up and one item in the user's todo list. This can filter out the impact of the database to a certain extend

| Item | Throughput | Latency |
| ---- | ---------- | ------- |
| Display Login Form (no cookie/authentication) | 81,736.26 | 03.35ms |
| Display Home Page (with cookie/authentication) | 14,284.19 | 21.31ms |
| Retrieve login user's TODO list | 11,128.40 | 25.69ms |

### B1. display login form (no session/authentication required)

```
luog@luog-Satellite-P50-A:~/p/greenlaw110/microservices-framework-benchmark$ wrk -t4 -c256 -d5s http://localhost:7003/login
Running 5s test @ http://localhost:7003/login
  4 threads and 256 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     3.35ms    6.69ms 208.21ms   97.83%
    Req/Sec    20.61k     3.03k   33.39k    78.50%
  412274 requests in 5.04s, 199.73MB read
Requests/sec:  81736.26
Transfer/sec:     39.60MB
```

### B2. display home page (with session/authentication)

```
luog@luog-Satellite-P50-A:~/p/greenlaw110/microservices-framework-benchmark$ wrk -t4 -c 256 -d5s -H 'Cookie: yat-session=fdc8c67b58312561b0fc5047bd286c6741e53117-___TS%011490769539386%00___ID%013c462618-466b-4f2f-b001-89407de440dc%00username%01green%40thinking.group' http://localhost:7003
Running 5s test @ http://localhost:7003
  4 threads and 256 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    21.31ms   20.57ms 290.55ms   88.52%
    Req/Sec     3.61k   295.87     4.64k    72.00%
  72064 requests in 5.05s, 127.28MB read
Requests/sec:  14284.19
Transfer/sec:     25.23MB
```

### B3. retrieve login user's TOOD list (only one item in the list w/ session and authentication)

```
luog@luog-Satellite-P50-A:~/p/greenlaw110/microservices-framework-benchmark$ wrk -t4 -c 256 -d5s -H 'Cookie: yat-session=fdc8c67b58312561b0fc5047bd286c6741e53117-___TS%011490769539386%00___ID%013c462618-466b-4f2f-b001-89407de440dc%00username%01green%40thinking.group' -H 'Accept: application/json' http://localhost:7003/todo
Running 5s test @ http://localhost:7003/todo
  4 threads and 256 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    25.69ms   20.99ms 237.27ms   83.32%
    Req/Sec     2.83k   316.90     6.51k    91.00%
  56504 requests in 5.08s, 24.63MB read
Requests/sec:  11128.40
Transfer/sec:      4.85MB
```


Should you have any questions, please comment below or submit an issue.