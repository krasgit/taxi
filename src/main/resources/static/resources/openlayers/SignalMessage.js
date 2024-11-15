 

class SignalMessage{
  
  /*
  const msg = new SignalMessage("type", "sender", "receiver");
  OR
  const msg = new SignalMessage("type", "sender", "receiver","ghjg");
    msg.add("a", 1);
    msg.add("b", 3);
    
    const jsonString = JSON.stringify(msg);
    OR 
    const jsonString=msg.json(); 
    
    log(jsonString); 
    */



    constructor(type,sender, receiver, data) {
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.data = data;
        this.map =  new Map();
    }

    toJSON() {
        return {
            type: this.type,
            sender: this.sender,
            receiver: this.receiver,
             data: this.data,
            map: Object.fromEntries(this.map)
            // you can exclude or include other properties here
        };
    }
    
    add(key,value){
    this.map.set(key,value);
    }
    
    json(){
     const jsonString = JSON.stringify(this);
    return jsonString;
    }
}



/* TEST
const msg = new SignalMessage("type", "sender", "receiver","ghjg");
    msg.add("a", 1);
    msg.add("b", 3);
    const jsonString = JSON.stringify(msg);
	OR
    log(msg.json()); 

*/
