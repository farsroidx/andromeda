@file:Suppress("unused")

package andromeda.crypto

import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

object AndromedaSignature {
    private const val SIGNATURE_ALGORITHM = "SHA256withRSA"

    fun sign(
        data: ByteArray,
        privateKey: PrivateKey,
    ): ByteArray {
        val signature = Signature.getInstance(SIGNATURE_ALGORITHM)
        signature.initSign(privateKey)
        signature.update(data)
        return signature.sign()
    }

    fun verify(
        data: ByteArray,
        signature: ByteArray,
        publicKey: PublicKey,
    ): Boolean {
        val verifier = Signature.getInstance(SIGNATURE_ALGORITHM)
        verifier.initVerify(publicKey)
        verifier.update(data)
        return verifier.verify(signature)
    }
}
