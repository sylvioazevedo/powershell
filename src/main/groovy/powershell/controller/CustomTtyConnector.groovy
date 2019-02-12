package powershell.controller

import com.jediterm.terminal.Questioner
import com.jediterm.terminal.TtyConnector

import java.awt.Dimension

class CustomTtyConnector implements TtyConnector {

    ConsoleProcess process

    CustomTtyConnector(process) {

        // keep process reference inside
        this.process = process
    }

    @Override
    boolean init(Questioner q) {
        true
    }

    @Override
    void close() {
        println "Closed"
    }

    @Override
    void resize(Dimension termSize, Dimension pixelSize) {
        println "Rized"
        process.setSize(termSize, pixelSize)
    }

    @Override
    String getName() {
        "Custom process"
    }

    @Override
    int read(char[] buf, int offset, int length) throws IOException {
        process.reader.read buf, offset, length
    }

    @Override
    void write(byte[] bytes) throws IOException {
        process.output.write bytes
        process.output.flush()
    }

    @Override
    boolean isConnected() {
        true
    }

    @Override
    void write(String s) throws IOException {
        write s.bytes
    }

    @Override
    int waitFor() throws InterruptedException {
        process.wait()
        return 0
    }
}
