@file:Suppress("unused")

package andromeda.crypto

import javax.crypto.Cipher

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

object AndromedaCipher {
    fun instance(transformation: String): Cipher = Cipher.getInstance(transformation)
}
