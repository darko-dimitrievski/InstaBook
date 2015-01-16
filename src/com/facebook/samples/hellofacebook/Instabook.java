package com.facebook.samples.hellofacebook;

import android.R.drawable;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.Implementation.BasicApplyEffect;
import com.example.Implementation.GammaApplyEffect;
import com.example.Implementation.BlackDotsApplyEffect;
import com.example.Implementation.GreyScaleApplyEffect;
import com.example.Implementation.NegativeApplyEffect;
import com.example.Implementation.OldBlackWhiteApplyEffect;
import com.example.Implementation.SepiaToneApplyEffect;
import com.example.Implementation.SnowApplyEffect;
import com.example.Implementation.SunnyApplyEffect;
import com.example.Infrastructure.ApplyEffect;
import com.facebook.*;
import com.facebook.android.Facebook;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.facebook.widget.*;
import com.meetme.android.horizontallistview.HorizontalListView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Instabook extends FragmentActivity {

	private static final String PERMISSION = "publish_actions";

	private final String PENDING_ACTION_BUNDLE_KEY = "com.facebook.samples.hellofacebook:PendingAction";

	private Button postPhotoButton;
	private LoginButton loginButton;
	private ProfilePictureView profilePictureView;
	private TextView greeting;
	private PendingAction pendingAction = PendingAction.NONE;
	private ViewGroup controlsContainer;
	private GraphUser user;
	private boolean canPresentShareDialog;
	private boolean canPresentShareDialogWithPhotos;
	private static final int CAMERA_REQUEST = 1000;
	private Bitmap image;

	private ImageView imageView;
	private ListView list = null;
	private ArrayAdapter<ApplyEffect> mAdapter = null;
	private List<ApplyEffect> items = new ArrayList<ApplyEffect>();
	// /

	private HorizontalListView listHor;
	private Bitmap b;

	private CustomData[] mCustomData;

	void loadCustomData() {
		b = BitmapFactory
				.decodeResource(this.getResources(), R.drawable.sample);
		mCustomData = new CustomData[8];
		for (int i = 0; i < 8; i++) {
			mCustomData[i] = new CustomData(Color.RED, "Red", b, items.get(i));
		}
	}

	// /
	private enum PendingAction {
		NONE, POST_PHOTO, POST_STATUS_UPDATE
	}

	private UiLifecycleHelper uiHelper;

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
		@Override
		public void onError(FacebookDialog.PendingCall pendingCall,
				Exception error, Bundle data) {
			Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
		}

		@Override
		public void onComplete(FacebookDialog.PendingCall pendingCall,
				Bundle data) {
			Log.d("HelloFacebook", "Success!");
		}
	};

	public void getImage(View view) {
		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, CAMERA_REQUEST);
	}

	public void getPicture(View view) {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, 1);
	}

	public void firstImage() {
		if (image == null) {
			Toast t = Toast.makeText(this, "Take a picture First!",
					Toast.LENGTH_SHORT);
			t.show();
		} else {
			imageView.setImageBitmap(image);
		}
	}

	// //
	private void setupCustomLists() {
		CustomArrayAdapter adapter = new CustomArrayAdapter(this, mCustomData);
		listHor.setAdapter(adapter);
	}

	// //

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			String name = savedInstanceState
					.getString(PENDING_ACTION_BUNDLE_KEY);
			pendingAction = PendingAction.valueOf(name);
		}

		setContentView(R.layout.main);

		loginButton = (LoginButton) findViewById(R.id.login_button);
		loginButton
				.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {

					// It's possible that we were waiting for this.user to
					// be populated in order to post a
					// status update.@Override
					@Override
					public void onUserInfoFetched(GraphUser user) {
						Instabook.this.user = user;
						updateUI();
						handlePendingAction();
					}
				});

		profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
		// greeting = (TextView) findViewById(R.id.greeting);
		imageView = (ImageView) findViewById(R.id.imageView1);
		//list = (ListView) findViewById(R.id.listView1);

