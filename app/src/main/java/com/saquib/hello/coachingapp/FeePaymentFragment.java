package com.saquib.hello.coachingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.Arrays;
import java.util.List;


public class FeePaymentFragment extends Fragment {

    public View view;
    public Button takeSnap,detectText;
    public TextView textView;
    public FeePaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fee_payment_layout, container, false);
        detectText = view.findViewById(R.id.DetectCard);
        takeSnap = view.findViewById(R.id.TakeSnap);
        textView = view.findViewById(R.id.RecognizedText);
        detectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectTxt();
            }
        });
        takeSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).dispatchTakePictureIntent();
            }
        });
        return view;
    }

    public void detectTxt()
    {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(((MainActivity)getActivity()).imageBitmap);
        FirebaseVisionCloudTextRecognizerOptions options = new FirebaseVisionCloudTextRecognizerOptions.Builder()
                .setLanguageHints(Arrays.asList("en", "hi"))
                .build();
        FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance()
                .getCloudTextRecognizer(options);
        Task<FirebaseVisionText> results = textRecognizer.processImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                ProcessText(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Unable to Recognize the Text",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void ProcessText(FirebaseVisionText text)
    {
        String s="";
        List<FirebaseVisionText.TextBlock> blocks = text.getTextBlocks();
        if(blocks.size()==0)
        {
            Toast.makeText(getContext(),"Sorry no text is There to Detect",Toast.LENGTH_LONG).show();
        }
        else
        {
            for(FirebaseVisionText.TextBlock b : text.getTextBlocks())
            {
                s+=b.getText()+" ";
            }
            textView.setText(s);
        }

    }
}
