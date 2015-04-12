# IndoorAtlas_SQL_uploader

A combination of an Android app, some PHP files & an SQL schema to enable an Android device to post IndoorAtlas position data to a SQL database on a remote server for later use by anything you can imagine. I use this as part of my [Mirrorshades project](https://github.com/CJ-Davies/Mirrorshades), to track the position of a user as they walk around a building & move a player within Unity to the equivalent vantage (see [this Unity script in particular](https://github.com/CJ-Davies/Mirrorshades/blob/master/Assets/Scripts/NewPointScript.cs) ).

IndoorAtlas_SQL_uploader
- an Eclipse project that is a modification of the IndoorAtlas example Eclipse project with the simple addition of posting the data to a database in addition to echoing it to the device's screen
- input IndoorAtlas API key/secret key, building ID/level ID/floor plan ID into IndoorAtlasExample.java

db_config.php
- enter username/password/database name/database server address & port here

updateposition.php
- the file that the Android app POSTs the data to

IndoorAtlas_db_schema.sql
- a mysqldump of an empty database, to give an idea of a schema
