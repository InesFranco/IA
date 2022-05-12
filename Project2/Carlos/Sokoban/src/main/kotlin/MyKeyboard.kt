import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JPanel

class MyKeyboard(panel: JPanel) : KeyListener {

    private var panel: JPanel

    init {
        this.panel = panel
    }
    override fun keyTyped(e: KeyEvent?) {
        println("key typed: " + e!!.keyChar)
    }

    override fun keyPressed(e: KeyEvent?) {
        println("key pressed: " + e!!.keyChar)
    }

    override fun keyReleased(e: KeyEvent?) {
        println("key released: " + e!!.keyChar)
    }
}