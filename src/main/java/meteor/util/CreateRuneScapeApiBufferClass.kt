package meteor.util

import meteor.PacketDecoder
import java.io.File

object RuneScapeApiBufferClass {
    val classHeader = "package net.runelite.rs.api;\n" +
            "\n" +
            "import net.runelite.api.Buffer;\n" +
            "import net.runelite.mapping.Import;\n" +
            "\n" +
            "import static net.runelite.rs.api.PacketFields.*;\n" +
            "\n" +
            "public interface RSBuffer extends Buffer, RSNode\n" +
            "{\n" +
            "@Import(\"array\")\n" +
            "byte[] getPayload();\n" +
            "\n" +
            "@Import(\"offset\")\n" +
            "int getOffset();\n" +
            "\n" +
            "@Import(\"offset\")\n" +
            "void setOffset(int offset);"

    val classFooter = "}"


    fun create() {
        var classText = RuneLiteApiBufferClass.classHeader
        classText += "\n"
        classText += "@Import(\"${getDeobName("writeInt")}\")\n"
        classText += "@Override\n"
        classText += "void writeInt(int i);\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("writeIntLE")}\")\n"
        classText += "@Override\n"
        classText += "void writeIntLE(int i);\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("writeIntIME")}\")\n"
        classText += "@Override\n"
        classText += "void writeIntIME(int i);\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("writeIntME")}\")\n"
        classText += "@Override\n"
        classText += "void writeIntME(int i);\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readInt")}\")\n"
        classText += "@Override\n"
        classText += "int readInt();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readIntLE")}\")\n"
        classText += "@Override\n"
        classText += "int readInt();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readIntIME")}\")\n"
        classText += "@Override\n"
        classText += "int readInt();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readIntME")}\")\n"
        classText += "@Override\n"
        classText += "int readInt();\n"
        classText += "\n"
        classText += classFooter

        File("./data/RSBuffer.java").writeText(classText)
    }

    fun getDeobName(name: String) : String? {
        for (bufferMethod in PacketDecoder.bufferMethods) {
            if (bufferMethod.method == name)
                return bufferMethod.deobName
        }
        return null
    }
}