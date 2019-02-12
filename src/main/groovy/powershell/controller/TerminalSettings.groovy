package powershell.controller

import com.jediterm.terminal.TerminalColor
import com.jediterm.terminal.TextStyle
import com.jediterm.terminal.ui.settings.DefaultSettingsProvider

class TerminalSettings extends DefaultSettingsProvider {

    @Override
    TextStyle getDefaultStyle() {
        new TextStyle(TerminalColor.WHITE, TerminalColor.rgb(0, 0, 50))
    }
}
