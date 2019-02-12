package powershell.view

import com.google.common.collect.Maps
import com.jediterm.pty.PtyMain
import com.jediterm.terminal.TtyConnector
import com.jediterm.terminal.ui.JediTermWidget
import com.jediterm.terminal.ui.TerminalSession
import com.jediterm.terminal.ui.TerminalWidget
import com.jediterm.terminal.ui.UIUtil
import com.jediterm.terminal.ui.settings.DefaultSettingsProvider
import com.pty4j.PtyProcess
import groovy.swing.SwingBuilder
import powershell.controller.ConsoleProcess
import powershell.controller.CustomTtyConnector
import powershell.controller.EventHandler
import powershell.controller.TerminalSettings

import javax.swing.*
import java.nio.charset.Charset

class MainFrame {


    private eh = new EventHandler(this)
    private JediTermWidget terminal
    private frame

    def show() {

        /**
         * Set look and feel - Available: [metal, nimbus, mac, motif, windows, win2k, gtk, synth, system]
         */
        SwingBuilder.lookAndFeel 'motif'

        new SwingBuilder().edt {
            frame = frame(
                    title: "Powershell by sazevedo",
                    size: [1024, 768],
                    show: true,
                    locationRelativeTo: null,
                    defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE) {

                menuBar {
                    menu(text: 'File') {
                        menuItem() {
                            action(name: 'New', closure: { println "Clicked on new menu" })
                        }
                        separator()

                        menuItem() {
                            action(name: 'Exit', closure: { System.exit(0) })
                        }
                    }
                    menu(text: 'Help') {
                        menuItem() {
                            action(name: 'About', closure: { })
                        }
                    }
                }

                scrollPane(id: 'scrollPane', constraints: context.CENTER, border: null) {
                    widget(terminal = new JediTermWidget(new TerminalSettings()))
                }

            }
        }

        def process = new ConsoleProcess(terminal.terminal)
        new Thread(process).start()

        openSession(terminal as JediTermWidget, new CustomTtyConnector(process))
    }

    void openSession(TerminalWidget terminal, TtyConnector ttyConnector) {
        TerminalSession session = terminal.createTerminalSession(ttyConnector)
        session.start()
    }
}
