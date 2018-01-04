package powershell.view

import groovy.swing.SwingBuilder
import powershell.controller.ConsoleMessage
import powershell.controller.EventHandler

import javax.swing.WindowConstants

class MainFrame {

    private textArea
    private eh = new EventHandler(this)

    def show() {

        /**
         * Set look and feel - Available: [metal, nimbus, mac, motif, windows, win2k, gtk, synth, system]
         */
        SwingBuilder.lookAndFeel 'motif'

        new SwingBuilder().edt {
            frame(
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
                    this.textArea = textArea(
                            id: 'cmdArea',
                            "${ConsoleMessage.welcome()}${ConsoleMessage.prompt()}",
                            editable: true,
                            keyPressed: eh.keyPressed
                        )
                }

            }
        }
    }
}
