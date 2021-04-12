package com.SuperClub.EQ.Menu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.SuperClub.EQ.Application.Application;
import com.SuperClub.EQ.Data.QueueInfo;
import com.SuperClub.EQ.R;
import com.SuperClub.EQ.network.RequestController;
import com.android.volley.VolleyError;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainMenuMiddleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenuMiddleFragment extends Fragment {

    private FloatingActionButton createQueueButton;
    private ImageButton searchButton;
    private ImageButton qrButton;
    private EditText codeText;
    private View progressOverlay;
    private ProgressBar progressBar;
    private Application instance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        instance = Application.getInstance(getContext());
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_main_menu, container, false);
        searchButton = root.findViewById(R.id.search_button);
        codeText = root.findViewById(R.id.title_text);
        qrButton = root.findViewById(R.id.qr_button);
        createQueueButton = root.findViewById(R.id.create_queue);
        createQueueButton.setOnClickListener(this::createQueueButtonClick);

        searchButton.setOnClickListener(this::searchButtonClick);
        qrButton.setOnClickListener(this::qrButtonClick);


        progressBar = root.findViewById(R.id.spin_kit);
        Sprite sprite = new Wave();
        progressBar.setIndeterminateDrawable(sprite);
        progressBar.setVisibility(View.VISIBLE);

        progressOverlay = root.findViewById(R.id.loading_layout);
        progressOverlay.setVisibility(View.INVISIBLE);

        return root;
    }

    private void createQueueButtonClick(View view) {
        Intent intent = new Intent(getActivity(), CreateQueueActivity.class);
        startActivity(intent);
    }

    public void searchButtonClick(View view) {
        progressOverlay.setVisibility(View.VISIBLE);
        String code = codeText.getText().toString();
        RequestController.getInstance(getContext()).sendQueueByCodeRequest(code, this::searchResponse, this::searchErrorResponse);
    }

    private void searchResponse(JSONObject response) {
        QueueInfo queue = new Gson().fromJson(response.toString(), QueueInfo.class);
        instance.passedQueue = queue;
        progressOverlay.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(getActivity(), QueueInfoActivity.class);
        startActivity(intent);
    }

    public void searchErrorResponse(VolleyError error) {
        progressOverlay.setVisibility(View.INVISIBLE);
        Toast toast = Toast.makeText(getActivity(), "Something went wrong" + error.toString(), Toast.LENGTH_LONG);
        toast.show();
    }


    public void qrButtonClick(View view) {
        Intent intent = new Intent(getActivity(), ScanQR.class);

        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        String code = data.getStringExtra("code");
        codeText.setText(code);
        searchButtonClick(null);
    }

}