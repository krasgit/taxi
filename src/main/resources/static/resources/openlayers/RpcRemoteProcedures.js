import { RpcClient } from "./RpcClient.js";

export class RemoteProcedures extends RpcClient {
  async multiplyByOneThousand(number) {
    return this.callRemoteProcedure("multiplyByOneThousand", [...arguments]);
  }

  async doSquareRoot(number) {
    return this.callRemoteProcedure("doSquareRoot", [...arguments]);
  }
  
  async sum(number1,number2) {
      return this.callRemoteProcedure("sum", [...arguments]);
    }
	
	
	async call(name,number1,number2) {
		var arg=[...arguments];
		arg.shift();
		
	      return this.callRemoteProcedure(name, arg);
	    }
	
}