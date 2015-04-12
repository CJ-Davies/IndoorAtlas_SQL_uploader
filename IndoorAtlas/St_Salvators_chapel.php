<?php

echo "<head>";
echo "<meta http-equiv='refresh' content='2.5'>";
echo "</head>";

// include database connect class
require_once __DIR__ . '/db_connect.php';

// connect to the database
$db = new DB_CONNECT();

// get whole table
// My Nexus 5
$result = mysql_query("SELECT * FROM devicelocations WHERE deviceId = '4192fe2d3b7fbf27'");

// Group's Nexus 4
//$result = mysql_query("SELECT * FROM devicelocations WHERE deviceId = 'c76129348d8acd2e'");

$arr = mysql_fetch_array($result);

$deviceId = ($arr[0]);
$buildingId = ($arr[1]);
$levelId = ($arr[2]);
$floorplanId = ($arr[3]);
$latitude = ($arr[4]);
$longitude = ($arr[5]);
$x = ($arr[6]);
$y = ($arr[7]);
$i = ($arr[8]);
$j = ($arr[9]);
$heading = ($arr[10]);
$probability = ($arr[11]);
$roundtrip = ($arr[12]);
$time = ($arr[13]);

// display the map image scaled down by 10
// display the marker at the position from the database, divded by 10
// both systems start index from top left corner?

echo "<div style='position: relative; left: 0; top: 0;'>";
echo "<img src='St_Salvators_chapel.gif' style='position: relative; top: 0; left: 0;'/>";
echo "<img src='splodge.png' width = 1% style='position: absolute; top: ";
echo ($j)/3;
echo "; left: ";
echo ($i)/3;
echo ";'/>";
echo "</div>";

echo "<br/>";

echo "deviceId: ";
echo $deviceId;
echo "<br/>";
echo "buildingId: ";
echo $buildingId;
echo "<br/>";
echo "levelId: ";
echo $levelId;
echo "<br/>";
echo "floorplanId: ";
echo $floorplanId;
echo "<br/>";
echo "latitude: ";
echo $latitude;
echo "<br/>";
echo "longitude: ";
echo $longitude;
echo "<br/>";
echo "x: ";
echo $x;
echo "<br/>";
echo "y: ";
echo $y;
echo "<br/>";
echo "i: ";
echo $i;
echo "<br/>";
echo "j: ";
echo $j;
echo "<br/>";
echo "heading: ";
echo $heading;
echo "<br/>";
echo "probability: ";
echo $probability;
echo "<br/>";
echo "roundtrip: ";
echo $roundtrip;
echo "<br/>";
echo "time: ";
echo $time;

?>
