package com.webbi.redes.lovefood;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MatchFragment extends Fragment {


    public MatchFragment() {


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view =  inflater.inflate(R.layout.fragment_match, container, false);


        return  view;


    }

}