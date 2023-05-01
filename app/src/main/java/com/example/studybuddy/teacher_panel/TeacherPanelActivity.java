package com.example.studybuddy.teacher_panel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studybuddy.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.studybuddy.R;
import com.example.studybuddy.admin_panel.AdminPanelActivity;
import com.example.studybuddy.models.ClassLink;

import java.util.HashMap;

public class TeacherPanelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_panel);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());

        findViewById(R.id.teacherPanelSeeAllVideos).setOnClickListener(view -> startActivity(new Intent(TeacherPanelActivity.this, VideoListShowWithDeleteActivity.class)));
        findViewById(R.id.teacherPanelTakeClassNow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherPanelActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.teacherPanelUploadLinkVideos).setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(TeacherPanelActivity.this);
            View v = getLayoutInflater().inflate(R.layout.teacher_video_upload_popup, null);

            final EditText title = v.findViewById(R.id.popupVideoTitleID);
            final EditText link = v.findViewById(R.id.popupVideoLinkID);
            Button cancelButton = v.findViewById(R.id.popupCancelButtonID);
            Button uploadButton = v.findViewById(R.id.popupUploadButtonID);

            title.setHint("Enter the topic");
            link.setHint("Enter class details");

            builder.setView(v);

            final AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);

            cancelButton.setOnClickListener(view12 -> alertDialog.dismiss());

            uploadButton.setOnClickListener(view1 -> {
                String title_text = title.getText().toString().trim();
                String link_text = link.getText().toString().trim();

                if (title_text.isEmpty()) {
                    title.setError("Enter the topic");
                    title.requestFocus();
                    return;
                }

                if (link_text.isEmpty()) {
                    link.setError("Enter class details");
                    link.requestFocus();
                    return;
                }

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("YoutubeLinks");

                String id = ref.push().getKey();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", id);
                hashMap.put("title", title_text);
                hashMap.put("link", link_text);

                ref.child(id).setValue(hashMap);

                Toast.makeText(TeacherPanelActivity.this, "Class updated", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            });
            alertDialog.show();
        });
    }
}
