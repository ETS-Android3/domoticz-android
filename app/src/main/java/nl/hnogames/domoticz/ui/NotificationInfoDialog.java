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
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import nl.hnogames.domoticz.R;
import nl.hnogames.domoticz.adapters.NotificationsAdapter;
import nl.hnogames.domoticzapi.Containers.NotificationInfo;

public class NotificationInfoDialog {

    private final MaterialDialog.Builder mdb;
    private final ArrayList<NotificationInfo> info;
    private final Context mContext;

    public NotificationInfoDialog(Context c,
                                  ArrayList<NotificationInfo> _info) {
        this.info = _info;
        this.mContext = c;

        mdb = new MaterialDialog.Builder(mContext);
        mdb.customView(R.layout.dialog_switch_timer, true)
                .positiveText(android.R.string.ok);
    }

    public void show() {
        mdb.title(R.string.category_notification);
        MaterialDialog md = mdb.build();
        View view = md.getCustomView();
        ListView listView = view.findViewById(R.id.list);
        NotificationsAdapter adapter = new NotificationsAdapter(mContext, info);
        listView.setAdapter(adapter);

        md.show();
    }
}
