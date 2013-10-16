/*
 * Copyright 2013 Roque Rueda.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.roque.rueda.notepad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * This fragment is used to show, edit and save text into a txt file.
 *
 * Created by Roque on 28/07/13.
 */
public class EditTextFragment extends android.support.v4.app.Fragment implements TextInput {


    private EditText mEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.edit_text, container, false);
        mEditText = (EditText) rootView.findViewById(R.id.text);

        if(savedInstanceState != null)
        {
            if (savedInstanceState.containsKey(Constants.ARG_TEXT_FILE)){
                mEditText.setText(getArguments().getString(Constants.ARG_TEXT_FILE));
            }
        }

        return rootView;
    }

    @Override
    public CharSequence getText() {
        return mEditText.getText();
    }

    @Override
    public void setText(CharSequence text) {
        mEditText.setText(text);
        mEditText.setSelection(text.length());
    }
}
