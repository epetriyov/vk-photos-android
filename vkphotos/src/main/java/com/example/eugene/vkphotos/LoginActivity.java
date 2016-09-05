package com.example.eugene.vkphotos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

/**
 * Created by Eugene on 04.09.2016.
 */
public class LoginActivity extends Activity {

    private String[] permisssions = {VKScope.PHOTOS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!VKSdk.isLoggedIn()) {
            setContentView(R.layout.act_login);
            findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VKSdk.login(LoginActivity.this, permisssions);
                }
            });
        } else {
            openPhotos();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                openPhotos();
            }

            @Override
            public void onError(VKError error) {
                Utils.showMessage(LoginActivity.this, error.errorMessage);
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void openPhotos() {
        startActivity(new Intent(this, ImagesGridActivity.class));
        finish();
    }
}
