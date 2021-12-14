package com.example.qrcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;


public class ScanQR extends AppCompatActivity {
    private static final String TAG = "Main_Activity";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView addressTxet, birthDayTxet, feverTxat, heightTxet, medicineTxet, nameTxet, phoneNumberTxet, weightTxet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        new IntentIntegrator(this).initiateScan();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                // todo
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                // todo
                DocumentReference docRef = db.collection("users").document(result.getContents());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData().toString());
                                Map<String, Object> getData = document.getData();
                                addressTxet.setText(getData.get("address").toString());
                                birthDayTxet.setText(getData.get("birthDay").toString());
                                feverTxat.setText(getData.get("fever").toString());
                                heightTxet.setText(getData.get("height").toString());
                                medicineTxet.setText(getData.get("medicine").toString());
                                nameTxet.setText(getData.get("name").toString());
                                phoneNumberTxet.setText(getData.get("phoneNumber").toString());
                                weightTxet.setText(getData.get("weight").toString());


                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }






}