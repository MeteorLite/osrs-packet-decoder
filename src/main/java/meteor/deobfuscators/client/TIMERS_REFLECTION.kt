/*
 * Copyright (c) 2016-2017, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package meteor.deobfuscators.client

import meteor.PacketDecoder
import net.runelite.asm.ClassGroup
import net.runelite.asm.attributes.code.Instruction
import net.runelite.asm.attributes.code.instructions.GetStatic
import net.runelite.asm.attributes.code.instructions.InvokeVirtual
import net.runelite.deob.Deobfuscator
import meteor.ObfuscatedBufferStructure
import meteor.ObfuscatedClientPacket
import net.runelite.asm.attributes.code.instructions.InvokeStatic

/**
 * These reside in doCycleLoggedIn and can be moved around in the method.
 * We find them first so we can ignore them on the actual doCycleLoggedIn deob
 */
class TIMERS_REFLECTION : Deobfuscator {
    var currentStructure: ArrayList<ObfuscatedBufferStructure> = ArrayList()
    var found = 0
    var currentPacket: ObfuscatedClientPacket? = null
    var currentStructureEncodingMethodName = ""

    var hasReflectionCheck = false
    var hasParam1 = false
    var hasId = false
    var hasItemId = false

    var mappedPackets = ArrayList<String>()

    fun resetHas() {
        hasReflectionCheck = false
        hasParam1 = false
        hasId = false
        hasItemId = false
    }

    /**
     * TODO: this is hardcoded and could change, we should actually analyze Timer.write()
     */
    fun createTimerStructure() {
        currentStructure.add(
            ObfuscatedBufferStructure(
                "writeShort",
                "time1"
            )
        )
        currentStructure.add(
            ObfuscatedBufferStructure(
                "writeShort",
                "time2"
            )
        )
        currentStructure.add(
            ObfuscatedBufferStructure(
                "writeShort",
                "time3"
            )
        )
        currentStructure.add(
            ObfuscatedBufferStructure(
                "writeShort",
                "time4"
            )
        )
    }

    override fun run(group: ClassGroup) {
        for (cf in group.classes) {
            for (m in cf.methods) {
                if (m.code == null)
                    continue

                if (m.isFinal)
                    if (m.name == "doCycleLoggedIn") {
                        val instructionsIterator: MutableIterator<Instruction> = m.code.instructions.iterator()
                        var currentInstruction: Instruction = instructionsIterator.next()

                        while (instructionsIterator.hasNext()) {
                            when (currentInstruction) {
                                is InvokeVirtual -> {
                                    if (currentInstruction.toString().contains("Timer.write")) {
                                        currentPacket!!.name = "LOGIN_STATISTICS"
                                        createTimerStructure()
                                    }
                                    if (currentInstruction.toString().contains("PacketBuffer.")) {
                                        currentPacket?.let {
                                            when (it.name) {
                                                "REFLECTION_CHECK_REPLY" -> {
                                                    currentStructure.add(
                                                        ObfuscatedBufferStructure(
                                                            currentStructureEncodingMethodName,
                                                            "response"
                                                        )
                                                    )
                                                }
                                                else -> {}
                                            }
                                        }
                                        currentStructureEncodingMethodName =
                                            currentInstruction.toString().split("PacketBuffer.")[1].split("(")[0]
                                        resetHas()
                                    }
                                }
                                is InvokeStatic -> {
                                    if (currentInstruction.toString().contains("performReflectionCheck")) {
                                        hasReflectionCheck = true
                                        currentPacket!!.name = "REFLECTION_CHECK_REPLY"
                                    }
                                }
                                is GetStatic -> {
                                    val fieldInfo = currentInstruction.field
                                    var fieldName = ""
                                    if (fieldInfo.toString().contains("ClientPacket.")) {
                                        fieldName = fieldInfo.toString().split("ClientPacket.")[1].split(" ")[0]
                                        if (!mappedPackets.contains(fieldName)) {
                                            mappedPackets.add(fieldName)
                                            currentPacket?.let {
                                                it.structure = currentStructure
                                                currentStructure = ArrayList()
                                            }
                                            currentPacket = PacketDecoder.getClientPacket(fieldName)
                                            currentPacket?.let {
                                                it.deobname = fieldName
                                            }
                                        }
                                    }
                                    resetHas()
                                }
                            }
                            currentInstruction = instructionsIterator.next()
                        }
                        break
                    }
            }
        }
    }
}