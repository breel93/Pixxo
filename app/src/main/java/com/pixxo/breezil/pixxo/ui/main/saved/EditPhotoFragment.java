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
package com.pixxo.breezil.pixxo.ui.main.saved;

import static com.pixxo.breezil.pixxo.utils.Constant.PIXXO_EDITED;
import static com.pixxo.breezil.pixxo.utils.Constant.STORAGE_PERMISSION_CODE;
import static com.pixxo.breezil.pixxo.utils.Constant.ZERO;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.FragmentEditPhotoBinding;
import com.pixxo.breezil.pixxo.model.EditedPhoto;
import com.pixxo.breezil.pixxo.ui.adapter.EditPhotoGridAdapter;
import com.pixxo.breezil.pixxo.ui.adapter.EditPhotoRecyclerAdapter;
import com.pixxo.breezil.pixxo.ui.bottom_sheet.ChoosePhotoBottomDialogFragment;
import com.pixxo.breezil.pixxo.utils.helper.BitmapHelper;
import dagger.android.support.DaggerFragment;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** A simple {@link Fragment} subclass. */
public class EditPhotoFragment extends DaggerFragment {

  private List<EditedPhoto> gridItems;
  private GridView gridView;
  private TextView editEmptyText;
  private Button clickEditBtn;
  private ChoosePhotoBottomDialogFragment choosePhotoBottomDialogFragment =
      new ChoosePhotoBottomDialogFragment();

  private FragmentEditPhotoBinding binding;

  public EditPhotoFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_photo, container, false);

//    gridView = view.findViewById(R.id.editGrid);
//    editEmptyText = view.findViewById(R.id.emptyText);
//    clickEditBtn = view.findViewById(R.id.clickToEditbtn);

    if (PIXXO_EDITED != null) {
      setGridAdapter(PIXXO_EDITED);
    }
    return binding.getRoot();
  }

  private void setGridAdapter(String path) {
    gridItems = getData(path);
    EditPhotoRecyclerAdapter adapter = new EditPhotoRecyclerAdapter();

    StaggeredGridLayoutManager staggeredGridLayoutManager =
        new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    binding.editedPhotoRecyclerView.setLayoutManager(staggeredGridLayoutManager);
    Collections.reverse(gridItems);

    if (gridItems.size() > ZERO) {
      binding.editedPhotoRecyclerView.setAdapter(adapter);
      adapter.submitList(gridItems);
    } else {
      binding.emptyText.setVisibility(View.VISIBLE);
      binding.clickToEditbtn.setVisibility(View.VISIBLE);
      binding.clickToEditbtn.setOnClickListener(
          v ->
              choosePhotoBottomDialogFragment.show(
                  getFragmentManager(), getString(R.string.choose_image)));
    }
  }

  private boolean isImageFile(String filePath) {
    // Add other formats as desired
    return filePath.endsWith(getString(R.string.jpg)) || filePath.endsWith(getString(R.string.png));
  }

//  @Override
//  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//    if (gridItems.get(position).isDirectory()) {
//      setGridAdapter(gridItems.get(position).getPath());
//    }
//  }

  /** This can be used to filter files. */
  private class ImageFileFilter implements FileFilter {

    @Override
    public boolean accept(File file) {
      if (file.isDirectory()) return true;
      else return isImageFile(file.getAbsolutePath());
    }
  }

  private ArrayList<EditedPhoto> getData(String directoryPath) {
    ArrayList<EditedPhoto> editedPhotos =new ArrayList<>();
    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED) {

      File filePath = new File(PIXXO_EDITED);
      EditedPhoto editedPhoto;
      File[] files = new File(directoryPath).listFiles(new ImageFileFilter());

      if (filePath.exists()) {
        assert files != null;
        if (files.length > 0) {
          for (File file : files) {

            editedPhoto = new EditedPhoto();
            try {
              Uri imageUri = Uri.fromFile(file);
              Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
              editedPhoto.setImage(bitmap);

            } catch (IOException e) {
              e.printStackTrace();
            }

            if(file.isDirectory() && file.listFiles(new ImageFileFilter()).length > 0){
              editedPhotos.add(new EditedPhoto(file.getAbsolutePath(), true, null));
            }

            editedPhotos.add(editedPhoto);
          }
        }
      }
    }else {
      ActivityCompat.requestPermissions(
          getActivity(),
          new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
          STORAGE_PERMISSION_CODE);
    }
    return editedPhotos;
  }
}
