package meteor.deobfuscators

import meteor.BufferMethod
import meteor.PacketDecoder
import net.runelite.asm.ClassGroup
import net.runelite.asm.attributes.code.Instruction
import net.runelite.asm.attributes.code.instructions.ALoad
import net.runelite.asm.attributes.code.instructions.BiPush
import net.runelite.asm.attributes.code.instructions.ISub
import net.runelite.asm.attributes.code.instructions.LDC
import net.runelite.asm.attributes.code.instructions.SiPush
import net.runelite.deob.Deobfuscator

class Buffer : Deobfuscator {

    override fun run(group: ClassGroup) {
        for (clazz in group.classes) {
            if (clazz.name == "Buffer")
                for (method in clazz.methods) {
                    var ldcs = 0
                    var bipushes = 0
                    var firstBiPush = -1
                    var secondBiPush = -1
                    var thirdBiPush = -1
                    var firstBiPushPosition = -1
                    var firstSiPush = -1
                    var secondSiPush = -1
                    var thirdSiPush = -1
                    var firstSiPushPosition = -1
                    var secondSiPushPosition = -1
                    var thirdSiPushPosition = -1
                    var firstISubPosition = -1
                    var firstISub = -1
                    var secondISub = -1
                    var thirdISub = -1
                    var secondISubPosition = -1
                    var thirdISubPosition = -1
                    var siPushes = 0
                    var iSubs = 0
                    var aLoads = 0
                    var debugFieldName = ""

                    if (method.code == null)
                        continue

                    var instructionsLength = method.code.instructions.size()
                    val instructionsIterator: MutableIterator<Instruction> = method.code.instructions.iterator()
                    var currentInstruction: Instruction = instructionsIterator.next()
                    var position = 0
                    while (instructionsIterator.hasNext()) {
                        when (currentInstruction) {
                            is LDC -> {
                                ldcs++
                            }
                            is ALoad -> {
                                aLoads++
                            }
                            is ISub -> {
                                if (firstISub == -1) {
                                    firstISub = 0
                                    firstISubPosition = position
                                } else if (secondISub == -1) {
                                    secondISub = 0
                                    secondISubPosition = position
                                }
                                iSubs++
                            }
                            is SiPush -> {
                                if (firstSiPush == -1) {
                                    firstSiPushPosition = position
                                    firstSiPush = currentInstruction.operand
                                } else if (secondSiPush == -1) {
                                    secondSiPushPosition = position
                                    secondSiPush = currentInstruction.operand
                                } else if (thirdSiPush == -1) {
                                    thirdSiPushPosition = position
                                    thirdSiPush = currentInstruction.operand
                                }
                                siPushes++
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
                        if (debugFieldName.isNotEmpty() && method.name == debugFieldName) {
                            println(currentInstruction)
                        }

                        position++
                        currentInstruction = instructionsIterator.next()
                    }
                    if (debugFieldName.isNotEmpty() && method.name == debugFieldName) {
                        if (firstISubPosition != -1)
                            println("firstIsub $firstISubPosition")
                        if (secondISubPosition != -1)
                            println("secondIsub $secondISubPosition")
                        if (firstBiPush != -1)
                            println("firstBiPush $firstBiPushPosition")
                        if (firstSiPushPosition != -1)
                            println("firstSiPush $firstSiPushPosition")
                        if (secondSiPushPosition != -1)
                            println("secondSiPush $secondSiPushPosition")
                        println("ldcs $ldcs bipushes $bipushes siPushes $siPushes isubs $iSubs aloads $aLoads instructions ${instructionsLength}")
                    }
                    if (ldcs == 2 && bipushes == 0 && siPushes == 2 && firstSiPushPosition == 13 && secondSiPushPosition == 15)
                        PacketDecoder.bufferMethods.add(BufferMethod("readUByteAdd", method.name))
                    if (ldcs == 3 && bipushes == 0 && siPushes == 1 && firstSiPushPosition == 15)
                        PacketDecoder.bufferMethods.add(BufferMethod("readUByteNeg", method.name))
                    if (ldcs == 2 && bipushes == 0 && siPushes == 2 && iSubs == 2 && firstISubPosition == 12 && secondISubPosition == 14)
                        PacketDecoder.bufferMethods.add(BufferMethod("readUByteSub", method.name))
                    if (ldcs == 2 && bipushes == 0 && siPushes == 1 && firstSiPushPosition == 13 && iSubs == 2)
                        PacketDecoder.bufferMethods.add(BufferMethod("readByteAdd", method.name))
                    if (ldcs == 3 && bipushes == 0 && siPushes == 0 && iSubs == 2 && firstISubPosition == 12 && secondISubPosition == 14)
                        PacketDecoder.bufferMethods.add(BufferMethod("readByteNeg", method.name))
                    if (ldcs == 2 && bipushes == 0 && siPushes == 1 && iSubs == 2 && firstISubPosition == 12 && secondISubPosition == 14)
                        PacketDecoder.bufferMethods.add(BufferMethod("readByteSub", method.name))
                    if (ldcs == 2 && bipushes == 0 && siPushes == 1 && iSubs == 1 && instructionsLength == 17)
                        PacketDecoder.bufferMethods.add(BufferMethod("readUnsignedByte", method.name))
                    if (ldcs == 2 && bipushes == 0 && siPushes == 0 && iSubs == 1 && firstISubPosition == 11 && instructionsLength == 15)
                        PacketDecoder.bufferMethods.add(BufferMethod("readByte", method.name))
                    if (ldcs == 3 && bipushes == 1 && siPushes == 2 && iSubs == 2 && firstBiPushPosition == 26)
                        PacketDecoder.bufferMethods.add(BufferMethod("readUnsignedShort", method.name))
                    if (ldcs == 4 && bipushes == 1 && siPushes == 3 && iSubs == 3)
                        PacketDecoder.bufferMethods.add(BufferMethod("readShort", method.name))
                    if (ldcs == 4 && bipushes == 0 && siPushes == 0 && iSubs == 3)
                        PacketDecoder.bufferMethods.add(BufferMethod("readString", method.name))
                    if (ldcs == 1 && bipushes == 1 && siPushes == 2 && iSubs == 2)
                        PacketDecoder.bufferMethods.add(BufferMethod("readShortSmart", method.name))
                    if (ldcs == 1 && bipushes == 0 && siPushes == 2 && iSubs == 1)
                        PacketDecoder.bufferMethods.add(BufferMethod("readUShortSmart", method.name))
                    if (ldcs == 2 && bipushes == 0 && siPushes == 0 && iSubs == 1 && firstISubPosition == 11 && instructionsLength != 15)
                        PacketDecoder.bufferMethods.add(BufferMethod("writeByte", method.name))
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
                    if (ldcs == 4 && bipushes == 1 && siPushes == 0 && iSubs == 2 && firstISubPosition == 11 && secondISubPosition == 26)
                        PacketDecoder.bufferMethods.add(BufferMethod("writeShortLE", method.name))
                    if (ldcs == 4 && bipushes == 1 && siPushes == 1 && iSubs == 2 && firstISubPosition == 11 && secondISubPosition == 28 && firstBiPushPosition == 13)
                        PacketDecoder.bufferMethods.add(BufferMethod("writeShortAdd", method.name))
                    if (ldcs == 4 && bipushes == 1 && siPushes == 1 && iSubs == 2 && firstISubPosition == 11 && secondISubPosition == 28 && firstBiPushPosition == 30)
                        PacketDecoder.bufferMethods.add(BufferMethod("writeShortAddLE", method.name))
                    if (ldcs == 3 && bipushes == 1 && siPushes == 2 && iSubs == 2 && firstISubPosition == 13 && secondISubPosition == 24)
                        PacketDecoder.bufferMethods.add(BufferMethod("readUShortLE", method.name))
                    if (ldcs == 3 && bipushes == 1 && siPushes == 3 && iSubs == 3 && firstISubPosition == 13 && secondISubPosition == 16)
                        PacketDecoder.bufferMethods.add(BufferMethod("readUShortAdd", method.name))
                    if (ldcs == 3 && bipushes == 1 && siPushes == 3 && iSubs == 3 && firstISubPosition == 13 && secondISubPosition == 24)
                        PacketDecoder.bufferMethods.add(BufferMethod("readShortAddLE", method.name))
                    if (ldcs == 4 && bipushes == 1 && siPushes == 4 && iSubs == 4 && firstISubPosition == 13 && secondISubPosition == 24)
                        PacketDecoder.bufferMethods.add(BufferMethod("readShortLE", method.name))
                    if (ldcs == 16 && bipushes == 7 && siPushes == 0 && iSubs == 8)
                        PacketDecoder.bufferMethods.add(BufferMethod("writeLong", method.name))
                    if (ldcs == 12 && bipushes == 5 && siPushes == 0 && iSubs == 6)
                        PacketDecoder.bufferMethods.add(BufferMethod("writeLongMedium", method.name))
                    if (ldcs == 4 && bipushes == 1 && siPushes == 0 && iSubs == 2 && firstBiPushPosition == 13)
                        PacketDecoder.bufferMethods.add(BufferMethod("writeShort", method.name))
                    if (ldcs == 2 && bipushes == 0 && siPushes == 1 && iSubs == 1 && instructionsLength == 20)
                        PacketDecoder.bufferMethods.add(BufferMethod("writeByteAdd", method.name))
                    if (ldcs == 2 && bipushes == 0 && siPushes == 1 && iSubs == 2 && instructionsLength == 20)
                        PacketDecoder.bufferMethods.add(BufferMethod("writeByteSub", method.name))
                    if (ldcs == 3 && bipushes == 0 && siPushes == 0 && iSubs == 2 && instructionsLength == 20)
                        PacketDecoder.bufferMethods.add(BufferMethod("writeByteNeg", method.name))
                    if (ldcs == 6 && bipushes == 0 && siPushes == 0 && iSubs == 1 && instructionsLength == 46)
                        PacketDecoder.bufferMethods.add(BufferMethod("writeString", method.name))
                }
        }
    }
}