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
package com.pixxo.breezil.pixxo.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.pixxo.breezil.pixxo.model.Photo;
import java.util.List;

@Dao
public interface PhotosDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(Photo photo);

  @Delete
  void delete(Photo photo);

  @Query("SELECT * FROM photo_table ORDER BY roomId DESC")
  LiveData<List<Photo>> getSavedPhoto();
}
