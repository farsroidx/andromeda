@file:Suppress("unused")

package andromeda.crypto

import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

/**
 * [AndromedaSignature] provides RSA digital signature generation and verification
 * using the SHA-256 hash algorithm.
 *
 * ## Features
 * - Signs data with a private RSA key.
 * - Verifies data integrity and authenticity using a public RSA key.
 * - Uses `SHA256withRSA` for strong cryptographic assurance.
 *
 * ## Usage
 * ```kotlin
 * // Signing data
 * val data = "Hello World".toByteArray()
 * val signatureBytes = AndromedaSignature.sign(data, privateKey)
 *
 * // Verifying data
 * val isValid = AndromedaSignature.verify(data, signatureBytes, publicKey)
 * println(isValid) // true if signature matches
 * ```
 *
 * ## Security Notes
 * - Always protect private keys; leaking a private key compromises all signatures.
 * - Use a secure key size (e.g., 2048-bit or 4096-bit RSA).
 * - Never reuse signatures with the same nonce or non-hashed data in unsafe ways.
 */
object AndromedaSignature {
    private const val SIGNATURE_ALGORITHM = "SHA256withRSA"

    /**
     * Signs the given data using the provided RSA private key.
     *
     * @param data Byte array of the data to sign
     * @param privateKey RSA private key
     * @return Byte array representing the digital signature
     */
    fun sign(
        data: ByteArray,
        privateKey: PrivateKey,
    ): ByteArray {
        val signature = Signature.getInstance(SIGNATURE_ALGORITHM)
        signature.initSign(privateKey)
        signature.update(data)
        return signature.sign()
    }

    /**
     * Verifies a digital signature using the given RSA public key.
     *
     * @param data Original data
     * @param signature Signature to verify
     * @param publicKey RSA public key
     * @return `true` if the signature is valid, `false` otherwise
     */
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
