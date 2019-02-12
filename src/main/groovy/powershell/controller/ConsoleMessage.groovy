package powershell.controller

class ConsoleMessage {

    static welcome(pw) {
        pw.print "\r\n"
        pw.print "┌─┐┌─┐┬ ┬┌─┐┬─┐┌─┐┬ ┬┌─┐┬  ┬     \r\n"
        pw.print "├─┘│ ││││├┤ ├┬┘└─┐├─┤├┤ │  │     \r\n"
        pw.print "┴  └─┘└┴┘└─┘┴└─└─┘┴ ┴└─┘┴─┘┴─┘   \r\n"
        pw.print " ┌┐ ┬ ┬  ┌─┐┌─┐┌─┐┌─┐┬  ┬┌─┐┌┬┐┌─┐\r\n"
        pw.print " ├┴┐└┬┘  └─┐├─┤┌─┘├┤ └┐┌┘├┤  │││ │\r\n"
        pw.print " └─┘ ┴   └─┘┴ ┴└─┘└─┘ └┘ └─┘─┴┘└─┘\r\n"
        pw.print "\r\n"
        pw.print "Powershell - Version 0.01-beta - Now: ${Calendar.instance.format("dd/MM/yyyy HH:mm:ss.SSS")}\r\n"
        pw.print " by sazevedo\r\n"
    }

    static prompt(def path) {
        path? "${path}/> ": "/> "
    }
}
