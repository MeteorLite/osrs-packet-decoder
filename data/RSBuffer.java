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
@Import("writeInt")
@Override
void writeInt(int i);

@Import("method7916")
@Override
void writeIntLE(int i);

@Import("method7971")
@Override
void writeIntIME(int i);

@Import("method7972")
@Override
void writeIntME(int i);

@Import("readInt")
@Override
int readInt();

@Import("method7973")
@Override
int readIntLE();

@Import("method7974")
@Override
int readIntIME();

@Import("method8135")
@Override
int readIntME();

}