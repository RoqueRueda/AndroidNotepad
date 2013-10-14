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

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Save a file using a text input also gets the text of a file
 * and presents as a string.
 * Created by Roque Rueda on 25/06/13.
 */
public class TextFile {

    private Context mContext;
    private static final String ENCODING = "utf8";
    private static final String TAG = "TextFile";
    private static final String DIR = "/textFiles";
    private boolean mExternalStorageAvailable;
    private boolean mExternalStorageWriteable;

    public TextFile(Context ctx) {
        mContext = ctx;

        String state = Environment.getExternalStorageState();

        // We need to know if we can write to external storage
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
    }

    /**
     * Create a file in the device file system.
     * @param name Path where a new text file is going to be created.
     * @param content Text content of the file.
     * @return True if the file can be created.
     * @throws java.io.IOException if there's a problem creating the new file.
     */
    public boolean createFile(String name, String content)
            throws IOException {
        OutputStreamWriter osw = null;
        try
        {
            FileOutputStream fileOutputStream = mContext.openFileOutput(name, Context.MODE_PRIVATE);
            osw = new OutputStreamWriter(fileOutputStream, ENCODING);
            osw.write(content);
            osw.flush();
            Log.w(TAG, String.format("A new file is created named:%s", name));
            return true;
        } finally {
            osw.close();
        }
    }

    /**
     * Creates a file if can in the sd card of this device, otherwise
     * creates the file in the the internal storage.
     * @param name name of the file.
     * @param content content of the file.
     * @return true if the file can be created.
     * @throws java.io.IOException if the file can't be created.
     */
    public boolean createSDFile(String name, String content)
            throws IOException {
        // We can write in the sd storage.
        if (mExternalStorageAvailable && mExternalStorageWriteable) {
            OutputStreamWriter osw = null;
            try
            {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + DIR);
                // Creates a new folder.
                dir.mkdirs();

                if (!dir.isDirectory())
                    return false;

                File file = new File(dir, name);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                osw = new OutputStreamWriter(fileOutputStream, ENCODING);
                osw.write(content);
                osw.flush();

                String saveFileMessage = mContext.getString(R.string.save_file) + name;

                Log.w(TAG, saveFileMessage);

                Toast.makeText(mContext, saveFileMessage, Toast.LENGTH_SHORT).show();

                return true;
            }
            finally {
                if (osw != null)
                osw.close();
            }
        } else {
            return createFile(name, content);
        }
    }

    /**
     * Reads the contents of a sd card file.
     * @param name name of the file that is going to be read.
     * @return Contents of the file as string.
     * @throws java.io.IOException if the file can't be found.
     */
    public String getSDFileContent(String name)
            throws IOException
    {
        if (mExternalStorageAvailable) {
            BufferedReader br = null;
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + DIR);
                File file = new File(dir, name);
                // This object contains the text will store the content of the file.
                StringBuilder text = new StringBuilder(400);
                br = new BufferedReader(new FileReader(file));
                String line;
                while((line = br.readLine()) != null) {
                    text.append(line).append('\n');
                }
                Log.w(TAG, String.format("File %s is read", name));
                return text.toString();
            } finally {
              if (br != null)
                  br.close();
            }
        }
        else {
            return getFileContent(name);
        }
    }

    /**
     * Read the contents of the file and returns as a string.
     * @param name Path of the file that is going to be read.
     * @return Content of the file.
     * @throws java.io.IOException if the file can't be found or if there's a problem
     * reading the content of he file.
     */
    public String getFileContent(String name)
            throws IOException{
        BufferedReader br = null;
        try {
            StringBuilder content = new StringBuilder(350);
            FileInputStream fileInputStream = mContext.openFileInput(name);
            InputStreamReader isr = new InputStreamReader(fileInputStream, ENCODING);
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null){
                content.append(line).append("\n");
            }
            Log.w(TAG, String.format("File %s is read", name));
            return content.toString();
        } finally {
            if (br != null)
            br.close();
        }
    }

}
