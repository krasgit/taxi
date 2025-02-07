export class RpcClient {
  /** @type WebSocket */
  socket;
  /** @type Map */
  waiting;

  constructor() {
    this.waiting = new Map();
  }

  /**
   * Create a WebSocket.
   * @returns @type Promise
   */
  open(url) {
    this.socket = new WebSocket(url);
    this.socket.addEventListener("message", this.#onMessage.bind(this));
    return new Promise((resolve, reject) => {
      this.socket.addEventListener("open", resolve, { once: true });
      this.socket.addEventListener("error", reject, { once: true });
    });
  }

  /**
   * Call a remote procedure. Return a Promise that is fulfilled when the server responds.
   * @param {string} procedure
   * @param {object} payload
   * @returns @type Promise
   */
  callRemoteProcedure(procedure, args) {
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

      this.socket.send( JSON.stringify({ id, procedure, args: [...args], })  );
    });
  }

  /**
   * Handle all incoming message. Match server replies to waiting callbacks.
   * @param {MessageEvent} event
   */
  #onMessage(event) {
    console.log(`[message] Data received from server: ${event.data}`);
    let response = JSON.parse(event.data);
	
	if(response.id==null){
		eval(response.result);
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