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
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class PhotosResult implements Parcelable {

  private int id;

  @SerializedName("hits")
  @Expose
  private List<Photo> hits = new ArrayList();

  @SerializedName("totalHits")
  @Expose
  private int totalHits;

  @SerializedName("total")
  @Expose
  private int total;

  public PhotosResult() {}

  public PhotosResult(int id, List<Photo> hits, int totalHits, int total) {
    this.id = id;
    this.hits = hits;
    this.totalHits = totalHits;
    this.total = total;
  }

  public PhotosResult(int id, List<Photo> hits) {
    this.id = id;
    this.hits = hits;
  }

  protected PhotosResult(Parcel in) {
    hits = in.createTypedArrayList(Photo.CREATOR);
    total = in.readInt();
    totalHits = in.readInt();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeTypedList(hits);
    dest.writeInt(total);
    dest.writeInt(totalHits);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<PhotosResult> CREATOR =
      new Creator<PhotosResult>() {
        @Override
        public PhotosResult createFromParcel(Parcel in) {
          return new PhotosResult(in);
        }

        @Override
        public PhotosResult[] newArray(int size) {
          return new PhotosResult[size];
        }
      };

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<Photo> getHits() {
    return hits;
  }

  public void setHits(List<Photo> hits) {
    this.hits = hits;
  }

  public int getTotalHits() {
    return totalHits;
  }

  public void setTotalHits(int totalHits) {
    this.totalHits = totalHits;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }
}
