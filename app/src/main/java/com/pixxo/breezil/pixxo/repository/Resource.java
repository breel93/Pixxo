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
package com.pixxo.breezil.pixxo.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Resource<T> {

  @NonNull public final Status status;

  @Nullable public final T data;

  @Nullable public final String message;

  public Resource(@NonNull Status status, @Nullable T data, @NonNull String message) {
    this.status = status;
    this.data = data;
    this.message = message;
  }

  public static <T> Resource<T> success(@NonNull T data, @NonNull String message) {
    return new Resource<>(Status.SUCCESS, data, message);
  }

  public static <T> Resource<T> error(@Nullable T data, @NonNull String msg) {
    return new Resource<>(Status.ERROR, data, msg);
  }

  public static <T> Resource<T> loading(@Nullable T data) {
    return new Resource<>(Status.LOADING, data, null);
  }

  public enum Status {
    SUCCESS,
    ERROR,
    LOADING
  }

  @Override
  public boolean equals(Object obj) {
    if (obj.getClass() != getClass() || obj.getClass() != Resource.class) {
      return false;
    }

    Resource<T> resource = (Resource) obj;

    if (resource.status != this.status) {
      return false;
    }

    if (this.data != null) {
      if (resource.data != this.data) {
        return false;
      }
    }

    if (resource.message != null) {
      if (this.message == null) {
        return false;
      }
      if (!resource.message.equals(this.message)) {
        return false;
      }
    }

    return true;
  }
}
