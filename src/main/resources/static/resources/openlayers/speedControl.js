//import {Control, defaults as defaultControls} from './ol/control.js';
class SpeedControl extends ol.control.Control {
  /**
   * @param {Object} [opt_options] Control options.
   */
  constructor(opt_options) {
    const options = opt_options || {};

 //   const button = document.createElement('button');
//	button.setAttribute("id", "b1");
 //   button.innerHTML ='<i class="fa fa-sign-in"></i>';
	//button.style.width="3.875em";
    
	//button.style.backgroundColor ="#4CAF50";
	
	
	const element = document.createElement('span');
	element.setAttribute("id", "speedctr");
    element.className = 'ol-unselectable ol-control';
	
	//element.style.backgroundColor = " rgba(0,0,136,0.5)";
	//element.style.backgroundColor ="#4CAF50";
	
	element.style.bottom="28px";
	element.style.left=".5em";
	
	//element.style.top = "95px"; 
   // element.appendChild(button);

    super({
      element: element,
      target: options.target,
    });

  //  button.addEventListener('click', this.handleRotateNorth.bind(this), false);
  }

}



