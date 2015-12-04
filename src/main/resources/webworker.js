self.onmessage = function(e){
    while(true){
      self.postMessage(e.data);  
    }
}