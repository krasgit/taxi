export class RpcClient {
  /** @type WebSocket */
  socket;
  /** @type Map */
  waiting;

  constructor() {
    this.waiting = new Map();
  }

  /** reconnect URL */
  reconnectURL;
  
  /**
   * Create a WebSocket.
   * @returns @type Promise
   */
  open(url) {
    this.socket = new WebSocket(url);
    this.socket.addEventListener("close", this.#onClose.bind(this));
	this.socket.addEventListener("open", this.#onOpen.bind(this));
	
	this.socket.addEventListener("message", this.#onMessage.bind(this));
	
    return new Promise((resolve, reject) => {
      this.socket.addEventListener("open", resolve, { once: true });
      this.socket.addEventListener("error", reject, { once: true });
    });
  }

  reOpen() {
	
	var user =Cookie.getCookie("user") ;
	var token =Cookie.getCookie("token") ;
	this.open(this.reconnectURL+'?user='+user+'&token='+token);
	
    }
  
	
	reOpenNoSesion() {
		
	
		this.open(this.reconnectURL);
		
	    }

 #onOpen(event) {
	
		 }
	
  #onClose(event) {
	
//	alert(event);
     console.log(`close [message] Data received from server: ${event.data}`);
	 this.socket=null;
	 
	var connectionState= document.getElementById("connectionState");
	if(connectionState)
	   connectionState.innerHTML =`		<?xml version="1.0" encoding="UTF-8"?>
		<!-- Uploaded to: SVG Repo, www.svgrepo.com, Generator: SVG Repo Mixer Tools -->
		 <svg width="16px" height="16px" viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">

		  <defs>
		    <radialGradient id="RG1" cx="50%" cy="50%" fx="50%" fy="50%" r="50%">
		      <stop style="stop-color:rgb(103,155,203);stop-opacity:0.75;" offset="0%"/>
		      <stop style="stop-color:rgb(18,49,65);stop-opacity:1;" offset="100%"/>
		    </radialGradient>
		  </defs>
		  <ellipse cx="35" cy="34" rx="32" ry="30" style="stroke-width:4;stroke:#dddddd;fill:none;"/>
		  <ellipse cx="35" cy="34" rx="32" ry="30" style="fill:url(#RG1);fill-opacity:1;fill-rule:nonzero"/>
		  <g style="fill:none;stroke:#aaaaaa;stroke-width:1.5px;stroke-linecap:butt;" >
		    <path d="M 36,64 C 22,56 19,46 19,34 19,22 26,10 36,4 l 0,60 C 36,64 54,55 54,34 54,13 36,4 36,4" />
		    <path d="m 4,34 63,0 0,0"/>
		    <path d="m 13,15 c 0,0 12,7 23,7 13,0 23,-7 23,-7"/>
		    <path d="m 13,54 c 0,0 9,-7 23,-7 16,0 23,7 23,7"/>
		  </g>
		  <ellipse cx="35" cy="34" rx="32" ry="30" style="stroke-width:3;stroke:#123141;fill:none;"/>

		  <ellipse cx="35" cy="22" rx="5" ry="5" style="fill:#000000;stroke:#cccccc;stroke-width:2"/>
		  <path style="fill:none;stroke:#000000;stroke-width:4;" d="M 38,81 C 12,92 3.9,67 10,58 17,46 32,46 39,35 41,32 41,25 36,22"/>
		
		  <path style="fill:#FF3D3D;stroke:#730000;stroke-width:3;fill-opacity:0.85" d="M 15,4.6 5,15 25,35 5.5,55 15,65 35,45 55,65 65,55 45,35 65,14 55,4.6 35,25 z"/>
		
		  <path style="fill:#111111;stroke:#666666" d="m 60,87 0,5 c 0,0 -3,0 -4,0 -2,0 -3,3 -3,3 1,0 26,0 27,0 0,0 -1,-3 -3,-3 -1,0 -4,0 -4,0 l 0,-5"/>
		  <path style="fill:#ffffff;stroke:#000000;stroke-width:4px;stroke-linecap:butt" d="m 39,83 c 0,-8 0,-25 0,-31 0,-2 0,-5 3,-5 6,0 45,0 48,0 3,0 3,3 3,7 0,6 0,26 0,29 0,1 0,3 -5,3 -3,0 -43,0 -46,0 -3,0 -3,-3 -3,-3 z"/>
		  <path style="fill:#444444;stroke:#ffffff" d="m 41,49 50,0 0,35 -50,0 z"/>

		</svg>`
	 
	 }
  
  /**
   * Call a remote procedure. Return a Promise that is fulfilled when the server responds.
   * @param {string} procedure
   * @param {object} payload
   * @returns @type Promise
   */
  callRemoteProcedure(procedure, args) {
	
	if(this.socket==null)
		{
			//alert("reconnectURL");
			console.log(`try reconnect`);
			if(procedure=='getAllConnectedUser'){
				this.reOpenNoSesion();
				return;
			}
			if(procedure=='getAllOpenOrders'){
						this.reOpenNoSesion();
						return;
					}
			this.reOpen();
			return;
		}
	
	
    const id = Math.random().toString(16).slice(2);
    return new Promise((resolve, reject) => {
      this.waiting.set(id, (error, responsePayload) => {
        if (error) {
			console.log("Server Error: " + error)
          reject("Server Error: " + error);
        } else {
          resolve(responsePayload);
        }
      });

	  var msg=JSON.stringify({ id, procedure, args: [...args], })
	  if (this.socket.readyState === 1)  
	  		this.socket.send( msg );
	 // this.sendMessage(msg)
    });
  }
  
   sendMessage(msg){
      // Wait until the state of the socket is not ready and send the message when it is...
      this.waitForSocketConnection(this.socket, function(){
        // console.log("miissage send!!!");
          socket.send(msg);
      });
  }

   waitForSocketConnection(socket, callback){
      setTimeout(
          function () {
              if (socket.readyState === 1) {
                  console.log("Connection is made")
                  if (callback != null){
                      callback();
                  }
              } else {
                  console.log("wait for connection...")
                  this.waitForSocketConnection(socket, callback);
              }

          }, 5); // wait milisecond for the connection
  }
  
  
  
  //todo//
  /**
   * Handle all incoming message. Match server replies to waiting callbacks.
   * @param {MessageEvent} event
   */
  #onMessage(event) {
	
	var connectionState= document.getElementById("connectionState");
				if(connectionState)
				connectionState.innerHTML =`					<?xml version="1.0" encoding="UTF-8"?>
					<!-- Uploaded to: SVG Repo, www.svgrepo.com, Generator: SVG Repo Mixer Tools -->
				 <svg width="16px" height="16px" viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">

					  <defs>
					    <radialGradient id="RG1" cx="50%" cy="50%" fx="50%" fy="50%" r="50%">
					      <stop style="stop-color:rgb(103,155,203);stop-opacity:0.75;" offset="0%"/>
					      <stop style="stop-color:rgb(18,49,65);stop-opacity:1;" offset="100%"/>
					    </radialGradient>
					  </defs>
					  <ellipse cx="35" cy="34" rx="32" ry="30" style="stroke-width:4;stroke:#dddddd;fill:none;"/>
					  <ellipse cx="35" cy="34" rx="32" ry="30" style="fill:url(#RG1);fill-opacity:1;fill-rule:nonzero"/>
					  <g style="fill:none;stroke:#aaaaaa;stroke-width:1.5px;stroke-linecap:butt;" >
					    <path d="M 36,64 C 22,56 19,46 19,34 19,22 26,10 36,4 l 0,60 C 36,64 54,55 54,34 54,13 36,4 36,4" />
					    <path d="m 4,34 63,0 0,0"/>
					    <path d="m 13,15 c 0,0 12,7 23,7 13,0 23,-7 23,-7"/>
					    <path d="m 13,54 c 0,0 9,-7 23,-7 16,0 23,7 23,7"/>
					  </g>
					  <ellipse cx="35" cy="34" rx="32" ry="30" style="stroke-width:3;stroke:#123141;fill:none;"/>

					  <ellipse cx="35" cy="22" rx="5" ry="5" style="fill:#000000;stroke:#cccccc;stroke-width:2"/>
					  <path style="fill:none;stroke:#000000;stroke-width:4;" d="M 38,81 C 12,92 3.9,67 10,58 17,46 32,46 39,35 41,32 41,25 36,22"/>
					<!--
					  <path style="fill:#FF3D3D;stroke:#730000;stroke-width:3;fill-opacity:0.85" d="M 15,4.6 5,15 25,35 5.5,55 15,65 35,45 55,65 65,55 45,35 65,14 55,4.6 35,25 z"/>
					  -->
					  <path style="fill:#111111;stroke:#666666" d="m 60,87 0,5 c 0,0 -3,0 -4,0 -2,0 -3,3 -3,3 1,0 26,0 27,0 0,0 -1,-3 -3,-3 -1,0 -4,0 -4,0 l 0,-5"/>
					  <path style="fill:#ffffff;stroke:#000000;stroke-width:4px;stroke-linecap:butt" d="m 39,83 c 0,-8 0,-25 0,-31 0,-2 0,-5 3,-5 6,0 45,0 48,0 3,0 3,3 3,7 0,6 0,26 0,29 0,1 0,3 -5,3 -3,0 -43,0 -46,0 -3,0 -3,-3 -3,-3 z"/>
					  <path style="fill:#444444;stroke:#ffffff" d="m 41,49 50,0 0,35 -50,0 z"/>

					</svg>`;
	
    //console.log(`[message] Data received from server: ${event.data}`);
    let response = JSON.parse(event.data);
	
	if(response.type=='sessionUpdate'){
	var session=	response.sender;
	Cookie.setCookie("token",session) ;
	
	
	}
	
	/*
	if(response.type='UpdateInfo'){
		eval(response.result);
			 return ;
	}
	*/
	if(response.id==null){
		eval(response.result);
	 return ;
	 }
	 
	 if(response.id=='handleUpdatePostion'){
		//log(response.result);
		updatePositionMarker(response.result);
		return ;
		}
	 
	 
    let callback = this.waiting.get(response.id);
    this.waiting.delete(response.id);
    callback(response.error, response.result);
  }
  
  /**
   * 
   */
  
}