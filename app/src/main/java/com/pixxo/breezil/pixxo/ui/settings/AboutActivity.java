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
package com.pixxo.breezil.pixxo.ui.settings;

import static com.pixxo.breezil.pixxo.utils.Constant.ZERO;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.ActivityAboutBinding;
import com.pixxo.breezil.pixxo.ui.BaseActivity;
import java.util.Calendar;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityAboutBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_about);

    View aboutPage = createPage();
    Toolbar mToolbar = findViewById(R.id.pixxoToolBar);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setTitle(R.string.about);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    binding.aboutLayout.addView(aboutPage, ZERO);
  }

  private View createPage() {
    return new AboutPage(this)
        .isRTL(false)
        .setDescription(getString(R.string.about_description))
        .setImage(R.mipmap.ic_launcher_round)
        .addWebsite(getString(R.string.privacy_policy_web), "Privacy Policy")
        .addItem(new Element().setTitle(getString(R.string.version)))
        .addGroup(getString(R.string.contacts))
        .addEmail(getString(R.string.email), getString(R.string.email_title))
        .addWebsite(getString(R.string.web), getString(R.string.website))
        .addTwitter(getString(R.string.twitter), getString(R.string.ontwitter))
        .addGitHub(getString(R.string.github), getString(R.string.ongithub))
        .addItem(getLibElement())
        .addItem(getCopyRights())
        .create();
  }

  private Element getCopyRights() {
    Element copyRightsElement = new Element();
    final String copyrights =
        String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
    copyRightsElement.setTitle(copyrights);
    copyRightsElement.setIconDrawable(R.drawable.ic_copyright_black_24dp);
    copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
    copyRightsElement.setIconNightTint(android.R.color.white);
    copyRightsElement.setGravity(Gravity.CENTER);
    return copyRightsElement;
  }

  private Element getLibElement() {
    Element libElement = new Element();

    libElement.setTitle(getString(R.string.open_source_libs));
    libElement.setOnClickListener(
        v ->
            new LibsBuilder()
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                .withActivityTitle(getString(R.string.library_text))
                .withAutoDetect(true)
                .start(AboutActivity.this));

    return libElement;
  }
}
