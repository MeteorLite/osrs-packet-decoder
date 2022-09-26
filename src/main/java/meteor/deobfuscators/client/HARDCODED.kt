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
    }

    fun eventMouseMove() {
        val eventMouseMove = PacketDecoder.getClientPacket("field3039")!!
        eventMouseMove.name = "EVENT_MOUSE_CLICK"
        eventMouseMove.structure.add(ObfuscatedBufferStructure("writeShort", "mouseInfo"))
        eventMouseMove.structure.add(ObfuscatedBufferStructure("writeShort", "x"))
        eventMouseMove.structure.add(ObfuscatedBufferStructure("writeShort", "y"))
    }

    fun eventKeyboard() {
        val eventKeyboard = PacketDecoder.getClientPacket("field3023")!!
        eventKeyboard.name = "EVENT_KEYBOARD"
        eventKeyboard.structure.add(ObfuscatedBufferStructure("method7983", "time"))
        eventKeyboard.structure.add(ObfuscatedBufferStructure("method8136", "keyPressed"))
    }
}