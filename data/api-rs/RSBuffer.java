package net.runelite.rs.api;

import net.runelite.api.Buffer;
import net.runelite.mapping.Import;

public interface RSBuffer extends Buffer, RSNode
{
@Import("array")
byte[] getPayload();

@Import("offset")
int getOffset();

@Import("offset")
void setOffset(int offset);

@Import("readUnsignedByteA")
@Override
int readUByteAdd();

@Import("readUnsignedByteC")
@Override
int readUByteNeg();

@Import("readUnsignedByteS")
@Override
int readUByteSub();

@Import("readByteA")
@Override
byte readByteAdd();

@Import("readByteC")
@Override
byte readByteNeg();

@Import("readByteS")
@Override
byte readByteSub();

@Import("readUnsignedByte")
@Override
int readUnsignedByte();

@Import("readByte")
@Override
byte readByte();

@Import("readUnsignedShort")
@Override
int readUnsignedShort();

@Import("readShort")
@Override
int readShort();

@Import("readStringCp1252NullTerminated")
@Override
String readString();

@Import("readShortSmart")
@Override
int readShortSmart();

@Import("readUShortSmart")
@Override
int readUShortSmart();

@Import("writeByte")
@Override
void writeByte(int i);

@Import("writeInt")
@Override
void writeInt(int i);

@Import("writeIntLE")
@Override
void writeIntLE(int i);

@Import("method2382")
@Override
void writeIntIME(int i);

@Import("method2383")
@Override
void writeIntME(int i);

@Import("writeShortLE")
@Override
void writeShortLE(int i);

@Import("writeShortA")
@Override
void writeShortAdd(int i);

@Import("writeShortLEA")
@Override
void writeShortAddLE(int i);

@Import("readUnsignedShortLE")
@Override
int readUShortLE();

@Import("readUnsignedShortA")
@Override
int readUShortAdd();

@Import("readUnsignedShortLEA")
@Override
int readShortAddLE();

@Import("null")
@Override
int readShortLE();

@Import("readInt")
@Override
int readInt();

@Import("readIntLE")
@Override
int readIntLE();

@Import("method2373")
@Override
int readIntIME();

@Import("method2384")
@Override
int readIntME();

@Import("writeLong")
@Override
void writeLong(long l);

@Import("writeLongMedium")
@Override
void writeLongMedium(long l);

@Import("writeShort")
@Override
void writeShort(int i);

@Import("writeByteA")
@Override
void writeByteAdd(int i);

@Import("writeByteS")
@Override
void writeByteSub(int i);

@Import("writeByteC")
@Override
void writeByteNeg(int i);

@Import("writeStringCp1252NullTerminated")
@Override
void writeString(String s);
}