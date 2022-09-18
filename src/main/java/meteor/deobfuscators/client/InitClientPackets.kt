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
import net.runelite.asm.attributes.code.instructions.Dup
import net.runelite.asm.attributes.code.instructions.PutStatic
import net.runelite.deob.Deobfuscator
import meteor.ObfuscatedClientPacket

class InitClientPackets : Deobfuscator {
    override fun run(group: ClassGroup) {

        var count = 0

        for (cf in group.classes) {
            if (cf.name.contains("/"))
                continue

            if (cf.name == "ClientPacket") {
                for (method in cf.methods) {
                    if (method.name == "<clinit>") {
                        val instructionsIterator: MutableIterator<Instruction> = method.code.instructions.iterator()
                        var currentInstruction: Instruction = instructionsIterator.next()
                        var lastClientPacketFieldName = ""
                        var opcode = -1
                        var size = -1
                        while (instructionsIterator.hasNext()) {
                            if (currentInstruction is PutStatic) {
                                lastClientPacketFieldName = currentInstruction.toString()
                                    .split("ClientPacket.")[1]
                                    .split(" ")[0]
                            }
                            if (currentInstruction is Dup) {

                                val opcodeInstruction = instructionsIterator.next()
                                opcode = opcodeInstruction.toString().split(" ")[1].toInt()

                                val sizeInstruction = instructionsIterator.next()
                                size = sizeInstruction.toString().split(" ")[1].toInt()
                            }

                            if (lastClientPacketFieldName.isNotEmpty()) {
                                PacketDecoder.clientPackets[opcode] =
                                    ObfuscatedClientPacket(lastClientPacketFieldName, size)
                            }

                            currentInstruction = instructionsIterator.next()
                        }
                    }
                }
            }
        }
    }
}