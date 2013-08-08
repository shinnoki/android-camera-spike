package com.konashi.fashionstamp.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.camerastamp.R;
import com.konashi.fashionstamp.entity.Item;

public class FeedActivity extends FragmentActivity {

	private Fragment mFragment;

	private static final int REQUEST_GALLERY = 100;
	private static final int REQUEST_CAMERA = 200;
	private static final int REQUEST_CROP = 300;
	private static final int REQUEST_UPLOAD = 400;
	private Uri mSaveUri;

	// Navigation Drawerに関するメンバ変数
	private String[] mFeedTitles = { "新着アイテム", "注目アイテム", "投稿したアイテム",
			"ログイン（未実装）", "設定（未実装）" };
	private ListView mDrawerList;
	private DrawerLayout mDrawerLayout;
	private CharSequence mTitle;
	private CharSequence mDrawerTitle;
	private ActionBarDrawerToggle mDrawerToggle;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed);

		// Navigation Drawerに関する設定
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// Navigation DrawerのListViewにメニューアイテム（ランキングなど）をセット
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mFeedTitles));

		// Navigation DrawerのClickListenerをセット
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {

			// Navigation Drawerが完全に閉じた状態の時に呼び出される
			public void onDrawerClosed(View view) {
				setTitle(mTitle);
				invalidateOptionsMenu();
			}

			// Navigation Drawerが完全に開いた状態の時に呼び出される
			public void onDrawerOpened(View drawerView) {
				setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};

		// Navigation Drawerにトグルの機能を持たせる
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// 引数はドキュメント参照（これだけでも理解できると思うので省略）
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
		R.string.app_name, /* "open drawer" description */
		R.string.app_name /* "close drawer" description */
		) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				setTitle(mTitle);
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				setTitle(mDrawerTitle);
			}
		};

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// Nabigation Drawerに関する記述はここまで

		if (savedInstanceState != null)
			mFragment = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		if (mFragment == null)
			mFragment = new BaseFeedFragment();
		Bundle bundle = new Bundle();
		bundle.putString("requestUrl",
				"http://still-ocean-5133.herokuapp.com/items.json");
		mFragment.setArguments(bundle);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mFragment).commit();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		if (item.getItemId() == R.id.menu_upload) {
			selectImage();
		}

		return super.onOptionsItemSelected(item);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Bundle args = new Bundle();
			switch (position) {
			case 0:
				mFragment = new BaseFeedFragment();
				args.putString("requestUrl",
						"http://still-ocean-5133.herokuapp.com/items.json");
				mFragment.setArguments(args);
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.content_frame, mFragment).commit();
				break;

			case 1:
				mFragment = new BaseFeedFragment();
				args.putString("requestUrl",
						"http://still-ocean-5133.herokuapp.com/ranking.json");
				mFragment.setArguments(args);
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.content_frame, mFragment).commit();
				break;

			default:
				break;
			}
			selectItem(position);
		}
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feed, menu);
		return true;
	}

	private void selectImage() {
		String[] items = { "写真を撮る", "画像を選択" };

		new AlertDialog.Builder(this).setItems(items,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						if (item == 0) {
							wakeUpCamera();
						}
						if (item == 1) {
							wakeUpGallery();
						}
					}
				}).show();
	}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CAMERA) {
            wakeUpCrop(mSaveUri);
            // wakeUpUploadActivity(mSaveUri);
        }

        if (requestCode == REQUEST_GALLERY) {
            wakeUpCrop(data.getData());
            /// wakeUpUploadActivity(data.getData());
        }
        
        if (requestCode == REQUEST_CROP) {
            Bundle ext = data.getExtras();

            if (ext != null) {
                Bitmap img = ext.getParcelable("data");

                Intent intent = new Intent(this, UploadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("image", img);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_UPLOAD);
            }
        }
        
        if (requestCode == REQUEST_UPLOAD) {
            Intent intent = new Intent(this, ItemShowActivity.class);
            Item item = (Item)data.getSerializableExtra("item");
            intent.putExtra("item", item);
            startActivity(intent);
            overridePendingTransition(R.anim.swipe_in_left, R.anim.swipe_out_left);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    
    private void wakeUpCamera(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(System.currentTimeMillis());
        stringBuilder.append(".jpg");
 
        String fileName = stringBuilder.toString();
 
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        ContentResolver contentResolver = getContentResolver();
        mSaveUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
 
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mSaveUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void wakeUpGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLERY);
    }
    
    private void wakeUpUploadActivity(Uri uri) {
        try {
            
            // Get orientation
            Cursor query = MediaStore.Images.Media.query(getContentResolver(), uri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION },
                null, null);
            query.moveToFirst();
            int degree = query.getInt(0);
            
            //画像自体は読み込まず、画像サイズなどのみを読み込む
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(uri.toString(), options);
             
            //読み込むスケールを計算する
            int scaleW = options.outWidth / 320+1;
            int scaleH = options.outHeight / 240+1;
            options.inSampleSize = Math.max(scaleW, scaleH);
             
            //計算したスケールで画像を読み込む
            options.inJustDecodeBounds = false;
            InputStream is = getContentResolver().openInputStream(uri);  
            Bitmap img = BitmapFactory.decodeStream(is, null, options);
            
            // Rotation
            Matrix mat = new Matrix();  
            mat.postRotate(degree);  
            int max_size = 1024;
            Bitmap rotated = Bitmap.createBitmap(img, 0, 0, max_size, max_size, mat, true);

            Intent intent = new Intent(this, UploadActivity.class);
            intent.putExtra("image", rotated);
            startActivityForResult(intent, REQUEST_UPLOAD);
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    
    private void wakeUpCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 600);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
       
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP);
    }
}
