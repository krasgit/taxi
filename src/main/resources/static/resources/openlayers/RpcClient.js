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
  
	

 #onOpen(event) {
	
		 }
	
  #onClose(event) {
	
//	alert(event);
     console.log(`close [message] Data received from server: ${event.data}`);
	 this.socket=null;
	 
	var connectionState= document.getElementById("connectionState");
	if(connectionState)
	   connectionState.innerHTML = "&#xf127;";
	 
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
    //console.log(`[message] Data received from server: ${event.data}`);
    let response = JSON.parse(event.data);
	
	if(response.type=='sessionUpdate'){
	var session=	response.sender;
	Cookie.setCookie("token",session) ;
	var connectionState= document.getElementById("connectionState");
			if(connectionState)
			connectionState.innerHTML ="&#xf0c1;";
	
	}
	
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