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

public class NetworkState {
  public enum Status {
    RUNNING,
    SUCCESS,
    FAILED,
    NO_RESULT
  }

  private final Status mStatus;

  public static final NetworkState LOADED;
  public static final NetworkState LOADING;

  public NetworkState(Status status) {
    mStatus = status;
  }

  static {
    LOADED = new NetworkState(Status.SUCCESS);
    LOADING = new NetworkState(Status.RUNNING);
  }

  public Status getStatus() {
    return mStatus;
  }
}
