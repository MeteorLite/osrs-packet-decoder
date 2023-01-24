package meteor.deobfuscators.client

import meteor.ObfuscatedBufferStructure
import meteor.PacketDecoder
import net.runelite.asm.ClassGroup
import net.runelite.deob.Deobfuscator

/**
 * These should all be found using analysis, but until then, we define them manually
 * Must be disabled or fixed between revisions
 */
class HARDCODED : Deobfuscator {
    companion object {
        var ran = false
    }
    override fun run(group: ClassGroup) {
        eventMouseMove()
        eventKeyboard()
        moveGameClick()
        ran = true
    }

    private fun eventMouseMove() {
        val eventMouseMove = PacketDecoder.getClientPacket("field2498")!!
        eventMouseMove.name = "EVENT_MOUSE_CLICK"
        eventMouseMove.structure.add(ObfuscatedBufferStructure("writeShort", "mouseInfo"))
        eventMouseMove.structure.add(ObfuscatedBufferStructure("writeShort", "x"))
        eventMouseMove.structure.add(ObfuscatedBufferStructure("writeShort", "y"))
    }

    private fun eventKeyboard() {
        val eventKeyboard = PacketDecoder.getClientPacket("field2432")!!
        eventKeyboard.name = "EVENT_KEYBOARD"
        eventKeyboard.structure.add(ObfuscatedBufferStructure("writeMedium", "time"))
        eventKeyboard.structure.add(ObfuscatedBufferStructure("writeByte", "keyPressed"))
    }

    private fun moveGameClick() {
        val eventKeyboard = PacketDecoder.getClientPacket("field2467")!!
        eventKeyboard.name = "MOVE_GAMECLICK"
        eventKeyboard.structure.add(ObfuscatedBufferStructure("writeByte", "_five_"))
        eventKeyboard.structure.add(ObfuscatedBufferStructure("method8580", "ctrlPressed"))
        eventKeyboard.structure.add(ObfuscatedBufferStructure("method8607", "worldX"))
        eventKeyboard.structure.add(ObfuscatedBufferStructure("writeIntME", "worldY"))
    }
}