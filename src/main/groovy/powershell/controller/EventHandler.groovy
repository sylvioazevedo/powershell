package powershell.controller

import powershell.view.MainFrame

import java.awt.event.KeyEvent

class EventHandler {

    private parent

    EventHandler(def parent) {
        this.parent = parent as MainFrame
    }

    def cmd = new StringBuffer()

    def keyPressed = { KeyEvent e ->

        switch(e.keyCode) {
            case KeyEvent.VK_SHIFT:
            case KeyEvent.VK_CONTROL:
            case KeyEvent.VK_ALT:
            case KeyEvent.VK_UP:
                e.consume()
                break

            case KeyEvent.VK_BACK_SPACE:
                cmd.length = cmd.length() > 0 ? cmd.length() - 1 : 0
                break

            case KeyEvent.VK_ENTER:

                try {
                    // try to execute buffer
                    def pb = new ProcessBuilder(cmd.toString().trim().split(" "))
                    def env = pb.environment()
                    env << System.getenv()
                    println env
                    pb.directory(new File(System.properties."user.dir" as String))
                    pb.redirectErrorStream true

                    def p = pb.start()

                    parent.textArea.append "\r\n"

                    p.inputStream.eachLine { l -> parent.textArea.append "${l}\r\n" }
                    p.errorStream.eachLine { l -> parent.textArea.append "${l}\r\n" }

                    p.waitFor()

                    parent.textArea.append "\r\n${ConsoleMessage.prompt()}"
                }
                catch(IOException ioe) {
                    def errors = new StringWriter()
                    ioe.printStackTrace(new PrintWriter(errors))
                    parent.textArea.append "${errors.toString()}\r\n${ConsoleMessage.prompt()}"
                }

                cmd.length = 0
                e.consume()
                break

            default:
                cmd << e.keyChar
        }

        //println "Buffer: ${cmd.toString().trim()}"
    }
}
