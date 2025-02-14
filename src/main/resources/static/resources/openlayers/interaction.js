var app = {};
		/**@constructor  @extends {module:ol/interaction/Pointer}  */
		app.Drag = function () {

			ol.interaction.Pointer.call(this, {
				handleDownEvent: app.Drag.prototype.handleDownEvent,
				handleDragEvent: app.Drag.prototype.handleDragEvent,
				handleMoveEvent: app.Drag.prototype.handleMoveEvent,
				handleUpEvent: app.Drag.prototype.handleUpEvent
			});

			/** @type {module:ol/pixel~Pixel}* @private */
			this.coordinate_ = null;

			//@type {string|undefined} @private
			this.cursor_ = 'pointer';
			//@type {module:ol/Feature~Feature} @private
			this.feature_ = null;
			//@type {string|undefined} @private
			this.previousCursor_ = undefined;

		};
		// inherits(app.Drag, PointerInteraction);
		ol.inherits(app.Drag, ol.interaction.Pointer);

		/**
		 * @param {module:ol/MapBrowserEvent~MapBrowserEvent} evt Map browser event.
		 * @return {boolean} `true` to start the drag sequence.
		 */
		app.Drag.prototype.handleDownEvent = function (evt) {
			var map = evt.map;

			var feature = map.forEachFeatureAtPixel(evt.pixel,
				function (feature) {
					return feature;
				});

			if (feature) {
				this.coordinate_ = evt.coordinate;
				this.feature_ = feature;
			}
		return !!feature;
		};

		/**
		 * @param {module:ol/MapBrowserEvent~MapBrowserEvent} evt Map browser event.
		 */
		app.Drag.prototype.handleDragEvent = function (evt) {
			var deltaX = evt.coordinate[0] - this.coordinate_[0];
			var deltaY = evt.coordinate[1] - this.coordinate_[1];

			var geometry = this.feature_.getGeometry();
			geometry.translate(deltaX, deltaY);

			this.coordinate_[0] = evt.coordinate[0];
			this.coordinate_[1] = evt.coordinate[1];

			var featureId = this.feature_.getId();
			var bntEl = document.getElementById("bnt" + featureId);

			var geometry = this.feature_.getGeometry().clone().transform(map.getView().getProjection(), 'EPSG:4326');
			var coordinates = geometry.flatCoordinates;

			reverseGeocoding(coordinates[0], coordinates[1], bntEl, callbackSetInputElVal)
			//reverseGeocodingProton(coordinates[0], coordinates[1], bntEl, callbackSetInputElVal)
		};

		/** @param {module:ol/MapBrowserEvent~MapBrowserEvent} evt Event.  */
		app.Drag.prototype.handleMoveEvent = function (evt) {
			if (this.cursor_) {
				var map = evt.map;
				var feature = map.forEachFeatureAtPixel(evt.pixel,
					function (feature) {
						return feature;
					});
				var element = evt.map.getTargetElement();
				if (feature) {
					if (element.style.cursor != this.cursor_) {
						this.previousCursor_ = element.style.cursor;
						element.style.cursor = this.cursor_;
					}

					//feature.getGeometry().setCoordinates(evt.coordinate);
				} else if (this.previousCursor_ !== undefined) {
					element.style.cursor = this.previousCursor_;
					this.previousCursor_ = undefined;
				}
			}
		};
		/**  @return {boolean} `false` to stop the drag sequence.    */
		app.Drag.prototype.handleUpEvent = function () {
			this.coordinate_ = null;
			this.feature_ = null;
			RouteControl.refresh()
			return false;
		};
	//-----------------------------------------------------------------------------
