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
    override fun run(group: ClassGroup) {
        eventMouseMove()
        eventKeyboard()
        moveGameClick()
    }

    fun eventMouseMove() {
        val eventMouseMove = PacketDecoder.getClientPacket("field3051")!!
        eventMouseMove.name = "EVENT_MOUSE_CLICK"
        eventMouseMove.structure.add(ObfuscatedBufferStructure("writeShort", "mouseInfo"))
        eventMouseMove.structure.add(ObfuscatedBufferStructure("writeShort", "x"))
        eventMouseMove.structure.add(ObfuscatedBufferStructure("writeShort", "y"))
    }

    fun eventKeyboard() {
        val eventKeyboard = PacketDecoder.getClientPacket("field3004")!!
        eventKeyboard.name = "EVENT_KEYBOARD"
        eventKeyboard.structure.add(ObfuscatedBufferStructure("method8449", "time"))
        eventKeyboard.structure.add(ObfuscatedBufferStructure("method8434", "keyPressed"))
    }

    fun moveGameClick() {
        val eventKeyboard = PacketDecoder.getClientPacket("field3091")!!
        eventKeyboard.name = "MOVE_GAMECLICK"
        eventKeyboard.structure.add(ObfuscatedBufferStructure("writeByte", "_five_"))
        eventKeyboard.structure.add(ObfuscatedBufferStructure("method8443", "worldY"))
        eventKeyboard.structure.add(ObfuscatedBufferStructure("method8546", "ctrlPressed"))
        eventKeyboard.structure.add(ObfuscatedBufferStructure("method8619", "worldX"))
    }
}