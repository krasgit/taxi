class Route{
	
	// routeStyle0 = new ol.style.Style({stroke: new ol.style.Stroke({width: 3, color: [40, 40, 40, 0.8], }), });
	
	// routeStyle1 = new ol.style.Style({stroke: new ol.style.Stroke({width: 3, color: [30, 30, 30, 0.8], }), });
	static getPath(ca) {
					let txt = "";
					let length = ca.length;

					for (let i = 0; i < length; i++) {
						
						var cc=ca[i];
						
						var val = cc[0]+','+cc[1];
						txt += val;
						if (i < (length - 1))
							txt += ";";
					}
					return txt;
				}
	
	static	getStule(routeFeatureId) {
		
		if(routeFeatureId=='routeFeature')
		   	 return new ol.style.Style({stroke: new ol.style.Stroke({width: 3, color: [255, 40, 40, 0.8], }), });
		   
		   return new ol.style.Style({stroke: new ol.style.Stroke({width: 3, color: [30, 255, 30, 0.8], }), });
		}			
				
	static	createRoute(path ,routeFeatureId) {
		
		var stule=Route.getStule(routeFeatureId);
				var extraparams = "?overview=full&alternatives=true&steps=true";  //&hints=;"
				var urll = 'https://' + hostname + ':8443/route/v1/driving/'
				const url = urll + path + extraparams;

				log("createRoute url " + url)
				fetch(url).then(function (r) {
					return r.json();
				}).then(function (json) {
					if (json.code !== 'Ok') {
						console.log('No route found.');
						return;
					}
					console.log('Route added');
					//points.length = 0;
					var r = Route.createRouteFeature(json.routes[0].geometry,routeFeatureId,stule);
					RouteControl.updateRouteInfo(json.routes[0]);
					// r.setId("ff");

					// 		let feature = vectorSource.getFeatureById('ff');
					//		console.log('Route added'+feature);
				});
			}
			
			static createRouteFeature (polyline,routeFeatureId,routeStyle) {
					// route is ol.geom.LineString
					var route = new ol.format.Polyline({
						factor: 1e5
					}).readGeometry(polyline, {
						dataProjection: 'EPSG:4326',
						featureProjection: 'EPSG:3857'
					});

					var routeFeature = vectorSource.getFeatureById(routeFeatureId);
					if (routeFeature)
						vectorSource.removeFeature(routeFeature);

					var feature = new ol.Feature({type: 'route', geometry: route});
					feature.setId(routeFeatureId)
					feature.setStyle(routeStyle);
					vectorSource.addFeature(feature);
					//	 map.render();
					return feature;
				}
			
}
