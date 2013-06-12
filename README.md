<img src="http://weso.es/img/logo_acota_850.png">
# ACOTA:  Automatic Collaborative Tagging 
Master: [![Build Status](https://travis-ci.org/weso/acota-feedback.png?branch=master)](https://travis-ci.org/weso/acota-feedback)
Develop: [![Build Status](https://travis-ci.org/weso/acota-feedback.png?branch=develop)](https://travis-ci.org/weso/acota-feedback)


## What is it? ##
Acota-Feedback adds an Enhancer powered by machine-learning techniques

Build upon:
- Apache Mahout

Supports:
 - MySQL
 - MariaDB
 - PostgresSQL
 - MongoDB


## Configuration example ##
Acota allows two different ways to configure it, programmatically and through a properties File:
 - [How to Configure Acota-Feedback] (https://github.com/weso/acota-feedback/wiki/Configure-Acota)
 - [Acota-Feedback (Properties-List)] (https://github.comweso/acota-feedback/wiki/Acota-Feedback-(Properties-List\))

## How to use it? ##
Detailed information of how to run acota-feedback: 
 - [Acota-Feedback] (https://github.com/weso/acota-feedback/wiki/Generate-Some-Keywords)

## Download ##
The current version of acota is **0.3.8-SNAPSHOT**, you can download it from:

### For Maven Users
Acota-Core is available in Maven Central:
 ```
  <dependency>
    <groupId>es.weso</groupId>
    <artifactId>acota-feedback</artifactId>
    <version>0.3.8-SNAPSHOT</version>
 </dependency>
 ```
 
Looking for other SNAPSHOTS?
* [SNAPSHOTS](https://oss.sonatype.org/content/repositories/snapshots/es/weso/acota-feedback/0.3.8-SNAPSHOT/ "Acota-feedback SNAPSHOTS Repository")

## Disclaimer
Acota-feedback requires an SQL Database, you can download the SQL Creation Script from:
 * [ACOTA's Database MySQL Dump](http://156.35.82.101:7000/downloads/acota/utils/mysql/acota.sql "ACOTA's Database SQL Dump")
 * [ACOTA's Database PostgreSQL Dump](http://156.35.82.101:7000/downloads/acota/utils/postgres/acota.sql "ACOTA's Database SQL Dump")


## License

```
  Copyright 2012-2013 WESO Research Group

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
