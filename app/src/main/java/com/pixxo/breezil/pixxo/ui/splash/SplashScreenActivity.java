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
package com.pixxo.breezil.pixxo.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.ui.BaseActivity;
import com.pixxo.breezil.pixxo.ui.main.MainActivity;

public class SplashScreenActivity extends BaseActivity {

  private static int SPLASH_TIME_OUT = 3000;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_splash_screen);

    new Handler()
        .postDelayed(
            () -> {
              Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
              startActivity(intent);
              finish();
            },
            SPLASH_TIME_OUT);
  }
}
