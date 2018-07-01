package money.transfer.transfermoney.network

import org.jetbrains.anko.doAsync
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import android.net.http.X509TrustManagerExtensions
import android.util.Base64
import money.transfer.transfermoney.BuildConfig
import java.security.KeyStore
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*
import java.io.OutputStreamWriter


class NetworkRequest {
    lateinit var urlConnection: HttpsURLConnection
    lateinit var reader: BufferedReader
    var result: String? = null

    enum class Method {
        GET, POST, PUT, DELETE
    }

    fun request(url: String, method: Method, accessToken: String?, postBody: String, listener: RequestListener?) {
        doAsync {
            try {
                // TODO We just hardcode the method to be GET Method from POST request as github page did not allow post method
                val requestMethod = Method.GET
                val url = URL(url)
                urlConnection = url.openConnection() as HttpsURLConnection
                urlConnection.readTimeout = 10000
                urlConnection.requestMethod = requestMethod.name

                if (requestMethod == Method.POST) {
                    urlConnection.setRequestProperty("Authorization", accessToken)
                    urlConnection.setRequestProperty("Content-Type", "application/json")
                    urlConnection.setRequestProperty("Accept", "application/json")

                    val wr = OutputStreamWriter(urlConnection.outputStream)
                    wr.write(postBody)
                    wr.flush()
                }
                urlConnection.connect()

                val validPins = Collections.singleton(BuildConfig.pinPub)
                validatePinning(getTrustManager(), urlConnection, validPins)

                val inputStream = urlConnection.inputStream

                val buffer = StringBuffer()
                if (inputStream == null) {
                }

                reader = BufferedReader(InputStreamReader(inputStream))
                reader.forEachLine {
                    buffer.append("${it}\n")
                }
                result = buffer.toString()
                listener?.onSuccess(result)
            } catch (e: Exception) {
                listener?.onError(e, result)
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect()
                }
                if (reader != null) {
                    try {
                        reader.close()
                    } catch (e: IOException) {
                        listener?.onError(e, result)
                    }
                }
            }
        }

    }

    private fun validatePinning(
            trustManagerExt: X509TrustManagerExtensions,
            conn: HttpsURLConnection, validPins: Set<String>) {
        var certChainMsg = ""
        try {
            val md = MessageDigest.getInstance("SHA-256")
            val trustedChain = trustedChain(trustManagerExt, conn)
            for (cert in trustedChain) {
                val publicKey = cert.publicKey.encoded
                md.update(publicKey, 0, publicKey.size)
                val pin = Base64.encodeToString(md.digest(),
                        Base64.NO_WRAP)
                certChainMsg += "\tsha256/$pin: ${cert.subjectDN}\n"
                if (validPins.contains(pin)) {
                    return
                }
            }
        } catch (e: NoSuchAlgorithmException) {
            throw SSLException(e)
        }
        throw SSLPeerUnverifiedException("Certificate pinning failure\n\tPeer certificate chain:\n$certChainMsg")
    }

    @Throws(SSLException::class)
    private fun trustedChain(
            trustManagerExt: X509TrustManagerExtensions,
            conn: HttpsURLConnection): List<X509Certificate> {
        val serverCerts = conn.getServerCertificates()
        val untrustedCerts = Arrays.copyOf(serverCerts,
                serverCerts.size, Array<X509Certificate>::class.java)
        val host = conn.url.host
        try {
            return trustManagerExt.checkServerTrusted(untrustedCerts,
                    "RSA", host)
        } catch (e: CertificateException) {
            throw SSLException(e)
        }
    }

    private fun getTrustManager(): X509TrustManagerExtensions {
        val trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)
        var x509TrustManager: X509TrustManager? = null
        for (trustManager in trustManagerFactory.getTrustManagers()) {
            if (trustManager is X509TrustManager) {
                x509TrustManager = trustManager
                break
            }
        }
        return X509TrustManagerExtensions(x509TrustManager)
    }

    interface RequestListener {
        fun onSuccess(result: String?)
        fun onError(err: Exception, response: String?)
    }
}