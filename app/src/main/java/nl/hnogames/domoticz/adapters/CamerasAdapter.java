/*
 * Copyright (C) 2015 Domoticz - Mark Heinis
 *
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package nl.hnogames.domoticz.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import github.nisrulz.recyclerviewhelper.RVHAdapter;
import github.nisrulz.recyclerviewhelper.RVHViewHolder;
import nl.hnogames.domoticz.R;
import nl.hnogames.domoticz.utils.PicassoUtil;
import nl.hnogames.domoticz.utils.SharedPrefUtil;
import nl.hnogames.domoticzapi.Containers.CameraInfo;
import nl.hnogames.domoticzapi.Domoticz;

@SuppressWarnings("unused")
public class CamerasAdapter extends RecyclerView.Adapter<CamerasAdapter.DataObjectHolder> implements RVHAdapter {
    public static List<String> mCustomSorting;
    private static onClickListener onClickListener;
    private final Context mContext;
    private SharedPrefUtil mSharedPrefs;
    private ArrayList<CameraInfo> mDataset;
    private Domoticz domoticz;
    private boolean refreshTimer;
    private Picasso picasso;

    public CamerasAdapter(ArrayList<CameraInfo> data, Context mContext, final Domoticz domoticz, boolean refreshTimer) {
        this.mContext = mContext;
        mSharedPrefs = new SharedPrefUtil(mContext);
        this.refreshTimer = refreshTimer;
        this.domoticz = domoticz;

        picasso = new PicassoUtil().getPicasso(mContext, domoticz.getSessionUtil().getSessionCookie(),
                domoticz.getUserCredentials(Domoticz.Authentication.USERNAME), domoticz.getUserCredentials(Domoticz.Authentication.PASSWORD));
        if (mCustomSorting == null)
            mCustomSorting = mSharedPrefs.getSortingList("cameras");
        setData(data);
    }

    public void setData(ArrayList<CameraInfo> data) {
        ArrayList<CameraInfo> sortedData = SortData(data);
        this.mDataset = sortedData;
    }

    public void setOnItemClickListener(onClickListener onClickListener) {
        CamerasAdapter.onClickListener = onClickListener;
    }

    private ArrayList<CameraInfo> SortData(ArrayList<CameraInfo> data) {
        ArrayList<CameraInfo> customdata = new ArrayList<>();
        if (mSharedPrefs.enableCustomSorting() && mCustomSorting != null) {
            for (String s : mCustomSorting) {
                for (CameraInfo d : data) {
                    if (s.equals(String.valueOf(d.getIdx())))
                        customdata.add(d);
                }
            }
            for (CameraInfo d : data) {
                if (!customdata.contains(d))
                    customdata.add(d);
            }
        } else
            customdata = data;
        return customdata;
    }

    private void SaveSorting() {
        List<String> ids = new ArrayList<>();
        for (CameraInfo d : mDataset) {
            ids.add(String.valueOf(d.getIdx()));
        }
        mCustomSorting = ids;
        mSharedPrefs.saveSortingList("cameras", ids);
    }

    @NonNull
    @Override
    public DataObjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.camera_row, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataObjectHolder holder, int position) {
        if (mDataset != null && mDataset.size() > 0) {
            CameraInfo cameraInfo = mDataset.get(position);
            String name = cameraInfo.getName();
            String address = cameraInfo.getAddress();
            String imageUrl = cameraInfo.getSnapShotURL();

            int numberOfDevices = cameraInfo.getDevices();
            try {
                String text = mContext.getResources().getQuantityString(R.plurals.devices, numberOfDevices, numberOfDevices);
                holder.name.setText(name);

                if (holder.camera.getDrawable() == null) {
                    picasso.load(imageUrl)
                            .placeholder(R.drawable.placeholder)
                            //.error(mSharedPrefs.darkThemeEnabled() ? R.drawable.baseline_error_outline_white_24 : R.drawable.baseline_error_outline_black_24)
                            .into(holder.camera);
                } else
                    picasso.load(imageUrl)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .noFade()
                            .noPlaceholder()
                            //.error(mSharedPrefs.darkThemeEnabled() ? R.drawable.baseline_error_outline_white_24 : R.drawable.baseline_error_outline_black_24)
                            .into(holder.camera);
            } catch (Exception ex) {
                Log.i("CameraAdapter", ex.getMessage());
            }
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        swap(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position, int direction) {
        remove(position);
    }

    private void remove(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    private void swap(int firstPosition, int secondPosition) {
        Collections.swap(mDataset, firstPosition, secondPosition);
        notifyItemMoved(firstPosition, secondPosition);
        SaveSorting();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setRefreshTimer(boolean timer) {
        this.refreshTimer = timer;
    }

    public interface onClickListener {
        void onItemClick(int position, View v);
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, RVHViewHolder {
        TextView name;
        ImageView camera;

        public DataObjectHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            camera = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onItemClick(getLayoutPosition(), v);
        }

        @Override
        public void onItemSelected(int actionstate) {
            System.out.println("Item is selected");
        }

        @Override
        public void onItemClear() {
            System.out.println("Item is unselected");
        }
    }
}