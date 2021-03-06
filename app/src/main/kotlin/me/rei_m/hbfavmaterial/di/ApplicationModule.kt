/*
 * Copyright (c) 2017. Rei Matsushita
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package me.rei_m.hbfavmaterial.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import me.rei_m.hbfavmaterial.R
import me.rei_m.hbfavmaterial.application.HatenaService
import me.rei_m.hbfavmaterial.application.TwitterService
import me.rei_m.hbfavmaterial.application.impl.HatenaServiceImpl
import me.rei_m.hbfavmaterial.application.impl.TwitterServiceImpl
import me.rei_m.hbfavmaterial.infra.network.HatenaOAuthManager
import me.rei_m.hbfavmaterial.infra.network.SignedRetrofitFactory
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideHatenaOAuthManager(httpClient: OkHttpClient): HatenaOAuthManager {
        return HatenaOAuthManager(application.getString(R.string.api_key_hatena_consumer_key),
                application.getString(R.string.api_key_hatena_consumer_secret),
                httpClient)
    }

    @Provides
    @Singleton
    fun provideTwitterService(): TwitterService =
            TwitterServiceImpl(application.getSharedPreferences("TwitterModel", Context.MODE_PRIVATE))

    @Provides
    @Singleton
    fun provideHatenaService(hatenaOAuthManager: HatenaOAuthManager,
                             signedRetrofitFactory: SignedRetrofitFactory): HatenaService =
            HatenaServiceImpl(application.getSharedPreferences("HatenaModel", Context.MODE_PRIVATE), hatenaOAuthManager, signedRetrofitFactory)
}
