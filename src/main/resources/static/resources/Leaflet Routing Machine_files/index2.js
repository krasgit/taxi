var map = L.map('map', { scrollWheelZoom: false }),
	waypoints = [
		L.latLng(43.516689, 27.817383),
		L.latLng(43.165123, 27.519379)
	
	];

L.tileLayer(LRM.tileLayerUrl, {
	attribution: 'Maps and routes from <a href="https://www.openstreetmap.org">OpenStreetMap</a>. ' +
		'data uses <a href="http://opendatacommons.org/licenses/odbl/">ODbL</a> license'
}).addTo(map);

var control = L.Routing.control({
		router: L.routing.osrmv1({
			serviceUrl: LRM.osmServiceUrl
		}),
		plan: L.Routing.plan(waypoints, {
			createMarker: function(i, wp) {
				return L.marker(wp.latLng, {
					draggable: true,
					icon: L.icon.glyph({ glyph: String.fromCharCode(65 + i) })
				});
			},
			geocoder: L.Control.Geocoder.nominatim(),
			routeWhileDragging: true
		}),
		routeWhileDragging: true,
		routeDragTimeout: 250,
		showAlternatives: true,
		altLineOptions: {
			styles: [
				{color: 'black', opacity: 0.15, weight: 9},
				{color: 'white', opacity: 0.8, weight: 6},
				{color: 'blue', opacity: 0.5, weight: 2}
			]
		}
	})
	.addTo(map)
	.on('routingerror', function(e) {
		try {
			map.getCenter();
		} catch (e) {
			map.fitBounds(L.latLngBounds(waypoints));
		}

		handleError(e);
	});








const polygon = L.polygon([[51.512642, -0.099993],    [51.520387, -0.087633],      [51.509116, -0.082483]], { fillOpacity: 0.5, fillColor: '#9a8fcd' });
polygon.addTo(map);



L.Routing.errorControl(control).addTo(map);





//const marker = L.marker([51.5, -0.09]).addTo(map).bindPopup('<b>Hello world!</b><br />I am a popup.').openPopup();
/*
	const circle = L.circle([51.508, -0.11], {
		color: 'red',
		fillColor: '#f03',
		fillOpacity: 0.5,
		radius: 500
	}).addTo(map).bindPopup('I am a circle.');
*/
/*
	const polygon = L.polygon([
		[51.509, -0.08],
		[51.503, -0.06],
		[51.51, -0.047]
	]).addTo(map).bindPopup('I am a polygon.');
*/



	const popup = L.popup()
		.setLatLng([43.516689, 27.817383])
		.setContent('I am a standalone popup.')
		.openOn(map);


	function onMapClick(e) {
	var waypointsArray = control.getWaypoints();
	
		popup
			.setLatLng(e.latlng)
			.setContent(`You clicked the map at ${e.latlng.toString()}`)
			.openOn(map);
	}

	map.on('click', onMapClick);



