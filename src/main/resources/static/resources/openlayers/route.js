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
		
		/*
		const style = new ol.style.Style({
							  text: new Text({
							    font: 'bold 11px "Open Sans", "Arial Unicode MS", "sans-serif"',
							    placement: 'line',
							    
							  }),
							});

return	style;	
		*/
		if(routeFeatureId=='routeFeature0')
				   	 return new ol.style.Style({stroke: new ol.style.Stroke({width: 3, color: [255, 40, 40, 0.8], }), });
			
		if(routeFeatureId=='routeFeature1')
						   	 return new ol.style.Style({stroke: new ol.style.Stroke({width: 3, color: [128, 40, 40, 0.8], }), });
		
		if(routeFeatureId=='routeFeature')
		   	 return new ol.style.Style({stroke: new ol.style.Stroke({width: 3, color: [255, 40, 40, 0.8], }), });
		   
		   return new ol.style.Style({stroke: new ol.style.Stroke({width: 3, color: [30, 255, 30, 0.8], }), });
		}			
		
		
		static	createRouteCB(sender,path ,routeFeatureId) {
				
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
							console.log('then createRouteCBRoute added1'+routeFeatureId);
							//points.length = 0;
							var r = Route.createRouteFeatureCB(json.routes[0].geometry,routeFeatureId,stule);
						//TODO	sender.updateRouteInfo(json.routes[0]);
							
							console.log('after createRouteCBRoute added1'+routeFeatureId);
							// r.setId("ff");

							// 		let feature = vectorSource.getFeatureById('ff');
							//		console.log('Route added'+feature);
						});
					}		
					
					
					
  static removeAllRoute(routeFeature){
    var f=vectorSource.getFeatures();
	for (var i = 0; i < f.length; i++) {
	  var dd=f[i];		
      var routeFeatureId=''+dd.getId(); //cast to string
      if(routeFeatureId.startsWith(routeFeature)){
        var routeFeature = vectorSource.getFeatureById(routeFeatureId);
		 if (routeFeature){}
		vectorSource.removeFeature(routeFeature);
													//log("delete rute "+routeFeature)
      }
	}			
  }
				
					
					
	static	createRoute(path ,routeFeatureId) {
		
		log('createRoute routeFeatureId'+routeFeatureId);
		
				var extraparams = "?overview=full&alternatives=true&steps=true";  //&hints=;"
				var urll = 'https://' + hostname + ':8443/route/v1/driving/'
				const url = urll + path + extraparams;
				
				log("createRoute url " + url);
				fetch(url).then(function (r) {
					return r.json();
				}).then(function (json) {
					if (json.code !== 'Ok') {
						console.log('No route found.');
						Route.removeAllRoute(routeFeatureId);
						return;
					}
					console.log('Route added');
					//points.length = 0;
					
					Route.removeAllRoute(routeFeatureId);
					
					if(routeFeatureId =='route2start'){//Resset
						var route2startEl1 = document.getElementById("route2start"+0);
						 route2startEl1.innerHTML=' ';
						
						var route2startEl2 = document.getElementById("route2start"+1);
							route2startEl2.innerHTML=' ';
					}
						
					
					
			
					for (var i = 0; i < json.routes.length; i++) {
			//			log('json.routes[0].'+json.routes[i].geometry);
						var stule=Route.getStule(routeFeatureId+i);				
						var r = Route.createRouteFeaturemt(json.routes[i].geometry,routeFeatureId+i,stule);
						
						const distanceInfo=RouteControl.updateRouteInfo(json.routes[i]);
						
						if(routeFeatureId =='route2start')
						{
							var route2startEl = document.getElementById("route2start"+i);
													route2startEl.innerHTML=distanceInfo;
													log('createRoute route2start info '+distanceInfo +' index '+i);
						}
						else
						{
						var refDistance = document.getElementById("refDistance"+i);
						refDistance.innerHTML=distanceInfo;
						log('createRoute !!!!!route2start   info '+distanceInfo +' index '+i);
						}
						}
					
					
					
					//var r = Route.createRouteFeature(json.routes[0].geometry,routeFeatureId,stule);
					//RouteControl.updateRouteInfo(json.routes[0]);
					// r.setId("ff");

					// 		let feature = vectorSource.getFeatureById('ff');
					//		console.log('Route added'+feature);
				});
			}
			
			static createRouteFeaturemt (polyline,routeFeatureId,routeStyle) {
									// route is ol.geom.LineString
									var route = new ol.format.Polyline({
										factor: 1e5
									}).readGeometry(polyline, {
										dataProjection: 'EPSG:4326',
										featureProjection: 'EPSG:3857'
									});

									//var routeFeature = vectorSource.getFeatureById(routeFeatureId);
									//if (routeFeature)
									//	vectorSource.removeFeature(routeFeature);
									var feature = new ol.Feature({type: 'route', geometry: route});
									feature.setId(routeFeatureId)
									feature.setStyle(routeStyle);
									vectorSource.addFeature(feature);
									//	 map.render();
									return feature;
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
			
			static createRouteFeatureCB (polyline,routeFeatureId,routeStyle) {
					// route is ol.geom.LineString
					var route = new ol.format.Polyline({
						factor: 1e5
					}).readGeometry(polyline, {
						dataProjection: 'EPSG:4326',
						featureProjection: 'EPSG:3857'
					});

				//	var routeFeature = vectorSource.getFeatureById(routeFeatureId);
				//	if (routeFeature)
				//		vectorSource.removeFeature(routeFeature);
					var feature = new ol.Feature({type: 'route', geometry: route});
					feature.setId(routeFeatureId)
					feature.setStyle(routeStyle);
					vectorSource.addFeature(feature);
					//	 map.render();
					return feature;
				}
			
}