//		mAdapter = new ArrayAdapter<ApplyEffect>(this,
//				android.R.layout.simple_list_item_1, items);

		this.loadEffects();

//		list.setAdapter(mAdapter);

		// //
		loadCustomData();

		listHor = (HorizontalListView) findViewById(R.id.hor);
		setupCustomLists();

		// //
		listHor.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				ApplyEffect ae = Instabook.this.items.get(position);
				Instabook.this.changePhoto(ae);

			}

		});

//		list.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1,
//					int position, long arg3) {
//
//				ApplyEffect ae = Instabook.this.items.get(position);
//				Instabook.this.changePhoto(ae);
//			}
//
//		});

		Toast t = Toast.makeText(this, "Take a pics or use one of ur album.",
				Toast.LENGTH_LONG);
		t.show();
		postPhotoButton = (Button) findViewById(R.id.postPhotoButton);
		postPhotoButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onClickPostPhoto();
			}
		});

		controlsContainer = (ViewGroup) findViewById(R.id.main_ui_container);

		final FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragment_container);
		if (fragment != null) {
			// If we're being re-created and have a fragment, we need to a) hide
			// the main UI controls and
			// b) hook up its listeners again.
			controlsContainer.setVisibility(View.GONE);
			if (fragment instanceof FriendPickerFragment) {
				// setFriendPickerListeners((FriendPickerFragment) fragment);
			} else if (fragment instanceof PlacePickerFragment) {
				// setPlacePickerListeners((PlacePickerFragment) fragment);
			}
		}

		// Listen for changes in the back stack so we know if a fragment got
		// popped off because the user
		// clicked the back button.
		fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
			@Override
			public void onBackStackChanged() {
				if (fm.getBackStackEntryCount() == 0) {
					// We need to re-show our UI.
					controlsContainer.setVisibility(View.VISIBLE);
				}
			}
		});

		// Can we present the share dialog for regular links?
		canPresentShareDialog = FacebookDialog.canPresentShareDialog(this,
				FacebookDialog.ShareDialogFeature.SHARE_DIALOG);
		// Can we present the share dialog for photos?
		canPresentShareDialogWithPhotos = FacebookDialog.canPresentShareDialog(
				this, FacebookDialog.ShareDialogFeature.PHOTOS);
	}

	public void changePhoto(ApplyEffect ae) {
		Bitmap changedPhoto = null;
		changedPhoto = ae.applyEffect(Instabook.this.image);
		// image = changedPhoto;
		Instabook.this.imageView.setImageBitmap(changedPhoto);

	}

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();
		AppEventsLogger.activateApp(this);

		updateUI();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
		outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			image = (Bitmap) data.getExtras().get("data");
			imageView.setImageBitmap(image);
		} else if (resultCode == RESULT_OK) {
			Uri chosenImageUri = data.getData();

			Bitmap mBitmap = null;

			BitmapFactory.Options bf = new BitmapFactory.Options();
			bf.inSampleSize = 8;

			try {
				mBitmap = BitmapFactory.decodeStream(getContentResolver()
						.openInputStream(chosenImageUri), null, bf);
				// mBitmap.
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			this.image = mBitmap;
			// image = "done";
			imageView.setImageBitmap(this.image);
		}
	}

	public void addEffect(ApplyEffect ae) {
		items.add(ae);
	}

	private void loadEffects() {
		BlackDotsApplyEffect hh = new BlackDotsApplyEffect(this);
		GammaApplyEffect hhh = new GammaApplyEffect(this);
		GreyScaleApplyEffect g = new GreyScaleApplyEffect(this);
		NegativeApplyEffect gg = new NegativeApplyEffect(this);
		OldBlackWhiteApplyEffect ggg = new OldBlackWhiteApplyEffect(this);
		SepiaToneApplyEffect k = new SepiaToneApplyEffect(this);
		SnowApplyEffect kk = new SnowApplyEffect(this);
		SunnyApplyEffect kkk = new SunnyApplyEffect(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
		AppEventsLogger.deactivateApp(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (pendingAction != PendingAction.NONE
				&& (exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException)) {
			new AlertDialog.Builder(Instabook.this)
					.setTitle(R.string.cancelled)
					.setMessage(R.string.permission_not_granted)
					.setPositiveButton(R.string.ok, null).show();
			pendingAction = PendingAction.NONE;
		} else if (state == SessionState.OPENED_TOKEN_UPDATED) {
			handlePendingAction();
		}
		updateUI();
	}

	private void updateUI() {
		Session session = Session.getActiveSession();
		boolean enableButtons = (session != null && session.isOpened());

		postPhotoButton.setEnabled(enableButtons
				|| canPresentShareDialogWithPhotos);

		if (enableButtons && user != null) {
			profilePictureView.setProfileId(user.getId());
			// greeting.setText(getString(R.string.hello_user,
			// user.getFirstName()));
		} else {
			profilePictureView.setProfileId(null);
			// greeting.setText(null);
		}

	}

	@SuppressWarnings("incomplete-switch")
	private void handlePendingAction() {
		PendingAction previouslyPendingAction = pendingAction;
		// These actions may re-set pendingAction if they are still pending, but
		// we assume they
		// will succeed.
		pendingAction = PendingAction.NONE;

		switch (previouslyPendingAction) {
		case POST_PHOTO:
			postPhoto();
			break;
		case POST_STATUS_UPDATE:
			// postStatusUpdate();
			break;
		}
	}

	private interface GraphObjectWithId extends GraphObject {
		String getId();
	}

	private void showPublishResult(String message, GraphObject result,
			FacebookRequestError error) {
		String title = null;
		String alertMessage = null;
		if (error == null) {
			title = getString(R.string.success);
			String id = result.cast(GraphObjectWithId.class).getId();
			alertMessage = getString(R.string.successfully_posted_post,
					message, id);
		} else {
			title = getString(R.string.error);
			alertMessage = error.getErrorMessage();
		}

		new AlertDialog.Builder(this).setTitle(title).setMessage(alertMessage)
				.setPositiveButton(R.string.ok, null).show();
	}

	private void onClickPostPhoto() {
		performPublish(PendingAction.POST_PHOTO,
				canPresentShareDialogWithPhotos);
	}

	private FacebookDialog.PhotoShareDialogBuilder createShareDialogBuilderForPhoto(
			Bitmap... photos) {
		return new FacebookDialog.PhotoShareDialogBuilder(this)
				.addPhotos(Arrays.asList(photos));
	}

	private void postPhoto() {
		if (canPresentShareDialogWithPhotos) {
			FacebookDialog shareDialog = createShareDialogBuilderForPhoto(image)
					.build();
			uiHelper.trackPendingDialogCall(shareDialog.present());
		} else if (hasPublishPermission()) {
			Request request = Request.newUploadPhotoRequest(
					Session.getActiveSession(), image, new Request.Callback() {
						@Override
						public void onCompleted(Response response) {
							showPublishResult(getString(R.string.photo_post),
									response.getGraphObject(),
									response.getError());
						}
					});
			request.executeAsync();
		} else {
			pendingAction = PendingAction.POST_PHOTO;
		}
	}

	private boolean hasPublishPermission() {
		Session session = Session.getActiveSession();
		return session != null
				&& session.getPermissions().contains("publish_actions");
	}

	private void performPublish(PendingAction action, boolean allowNoSession) {
		Session session = Session.getActiveSession();
		if (session != null) {
			pendingAction = action;
			if (hasPublishPermission()) {
				// We can do the action right away.
				handlePendingAction();
				return;
			} else if (session.isOpened()) {
				// We need to get new permissions, then complete the action when
				// we get called back.
				session.requestNewPublishPermissions(new Session.NewPermissionsRequest(
						this, PERMISSION));
				return;
			}
		}

		if (allowNoSession) {
			pendingAction = action;
			handlePendingAction();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            this.firstImage();
        }
        return super.onOptionsItemSelected(item);
    }
}
