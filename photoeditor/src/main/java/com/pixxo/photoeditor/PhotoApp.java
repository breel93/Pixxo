/**
 * Designed and developed by Kola Emiola
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pixxo.photoeditor;

import android.app.Application;
import android.content.Context;

/** Created by Burhanuddin Rashid on 1/23/2018. */
public class PhotoApp extends Application {
  private static PhotoApp sPhotoApp;
  private static final String TAG = PhotoApp.class.getSimpleName();

  @Override
  public void onCreate() {
    super.onCreate();
    sPhotoApp = this;
    /*   FontRequest fontRequest = new FontRequest(
               "com.google.android.gms.fonts",
               "com.google.android.gms",
               "Noto Color Emoji Compat",
               R.array.com_google_android_gms_fonts_certs);

       EmojiCompat.Config config = new FontRequestEmojiCompatConfig(this, fontRequest)
               .setReplaceAll(true)
               //    .setEmojiSpanIndicatorEnabled(true)
               //     .setEmojiSpanIndicatorColor(Color.GREEN)
               .registerInitCallback(new EmojiCompat.InitCallback() {
                   @Override
                   public void onInitialized() {
                       super.onInitialized();
                       Log.e(TAG, "Success");
                   }

                   @Override
                   public void onFailed(@Nullable Throwable throwable) {
                       super.onFailed(throwable);
                       Log.e(TAG, "onFailed: " + throwable.getMessage());
                   }
               });

    //   BundledEmojiCompatConfig bundledEmojiCompatConfig = new BundledEmojiCompatConfig(this);
       EmojiCompat.init(config);*/
  }

  public static PhotoApp getPhotoApp() {
    return sPhotoApp;
  }

  public Context getContext() {
    return sPhotoApp.getContext();
  }
}
