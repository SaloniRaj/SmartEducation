package com.nhce.saloni.smartEducation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.nhce.saloni.smartEducation.data.model.TVSerial;
import com.nhce.saloni.smartEducation.data.table.TVSeries;

import java.util.HashMap;
import java.util.stream.Collectors;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        setListActivityObserver();
    }

    private void setListActivityObserver() {
        TVSeries.getInstance().datatableObservableSubject.subscribe(new Observer<HashMap<String, TVSerial>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(HashMap<String, TVSerial> tVSeries) {
                ((TextView) findViewById(R.id.MovieDeals)).setText(
                        tVSeries.keySet().stream().map(id -> tVSeries.get(id).getTitle()).collect(Collectors.joining(", \n"))
                );
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
            }
        });
    }
}