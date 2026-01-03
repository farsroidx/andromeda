@file:Suppress("unused")

package ir.farsroidx.andromeda.ktx

import java.util.UUID

val uuid: UUID
    get() {
        return UUID.randomUUID()
    }

val String.uuid: UUID
    get() {
        return UUID.fromString(this)
    }

val ByteArray.uuid: UUID
    get() {
        return UUID.nameUUIDFromBytes(this)
    }

val uuidStr: String
    get() {
        return uuid.toString()
    }

val String.uuidStr: String
    get() {
        return this.uuid.toString()
    }

val ByteArray.uuidStr: String
    get() {
        return this.uuid.toString()
    }