package com.tarija.tresdos.ejemplotaxi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private View mRootView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mRootView = findViewById(R.id.root);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 777) {
            handleSignInResponse(resultCode, data);
        }
    }

    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (resultCode == ResultCodes.OK) {
            goMainScreen();
        } else {
            if (response == null) {
                Toast.makeText(mRootView.getContext(), "Sesion Cancelada", Toast.LENGTH_LONG).show();
                return;
            }
            if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                Toast.makeText(mRootView.getContext(), "Sin Conexion", Toast.LENGTH_LONG).show();
                return;
            }
            if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                Toast.makeText(mRootView.getContext(), "Error desconocido", Toast.LENGTH_LONG).show();
                return;
            }
        }
        Toast.makeText(this, "Respuesta desconocida", Toast.LENGTH_LONG).show();
    }
    public void signInPhone(View view){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()))
                        .build(),
                777);
    }
    public void goMainScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
