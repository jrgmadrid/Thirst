/*
  __  __              
 |  \/  |__ _ _ __ ___
 | |\/| / _` | '_ (_-<
 |_|  |_\__,_| .__/__/
             |_|      

The javascript map handler for Thirst

////////////////////////////////////////

   ___ _     _          _    
  / __| |___| |__  __ _| |___
 | (_ | / _ \ '_ \/ _` | (_-<
  \___|_\___/_.__/\__,_|_/__/
                             
*/

var fountains;
var favorites;
var map;
var markers;
google.maps.event.addDomListener(window, 'load', mapInitialize);

/*

  ___             _   _             
 | __|  _ _ _  __| |_(_)___ _ _  ___
 | _| || | ' \/ _|  _| / _ \ ' \(_-<
 |_| \_,_|_||_\__|\__|_\___/_||_/__/
                                    
Function library
TOC:
1. Draw
	- 1.1 drawMarkers
2. Get
	- 2.1 getFavorites
	- 2.2 getFountains
3. Find
	- 3.1 findBounds
	- 3.2 findCenter
4. Map
	- 4.1 mapInitialize
	- 4.2 mapSetup
*/                                    

/*
drawFavorites - draw favorite fountains
*/
function drawFavorites(){
	mapClear();
	markers = mapMarkers(favorites);
	drawMarkers();
}
/*
drawFountains - draw the whole set of fountains
*/
function drawFountains(){
	mapClear();
	markers = mapMarkers(fountains);
	drawMarkers();
}

/*
drawMarkers - draw the set of markers on the map
*/
function drawMarkers(){
	for(var i=0; i<markers.length;i++){
		markers[i].setMap(map);
	}
}

/*
getFavorites - retrieve favorites data

Returns - an array of maps.LatLng objects
*/
function getFavorites(){
	var fav = document.querySelector('#favorites').getAttribute('value').split(';');
	favorites = [];
	for(var i=0; i<fav.length;i++){
		var x = parseFloat(fav[i].split(',')[0]);
		var y = parseFloat(fav[i].split(',')[1]);

		var coords = new google.maps.LatLng(x,y);
		favorites.push(coords);
	}
	return favorites;
}

/*
getFountains - retrieve fountain data

Returns - an array of maps.LatLng objects
*/
function getFountains(){
	var fnt = document.querySelector('#fountains').getAttribute('value').split(';');
	fountains = [];
	for(var i=0; i<fnt.length;i++){
		var x = parseFloat(fnt[i].split(',')[0]);
		var y = parseFloat(fnt[i].split(',')[1]);

		var coords = new google.maps.LatLng(x,y);
		fountains.push(coords);
	}
	return fountains;
}


/*
findBounds - find the bounds to set the zoom level for tha map

Params:
	p: An array of points
Returns:
	A maps.LatLngBounds object
*/
function findBounds(p){

}
/*
findCenter - find the center point of a set of points

Params:
	p: An array of maps.LatLng points
Returns:
	A maps.LatLng object
*/
function findCenter(p){
	var highLat = p[0].lat();
	var highLong =p[1].lng();
	var lowLat = p[0].lat();
	var lowLong = p[1].lng();
	for(var i=0; i<p.length; i++){
		var x = p[i].lng();
		var y = p[i].lat();
		if(y > highLat){
			highLat = y;
		}
		if(y < lowLat){
			lowLat = y;
		}
		if(x > highLong){
			highLong = x;
		}
		if(x < lowLong){
			lowLong = x;
		}
	}
	var centerLat = (highLat + lowLat) / 2;
	var centerLong = (highLong + lowLong) / 2;
	var center = new google.maps.LatLng(centerLat, centerLong);
	return center;
}

/*
mapClear - clear markers from the map
*/
function mapClear(){
	for(var i=0;i<markers.length;i++){
		markers[i].setMap(null);
	}
}

/*
mapInitialize - initialize the map
*/
function mapInitialize(){
	fountains = getFountains();
	favorites = getFavorites();
	var options = mapSetup(findCenter(fountains),10);
	map = new google.maps.Map(document.querySelector('#map'),options);
	markers = mapMarkers(fountains);
	drawFountains();

	/*var centerMark = new google.maps.Marker({
		position: findCenter(points),
		map: map,
		title: "Center"});*/
}

/*
mapMarkers - create an array of markers
Params:
	l - an array of LatLng objects
Returns
	An array of markers
*/
function mapMarkers(l){
	var markers = [];
	for(var i=0; i<l.length; i++){
		var m = new google.maps.Marker({
			position: l[i],
		});
		markers.push(m);
	}
	return markers;
}
/*
mapSetup - setup mapOptions

Params:
	c - center value to use
	z - zoom level
*/
function mapSetup(c, z){
	var mapOptions = {
		center: c,
		zoom: z
	};
	return mapOptions;
}

