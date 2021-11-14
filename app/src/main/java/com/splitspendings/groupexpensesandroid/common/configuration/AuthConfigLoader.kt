package io.curity.identityserver.client.configuration

import android.content.Context
import com.splitspendings.groupexpensesandroid.R
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okio.Buffer
import okio.buffer
import okio.source
import java.nio.charset.Charset

class AuthConfigLoader {

    /*
     * Load configuration from the resource
     */
    fun load(context: Context): AuthConfig? {

        // Get the raw resource
        val stream = context.resources.openRawResource(R.raw.config)
        val configSource = stream.source().buffer()

        // Read it as JSON text
        val configBuffer = Buffer()
        configSource.readAll(configBuffer)
        val configJson = configBuffer.readString(Charset.forName("UTF-8"))

        // Deserialize it into objects
        val moshi: Moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<AuthConfig> = moshi.adapter(AuthConfig::class.java)
        return adapter.fromJson(configJson)
    }
}