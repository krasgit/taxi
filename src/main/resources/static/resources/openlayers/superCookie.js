	class SuperCookie extends ol.control.Control {
	  /**
	   * @param {Object} [opt_options] Control options.
	   */
	  constructor(opt_options) {
	    const options = opt_options || {};

	    const button = document.createElement('button');
		button.setAttribute("id", "b1");
	    button.innerHTML ='<i class="fa fa-sign-in"></i>';// 'Login';
		////button.innerHTML ='<i class="fa fa-sign-out"></i>';// 'Loginout';
		//button.style.width="3.875em";
	    
		//button.style.backgroundColor ="#4CAF50";
		
		
		const element = document.createElement('div');
		element.setAttribute("id", "d1");
	    element.className = 'rotate-north ol-unselectable ol-control';
		
		//element.style.backgroundColor = " rgba(0,0,136,0.5)";
		//element.style.backgroundColor ="#4CAF50";
		
		
		element.style.top = "95px"; 
	    element.appendChild(button);

	    super({
	      element: element,
	      target: options.target,
	    });

	    button.addEventListener('click', this.handleRotateNorth.bind(this), false);
	  }
	  setState(state) {

	  }
	  
	  handleRotateNorth(e) {
		wsPing();
		//{"lon":27.91940200827637,"lat":43.22019301099303, //105   001293523373954321
		//"lon":27.920689468603523,"lat":43.22006791652882,
		
		//"lon":27.931085710745243,"lat":43.21074765713317,
		//"lon":27.92988408110657,"lat":43.210763296345306,
		

		//"lon":27.7548645784668,"lat":43.15861553537073,"name":"Константиново, Варна, България"},{"lon":27.766880874853527,"lat":43.15861553537073,"name":"Припек, Константиново, Варна, 9140, България"}]}
//var distance=utils.distanceBetweenPoints ([27.7548645784668, 43.15861553537073], [27.766880874853527,43.21074765713317]);

		const button=document.getElementById("b1");
		button.innerHTML="W";
	    //this.getMap().getView().setRotation(0);
	  }
	}
