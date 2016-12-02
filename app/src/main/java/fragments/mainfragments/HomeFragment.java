package fragments.mainfragments;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.vision.text.Text;
import com.tritiumlabs.grouper.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import interfaces.ExternalDB;
import objects.ExternalDBResponse;
import objects.Groupie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment{

    private TextView txtWelcome;
    private TextView txtUsername;
    private TextView txtCityState;
    private TextView txtUserbio;
    private TextView txtUserinformation;
    private TextView txtRecentActivity;
    private ImageView imgProfilePicture;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_home_fragment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home");

        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Typeface sab = Typeface.createFromAsset(getActivity().getAssets(), "Sabandija-font-ffp.ttf");

        txtWelcome = (TextView) view.findViewById(R.id.txtHomeWelcomeBack);
        txtUsername = (TextView) view.findViewById(R.id.txtHomeUserName);
        txtCityState = (TextView) view.findViewById(R.id.txtHomeCityState);
        txtUserbio = (TextView) view.findViewById(R.id.txtHomeUserbio);
        txtUserinformation = (TextView) view.findViewById(R.id.txtHomeUserinfo);
        txtRecentActivity = (TextView) view.findViewById(R.id.txtHomeRecentActivity);

        txtWelcome.setTypeface(sab);
        txtUsername.setTypeface(sab);
        txtCityState.setTypeface(sab);
        txtUserbio.setTypeface(sab);
        txtRecentActivity.setTypeface(sab);
        //txtUserinformation.setTypeface(sab);

        //TODO: Should be pulling this info from the database rather than a shared preference. -KD
        String username = sharedPref.getString("username", "");
        String citystate = sharedPref.getString("citystate", "");
        String userbio = sharedPref.getString("userbio", "");
        String userinfo = sharedPref.getString("userinfo", "");

        txtUsername.setText(username);

        if (citystate.equals("")) {
            txtCityState.setText("Enter State");
            txtCityState.setTextColor(getResources().getColor(R.color.primary_darkest_translucent));
        } else {
            txtCityState.setText(citystate);
        }

        if (userbio.equals("")) {
            txtUserbio.setText("Enter bio");
            txtUserbio.setTextColor(getResources().getColor(R.color.primary_darkest_translucent));
        } else {
            txtUserbio.setText(userbio);
        }

        if (userinfo.equals("")) {
            txtUserinformation.setText("Enter info");
            txtUserinformation.setTextColor(getResources().getColor(R.color.primary_darkest_translucent));
        } else {
            txtUserinformation.setText(userinfo);
        }

        try {
            ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File f = new File(directory, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imgProfilePicture = (ImageView) view.findViewById(R.id.imgHomeProfileImage);
            imgProfilePicture.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        final TextView areaCount = (TextView) view.findViewById(R.id.txtAreaCount);

        ExternalDB dbInterface = ExternalDB.retrofit.create(ExternalDB.class);
        final Call<List<ExternalDBResponse>> call = dbInterface.getAreaCount("usernameHere",5.0,5,2);

        call.enqueue(new Callback<List<ExternalDBResponse>>() {
            @Override
            public void onResponse(Call<List<ExternalDBResponse>> call, Response<List<ExternalDBResponse>> response)
            {

                Log.d("response: ", response.body().get(0).getMainResponse());
                Log.d("response: ", response.body().get(0).getResponseCode());
                Log.d("response: ", response.body().get(0).getResponseMessage());
                Log.d("response: ", response.body().get(0).getEchoInput());

                //TODO set this as a string resource and whatnot -AB

                areaCount.setText(response.body().get(0).getMainResponse() + " Users in your area!");


            }

            @Override
            public void onFailure(Call<List<ExternalDBResponse>> call, Throwable t)
            {
                Log.d("Tracker", t.getMessage());
            }
        });

        dildoTest();

        return view;
    }

    private void dildoTest()
    {
        Groupie derp = new Groupie();

        derp.setGroupieName("groupiename");
        derp.setGroupieCreator("testUser");
        derp.setGroupieDescription("buttplug party, cum get sum fuk");
        derp.setPrivateIndicator(false);
        derp.setGroupieStartDate("12/01/2016");
        derp.setGroupieStartTime("16:30");
        derp.setGroupieEndDate("12/01/2016");
        derp.setGroupieEndTime("20:00");
        derp.setGroupieLat(41.696969);
        derp.setGroupieLong(-71.424242);
        derp.setGroupieAddress(null);

        Log.d("DERP", derp.groupieCreationString());


    }


}
