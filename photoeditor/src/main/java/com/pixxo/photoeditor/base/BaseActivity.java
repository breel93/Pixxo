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
package com.pixxo.photoeditor.base;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.snackbar.Snackbar;

/** Created by Burhanuddin Rashid on 1/17/2018. */
public class BaseActivity extends AppCompatActivity {

  public static final int READ_WRITE_STORAGE = 52;
  private ProgressDialog mProgressDialog;

  public boolean requestPermission(String permission) {
    boolean isGranted =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    if (!isGranted) {
      ActivityCompat.requestPermissions(this, new String[] {permission}, READ_WRITE_STORAGE);
    }
    return isGranted;
  }

  public void isPermissionGranted(boolean isGranted, String permission) {}

  public void makeFullScreen() {
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow()
        .setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    switch (requestCode) {
      case READ_WRITE_STORAGE:
        isPermissionGranted(grantResults[0] == PackageManager.PERMISSION_GRANTED, permissions[0]);
        break;
    }
  }

  protected void showLoading(@NonNull String message) {
    mProgressDialog = new ProgressDialog(this);
    mProgressDialog.setMessage(message);
    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    mProgressDialog.setCancelable(false);
    mProgressDialog.show();
  }

  protected void hideLoading() {
    if (mProgressDialog != null) {
      mProgressDialog.dismiss();
    }
  }

  protected void showSnackbar(@NonNull String message) {
    View view = findViewById(android.R.id.content);
    if (view != null) {
      Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    } else {
      Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
  }
}
