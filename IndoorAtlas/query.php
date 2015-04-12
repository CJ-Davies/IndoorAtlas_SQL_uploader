<?php
// include database connect class
require_once __DIR__ . '/db_connect.php';

// connect to the database
$db = new DB_CONNECT();

// get whole table
$result = mysql_query("SELECT * FROM devicelocations");

$arr = mysql_fetch_array($result);

print_r($arr);
?>
