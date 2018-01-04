package powershell.controller

class ConsoleMessage {

    static welcome() {
"""
       ___                         __       ____          
      / _ \\___ _    _____ _______ / /  ___ / / /          
     / ___/ _ \\ |/|/ / -_) __(_-</ _ \\/ -_) / /           
    /_/   \\___/__,__/\\__/_/ /___/_//_/\\__/_/_/       __   
         / /  __ __    ___ ___ ____ ___ _  _____ ___/ /__ 
        / _ \\/ // /   (_-</ _ `/_ // -_) |/ / -_) _  / _ \\
       /_.__/\\_, /   /___/\\_,_//__/\\__/|___/\\__/\\_,_/\\___/
            /___/               
            
Powershell - Version 0.01-beta - Now: ${Calendar.instance.format("dd/MM/yyyy HH:mm:ss.SSS")}
 by sazevedo
                                      
"""
    }

    static prompt(def path) {
        path? "${path}/> ": "/> "
    }
}
