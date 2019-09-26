package com.example.iquarium;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.Context;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Map;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceContextBuilder;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class Chatbot extends AppCompatActivity implements AIListener {


    private AIService aiService;
    SessionsClient sessionsClient;
    SessionName session;
    private Button listenButton,chatbttn, clrbttn;
    private TextView nickname, resultTextView;
    private EditText userqry;
    private Context context;
    AIDataService aiDataService;
    View view;
    AIServiceContextBuilder customAIServiceCOntext;
    TextView connVar;
    RecyclerView recyclerView;
    EditText editText;
    RelativeLayout addBtn;
    DatabaseReference ref;
    FirebaseRecyclerAdapter adapter;
    Query query;
    Boolean flagFab = true;
    FirebaseRecyclerOptions<ChatMessage> options;
    String androidId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbot2);

       // connVar = findViewById(R.id.connectVar);
        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
/*
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                  connVar.setText("• Connected");
                  connVar.setTextColor(Color.GREEN);
                } else {
                 connVar.setText("• Not Connected");
                 connVar.setTextColor(Color.RED);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
              toasMessage("Listener was cancelled");
            }
        });


 */


        recyclerView = findViewById(R.id.recyclerView);
        editText = findViewById(R.id.editText);
        addBtn = findViewById(R.id.addBtn);


        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);

            final AIConfiguration config = new AIConfiguration("aa6eceedc96e435eb3388bc702ed56e9",
                    AIConfiguration.SupportedLanguages.English, AIConfiguration.RecognitionEngine.System);

         aiService = AIService.getService(this,config);
         aiService.setListener(this);

        aiDataService = new AIDataService(config);
        final AIRequest aiRequest = new AIRequest();


        welcomeMessage();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref = FirebaseDatabase.getInstance().getReference();
                ref.keepSynced(true);
               String message = editText.getText().toString().trim();

                if(!message.equals("")) {

                    ChatMessage chatMessage = new ChatMessage(message, "user");
                     ref.child(androidId).push().setValue(chatMessage);

                    aiRequest.setQuery(message);

                    new AsyncTask<AIRequest, Void, AIResponse>() {
                        @Override
                        protected AIResponse doInBackground(AIRequest... requests) {
                            final AIRequest request = requests[0];
                            try {
                                final AIResponse response = aiDataService.request(aiRequest);
                                return response;
                            } catch (AIServiceException e) {
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(AIResponse aiResponse) {
                            if (aiResponse != null) {

                                Result result = aiResponse.getResult();
                               String reply = result.getFulfillment().getSpeech();
                                ChatMessage chatMessage = new ChatMessage(reply, "bot");
                                    ref.child(androidId).push().setValue(chatMessage);

                            }

                        }
                    }.execute(aiRequest);
                } else{
                    aiService.startListening();
                }

                editText.setText("");

            }
        });


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ImageView fab_img = (ImageView)findViewById(R.id.fab_img);
                Bitmap img = BitmapFactory.decodeResource(getResources(),R.drawable.ic_send_white_24dp);
                    Bitmap img1 = BitmapFactory.decodeResource(getResources(),R.drawable.ic_mic_white_24dp);


                    if (s.toString().trim().length()!=0 && flagFab){
                        ImageViewAnimatedChange(Context.getDefaultInstance(),fab_img,img);
                    flagFab=false;

                }
                else if (s.toString().trim().length()==0){
                    ImageViewAnimatedChange(Context.getDefaultInstance(),fab_img,img1);
                    flagFab=true;

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        options =  new FirebaseRecyclerOptions.Builder<ChatMessage>().setQuery(ref.child(androidId), ChatMessage.class).build();
        adapter = new FirebaseRecyclerAdapter<ChatMessage, chat_rec>(options) {

            @Override
            protected void onBindViewHolder(@NonNull chat_rec viewHolder, int position, @NonNull ChatMessage model) {

                final String msgText = model.getMsgText();

                    if (model.getMsgUser().equals("user")) {

                        viewHolder.rightText.setText(msgText);

                        viewHolder.rightText.setVisibility(View.VISIBLE);
                        viewHolder.leftText.setVisibility(View.GONE);
                    }
                    else if(model.getMsgUser().equals("initiate"))
                    {
                        viewHolder.rightText.setText(msgText);

                        viewHolder.rightText.setVisibility(View.GONE);
                        viewHolder.leftText.setVisibility(View.GONE);
                    }
                    else {
                        viewHolder.leftText.setText(msgText);

                        viewHolder.rightText.setVisibility(View.GONE);
                        viewHolder.leftText.setVisibility(View.VISIBLE);
                    }

                }

            @NonNull
            @Override
            public chat_rec onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(Chatbot.this).inflate(R.layout.msglist, parent, false);

                return new chat_rec(view);
            }

        };


        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                int msgCount = adapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (lastVisiblePosition == -1 ||
                        (positionStart >= (msgCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);

                }

            }
        });

       adapter.startListening();

       recyclerView.setAdapter(adapter);

        isRecordAudioPermissionGranted();

    }



    @Override
    public void onBackPressed()
    {

        Intent intent = new Intent(this, MainActivity.class);
        clearMessage();
        startActivity(intent);

    }

    @Override
    protected void onDestroy()
    {
        clearMessage();
        super.onDestroy();

    }

    @Override
    protected void onRestart()
    {

        super.onRestart();
    }



    @Override
    protected void onStart()
    {


        super.onStart();

    }

    public void welcomeMessage(){

        final AIRequest aiRequest = new AIRequest();
        ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);
        String message = "Hi";


        ChatMessage chatMessage = new ChatMessage(message, "initiate");
        ref.child(androidId).push().setValue(chatMessage);

        aiRequest.setQuery(message);

        new AsyncTask<AIRequest, Void, AIResponse>() {
            @Override
            protected AIResponse doInBackground(AIRequest... requests) {
                final AIRequest request = requests[0];
                try {
                    final AIResponse response = aiDataService.request(aiRequest);
                    return response;
                } catch (AIServiceException e) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(AIResponse aiResponse) {
                if (aiResponse != null) {

                    Result result = aiResponse.getResult();
                    String reply = result.getFulfillment().getSpeech();
                    ChatMessage chatMessage = new ChatMessage(reply, "bot");
                    ref.child(androidId).push().setValue(chatMessage);

                }

            }
        }.execute(aiRequest);
    }


    private void toasMessage(String message)
    {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show();
    }

    public void ImageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(Chatbot.this, R.anim.zoom_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(Chatbot.this, R.anim.zoom_in);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageBitmap(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }

    private boolean isRecordAudioPermissionGranted()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) ==
                    PackageManager.PERMISSION_GRANTED) {
                // put your code for Version>=Marshmallow
                return true;
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                    Toast.makeText(this,
                            "App required access to audio", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO
                },50);
                return false;
            }

        } else {
            // put your code for Version < Marshmallow
            return true;
        }
    }
/*
    public void listenButtonOnClick(final View view){

            aiService.startListening();

    }
*/

    @Override
    public void onResult(final AIResponse response) {

        Result result = response.getResult();


        // Get parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }

        result = response.getResult();

        String message = result.getResolvedQuery();
        ChatMessage chatMessage0 = new ChatMessage(message, "user");
        ref.child(androidId).push().setValue(chatMessage0);


        String reply = result.getFulfillment().getSpeech();
        ChatMessage chatMessage = new ChatMessage(reply, "bot");
        ref.child(androidId).push().setValue(chatMessage);


    }

    @Override
    public void onError(AIError error) {
        toasMessage(error.toString());
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {


    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    // Clearing messages
    public void clearMessage()
    {
                ref = FirebaseDatabase.getInstance().getReference(androidId);
                ref.removeValue();

    }

}


