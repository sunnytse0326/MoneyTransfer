package money.transfer.transfermoney.data

import money.transfer.transfermoney.BuildConfig
import money.transfer.transfermoney.encryption.DataEncryption
import org.junit.Assert
import org.junit.Test
import android.util.Base64
import android.util.Log
import money.transfer.transfermoney.utils.AppStorage
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.api.mockito.PowerMockito.`when`
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString




@RunWith(PowerMockRunner::class)
@PrepareForTest(Base64::class)
@PowerMockIgnore("javax.crypto.*")
class DataDecryptionTest{
    val accessToken = "6bb089648c8a85abf84a5f4f8692ea7c73e2fb4e7597073aba22c8875f39f468"

    @Test
    fun dataEncryptionAndDecryption(){
        PowerMockito.mockStatic(Base64::class.java)
        `when`(Base64.encode(any(), anyInt())).thenAnswer({ invocation -> java.util.Base64.getEncoder().encode(invocation.getArguments()[0] as ByteArray) })
        `when`(Base64.decode(any(ByteArray::class.java), anyInt())).thenAnswer({ invocation -> java.util.Base64.getDecoder().decode(invocation.getArguments()[0] as ByteArray) })
        `when`(Base64.decode(anyString(), anyInt())).thenAnswer { invocation -> java.util.Base64.getMimeDecoder().decode(invocation.arguments[0] as String) }

        var dataEncryption = DataEncryption()

        Assert.assertEquals("1500",  dataEncryption.decryptData(dataEncryption.encryptData("1500", BuildConfig.dataPublic), BuildConfig.dataPrivate))
        Assert.assertNotEquals("2500",  dataEncryption.decryptData(dataEncryption.encryptData("1500", BuildConfig.dataPublic), BuildConfig.dataPrivate))

        Assert.assertEquals(accessToken, dataEncryption.decryptData(dataEncryption.encryptData(accessToken, BuildConfig.dataPublic), BuildConfig.dataPrivate))
    }
}