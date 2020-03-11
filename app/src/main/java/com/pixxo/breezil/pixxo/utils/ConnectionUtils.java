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
package com.pixxo.breezil.pixxo.utils;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import javax.inject.Inject;

public class ConnectionUtils {

  private Application application;

  @Inject
  public ConnectionUtils(Application application) {
    this.application = application;
  }

  public boolean sniff() {
    ConnectivityManager connectivityManager =
        (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo networkInfo = null;
    if (connectivityManager != null) {
      networkInfo = connectivityManager.getActiveNetworkInfo();
    }

    return networkInfo != null
        && networkInfo.isConnected()
        && networkInfo.isConnectedOrConnecting()
        && networkInfo.isAvailable();
  }
}
