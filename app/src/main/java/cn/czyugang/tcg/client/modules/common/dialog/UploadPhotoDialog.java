package cn.czyugang.tcg.client.modules.common.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseDialogFragment;

/**
 * Created by wuzihong on 2017/9/30.
 * 上传图片对话框
 */

public class UploadPhotoDialog extends BaseDialogFragment {
    private static final int REQUEST_CAMERA = 100;//拍照
    private static final int REQUEST_ALBUM = 101;//相册
    private static final int REQUEST_CROP = 102;//裁剪
    private Uri mImageUri;//图片uri
    private int mWidth;
    private int mHeight;
    private OnUploadPhotoListener mListener;

    public static UploadPhotoDialog newInstance() {
        return new UploadPhotoDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.dialog_upload_photo, container);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    startActivityForResult(createCrop(mImageUri, mWidth, mHeight), REQUEST_CROP);
                    break;
                case REQUEST_ALBUM:
                    if (data.getData() != null) {
                        startActivityForResult(createCrop(data.getData(), mWidth, mHeight), REQUEST_CROP);
                    }
                    break;
                case REQUEST_CROP:
                    if (mListener != null) {
                        mListener.onUploadPhoto(mImageUri);
                    }
                    dismiss();
                    break;
            }
        } else {
            dismiss();
        }
    }

    @Override
    protected void initWindow(Window window) {
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
    }

    /**
     * 设置裁剪尺寸
     *
     * @param width
     * @param height
     * @return
     */
    public UploadPhotoDialog setCropSize(int width, int height) {
        mWidth = width;
        mHeight = height;
        return this;
    }

    public UploadPhotoDialog setOnUploadPhotoListener(OnUploadPhotoListener listener) {
        mListener = listener;
        return this;
    }

    /**
     * 创建裁剪完图片存储文件
     *
     * @return 创建文件是否成功
     */
    private Uri createFile() {
        //创建拍照图片存储文件，拿到uri
        if (Environment.getExternalStorageDirectory() != null) {
            File cameraDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + "tcg");
            if (!cameraDir.exists()) {
                cameraDir.mkdirs();
            }
            mImageUri = Uri.fromFile(new File(cameraDir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg"));
            return mImageUri;
        } else {
            Toast.makeText(context, "未安装SD卡", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /**
     * 获取调用拍照intent
     *
     * @return
     */
    private Intent createCamera() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        return openCameraIntent;
    }

    /**
     * 获取调用相册intent
     *
     * @return
     */
    private Intent createAlbum() {
        return new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    /**
     * 获取调用裁剪intent
     *
     * @param uri
     * @param x
     * @param y
     * @return
     */
    private Intent createCrop(Uri uri, int x, int y) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        // 照片URL地址
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        if (x > 0 && y > 0) {
            intent.putExtra("aspectX", x);
            intent.putExtra("aspectY", y);
            intent.putExtra("outputX", x);
            intent.putExtra("outputY", y);
        }
        // 输出路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        // 输出格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 不启用人脸识别
        intent.putExtra("noFaceDetection", false);
        intent.putExtra("return-data", false);
        return intent;
    }

    @OnClick({R.id.tv_camera, R.id.tv_album, R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_camera:
                if (createFile() != null) {
                    startActivityForResult(createCamera(), REQUEST_CAMERA);
                }
                break;
            case R.id.tv_album:
                if (createFile() != null) {
                    startActivityForResult(createAlbum(), REQUEST_ALBUM);
                }
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }

    public interface OnUploadPhotoListener {
        void onUploadPhoto(Uri uri);
    }
}
