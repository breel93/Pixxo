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

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public abstract class AppDatabaseTest {
  protected AppDatabase db;

  @Before
  public void initDb() {
    db =
        Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().getContext(), AppDatabase.class)
            .allowMainThreadQueries()
            //            AppDatabase.class).allowMainThreadQueries()
            .build();
  }

  @After
  public void closeDb() {
    db.close();
  }
}
