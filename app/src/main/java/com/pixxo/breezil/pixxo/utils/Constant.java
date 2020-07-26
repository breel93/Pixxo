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

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import com.pixxo.breezil.pixxo.R;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Constant {

  public static int ZERO = 0;
  public static int ONE = 1;
  public static int TWO = 2;
  public static int FOUR = 4;
  public static int FIVE = 5;
  public static int TEN = 10;
  public static String SINGLE_PHOTO = "single_photo";
  public static String TYPE = "type";
  public static String CATEGORY = "category";
  public static String FIRST_TYPE = "initial_type";
  public static String SAVED_TYPE = "saved_type";
  public static String EDIT_IMAGE_URI_STRING = "edit_uri_string";
  public static String EDITED_IMAGE_URI_STRING = "edited_uri_string";
  public static String PHOTO_STRING = "image_string";
  public static String QUICK_SEARCH_STRING = "quick_search_string";
  public static int STORAGE_PERMISSION_CODE = 99;
  public static int CAMERA_PERMISSION_CODE = 90;
  public static String PIXXO_DB = "pixxo.db";

  public static int CAMERA_REQUEST_CODE = 1;
  public static int GALLERY_REQUEST_CODE = 2;
  public static String DOTPNG = ".png";
  public static String DOTJPG = ".jpg";

  public static String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

  public static String PIXXO_EDITED = ROOT_DIR + "/Pictures/Pixxo_Edited";

  public static boolean checkInternetConnection(Context context) {
    ConnectivityManager connectivity =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivity == null) {
      return false;
    } else {
      NetworkInfo[] info = connectivity.getAllNetworkInfo();
      if (info != null) {
        for (NetworkInfo anInfo : info) {
          if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
            return true;
          }
        }
      }
    }
    return false;
  }

  public String getCategoryList(SharedPreferences sharedPreferences, Context context) {
    Set<String> categorySet = new HashSet<>();
    categorySet.add(context.getString(R.string.pref_category_all_value));

    List<String> entries =
        new ArrayList<>(
            Objects.requireNonNull(
                sharedPreferences.getStringSet(
                    context.getString(R.string.pref_category_key), categorySet)));
    StringBuilder selectedcategories = new StringBuilder();

    for (int i = 0; i < entries.size(); i++) {
      selectedcategories.append(entries.get(i)).append(",");
    }
    if (selectedcategories.length() > 0) {
      selectedcategories.deleteCharAt(selectedcategories.length() - 1);
    }

    return selectedcategories.toString();
  }

}
