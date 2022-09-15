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
package meteor.deobfuscators

import meteor.PacketDecoder
import net.runelite.asm.ClassGroup
import net.runelite.asm.Method
import net.runelite.asm.attributes.code.Instruction
import net.runelite.asm.attributes.code.instructions.BiPush
import net.runelite.asm.attributes.code.instructions.GetStatic
import net.runelite.asm.attributes.code.instructions.ILoad
import net.runelite.asm.attributes.code.instructions.InvokeVirtual
import net.runelite.asm.attributes.code.instructions.LDC
import net.runelite.deob.Deobfuscator
import net.runelite.deob.ObfuscatedBufferStructure
import net.runelite.deob.ObfuscatedClientPacket
import java.lang.Exception

class IF_BUTTON : Deobfuscator {
    override fun run(group: ClassGroup) {

        var skipped = 0
        var currentIfOpcode = 1
        var currentObfuscatedStep = 0
        var currentPacket: ObfuscatedClientPacket? = null
        var currentObfuscatedStructure: ArrayList<ObfuscatedBufferStructure>? = ArrayList()
        var importantLines = false
        var ifButtonHandlerMethod: Method? = null

        // find IF handler method
        for (cf in group.classes) {
            for (m in cf.methods) {
                if (m.code == null)
                    continue
                val instructionsIterator: MutableIterator<Instruction> = m.code.instructions.iterator()
                var currentInstruction: Instruction = instructionsIterator.next()
                var maxIfOpcode = 0
                var usesClientPackets = false
                while (instructionsIterator.hasNext()) {
                    try {
                        when (currentInstruction) {
                            is ILoad -> {
                                when (currentInstruction.variableIndex) {
                                    0 -> {
                                        if (skipped != 2) {
                                            skipped++
                                        } else {
                                            importantLines = true
                                        }

                                    }
                                    1 -> {
                                        if (importantLines) {
                                            currentObfuscatedStep = 1
                                        }

                                    }
                                    2 -> {
                                        if (importantLines) {
                                            currentObfuscatedStep = 2
                                        }
                                    }
                                    3 -> {
                                        if (importantLines) {
                                            currentObfuscatedStep = 3
                                        }
                                    }
                                }
                            }
                            is LDC -> {
                                if (importantLines) {
                                    maxIfOpcode = currentInstruction.toString().split(" ")[1].toInt()
                                }
                            }
                            is BiPush -> {
                                if (importantLines) {
                                    maxIfOpcode = currentInstruction.toString().split(" ")[1].toInt()
                                }
                            }
                            is GetStatic -> {
                                if (importantLines) {
                                    if (!currentInstruction.toString().contains("PacketWriter")) {
                                        val fieldName = currentInstruction.toString().split("ClientPacket.")[1].split(" ")[0]
                                        usesClientPackets = true
                                    }
                                }

                            }
                        }
                    } catch (_: Exception) {

                    }
                    currentInstruction = instructionsIterator.next()
                }
                if (maxIfOpcode == 10 && usesClientPackets)
                    ifButtonHandlerMethod = m
            }
        }

        skipped = 0
        importantLines = false

        // deobfuscate name/structure

        val instructionsIterator: MutableIterator<Instruction> = ifButtonHandlerMethod!!.code.instructions.iterator()
        var currentInstruction: Instruction = instructionsIterator.next()

        while (instructionsIterator.hasNext()) {
            when (currentInstruction) {
                is ILoad -> {
                    when (currentInstruction.variableIndex) {
                        0 -> {
                            if (skipped != 2) {
                                skipped++
                            } else {
                                importantLines = true
                            }

                        }
                        1 -> {
                            if (importantLines) {
                                currentObfuscatedStep = 1
                            }

                        }
                        2 -> {
                            if (importantLines) {
                                currentObfuscatedStep = 2
                            }
                        }
                        3 -> {
                            if (importantLines) {
                                currentObfuscatedStep = 3
                            }
                        }
                    }
                }
                is LDC -> {
                    if (importantLines) {
                        currentIfOpcode = currentInstruction.toString().split(" ")[1].toInt()
                    }
                }
                is BiPush -> {
                    if (importantLines) {
                        currentIfOpcode = currentInstruction.toString().split(" ")[1].toInt()
                    }
                }
                is GetStatic -> {
                    if (importantLines) {
                        if (!currentInstruction.toString().contains("PacketWriter")) {
                            val fieldName = currentInstruction.toString().split("ClientPacket.")[1].split(" ")[0]
                            currentPacket = PacketDecoder.getClientPacket(fieldName)!!
                            currentPacket.deobname = fieldName
                        }

                        if (currentObfuscatedStep == 3) {
                            if (currentPacket != null) {
                                currentPacket.name = "IF_BUTTON$currentIfOpcode"
                                currentPacket.structure = currentObfuscatedStructure!!
                                currentObfuscatedStructure = ArrayList()
                            }
                            currentObfuscatedStep = 0
                        }
                    }

                }
                is InvokeVirtual -> {
                    if (importantLines) {
                        if (!currentInstruction.toString().contains("addNode")) {
                            val type = currentInstruction.toString().split("PacketBuffer.write")[1].split("(I)V")[0]
                            var argument = ""
                            when (currentObfuscatedStep) {
                                1 -> argument = "param1"
                                2 -> argument = "param0"
                                3 -> argument = "itemId"
                            }
                            val structure = ObfuscatedBufferStructure(type, argument)
                            currentObfuscatedStructure!!.add(structure)
                        }
                    }
                }
            }

            currentInstruction = instructionsIterator.next()
        }
    }
}