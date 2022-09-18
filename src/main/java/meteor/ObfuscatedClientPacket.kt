package meteor

class ObfuscatedClientPacket(
    var name: String, val size: Int, var deobname: String? = "",
    var structure: ArrayList<ObfuscatedBufferStructure> = ArrayList()) {
}