import { RemoteProcedures } from "./RpcRemoteProcedures.js";
import {sampleFunction} from './mylib.js';

window.sampleFunction=sampleFunction;

export const remoteProcedures = new RemoteProcedures();

const PORT = 8443;
const MAPPING = "/rpc";

await remoteProcedures.open('wss://' + window.location.hostname + ':' + PORT + MAPPING);
/*
const input = document.querySelector("#input");
const multiplyButton = document.querySelector("#multiply-button");
const sqrtButton = document.querySelector("#sqrt-button");
*/

/*
multiplyButton.onclick = async () => {
  const result = await remoteProcedures.multiplyByOneThousand(
    Number(input.value)
  );
  input.value = result;
    
};
*/
/*
const sumButton = document.querySelector("#sum-button");

sumButton.onclick = async () => {
  
  const result = await remoteProcedures.sum(
      Number(input.value), Number(input.value)
    );
    input.value = result;
    
  
};
sqrtButton.onclick = async () => {
  const result = await remoteProcedures.doSquareRoot(Number(input.value));
  input.value = result;
};

*/

export async function  calll(){
	 const result =  await remoteProcedures.sum( 1, 2  );
	 input.value = result;
	 return result;
}

export async function  printAddress(qaz,wsx)   {
  const result =  await remoteProcedures.sum( qaz,wsx  );
   
   return  result;
  
};

export async function  callRPC(name ,qaz,wsx)   {
   const result =  await remoteProcedures.call(name, qaz,wsx,  );
   
   return  result;
  
};

window.qazFunction=printAddress;
window.callRPC=callRPC;




