package edu.byu.cs.client.view.main.startup;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import edu.byu.cs.client.R;
import edu.byu.cs.client.presenter.RegisterPresenter;
import edu.byu.cs.client.view.asyncTasks.RegisterTask;
import edu.byu.cs.client.view.main.MainActivity;
import request.RegisterRequest;
import response.LoginResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RegisterFragment extends Fragment implements RegisterPresenter.View {
    private ImageView profileIcon;
    private TextView editPhoto;
    private EditText registerEmail;
    private EditText registerPassword;
    private EditText registerFirstName;
    private EditText registerLastName;
    private EditText registerHandle;
    private Button registerButton;
    private RegisterPresenter presenter;
    private final int READ_EXTERNAL_PERMISSION = 10;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        presenter = new RegisterPresenter(this);
        profileIcon = v.findViewById(R.id.profileIcon);
        Drawable defaultIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_camera).colorRes(R.color.colorPrimary).sizeDp(100);
        profileIcon.setImageDrawable(defaultIcon);
        editPhoto = v.findViewById(R.id.editPhoto);
        registerEmail = v.findViewById(R.id.registerEmail);
        registerPassword = v.findViewById(R.id.registerPassword);
        registerFirstName = v.findViewById(R.id.registerFirstName);
        registerLastName = v.findViewById(R.id.registerLastName);
        registerHandle = v.findViewById(R.id.registerHandle);
        registerButton = v.findViewById(R.id.registerButton);
        registerButton.setEnabled(false);
        this.setListeners();
        return v;
    }

    private void validateForm() {
        if (!(registerEmail.getText().toString().equals("") || registerPassword.getText().toString().equals("")
                || registerFirstName.getText().toString().equals("") || registerLastName.getText().toString().equals("")
                || registerHandle.getText().toString().equals("") || profileIcon.getDrawable() instanceof IconDrawable)) {
            registerButton.setEnabled(true);
        } else {
            registerButton.setEnabled(false);
        }
    }

    private void setListeners() {
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateForm();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        registerEmail.addTextChangedListener(tw);
        registerPassword.addTextChangedListener(tw);
        registerFirstName.addTextChangedListener(tw);
        registerLastName.addTextChangedListener(tw);
        registerHandle.addTextChangedListener(tw);
        registerButton.setOnClickListener(v -> {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ((BitmapDrawable)profileIcon.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, baos);
            String encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

            new RegisterTask(presenter).setListener(this::userRetrieved)
            .execute(new RegisterRequest(registerEmail.getText().toString(), registerPassword.getText().toString(),
                    registerFirstName.getText().toString(), registerLastName.getText().toString(),
                    registerHandle.getText().toString(), encodedImage));
        });
        profileIcon.setOnClickListener(v1 -> selectImage());
        editPhoto.setOnClickListener(v1 -> selectImage());
    }

    private void selectImage() {
        final CharSequence[] options = {"Camera", "Choose from Gallery", "Cancel"};
        Context context = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Image Source");
        builder.setItems(options, (dialog, which) -> {
            if (options[which].equals("Camera")) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            } else if (options[which].equals("Choose from Gallery")) {
                Activity activity = getActivity();
                if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    }
                    else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                READ_EXTERNAL_PERMISSION);
                    }
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
            }
            else if (options[which].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) { // if no more permissions needed, change to if statement
            case READ_EXTERNAL_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else {
                    Toast.makeText(getActivity(), "Permission denied; Cannot access photos.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                profileIcon.setImageBitmap(imageBitmap);
            } else if (requestCode == 2) {
                Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    // make own resize function
                    profileIcon.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            validateForm();
        }
    }

    public void userRetrieved(LoginResponse response) {
        if (response.isSuccess()) {
            presenter.setCurrentUser(response.getUser());
            presenter.setAuthtoken(response.getAuthtoken());
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
        else {
            Toast.makeText(getActivity(), response.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
