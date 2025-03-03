//import {Control, defaults as defaultControls} from './ol/control.js';
class LoginControl extends ol.control.Control {
  /**
   * @param {Object} [opt_options] Control options.
   */
  
  static routeControl = null;
  _count;
  //container;
  
  constructor(opt_options) {
	
    const options = opt_options || {};
		
			var container = LoginControl.createContainer(options);
			super({element: container, target: options.target, });
			
			this._count=container;
				/*
				Object.defineProperty(this, "test", {
				  value: 42,
				  writable: false,
				  enumerable:false,
				});
				*/
				/*
				Object.defineProperty(this, "conteiner", {
								  value: container,
								  writable: true,
								  enumerable:true,
								});
								
								*/
					//			LoginControl.curretActivePage="hgffgh";
					
				LoginControl.routeControl=new RouteControl();	
							
								
  }

  set count(value)
  {
	this._count=value;
  }
  get count(){
	return this._count;
  }
  static exp(){
	var dd=RouteControl.get();
	const cWaypoint = document.getElementById('userName');
	cWaypoint.appendChild(dd);
  }
  
  static  maximize(){
	
  }
  
  static  showrouteControl(logned)
  {
	
	
	const logoutButton = document.getElementById('buttonContent');
	
	if(logned)
				{			
					var user =Cookie.getCookie("user") ;
					logoutButton.setAttribute('onclick', 'RouteControl.logOut();');
					
					var buttonContent="logout:"+user;
					const isTaxi=Cookie.getCookie("isTaxi");
						if(isTaxi=='true')
								 buttonContent+='<i class="fa fa-taxi" style="font-size:12px"></i>';
					
					
					logoutButton.innerHTML = buttonContent;
				}
				else
				{
				logoutButton.innerHTML = '<a href="#Foo" onclick="LoginControl.visible(true);">login</a>';
	}
	
	
	const container = document.getElementById('LoginControlContainer');
	var rc = new RouteControl({mode:true});
	var routeContainer=	RouteControl.createContainerEx({mode:logned})
	
	container.innerHTML=routeContainer;
	   //container.appendChild(routeContainer);
	   rc.init();
	   
	   if(logned)
	     RouteControl.loadOrders();
	   
   return;
  }
    
static logIn() {
		
		var username=document.getElementById('username').value;
		var password=document.getElementById('password').value;

		callRPC("login",username,username).then((resultJson) => {
	
			if(resultJson==null){

				   }
				   else 
				   {
					
					
					let result=JSON.parse(resultJson);
					
					Cookie.setCookie("user",username);
					Cookie.setCookie("token",result.sessionId);
					Cookie.setCookie("isTaxi",result.isTaxi);
					
					LoginControl.showrouteControl(true);
					
				   }
				   });
}

static PrincipalRegistration() {
		
		var username=document.getElementById('username').value;
		var password=document.getElementById('password').value;
		
		wsPrincipalRegistration(username,password);
		
	Cookie.setCookie("user",username)
	//log("PrincipalRegistration ");
	}

	
static visible(mode)
	{
		const container = document.getElementById('LoginControlContainer');
			
			
				
			var login=	LoginControl.getLoginForm();
				
			container.innerHTML=login;
	
	}
	
static createContainer(options) {
		var node_1 = document.createElement('DIV');
 			node_1.setAttribute('name', 'LoginControlContainer');
  			node_1.setAttribute('id', '');
  			node_1.setAttribute('class', '     border w3-border-red  ');
  			//node_1.setAttribute('style', ' background-color: lightblue; min-width: 350;position: absolute; right : 0em; bottom : 0em ');
			node_1.setAttribute('style', ' background-color: lightblue; min-width: 350;position: absolute; right : 0em; top : 0em ');

						node_1.innerHTML=LoginControl.getLoginControlContent();
  		return node_1;
  		}
 
static getLoginControlContent(){
		
		//var form=LoginControl.getLoginForm();
		
	let loginContent = `
	<div class="ccontainer">
		<div class="card" id="log-in-card">
			<header class="card-header">
			
			<h1 id="connectionState" class="card-header-title" style="padding: 0.1rem;"></h1>
			<h1 id="buttonContent" class="card-header-title" style="padding: 0.1rem;">Log In</h1>
			<h1 id="routeUpdateInfo" class="card-header-title" style="padding: 0.1rem;"></h1>
				
			</header>
			
			<div id="LoginControlContainer" ><div>
			 
		
			</div>
		   </div>`;
			  
		   return loginContent;
		    }

			
			
	static getLoginForm(){
	var lc=`
		<div class="card-content" style="padding: 0.3rem;">
			<div class="content">
				<div id="userName" class="field">
					<label class="label">Username</label>
			      		<div class="control">
							<input id="username" class="input" type="email" placeholder="user@example.com">
						</div>
				</div>  
				<div class="field">
					<label class="label">Password</label>
						<div class="control">
							<input id="password" class="input" type="password">
					    </div>
				</div>
			
					<p style="text-align: right;"><a href="#" id="open-userinfo">Forgot your password?</a></p>
			</div>
		</div>
		
		<footer class="card-footer" style="padding: 0.3rem;">
			<div class="card-footer-item" style="padding: 0.2rem;">
				<a href="#" onclick="LoginControl.logIn();" class="button is-primary" id="log-in-button">Log In</a>
				&nbsp;
				<a href="#" onclick="LoginControl.PrincipalRegistration();" class="button is-primary" id="log-in-button">Registration</a>
			</div>
		</footer>`;		  
	return lc;
	}
			
}