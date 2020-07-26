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
package com.pixxo.breezil.pixxo.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.net.Uri;

public class EditedPhoto implements Parcelable {
  private String path;
  private boolean isDirectory;
  private Bitmap image;
  private Uri uri;

  public EditedPhoto(String path, boolean isDirectory, Bitmap image, Uri uri) {
    this.path = path;
    this.isDirectory = isDirectory;
    this.image = image;
    this.uri = uri;
  }

  public EditedPhoto() {}

  protected EditedPhoto(Parcel in) {
    path = in.readString();
    isDirectory = in.readByte() != 0;
    image = in.readParcelable(Bitmap.class.getClassLoader());
    uri = in.readParcelable(Uri.class.getClassLoader());
  }

  public static final Creator<EditedPhoto> CREATOR =
      new Creator<EditedPhoto>() {
        @Override
        public EditedPhoto createFromParcel(Parcel in) {
          return new EditedPhoto(in);
        }

        @Override
        public EditedPhoto[] newArray(int size) {
          return new EditedPhoto[size];
        }
      };

  public boolean isDirectory() {
    return isDirectory;
  }

  public void setDirectory(boolean directory) {
    isDirectory = directory;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Bitmap getImage() {
    return image;
  }

  public void setImage(Bitmap image) {
    this.image = image;
  }

  public Uri getUri() {
    return uri;
  }

  public void setUri(Uri uri) {
    this.uri = uri;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(path);
    dest.writeByte((byte) (isDirectory ? 1 : 0));
    dest.writeParcelable(image, flags);
    dest.writeParcelable(uri, flags);
  }
}
