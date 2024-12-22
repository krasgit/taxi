		
var utils = {
  distanceBetweenPoints (latlng1, latlng2){
	
	var  length = ol.Sphere.getLength_([latlng1, latlng2],6371008.8);
	return length;
    //var line = new ol.geom.LineString([latlng1, latlng2]);
    //return line.getLength()*1000*100/1.2;//in m??
  },
  getWaypointName(index,length) {
   return index === 0 ?'Start' :(index < length - 1 ? 'Via ' + index : 'End');
  },
  elementChildren: function (element) {
    var childNodes = element.childNodes,
				children = [],
				i = childNodes.length;
    while (i--) {
	  if (childNodes[i].nodeType == 1) {
	    children.unshift(childNodes[i]);
		}
	}
    return children;
  },
  createFeature: function (coord) {
	g = new ol.geom.Point(ol.proj.fromLonLat(coord));
	var feature = new ol.Feature({type: 'place', geometry: g});
	feature.setStyle(routeStyle);
	vectorSource.addFeature(feature);
	var translate1 = new app.Drag({features: new ol.Collection([feature])});
			map.addInteraction(translate1);
		},
		createRoute: function (polyline) {
			// route is ol.geom.LineString
			var route = new ol.format.Polyline({
				factor: 1e5
			}).readGeometry(polyline, {
				dataProjection: 'EPSG:4326',
				featureProjection: 'EPSG:3857'
			});

			var routeFeature = vectorSource.getFeatureById('routeFeature');
			if (routeFeature)
				vectorSource.removeFeature(routeFeature);

			var feature = new ol.Feature({type: 'route', geometry: route});
			feature.setId('routeFeature')
			feature.setStyle(routeStyle);
			vectorSource.addFeature(feature);
			//	 map.render();
			return feature;
		},
		to4326: function (coord) {
			return ol.proj.transform([
				parseFloat(coord[0]), parseFloat(coord[1])
			], 'EPSG:3857', 'EPSG:4326');
		}
	};