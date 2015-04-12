<?php

/*
 * Following code will create or update rows
 * All product details are read from HTTP Post Request
 */

// array for JSON response
$response = array();

// check for required fields in POST data
if (isset($_POST['deviceId']) && isset($_POST['buildingId']) && isset($_POST['levelId']) && isset($_POST['floorplanId']) && isset($_POST['latitude']) && isset($_POST['longitude']) && isset($_POST['x']) && isset($_POST['y'])	&& isset($_POST['i']) && isset($_POST['j']) && isset($_POST['heading']) && isset($_POST['probability']) && isset($_POST['roundtrip']) && isset($_POST['time'])) {

	// extract data from POST into variables
	$deviceId = $_POST['deviceId'];
	$buildingId = $_POST['buildingId'];
	$levelId = $_POST['levelId'];
	$floorplanId = $_POST['floorplanId'];
	$latitude = $_POST['latitude'];
	$longitude = $_POST['longitude'];
    $x = $_POST['x'];
    $y = $_POST['y'];
    $i = $_POST['i'];
    $j = $_POST['j'];
    $heading = $_POST['heading'];
    $probability = $_POST['probability'];
	$roundtrip = $_POST['roundtrip'];
    $time = $_POST['time'];

	// include database connect class
	require_once __DIR__ . '/db_connect.php';

 	// connect to the database
 	$db = new DB_CONNECT();

	// check whether primary key already exists
	$result = mysql_query("SELECT * FROM devicelocations WHERE deviceId = '$deviceId';");
	$arr = mysql_fetch_array($result);

	// if the select statement returns the empty set, sizeof will return 1
	// if the select statement returns a result, sizeof will return 28 (there are 14 columns in each row)

	// so if the deviceID does not already exist in the database, do an INSERT
	if (sizeof($arr) == 1) {
		error_log("Database is empty, doing an INSERT.", 0);
		$result = mysql_query("INSERT INTO devicelocations (deviceId, buildingId, levelId, floorplanId, latitude, longitude, x, y, i, j, heading, probability, roundtrip, time) VALUES ('$deviceId', '$buildingId', '$levelId', '$floorplanId', '$latitude', '$longitude', '$x', '$y', '$i', '$j', '$heading', '$probability', '$roundtrip', '$time');");
	}

	// if the deviceID does already exist in the database, do an UPDATE
	else {
		error_log("Database already has that deviceId, doing an UPDATE.", 0);
		$result = mysql_query("UPDATE devicelocations SET buildingId = '$buildingId', levelId = '$levelId', floorplanId = '$floorplanId', latitude = '$latitude', longitude = '$longitude', x = '$x', y = '$y', i = '$i', j = '$j', heading = '$heading', probability = '$probability', roundtrip = '$roundtrip', time = '$time' WHERE deviceId = '$deviceId';");
	}

    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Entry successfully inserted or updated.";

        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Entry was not successfully inserted or updated.";

        // echoing JSON response
        echo json_encode($response);
    }

} else {
    // required POST field is missing
    $response["success"] = 0;
    $response["message"] = "Required POST field(s) missing";

    // echoing JSON response
    echo json_encode($response);
}
?>
