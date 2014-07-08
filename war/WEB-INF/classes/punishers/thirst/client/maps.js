/*
  __  __              
 |  \/  |__ _ _ __ ___
 | |\/| / _` | '_ (_-<
 |_|  |_\__,_| .__/__/
             |_|      

The javascript map handler for Thirst
*/
/*
getPoints - retrieve points from an element

returns - an array of points
*/
function getPoints(){
	var points = document.querySelector('#points').getAttribute('value').split(';');
	return points;
}
/*
findCenter - find the center point of a set of points

Params:
	p: An array of points
Returns:
	A maps.LatLng object
*/
function findCenter(p){
	var highLat = 0;
	var highLong = 0;
	var lowLat = 180;
	var lowLong = 180;

	for(point in p){
		var coords = point.split(',');
		if(coords[0] > highLat)
			highLat = coords[0];
		if(coords[0] < lowLat)
			lowLat = coords[0];
		if(coords[1] > highLong)
			highLong = coords[1];
		if(coords[1] < lowLong)
			lowLong = coords[1];
	}
	var centerLat = (highLat+lowLat)/2;
	var centerLong = (highLong + lowLong)/2;
	var center = new google.maps.LatLng(centerLat, centerLong);
	return center;
}

/*
mapSetup - setup mapOptions

Params:
	c - center value to use
	z - zoom level
*/
function mapSetup(){
	var mapOptions = {
		center: new google.maps.LatLng(-34.397, 150.644),
		zoom: 10
	};
	return mapOptions;
}
/*
mapInitialize - initialize the map
*/
function mapInitialize(){
	var options = mapSetup();
	var map = new google.maps.Map(document.querySelector('#map'),options);
	return map;
}

google.maps.event.addDomListener(window, 'load', mapInitialize);