package meteor.deobfuscators

import meteor.PacketDecoder
import java.io.File

object RuneScapeApiClientPacketsClass {
    val classHeader = "package net.runelite.rs.api;\n" +
            "\n" +
            "import net.runelite.api.packets.ClientPacket;\n" +
            "import net.runelite.mapping.Import;\n" +
            "\n" +
            "public interface RSClientPacket extends ClientPacket\n" +
            "{\n" +
            "@Import(\"id\")\n" +
            "@Override\n" +
            "int getId();\n" +
            "\n" +
            "@Import(\"length\")\n" +
            "@Override\n" +
            "int getLength();\n\n"

    val classFooter = "}"


    fun create() {
        var classText = classHeader
        for (opcode in PacketDecoder.clientPackets.keys) {
            val packet = PacketDecoder.clientPackets[opcode]
            if (!packet!!.name.startsWith("field")) {
                classText += "@Import(\"${packet.deobname}\")\n"
                classText += "@Override\n"
                classText += "RSClientPacket ${packet.name}();\n\n"
            }
        }
        classText += classFooter

        File("./data/RSClientPacket.java").writeText(classText)
    }
}