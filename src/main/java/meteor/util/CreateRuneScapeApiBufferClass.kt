package meteor.util

import meteor.PacketDecoder
import java.io.File

object RuneScapeApiBufferClass {
    val classHeader = "package net.runelite.rs.api;\n" +
            "\n" +
            "import net.runelite.api.Buffer;\n" +
            "import net.runelite.mapping.Import;\n" +
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
            "void setOffset(int offset);\n"

    val classFooter = "}"


    fun create() {
        var classText = classHeader
        classText += "\n"
        classText += "@Import(\"${getDeobName("readUByteAdd")}\")\n"
        classText += "@Override\n"
        classText += "int readUByteAdd();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readUByteNeg")}\")\n"
        classText += "@Override\n"
        classText += "int readUByteNeg();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readUByteSub")}\")\n"
        classText += "@Override\n"
        classText += "int readUByteSub();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readByteAdd")}\")\n"
        classText += "@Override\n"
        classText += "byte readByteAdd();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readByteNeg")}\")\n"
        classText += "@Override\n"
        classText += "byte readByteNeg();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readByteSub")}\")\n"
        classText += "@Override\n"
        classText += "byte readByteSub();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readUnsignedByte")}\")\n"
        classText += "@Override\n"
        classText += "int readUnsignedByte();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readByte")}\")\n"
        classText += "@Override\n"
        classText += "byte readByte();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readUnsignedShort")}\")\n"
        classText += "@Override\n"
        classText += "int readUnsignedShort();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readShort")}\")\n"
        classText += "@Override\n"
        classText += "int readShort();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readString")}\")\n"
        classText += "@Override\n"
        classText += "String readString();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readShortSmart")}\")\n"
        classText += "@Override\n"
        classText += "int readShortSmart();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readUShortSmart")}\")\n"
        classText += "@Override\n"
        classText += "int readUShortSmart();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("writeByte")}\")\n"
        classText += "@Override\n"
        classText += "void writeByte(int i);\n"
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
        classText += "@Import(\"${getDeobName("writeShortLE")}\")\n"
        classText += "@Override\n"
        classText += "void writeShortLE(int i);\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("writeShortAdd")}\")\n"
        classText += "@Override\n"
        classText += "void writeShortAdd(int i);\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("writeShortAddLE")}\")\n"
        classText += "@Override\n"
        classText += "void writeShortAddLE(int i);\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readUShortLE")}\")\n"
        classText += "@Override\n"
        classText += "int readUShortLE();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readUShortAdd")}\")\n"
        classText += "@Override\n"
        classText += "int readUShortAdd();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readShortAddLE")}\")\n"
        classText += "@Override\n"
        classText += "int readShortAddLE();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readShortLE")}\")\n"
        classText += "@Override\n"
        classText += "int readShortLE();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readInt")}\")\n"
        classText += "@Override\n"
        classText += "int readInt();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readIntLE")}\")\n"
        classText += "@Override\n"
        classText += "int readIntLE();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readIntIME")}\")\n"
        classText += "@Override\n"
        classText += "int readIntIME();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("readIntME")}\")\n"
        classText += "@Override\n"
        classText += "int readIntME();\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("writeLong")}\")\n"
        classText += "@Override\n"
        classText += "void writeLong(long l);\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("writeLongMedium")}\")\n"
        classText += "@Override\n"
        classText += "void writeLongMedium(long l);\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("writeShort")}\")\n"
        classText += "@Override\n"
        classText += "void writeShort(int i);\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("writeByteAdd")}\")\n"
        classText += "@Override\n"
        classText += "void writeByteAdd(int i);\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("writeByteSub")}\")\n"
        classText += "@Override\n"
        classText += "void writeByteSub(int i);\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("writeByteNeg")}\")\n"
        classText += "@Override\n"
        classText += "void writeByteNeg(int i);\n"
        classText += "\n"
        classText += "@Import(\"${getDeobName("writeString")}\")\n"
        classText += "@Override\n"
        classText += "void writeString(String s);\n"
        classText += classFooter

        File("./data/api-rs/RSBuffer.java").writeText(classText)
    }

    fun getDeobName(name: String) : String? {
        for (bufferMethod in PacketDecoder.bufferMethods) {
            if (bufferMethod.method == name)
                return bufferMethod.deobName
        }
        return null
    }
}