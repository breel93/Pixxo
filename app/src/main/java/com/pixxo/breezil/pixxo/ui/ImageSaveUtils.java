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


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.pixxo.breezil.pixxo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ImageSaveUtils {
  Context context;

  public ImageSaveUtils(Context context) {
    this.context = context;
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


  public void saveImage(Context context, Bitmap bitmap, @NonNull String directory) {
    OutputStream fos = null;
    File storageDir =
        new File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + directory);
    String fileName = "Pixxo" + System.currentTimeMillis() + ".png";
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      ContentResolver resolver = context.getContentResolver();
      ContentValues contentValues = new ContentValues();
      contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
      contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
      contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + directory);
      Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
      try {
        fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      saveImageToStream(bitmap, fos);
    } else {
      if (!storageDir.exists()) {
        storageDir.mkdirs();
      }
      File file = new File(storageDir, fileName);
      try {
        this.saveImageToStream(bitmap, (new FileOutputStream(file)));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      if (file.getAbsolutePath() != null) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
      }
    }
  }


  private void saveImageToStream(Bitmap bitmap, OutputStream outputStream) {
    if (outputStream != null) {
      try {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        Objects.requireNonNull(outputStream).close();
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

  public File getCompressFile(Uri uri, Context context){
    File actualImage = new File(uri.getPath());
//    new Compressor(context)
//        .compressToFileAsFlowable(actualImage)
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(file -> newFile = file, throwable -> throwable.printStackTrace());
//    return newFile;
    try {
      File compressedImage = new Compressor(context)
          .setMaxWidth(640)
          .setMaxHeight(480)
          .setQuality(75)
          .setCompressFormat(Bitmap.CompressFormat.WEBP)
          .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
              Environment.DIRECTORY_PICTURES).getAbsolutePath())
          .compressToFile(actualImage);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return actualImage;
  }

}
