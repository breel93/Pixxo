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

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "photo_table")
public class Photo implements Parcelable {

  @PrimaryKey(autoGenerate = true)
  private int roomId;

  @SerializedName("largeImageURL")
  @Expose
  private String largeImageURL;

  @SerializedName("id")
  @Expose
  private int photo_id;

  @SerializedName("webformatURL")
  @Expose
  private String webformatURL;

  @SerializedName("webformatWidth")
  @Expose
  private long webformatWidth;

  @SerializedName("webformatHeight")
  @Expose
  private long webformatHeight;

  @SerializedName("type")
  @Expose
  private String type;

  @SerializedName("tags")
  @Expose
  private String tags;

  public Photo() {}

  public Photo(
      String largeImageURL,
      int photo_id,
      String webformatURL,
      long webformatWidth,
      long webformatHeight,
      String type,
      String tags) {
    this.largeImageURL = largeImageURL;
    this.photo_id = photo_id;
    this.webformatURL = webformatURL;
    this.webformatWidth = webformatWidth;
    this.webformatHeight = webformatHeight;
    this.type = type;
    this.tags = tags;
  }

  public Photo(
      int roomId,
      String largeImageURL,
      int photo_id,
      String webformatURL,
      long webformatWidth,
      long webformatHeight,
      String type,
      String tags) {
    this.roomId = roomId;
    this.largeImageURL = largeImageURL;
    this.photo_id = photo_id;
    this.webformatURL = webformatURL;
    this.webformatWidth = webformatWidth;
    this.webformatHeight = webformatHeight;
    this.type = type;
    this.tags = tags;
  }

  protected Photo(Parcel in) {
    largeImageURL = in.readString();
    photo_id = in.readInt();
    webformatURL = in.readString();
    webformatWidth = in.readLong();
    webformatHeight = in.readLong();
    type = in.readString();
    tags = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(largeImageURL);
    dest.writeInt(photo_id);
    dest.writeString(webformatURL);
    dest.writeLong(webformatWidth);
    dest.writeLong(webformatHeight);
    dest.writeString(type);
    dest.writeString(tags);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<Photo> CREATOR =
      new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
          return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
          return new Photo[size];
        }
      };

  public int getRoomId() {
    return roomId;
  }

  public void setRoomId(int roomId) {
    this.roomId = roomId;
  }

  public String getLargeImageURL() {
    return largeImageURL;
  }

  public void setLargeImageURL(String largeImageURL) {
    this.largeImageURL = largeImageURL;
  }

  public int getPhoto_id() {
    return photo_id;
  }

  public void setPhoto_id(int photo_id) {
    this.photo_id = photo_id;
  }

  public String getWebformatURL() {
    return webformatURL;
  }

  public void setWebformatURL(String webformatURL) {
    this.webformatURL = webformatURL;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  public long getWebformatWidth() {
    return webformatWidth;
  }

  public void setWebformatWidth(long webformatWidth) {
    this.webformatWidth = webformatWidth;
  }

  public long getWebformatHeight() {
    return webformatHeight;
  }

  public void setWebformatHeight(long webformatHeight) {
    this.webformatHeight = webformatHeight;
  }
}
