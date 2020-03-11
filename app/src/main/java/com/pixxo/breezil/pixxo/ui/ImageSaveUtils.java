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
package com.pixxo.breezil.pixxo.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import com.pixxo.breezil.pixxo.R;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageSaveUtils {
  Context context;

  public ImageSaveUtils(Context context) {
    this.context = context;
  }

  public void startDownloading(Context context, Bitmap bitmap) {
    File storageDir =
        new File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                + context.getString(R.string._slash_pixxo));
    if (Build.VERSION.SDK_INT >= 29) {
      ContentValues values = this.contentValues();
      values.put(
          MediaStore.Images.Media.RELATIVE_PATH,
          "Pictures/" + context.getString(R.string._slash_pixxo));
      values.put(MediaStore.Images.Media.IS_PENDING, true);
      Uri uri =
          context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
      if (uri != null) {
        try {
          this.saveImageToStream(bitmap, context.getContentResolver().openOutputStream(uri));
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
        context.getContentResolver().update(uri, values, null, null);
      }
    } else {

      if (!storageDir.exists()) {
        storageDir.mkdirs();
      }
      String fileName = System.currentTimeMillis() + ".png";
      File file = new File(storageDir, fileName);
      try {
        this.saveImageToStream(bitmap, (new FileOutputStream(file)));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      if (file.getAbsolutePath() != null) {
        ContentValues values = this.contentValues();
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
      }
    }
  }

  public Uri getLocalBitmapUri(Bitmap bmp, Context context) {
    Uri bmpUri = null;
    try {

      File file =
          new File(
              context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
              context.getString(R.string.share_image_)
                  + System.currentTimeMillis()
                  + context.getString(R.string.png));
      FileOutputStream out = new FileOutputStream(file);
      bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
      out.flush();
      out.close();

      bmpUri =
          FileProvider.getUriForFile(
              context, context.getPackageName() + context.getString(R.string._provider), file);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return bmpUri;
  }

  private ContentValues contentValues() {
    ContentValues values = new ContentValues();
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
    values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / (long) 1000);
    values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
    return values;
  }

  private void saveImageToStream(Bitmap bitmap, OutputStream outputStream) {
    if (outputStream != null) {
      try {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        outputStream.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private static void galleryAddPic(Context context, String imagePath) {
    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    File f = new File(imagePath);
    Uri contentUri = Uri.fromFile(f);
    mediaScanIntent.setData(contentUri);
    context.sendBroadcast(mediaScanIntent);
  }

  //   public String startDownloading(Context context, Bitmap image) {
  //    String savedImagePath = null;
  //    // Create the new file in the external storage
  //    String timeStamp =
  //        new SimpleDateFormat(context.getString(R.string.date_format), Locale.getDefault())
  //            .format(new Date());
  //    String imageFileName =
  //        context.getString(R.string.app_name) + timeStamp + context.getString(R.string.png);
  ////    File storageDir =
  ////        new File(
  ////            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
  ////                + context.getString(R.string._slash_pixxo));
  //    File storageDir =
  //        new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+
  //            context.getString(R.string._slash_pixxo));
  //    boolean success = true;
  //    if (!storageDir.exists()) {
  //      success = storageDir.mkdirs();
  //    }
  //
  //    // Save the new Bitmap
  //    if (success) {
  //      File imageFile = new File(storageDir, imageFileName);
  //      savedImagePath = imageFile.getAbsolutePath();
  //      try {
  //        OutputStream fileOut = new FileOutputStream(imageFile);
  //        image.compress(Bitmap.CompressFormat.JPEG, 100, fileOut);
  //        fileOut.close();
  //      } catch (Exception e) {
  //        e.printStackTrace();
  //      }
  //
  //      // Add the image to the system gallery
  //      galleryAddPic(context, savedImagePath);
  //    }
  //
  //    return savedImagePath;
  //  }

  //  public String startDownloading(Context context, Bitmap image) {
  //    String savedImagePath = null;
  //    File storageDir =
  //        new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+
  //            context.getString(R.string._slash_pixxo));
  //
  //    if (Build.VERSION.SDK_INT >= 29) {
  //      ContentValues values = this.contentValues();
  //      values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" +
  // context.getString(R.string._slash_pixxo));
  //      values.put(MediaStore.Images.Media.IS_PENDING, true);
  //      Uri uri =
  // context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
  //      if (uri != null) {
  //        try {
  //          this.saveImageToStream(image, context.getContentResolver().openOutputStream(uri));
  //        } catch (FileNotFoundException e) {
  //          e.printStackTrace();
  //        }
  //        context.getContentResolver().update(uri, values, null, null);
  //      }
  //    } else {
  //      String timeStamp =
  //          new SimpleDateFormat(context.getString(R.string.date_format), Locale.getDefault())
  //              .format(new Date());
  //      String imageFileName =
  //          context.getString(R.string.app_name) + timeStamp + context.getString(R.string.png);
  //
  //      boolean success = true;
  //      if (!storageDir.exists()) {
  //        success = storageDir.mkdirs();
  //      }
  //
  //      // Save the new Bitmap
  //      if (success) {
  //        File imageFile = new File(storageDir, imageFileName);
  //        savedImagePath = imageFile.getAbsolutePath();
  //        try {
  //          OutputStream fileOut = new FileOutputStream(imageFile);
  //          image.compress(Bitmap.CompressFormat.PNG, 100, fileOut);
  //          fileOut.close();
  //        } catch (Exception e) {
  //          e.printStackTrace();
  //        }
  //      }
  //      // Add the image to the system gallery
  //      galleryAddPic(context, savedImagePath);
  //    }
  //
  //    return savedImagePath;
  //  }
}
