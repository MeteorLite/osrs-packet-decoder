package meteor.util

import meteor.BufferMethod
import meteor.PacketDecoder
import java.io.File

object RuneLiteApiBufferClass {
    val classHeader = "package net.runelite.api;\n" +
            "\n" +
            "public interface Buffer extends Node\n" +
            "{\n" +
            "byte[] getPayload();\n" +
            "int getOffset();\n"

    val classFooter = "}"


    fun create() {
        var classText = classHeader
        classText += "\n"
        classText += "int readUByteAdd();\n"
        classText += "int readUByteNeg();\n"
        classText += "int readUByteSub();\n"
        classText += "byte readByteAdd();\n"
        classText += "byte readByteNeg();\n"
        classText += "byte readByteSub();\n"
        classText += "int readUnsignedByte();\n"
        classText += "byte readByte();\n"
        classText += "int readUnsignedShort();\n"
        classText += "int readShort();\n"
        classText += "String readString();\n"
        classText += "int readShortSmart();\n"
        classText += "int readUShortSmart();\n"
        classText += "void writeByte(int i);\n"
        classText += "void writeInt(int i);\n"
        classText += "void writeIntLE(int i);\n"
        classText += "void writeIntIME(int i);\n"
        classText += "void writeIntME(int i);\n"
        classText += "int readInt();\n"
        classText += "int readIntLE();\n"
        classText += "int readIntIME();\n"
        classText += "int readIntME();\n"
        classText += "void writeShortLE(int i);\n"
        classText += "void writeShortAdd(int i);\n"
        classText += "void writeShortAddLE(int i);\n"
        classText += "int readUShortLE();\n"
        classText += "int readUShortAdd();\n"
        classText += "int readShortAddLE();\n"
        classText += "int readShortLE();\n"
        classText += "void writeLong(long l);\n"
        classText += "void writeLongMedium(long l);\n"
        classText += classFooter
        File("./data/Buffer.java").writeText(classText)
    }
}