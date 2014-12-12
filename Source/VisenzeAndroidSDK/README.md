Visenze Android SDK Getting-started Guide
=====

This tutorial guides the developer to start an Android project using the Visenze Android SDK step by step. Following the tutorial, a demonstrable Android app can be built. The demo app should be able to load an image file from the photo gallery and to start an image search. The search result is returned as list of image names.


##Prerequisites
* Android development environment. If haven't setup, follow the link to setup [Android Studio](https://developer.android.com/sdk/installing/studio.html)
* Visenze developer account with valid API key pair. If don't have, follow the [instruction](http://www.visenze.com/docs/overview/introduction) to register
* Some indexed images should be uploaded to your Visenze account already. You can do this through Dashboard or using the [data API](http://www.visenze.com/docs/api/data)


##1. Setup


###Integrate SDK
You can import the project under SDK/VisenzeSDK/ directory and explore the source code of the SDK. In this tutorial we focus on how to setup a new android project and deliver a simple app using the compiled jar library.

Add the dependency by

```
compile 'xxx.xxx.xxx:1.0.0'
```

This SDK provides all the search functions supported by Visenze search API that could be easily integrated into your Android apps. Start a new project in Android Studio and follow the steps to setup the dependency and development environment. 


###Add dependency
In `build.gradle` file, add the following lines under `andoid ` and `dependencies`. More about gradle configuration to build Android project can be found [here](http://tools.android.com/tech-docs/new-build-system/user-guide). 
```
android {
	...
	
	    packagingOptions {
        exclude 'META-INF/NOTICE'
        exclude 'META-INF/LICENSE'
    }
}

dependencies {
    
    compile 'xxx.visenze.android.sdk.xxx:x.x.x'
    compile 'org.apache.httpcomponents:httpmime:4.3.5'
    compile 'commons-codec:commons-codec:1.6'
    compile 'org.apache.httpcomponents:httpclient-android:4.3.5'
}
```
###Add user permissions
VisenzeSDK needs these user permissions to work. Add the following declarations to the `AndroidManifest.xml` file.  Network permission allows the app to connect to network services, while write/read to external storage permissions allow the app to load and save images on the device.

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.visenze.android.visenze_demo_test">

	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
	<uses-permission android:name="android.permission.INTERNET"/>
	<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
	<uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
	<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
	<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>

	<application>
	...
	</application>
</manifest>
```

##2. Initialization
The search functions are provided by the `ViSearcher` singleton. `ViSearcher` must be initialized with an accessKey/secretKey pair before it can be used. In order to be notified of the search result, A `ViSearcher.ResultListener` must be implemented. In this example, it is implemented by the activity itself. 

Remember to replace the key pair with your own keys and inform the `ViSearcher` instance of the listener by calling `setListener`.

```java
import android.app.Activity;
import android.os.Bundle;

import com.visenze.android.searchlib.ViSearcher;

public class MyActivity extends Activity implements ViSearcher.ResultListener{
	//set api access key / secret
    private static final String accessKey = "your_access_key";
    private static final String secretKey = "your_secret_key";

    private ViSearcher viSearcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        //get ViSearcher instance, init with key pair and set listener
        viSearcher = ViSearcher.getInstance();
        viSearcher.initSearch(this, accessKey, secretKey);
        viSearcher.setListener(this);
		
		//TODO: setup UI
    }
	
	...
	
	@Override
    public void onSearchResult(ResultList resultList) {
        
    }

    @Override
    public void onSearchError(String error) {
        
    }

}
```

##3. Set Up UI

We need to setup a simple UI to demonstrate the functions. `Button` is used to open photo gallery for image selection. `ImageView` is to display the image preview. Search result is shown as a `ListView`. Error message is shown as `TextView` if occurs. In `activity_my.xml` under res/layout folder, add the UI elements: 

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center_horizontal"
    android:orientation="vertical">
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_weight="3"
        android:background="#eeeeee"
        android:paddingTop="40dp"
        android:paddingBottom="40dp"
        android:paddingLeft="20dp"
        android:paddingRight="20dp">
        <ImageView
            android:id="@+id/image_preview"
            android:layout_width="match_parent"
            android:layout_height="45dp"
            android:src="@drawable/img_add_icon"
            android:layout_weight="1"/>
        <Button
            android:id="@+id/upload_button"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Upload Search"
            android:textColor="@android:color/white"
            android:layout_weight="1"/>
    </LinearLayout>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:background="@android:color/white"
        android:layout_weight="1">
        <TextView
            android:id="@+id/result_text"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textColor="@android:color/darker_gray"
            android:text="Search Result"
            android:layout_marginTop="20dp"
            android:layout_marginLeft="20dp"
            android:layout_marginRight="20dp"/>
        <ListView
            android:id="@+id/result_list"
            android:padding="30dp"
            android:scrollbars="none"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>
    </LinearLayout>
</LinearLayout>
```

In `MyActivity.java` class, instantiate the UI elements:

```java
public class MyActivity extends Activity implements ViSearcher.ResultListener{
    ...

    //setup UI
    private Button uploadButton;
    private ImageView imagePreview;
    private ListView resultListView;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
		
		//get ViSearcher instance, init with key pair and set listener
        ...
        
        //Setup UI
        uploadButton = (Button)findViewById(R.id.upload_button);
        imagePreview = (ImageView)findViewById(R.id.image_preview);
        resultListView = (ListView)findViewById(R.id.result_list);
        resultText = (TextView)findViewById(R.id.result_text);
		
		//TODO: implement button click
	}
}

```


##4. Set Search Parameters
`ViSearcher` provides three search functions: `idSearch`(*search by image name*), `colorSearch`(*search by color code*)  and `uploadSearch`(*search by an image*). Before the search session starts, search parameters should be correctly set. The following section gives a step-by-step introduction on how to use a `UploadSearchParams` to start a `uploadSearch` using an image selected from the gallery.

###Start activity to select image from gallery

Implement the button click function to start a new activity that opens the photo gallery for image selection. This requires a new `Intent` being started, which is implemented in the private method `startGallery`. `onActivityResult` method is override listening for a result returned from the image selection activity. 

```java
public class MyActivity extends Activity implements ViSearcher.ResultListener{
	
    //activity request code for intent to open album and get image uri
    private static final int RESULT_LOAD_IMAGE = 0x00;
    
    ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        ...
        
	//button click: start photo gallery
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGallery();
            }
        });

    }

    private void startGallery() {

        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        startActivityForResult(openAlbumIntent, RESULT_LOAD_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri uri = data.getData();

            //TODO: Decode and resize the image using the uri, start search
        }
    }
    
    ...
    
}
```

###Initialize Search Parameters

Image decode and resize are handled in `Image` class. A new `Image` instance can be constructed with `Uri` or `String` of a local file path. Optimization of the image size for upload is automatically performed. The `UploadSearchParams` is instantiated with `Image`:

```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri uri = data.getData();
            
            //Decode and resize the image using the uri
            Image image = new Image(this, uri);
            
			//update UI: set image preview
            imagePreview.setImageBitmap(image.getBitmap());
			
			//set upload parameters
            UploadSearchParams uploadSearchParams = new UploadSearchParams(image);
            
            //TODO: start search
         
        }
    }
```

##5. Start a Search Session
After the search parameters has been set, a search session can be started by calling `uploadSearch()` with the search parameters:

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {

super.onActivityResult(requestCode, resultCode, data);

if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
    Uri selectedImage = data.getData();
    
    //Decode and resize the image using the uri
    ...
    
    //start search
    viSearcher.uploadSearch(uploadSearchParams);
}
}
```


##6. Result Display
When a search result is returned, `ViSearcher` instance will call `onSearchResult` callback of `ViSearcher.ResultListener `. If error occurs, `onSearchError` is called. Implement the callbacks to further process the search result. The search result `ResultList` contains a list of `ResultList.ImageResult` and some other search result information. 

```java
	...
	
    @Override
    public void onGetResult(ResultList resultList) {
        resultText.setText("Result List: ");
        if(resultList != null && resultList.getImageList().size() > 0) {
            resultListView.setAdapter(new ResultListAdapter(this, resultList.getImageList()));
            resultListView.invalidate();
        }
    }

    @Override
    public void onSearchError(String s) {
        resultText.setText(s);

    }
```


`ResultListAdapter` need to be implemented to handle a list of object as input for viewing. It is in a separate class file. Create the class in a new .java file `ResultListAdapter.java`:

```java
package com.visenze.android.visenzesdk_demo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.visenze.android.searchlib.ResultList;

import java.util.List;

public class ResultListAdapter extends BaseAdapter {
    private Context mContext;
    private List<ResultList.ImageResult> resultList;

    public ResultListAdapter(Context context, List<ResultList.ImageResult> resultList) {
        mContext = context;
        this.resultList =  resultList;
    }

    public int getCount() {
        return resultList.size();
    }

    public Object getItem(int position) {
        return resultList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;

        if (convertView == null) {  
            textView = new TextView(mContext);
        } else {
            textView = (TextView) convertView;
        }

        textView.setText("Image Name: " + resultList.get(position).getImageName());

        return textView;
    }
}
```
##7. Run
Build and run the project. Now you can start a search with an image from the photo gallery
