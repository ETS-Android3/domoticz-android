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

package nl.hnogames.domoticz.ui;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import nl.hnogames.domoticz.R;

public class SimpleTextDialog {

    private final MaterialDialog.Builder mdb;

    private final Context mContext;
    private String title;
    private String text;

    public SimpleTextDialog(Context mContext) {

        this.mContext = mContext;

        mdb = new MaterialDialog.Builder(mContext);

        boolean wrapInScrollView = true;

        //noinspection ConstantConditions
        mdb.customView(R.layout.dialog_text, wrapInScrollView)
                .positiveText(android.R.string.ok);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitle(int titleResourceId) {
        this.title = mContext.getResources().getString(titleResourceId);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setText(int textResourceId) {
        this.text = mContext.getResources().getString(textResourceId);
    }

    public void show() {
        mdb.title(title);
        MaterialDialog md = mdb.build();
        View view = md.getCustomView();

        TextView dialogText = view.findViewById(R.id.textDialog_text);
        dialogText.setText(text);
        md.show();
    }
}