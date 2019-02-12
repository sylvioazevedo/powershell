package powershell.controller

import com.jediterm.terminal.Terminal
import com.jediterm.terminal.ui.UIUtil
import com.pty4j.PtyProcess
import com.pty4j.WinSize

import java.awt.Dimension
import java.awt.event.KeyEvent

class ConsoleProcess implements Runnable {

    def pos = new PipedOutputStream()
    def pw = new PrintWriter(pos)

    def pis = new PipedInputStream(pos)
    def reader = new InputStreamReader(pis)

    def output = new PipedOutputStream()
    def pOut = new PipedInputStream(output)

    def input = new InputStreamReader(pOut)
    def baos = new ByteArrayOutputStream()

    private Terminal t

    private Dimension termSize
    private Dimension pixelSize

    ConsoleProcess(t) {
        this.t = t
    }

    @Override
    void run() {

        ConsoleMessage.welcome(pw)

        pw.print ConsoleMessage.prompt(System.getProperty("user.dir"))
        pw.flush()

        int ch

        while(true) {

            while((ch = input.read())!=-1) {

                switch (ch) {

                    case 10: // new line
                    case 13: // Carriage return

                        String cmd = baos.toString()
                        baos.reset()

                        process cmd

                        pw.print ConsoleMessage.prompt(System.getProperty("user.dir"))
                        pw.flush()
                        break

                    case 127: // back space

                        if(baos.size()==0) {
                            break
                        }

                        // adjust command buffer
                        String cmd = baos.toString()
                        baos.reset()
                        baos.write cmd.substring(0, cmd.size()-1).bytes
                        baos.flush()

                        // update terminal
                        t.backspace()
                        t.eraseCharacters 1
                        break

                    case 27:    // arrow up
                        break

                    case KeyEvent.VK_TAB: //tab
                        break

                    default:
                        baos.write ch
                        baos.flush()

                        pw.print ch as char
                        pw.flush()
                }
            }

            Thread.sleep(100)
        }
    }

    def process(String cmd) {

        def cmds = cmd.split(" ")

        switch (cmds[0]) {

            case "cd":
                pw.write "\r\n"

                if(cmds.size()==1) {
                    pw.write "${System.getProperty "user.dir"}"
                }
                else {
                    def dir = new File("${System.getProperty("user.dir")}${File.separator}${cmds[1]}")

                    if(!dir.isDirectory()) {
                        pw.write "Not a directory: ${dir.canonicalPath}\r\n"
                    }
                    else {
                        System.setProperty "user.dir", dir.canonicalPath
                    }
                }
                pw.write "\r\n"
                pw.flush()
                break

            case "version":
                break


            case "exit":
            case "quit":
                System.exit 0
                break

            // external process
            default:

                try {
                    def envs = [:]
                    envs.putAll System.getenv()

                    def command

                    if (UIUtil.isWindows) {
                        command = ["cmd.exe", "/c"]
                        command.addAll cmds
                    }
                    else {
                        command = ["/bin/bash", "-c"]
                        envs.put("TERM", "xterm")
                        command << cmd
                    }

                    def process = PtyProcess.exec(command as String[], envs, System.getProperty("user.dir"))

                    process.setWinSize(
                            new WinSize(
                                    termSize.width as int, termSize.height as int,
                                    pixelSize.width as int, pixelSize.height as int
                            )
                    )

                    pw.write("\r\n")
                    pw.flush()

                    def ot = Thread.start {

                        try{
                            int ch
                            while ((ch = input.read())!=-1) {
                                process.out.write ch
                                process.out.flush()
                            }
                        }
                        catch(InterruptedIOException iioe) {
                            // nothing to do
                        }
                    }

                    def it = Thread.start {
                        int read
                        def buffer = new byte[1024]

                        while((read = process.in.read(buffer))!=-1) {
                            pos.write buffer, 0, read
                            pos.flush()
                        }
                    }

                    process.waitFor()

                    ot.interrupt()
                    it.interrupt()

                    ot.join()
                    it.join()

                    pw.write("\r\n")
                    pw.flush()

                }
                catch(InterruptedIOException iioe) {
                    pw.print "\r\nError executing command [${cmd}]: ${iioe.message}\r\n"
                }
        }
    }

    def setSize(Dimension termSize, Dimension pixelSize) {

        // keep dimensions references inside
        this.termSize = termSize
        this.pixelSize = pixelSize
    }
}
