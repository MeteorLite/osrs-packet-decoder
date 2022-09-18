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
import net.runelite.asm.attributes.code.instructions.ILoad
import net.runelite.asm.attributes.code.instructions.InvokeVirtual
import net.runelite.asm.attributes.code.instructions.LDC
import net.runelite.deob.Deobfuscator
import meteor.ObfuscatedBufferStructure
import meteor.ObfuscatedClientPacket

class MENU_ACTION : Deobfuscator {
    var currentStructure: ArrayList<ObfuscatedBufferStructure> = ArrayList()
    var found = 0
    var currentPacket: ObfuscatedClientPacket? = null
    var currentStructureFieldName = ""
    var currentStructureEncodingMethodName = ""

    var hasParam0 = false
    var hasParam1 = false
    var hasId = false
    var hasItemId = false

    var mappedPackets = ArrayList<String>()

    fun resetHas() {
        hasParam0 = false
        hasParam1 = false
        hasId = false
        hasItemId = false
    }

    override fun run(group: ClassGroup) {
        for (cf in group.classes) {
            for (m in cf.methods) {
                if (m.code == null)
                    continue

                if (m.isStatic)
                    if (m.name == "menuAction") {
                        val instructionsIterator: MutableIterator<Instruction> = m.code.instructions.iterator()
                        var currentInstruction: Instruction = instructionsIterator.next()
                        var toPrint = 0

                        while (instructionsIterator.hasNext()) {
                            when (currentInstruction) {
                                is InvokeVirtual -> {
                                    if (currentInstruction.toString().contains("PacketBuffer.")) {
                                        currentStructureEncodingMethodName =
                                            currentInstruction.toString().split("PacketBuffer.")[1].split("(")[0]
                                        if (hasId) {
                                            currentStructure.add(
                                                ObfuscatedBufferStructure(
                                                    currentStructureEncodingMethodName,
                                                    "id"
                                                )
                                            )
                                        } else if (hasItemId) {
                                            currentStructure.add(
                                                ObfuscatedBufferStructure(
                                                    currentStructureEncodingMethodName,
                                                    "itemId"
                                                )
                                            )
                                        } else if (hasParam0) {
                                            currentStructure.add(
                                                ObfuscatedBufferStructure(
                                                    currentStructureEncodingMethodName,
                                                    "param0"
                                                )
                                            )
                                        } else if (hasParam1) {
                                            currentStructure.add(
                                                ObfuscatedBufferStructure(
                                                    currentStructureEncodingMethodName,
                                                    "param1"
                                                )
                                            )
                                        } else {
                                            currentStructure.add(
                                                ObfuscatedBufferStructure(
                                                    currentStructureEncodingMethodName,
                                                    currentStructureFieldName
                                                )
                                            )
                                        }

                                        resetHas()
                                    }
                                }

                                is GetStatic -> {
                                    val fieldInfo = currentInstruction.field
                                    var fieldName = ""
                                    if (fieldInfo.toString().contains("ClientPacket.")) {
                                        fieldName = fieldInfo.toString().split("ClientPacket.")[1].split(" ")[0]
                                        if (!mappedPackets.contains(fieldName)) {
                                            mappedPackets.add(fieldName)
                                            currentPacket = PacketDecoder.getClientPacket(fieldName)
                                            currentPacket?.let {
                                                it.deobname = fieldName
                                                when (found) {
                                                    0 -> it.name = "OPLOCU"
                                                    1 -> it.name = "OPLOCT"
                                                    2 -> it.name = "OPLOC1"
                                                    3 -> it.name = "OPLOC2"
                                                    4 -> it.name = "OPLOC3"
                                                    5 -> it.name = "OPLOC4"
                                                    6 -> it.name = "OPNPCU"
                                                    7 -> it.name = "OPNPCT"
                                                    8 -> it.name = "OPNPC1"
                                                    9 -> it.name = "OPNPC2"
                                                    10 -> it.name = "OPNPC3"
                                                    11 -> it.name = "OPNPC4"
                                                    12 -> it.name = "OPNPC5"
                                                    13 -> it.name = "OPPLAYERU"
                                                    14 -> it.name = "OPPLAYERT"
                                                    15 -> it.name = "OPOBJU"
                                                    16 -> it.name = "OPOBJT"
                                                    17 -> it.name = "OPOBJ1"
                                                    18 -> it.name = "OPOBJ2"
                                                    19 -> it.name = "OPOBJ3"
                                                    20 -> it.name = "OPOBJ4"
                                                    21 -> it.name = "OPOBJ5"
                                                    22 -> it.name = "BUTTON_CLICK"
                                                    25 -> it.name = "OPHELDU"
                                                    26 -> it.name = "OPHELDT"
                                                    27 -> it.name = "OPHELD1"
                                                    28 -> it.name = "OPHELD2"
                                                    29 -> it.name = "OPHELD3"
                                                    30 -> it.name = "OPHELD4"
                                                    31 -> it.name = "OPHELD5"
                                                    32 -> it.name = "IF1_BUTTON1"
                                                    33 -> it.name = "IF1_BUTTON2"
                                                    34 -> it.name = "IF1_BUTTON3"
                                                    35 -> it.name = "IF1_BUTTON4"
                                                    36 -> it.name = "IF1_BUTTON5"
                                                    37 -> it.name = "OPPLAYER1"
                                                    38 -> it.name = "OPPLAYER2"
                                                    39 -> it.name = "OPPLAYER3"
                                                    40 -> it.name = "OPPLAYER4"
                                                    41 -> it.name = "OPPLAYER5"
                                                    42 -> it.name = "OPPLAYER6"
                                                    43 -> it.name = "OPPLAYER7"
                                                    44 -> it.name = "OPPLAYER8"
                                                    45 -> it.name = "IF_BUTTONT"
                                                    46 -> it.name = "OPLOC5"
                                                    47 -> it.name = "OPLOC6"
                                                    48 -> it.name = "OPNPC6"
                                                    49 -> it.name = "OPOBJ6"
                                                }
                                                when (it.name) {
                                                    "BUTTON_CLICK" -> found += 3
                                                    "OPOBJ6" -> found += 2
                                                    else -> found++
                                                }
                                            }
                                        }
                                    }
                                    resetHas()
                                    if (fieldInfo.toString().contains(".selectedItemId "))
                                        currentStructureFieldName = "selectedItemId"
                                    else if (fieldInfo.toString().contains(".selectedItemWidget "))
                                        currentStructureFieldName = "selectedItemWidget"
                                    else if (fieldInfo.toString().contains(".selectedItemSlot "))
                                        currentStructureFieldName = "selectedItemSlot"
                                    else if (fieldInfo.toString().contains(".selectedSpellChildIndex "))
                                        currentStructureFieldName = "selectedSpellChildIndex"
                                    else if (fieldInfo.toString().contains(".selectedSpellWidget "))
                                        currentStructureFieldName = "selectedSpellWidget"
                                    else if (fieldInfo.toString().contains(".selectedSpellItemId "))
                                        currentStructureFieldName = "selectedSpellItemId"
                                    else if (fieldInfo.toString().contains(".baseY "))
                                        currentStructureFieldName = "worldY"
                                    else if (fieldInfo.toString().contains(".baseX "))
                                        currentStructureFieldName = "worldX"
                                    else if (fieldInfo.toString().contains("KeyHandler_pressedKeys"))
                                        currentStructureFieldName = "shiftPressed"
                                }

                                is ILoad -> {
                                    val paramater = currentInstruction.variableIndex
                                    if (paramater == 2) {
                                        if (currentStructure.size != 0) {
                                            currentPacket!!.structure = currentStructure
                                            currentStructure = ArrayList()
                                        }
                                    } else if (paramater == 1) {
                                        resetHas()
                                        hasParam1 = true
                                    } else if (paramater == 0) {
                                        resetHas()
                                        hasParam0 = true
                                    } else if (paramater == 3) {
                                        resetHas()
                                        hasId = true
                                    } else if (paramater == 4) {
                                        resetHas()
                                        hasItemId = true
                                    }
                                }

                                is LDC -> {
                                    try {
                                        val constant = currentInstruction.constantAsInt
                                        when (constant) {
                                            1 -> {

                                            }
                                        }
                                    } catch (_: Exception) {
                                    }
                                }
                            }
                            if (toPrint < 50) {
                                toPrint++
                            }
                            currentInstruction = instructionsIterator.next()
                        }
                        break
                    }
            }
        }
    }
}