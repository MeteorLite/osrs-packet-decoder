package meteor.util

import meteor.PacketDecoder
import java.io.File

object RuneLiteApiClientPacketsClass {
    val classHeader = "package net.runelite.api.packets;\n" +
            "\n" +
            "public interface ClientPacket\n" +
            "{\n" +
            "int getId();\n" +
            "int getLength();\n"

    val classFooter = "}"


    fun create() {
        var classText = classHeader
        for (opcode in PacketDecoder.clientPackets.keys) {
            val packet = PacketDecoder.clientPackets[opcode]
            if (!packet!!.name.startsWith("field")) {
                classText += "ClientPacket ${packet.name}();\n"
            }
        }
        classText += classFooter

        File("./data/api/ClientPacket.java").writeText(classText)
    }
}