package com.nhce.saloni.smartEducation.data.persistence.operations;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.nhce.saloni.smartEducation.data.persistence.core.RealTimeDatabaseFirebaseConfig;

import java.util.HashMap;

import io.reactivex.subjects.PublishSubject;

public class RealTimeDatabaseFirebase<T> extends RealTimeDatabaseFirebaseConfig {

    public PublishSubject<HashMap<String, T>> datatableObservableSubject;

    protected RealTimeDatabaseFirebase(String pathFromRootNode, final Class<T> serializeToClass) {
        super(pathFromRootNode);

        this.datatableObservableSubject = PublishSubject.create();
        setChildEventListener(serializeToClass);
    }

    @Override
    protected void setChildEventListener(final Class serializeToClass) {
        this.databaseReference.addChildEventListener(getChildEventListener(serializeToClass));
    }

    public Boolean insertDataRow(T dataRow) {
        Boolean returnFlag;
        try {
            this.databaseReference.push().setValue(dataRow);
            returnFlag = true;
        } catch (Exception e) {
            returnFlag = false;
        }
        return returnFlag;
    }

    public ChildEventListener getChildEventListener(final Class<T> serializeToClass){
        return new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                datatable.put(snapshot.getKey(), snapshot.getValue(serializeToClass));
                datatableObservableSubject.onNext(datatable);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                datatable.computeIfPresent(snapshot.getKey(), (k, v) -> snapshot.getValue(serializeToClass));
                datatableObservableSubject.onNext(datatable);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                datatable.remove(snapshot.getKey());
                datatableObservableSubject.onNext(datatable);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

}
