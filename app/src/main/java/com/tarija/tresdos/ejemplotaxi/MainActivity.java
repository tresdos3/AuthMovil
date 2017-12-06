package com.tarija.tresdos.ejemplotaxi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {

    private TextView mPhoneTextView;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPhoneTextView = (TextView) findViewById(R.id.phoneTextView);
        mDatabase = FirebaseDatabase.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            myRef = mDatabase.getReference("choferes/"+user.getPhoneNumber());
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.d("Mensaje", "El UID existe");
                        myRef = mDatabase.getReference("choferes/"+user.getPhoneNumber()+"/token");
                        myRef.setValue(FirebaseInstanceId.getInstance().getToken());
                        myRef = mDatabase.getReference("choferes/"+user.getPhoneNumber()+"/estado");
                        myRef.setValue("A");
                    }
                    else {
                        Log.d("Mensaje", "El UID no existe");
                        signOut();
                        Toast("Usted no esta registrado en nuestra base de datos, pase por las oficina correspondiente");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
//
            populateProfile(user);
        } else {
            goLogInScreen();
        }
    }
    private void populateProfile(FirebaseUser user) {
        mPhoneTextView.setText(user.getPhoneNumber());
    }
    public void signOut(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            goLogInScreen();
                        } else {
                            Toast("Error al cerrar session");
                        }
                    }
                });
    }
    public void signOut(View view) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            goLogInScreen();
                        } else {
                            Toast("Error al cerrar session");
                        }
                    }
                });
    }
    private void goLogInScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private  void Toast(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}
