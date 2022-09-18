package meteor.deobfuscators

import meteor.BufferMethod
import meteor.PacketDecoder
import net.runelite.asm.ClassGroup
import net.runelite.asm.attributes.code.Instruction
import net.runelite.asm.attributes.code.instructions.BiPush
import net.runelite.asm.attributes.code.instructions.LDC
import net.runelite.deob.Deobfuscator

class Buffer : Deobfuscator {

    override fun run(group: ClassGroup) {
        for (clazz in group.classes) {
            for (method in clazz.methods) {
                var ldcs = 0
                var bipushes = 0
                var firstBiPush = -1
                var secondBiPush = -1
                var thirdBiPush = -1
                var firstBiPushPosition = -1

                if (method.code == null)
                    continue
                val instructionsIterator: MutableIterator<Instruction> = method.code.instructions.iterator()
                var currentInstruction: Instruction = instructionsIterator.next()
                var position = 0
                while (instructionsIterator.hasNext()) {
                    when (currentInstruction) {
                        is LDC -> {
                            ldcs++
                        }
                        is BiPush -> {
                            if (firstBiPush == -1) {
                                if (currentInstruction.operand == 8) {
                                    firstBiPushPosition = position
                                }
                                firstBiPush = currentInstruction.operand
                            } else if (secondBiPush == -1) {
                                secondBiPush = currentInstruction.operand
                            } else if (thirdBiPush == -1) {
                                thirdBiPush = currentInstruction.operand
                            }
                            bipushes++
                        }
                    }
/*                    if (method.name == "readInt") {
                        println(currentInstruction)
                    }*/

                    position++
                    currentInstruction = instructionsIterator.next()
                }
/*                if (method.name == "readInt") {
                    println("ldcs $ldcs bipushes $bipushes")
                }*/
                if (ldcs == 5 && bipushes == 3 && firstBiPush == 16 && secondBiPush == 8 && thirdBiPush == 24)
                    PacketDecoder.bufferMethods.add(BufferMethod("readInt", method.name))
                if (ldcs == 5 && bipushes == 3 && firstBiPush == 8 && secondBiPush == 16 && thirdBiPush == 24 && firstBiPushPosition == 26)
                    PacketDecoder.bufferMethods.add(BufferMethod("readIntLE", method.name))
                if (ldcs == 5 && bipushes == 3 && firstBiPush == 24 && secondBiPush == 8 && thirdBiPush == 16)
                    PacketDecoder.bufferMethods.add(BufferMethod("readIntIME", method.name))
                if (ldcs == 5 && bipushes == 3 && firstBiPush == 8 && secondBiPush == 16 && thirdBiPush == 24 && firstBiPushPosition == 17)
                    PacketDecoder.bufferMethods.add(BufferMethod("readIntME", method.name))
                if (ldcs == 8 && bipushes == 3 && firstBiPush == 24 && secondBiPush == 16 && thirdBiPush == 8)
                    PacketDecoder.bufferMethods.add(BufferMethod("writeInt", method.name))
                if (ldcs == 8 && bipushes == 3 && firstBiPush == 8 && secondBiPush == 16 && thirdBiPush == 24)
                    PacketDecoder.bufferMethods.add(BufferMethod("writeIntLE", method.name))
                if (ldcs == 8 && bipushes == 3 && firstBiPush == 8 && secondBiPush == 24 && thirdBiPush == 16)
                    PacketDecoder.bufferMethods.add(BufferMethod("writeIntIME", method.name))
                if (ldcs == 8 && bipushes == 3 && firstBiPush == 16 && secondBiPush == 24 && thirdBiPush == 8)
                    PacketDecoder.bufferMethods.add(BufferMethod("writeIntME", method.name))
            }
        }
    }
}