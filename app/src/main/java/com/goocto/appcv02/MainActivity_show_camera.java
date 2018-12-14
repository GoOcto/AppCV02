package com.goocto.appcv02;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;

import org.opencv.android.JavaCameraView;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;


// OpenCV Classes
public class MainActivity_show_camera extends AppCompatActivity implements CvCameraViewListener2 {

    // Used for logging success or failure messages
    private static final String TAG = "OCVSample::Activity";

    // Loads camera view of OpenCV for us to use. This lets us see using OpenCV
    private CameraBridgeViewBase mOpenCvCameraView;

    // Used in Camera selection from menu (when implemented)
    private boolean              mIsJavaCamera = true;
    private MenuItem             mItemSwitchCamera = null;

    // These variables are used (at the moment) to fix camera orientation from 270degree to 0degree
    Mat mRgba;
    Mat mRgbaF;
    Mat mRgbaT;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public MainActivity_show_camera() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.show_camera);

        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.show_camera_activity_java_surface_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {

        String msg = String.format("Camera dimensions: %d x %d",width,height);
        Log.d(TAG,msg);

        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mRgbaF = new Mat(height, width, CvType.CV_8UC4);
        mRgbaT = new Mat(width, width, CvType.CV_8UC4);
    }

    public void onCameraViewStopped() {
        //mRgba.release();
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {

        mRgba = inputFrame.rgba();
        //Mat mGray = inputFrame.gray();


        //mRgba = Scalar::all( new Scalar(255,0,0,0) );
        Mat mOut = new Mat( 480,720, CvType.CV_8UC4 );
        mOut.setTo( new Scalar(255,0,0,0));
        return mOut;

        //mRgba.setTo( new Scalar(255,0,0,0));
        //return mRgba;

//        String msg = String.format("Frame dimensions: %d x %d",mRgba.cols(),mRgba.rows());
//        Log.d(TAG,msg);
//
//        // Rotate mRgba 90 degrees
//        Core.transpose(mRgba, mRgbaT);
//
//        Imgproc.resize(mRgbaT, mRgbaF, mRgbaF.size(), 0,0, 0);
//        Size sz = new Size( mRgbaT.cols(), mRgbaT.rows()*2 );
//        Imgproc.resize(mRgbaT, mRgbaF, sz, 1,2, 0);
//
//        return mRgbaF;

        //Core.flip(mRgbaF, mRgba, -1 );
        //return mRgba;
//
////        // work in B&W
////        Mat mGray = new Mat();
////        Imgproc.cvtColor(mRgba,mGray,Imgproc.COLOR_BGRA2GRAY);
//
//        // detect edges
//        Mat mEdges = new Mat();
//        int lowThreshold = 100;
//        int ratio = 3;
//        Imgproc.Canny(mRgba,mEdges,lowThreshold,lowThreshold*ratio);
//
////        Mat lines = new Mat();
////        Imgproc.HoughLinesP(mEdges,lines,2,5*Math.PI/180,50,50,10);
////
////        String str = String.format("%d %d", lines.rows(), lines.cols() );
////        Log.d(TAG,str);
////
////        for ( int i=0; i<lines.rows(); i++) {
////            double[] val = lines.get(i,0);
////            Point p1 = new Point(val[0],val[1]);
////            Point p2 = new Point(val[2],val[3]);
////            Scalar c = new Scalar(255,0,0);
////            Imgproc.line(mRgba, p1,p2,c,2);
////        }
//
//        //return mRgba; // This function must return
//        return mEdges; // This function must return
    }
}