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
package com.pixxo.photoeditor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pixxo.photoeditor.base.BaseActivity;
import com.pixxo.photoeditor.filters.FilterListener;
import com.pixxo.photoeditor.filters.FilterViewAdapter;
import com.pixxo.photoeditor.tools.EditingToolsAdapter;
import com.pixxo.photoeditor.tools.ToolType;
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.SaveSettings;
import ja.burhanrashid52.photoeditor.TextStyleBuilder;
import ja.burhanrashid52.photoeditor.ViewType;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditImageActivity extends BaseActivity
    implements OnPhotoEditorListener,
        View.OnClickListener,
        PropertiesBSFragment.Properties,
        EmojiBSFragment.EmojiListener,
        StickerBSFragment.StickerListener,
        EditingToolsAdapter.OnItemSelected,
        FilterListener {

  public static final String FILE_PROVIDER_AUTHORITY =
      "com.burhanrashid52.photoeditor.fileprovider";
  private static final String TAG = EditImageActivity.class.getSimpleName();
  public static final String EXTRA_IMAGE_PATHS = "extra_image_paths";
  public static String EDIT_IMAGE_URI_STRING = "edit_uri_string";
  private static final int CAMERA_REQUEST = 1;
  private static final int PICK_REQUEST = 2;
  PhotoEditor mPhotoEditor;
  private PhotoEditorView mPhotoEditorView;
  private PropertiesBSFragment mPropertiesBSFragment;
  private EmojiBSFragment mEmojiBSFragment;
  private StickerBSFragment mStickerBSFragment;
  private TextView mTxtCurrentTool;
  private Typeface mWonderFont;
  private RecyclerView mRvTools, mRvFilters;
  private EditingToolsAdapter mEditingToolsAdapter = new EditingToolsAdapter(this);
  private FilterViewAdapter mFilterViewAdapter = new FilterViewAdapter(this);
  private ConstraintLayout mRootView;
  private ConstraintSet mConstraintSet = new ConstraintSet();
  private boolean mIsFilterVisible;
  public static String SINGLE_PHOTO = "single_photo";
  public String photoString;
  public String galleryCameraString;
  private Toolbar mToolbar;
  Uri uri;
  String savedImagePath = null;

  @Nullable @VisibleForTesting Uri mSaveImageUri;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    makeFullScreen();
    setContentView(R.layout.activity_edit_image);
    if (getIntent().hasExtra(SINGLE_PHOTO)) {
      photoString = getIntent().getStringExtra(SINGLE_PHOTO);
    } else if (getIntent().hasExtra(EDIT_IMAGE_URI_STRING)) {
      galleryCameraString = getIntent().getStringExtra(EDIT_IMAGE_URI_STRING);
      uri = Uri.parse(getIntent().getStringExtra(EDIT_IMAGE_URI_STRING));
    } else {
      finish();
      return;
    }
    initViews();

    handleIntentImage(mPhotoEditorView.getSource());

    mWonderFont =
        Typeface.createFromAsset(getAssets(), getString(R.string.beyond_wonderland_dot_ttf));

    mPropertiesBSFragment = new PropertiesBSFragment();
    mEmojiBSFragment = new EmojiBSFragment();
    mStickerBSFragment = new StickerBSFragment();
    mStickerBSFragment.setStickerListener(this);
    mEmojiBSFragment.setEmojiListener(this);
    mPropertiesBSFragment.setPropertiesChangeListener(this);

    LinearLayoutManager llmTools =
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    mRvTools.setLayoutManager(llmTools);
    mRvTools.setAdapter(mEditingToolsAdapter);

    LinearLayoutManager llmFilters =
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    mRvFilters.setLayoutManager(llmFilters);
    mRvFilters.setAdapter(mFilterViewAdapter);

    mToolbar = findViewById(R.id.pixxoToolBar);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle(R.string.edit_image);
    mToolbar.setTitle(R.string.edit_image);
    mToolbar.setNavigationOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            onBackPressed();
          }
        });
    // Typeface mTextRobotoTf = ResourcesCompat.getFont(this, R.font.roboto_medium);
    // Typeface mEmojiTypeFace = Typeface.createFromAsset(getAssets(), "emojione-android.ttf");

    mPhotoEditor =
        new PhotoEditor.Builder(this, mPhotoEditorView)
            .setPinchTextScalable(true) // set flag to make text scalable when pinch
            // .setDefaultTextTypeface(mTextRobotoTf)
            // .setDefaultEmojiTypeface(mEmojiTypeFace)
            .build(); // build photo editor sdk

    mPhotoEditor.setOnPhotoEditorListener(this);

    // Set Image Dynamically
    // mPhotoEditorView.getSource().setImageResource(R.drawable.color_palette);
  }

  private void handleIntentImage(ImageView source) {
    Intent intent = getIntent();
    if (intent != null) {
      String intentType = intent.getType();
      if (intentType != null && intentType.startsWith("image/")) {
        Uri imageUri = intent.getData();
        if (imageUri != null) {
          source.setImageURI(imageUri);
        }
      }
    }
  }

  private void initViews() {
    ImageView imgUndo;
    ImageView imgRedo;
    ImageView imgCamera;
    ImageView imgGallery;
    ImageView imgSave;
    ImageView imgClose;
    ImageView imgShare;

    mPhotoEditorView = findViewById(R.id.photoEditorView);
    mTxtCurrentTool = findViewById(R.id.txtCurrentTool);
    mRvTools = findViewById(R.id.rvConstraintTools);
    mRvFilters = findViewById(R.id.rvFilterView);
    mRootView = findViewById(R.id.rootView);

    imgUndo = findViewById(R.id.imgUndo);
    imgUndo.setOnClickListener(this);

    imgRedo = findViewById(R.id.imgRedo);
    imgRedo.setOnClickListener(this);

    imgCamera = findViewById(R.id.imgCamera);
    imgCamera.setOnClickListener(this);

    imgGallery = findViewById(R.id.imgGallery);
    imgGallery.setOnClickListener(this);

    imgSave = findViewById(R.id.imgSave);
    imgSave.setOnClickListener(this);

    imgClose = findViewById(R.id.imgClose);
    imgClose.setOnClickListener(this);

    imgShare = findViewById(R.id.imgShare);
    imgShare.setOnClickListener(this);

    if (photoString != null) {
      Glide.with(this)
          .asBitmap()
          .load(photoString)
          .listener(
              new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(
                    @Nullable GlideException e,
                    Object model,
                    Target<Bitmap> target,
                    boolean isFirstResource) {
                  return false;
                }

                @Override
                public boolean onResourceReady(
                    Bitmap bitmap,
                    Object model,
                    Target<Bitmap> target,
                    DataSource dataSource,
                    boolean isFirstResource) {
                  mPhotoEditorView.getSource().setImageBitmap(bitmap);
                  return true;
                }
              })
          .submit();
    }

    if (EDIT_IMAGE_URI_STRING != null) {
      mPhotoEditorView.getSource().setImageURI(uri);
    }
  }

  @Override
  public void onEditTextChangeListener(final View rootView, String text, int colorCode) {
    TextEditorDialogFragment textEditorDialogFragment =
        TextEditorDialogFragment.show(this, text, colorCode);
    textEditorDialogFragment.setOnTextEditorListener(
        new TextEditorDialogFragment.TextEditor() {
          @Override
          public void onDone(String inputText, int colorCode) {
            final TextStyleBuilder styleBuilder = new TextStyleBuilder();
            styleBuilder.withTextColor(colorCode);

            mPhotoEditor.editText(rootView, inputText, styleBuilder);
            mTxtCurrentTool.setText(R.string.label_text);
          }
        });
  }

  @Override
  public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {
    Log.d(
        TAG,
        "onAddViewListener() called with: viewType = ["
            + viewType
            + "], numberOfAddedViews = ["
            + numberOfAddedViews
            + "]");
  }

  @Override
  public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {
    Log.d(
        TAG,
        "onRemoveViewListener() called with: viewType = ["
            + viewType
            + "], numberOfAddedViews = ["
            + numberOfAddedViews
            + "]");
  }

  @Override
  public void onStartViewChangeListener(ViewType viewType) {
    Log.d(TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
  }

  @Override
  public void onStopViewChangeListener(ViewType viewType) {
    Log.d(TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
  }

  @Override
  public void onClick(View view) {
    int id = view.getId();
    if (id == R.id.imgUndo) {
      mPhotoEditor.undo();
    } else if (id == R.id.imgRedo) {
      mPhotoEditor.redo();
    } else if (id == R.id.imgSave) {
      saveImage(getApplicationContext());
    } else if (id == R.id.imgClose) {
      onBackPressed();
    } else if (id == R.id.imgShare) {
      shareImage();
    } else if (id == R.id.imgCamera) {
      Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      startActivityForResult(cameraIntent, CAMERA_REQUEST);
    } else if (id == R.id.imgGallery) {
      Intent intent = new Intent();
      intent.setType(getString(R.string.image_));
      intent.setAction(Intent.ACTION_GET_CONTENT);
      startActivityForResult(
          Intent.createChooser(intent, getString(R.string.select_picture)), PICK_REQUEST);
    }
  }

  private void shareImage() {
    if (mSaveImageUri == null) {
      showSnackbar(getString(R.string.msg_save_image_to_share));
      return;
    }

    Intent intent = new Intent(Intent.ACTION_SEND);
    intent.setType("image/*");
    intent.putExtra(Intent.EXTRA_STREAM, buildFileProviderUri(mSaveImageUri));
    startActivity(Intent.createChooser(intent, getString(R.string.msg_share_image)));
  }

  private Uri buildFileProviderUri(@NonNull Uri uri) {
    return FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, new File(uri.getPath()));
  }

  @SuppressLint("MissingPermission")
  private void saveImage(final Context context) {
    if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
      showLoading(getString(R.string.saving_));

      // Create the new file in the external storage
      String timeStamp =
          new SimpleDateFormat(getString(R.string.date_format), Locale.getDefault())
              .format(new Date());
      String imageFileName = getString(R.string.pixxo) + timeStamp + getString(R.string.dot_png);
      File storageDir =
          new File(
              Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                  + getString(R.string.slash_pixxo_edited));
      boolean success = true;
      if (!storageDir.exists()) {
        success = storageDir.mkdirs();
      }
      if (success) {

        File imageFile = new File(storageDir, imageFileName);
        savedImagePath = imageFile.getAbsolutePath();
        try {

          SaveSettings saveSettings =
              new SaveSettings.Builder()
                  .setClearViewsEnabled(true)
                  .setTransparencyEnabled(true)
                  .build();

          mPhotoEditor.saveAsFile(
              imageFile.getAbsolutePath(),
              saveSettings,
              new PhotoEditor.OnSaveListener() {
                @Override
                public void onSuccess(@NonNull String imagePath) {
                  hideLoading();
                  showSnackbar(getString(R.string.save_successfully));
                  galleryAddPic(context, savedImagePath);
                  mSaveImageUri = Uri.fromFile(new File(imagePath));
                  mPhotoEditorView.getSource().setImageURI(mSaveImageUri);
                }

                @Override
                public void onFailure(@NonNull Exception exception) {
                  hideLoading();
                  showSnackbar("Failed to save Image");
                }
              });
        } catch (Exception e) {
          e.printStackTrace();
          hideLoading();
          showSnackbar(e.getMessage());
        }
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

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      switch (requestCode) {
        case CAMERA_REQUEST:
          mPhotoEditor.clearAllViews();
          Bitmap photo = (Bitmap) data.getExtras().get("data");
          mPhotoEditorView.getSource().setImageBitmap(photo);
          break;
        case PICK_REQUEST:
          try {
            mPhotoEditor.clearAllViews();
            Uri uri = data.getData();
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            mPhotoEditorView.getSource().setImageBitmap(bitmap);
          } catch (IOException e) {
            e.printStackTrace();
          }
          break;
      }
    }
  }

  @Override
  public void onColorChanged(int colorCode) {
    mPhotoEditor.setBrushColor(colorCode);
    mTxtCurrentTool.setText(R.string.label_brush);
  }

  @Override
  public void onOpacityChanged(int opacity) {
    mPhotoEditor.setOpacity(opacity);
    mTxtCurrentTool.setText(R.string.label_brush);
  }

  @Override
  public void onBrushSizeChanged(int brushSize) {
    mPhotoEditor.setBrushSize(brushSize);
    mTxtCurrentTool.setText(R.string.label_brush);
  }

  @Override
  public void onEmojiClick(String emojiUnicode) {
    mPhotoEditor.addEmoji(emojiUnicode);
    mTxtCurrentTool.setText(R.string.label_emoji);
  }

  @Override
  public void onStickerClick(Bitmap bitmap) {
    mPhotoEditor.addImage(bitmap);
    mTxtCurrentTool.setText(R.string.label_sticker);
  }

  @Override
  public void isPermissionGranted(boolean isGranted, String permission) {
    if (isGranted) {
      saveImage(getApplicationContext());
    }
  }

  private void showSaveDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(R.string.are_you_want_to_exit_without_saving_image);
    builder.setPositiveButton(
        R.string.save,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            saveImage(getApplicationContext());
          }
        });
    builder.setNegativeButton(
        R.string.cancel,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        });

    builder.setNeutralButton(
        R.string.discard,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            finish();
          }
        });
    builder.create().show();
  }

  @Override
  public void onFilterSelected(PhotoFilter photoFilter) {
    mPhotoEditor.setFilterEffect(photoFilter);
  }

  @Override
  public void onToolSelected(ToolType toolType) {
    switch (toolType) {
      case BRUSH:
        mPhotoEditor.setBrushDrawingMode(true);
        mTxtCurrentTool.setText(R.string.label_brush);
        mPropertiesBSFragment.show(getSupportFragmentManager(), mPropertiesBSFragment.getTag());
        break;
      case TEXT:
        TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(this);
        textEditorDialogFragment.setOnTextEditorListener(
            new TextEditorDialogFragment.TextEditor() {
              @Override
              public void onDone(String inputText, int colorCode) {
                final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                styleBuilder.withTextColor(colorCode);

                mPhotoEditor.addText(inputText, styleBuilder);
                mTxtCurrentTool.setText(R.string.label_text);
              }
            });
        break;
      case ERASER:
        mPhotoEditor.brushEraser();
        mTxtCurrentTool.setText(R.string.label_eraser);
        break;
      case FILTER:
        mTxtCurrentTool.setText(R.string.label_filter);
        showFilter(true);
        break;
      case EMOJI:
        mEmojiBSFragment.show(getSupportFragmentManager(), mEmojiBSFragment.getTag());
        break;
      case STICKER:
        mStickerBSFragment.show(getSupportFragmentManager(), mStickerBSFragment.getTag());
        break;
    }
  }

  void showFilter(boolean isVisible) {
    mIsFilterVisible = isVisible;
    mConstraintSet.clone(mRootView);

    if (isVisible) {
      mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.START);
      mConstraintSet.connect(
          mRvFilters.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
      mConstraintSet.connect(
          mRvFilters.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
    } else {
      mConstraintSet.connect(
          mRvFilters.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END);
      mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.END);
    }

    ChangeBounds changeBounds = new ChangeBounds();
    changeBounds.setDuration(350);
    changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
    TransitionManager.beginDelayedTransition(mRootView, changeBounds);

    mConstraintSet.applyTo(mRootView);
  }

  @Override
  public void onBackPressed() {
    if (mIsFilterVisible) {
      showFilter(false);
      mTxtCurrentTool.setText(R.string.app_name);
    } else if (!mPhotoEditor.isCacheEmpty()) {
      showSaveDialog();
    } else {
      super.onBackPressed();
    }
  }
}