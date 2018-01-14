package cn.czyugang.tcg.client.utils.img;

import android.app.Activity;
import android.content.Intent;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Progress;

/**
 * @author ruiaa
 * @date 2017/12/21
 */

public class UploadImg {

    public String compressPath="";
    public String uploadId = "";

    public void openSelector(Activity activity) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.picture_white_style)
                .selectionMode(PictureConfig.SINGLE)
                .glideOverride(160, 160)
                .compress(true)
                .minimumCompressSize(100)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    public boolean parseResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != PictureConfig.CHOOSE_REQUEST) return false;
        if (resultCode == Activity.RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            // 例如 LocalMedia 里面返回三种path
            // 1.media.getPath(); 为原图path
            // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
            // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
            // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
            if (selectList.size() > 0) {
                compressPath=selectList.get(0).getCompressPath();
            }
        }
        return true;
    }

    public void upload(String filePath,String uploadType) {
        if (filePath.equals("")) return;
        HashMap<String, Object> filesMap = new HashMap<>();
        File file = new File(filePath);
        filesMap.put("file", file);
        UserOAuth.getInstance().upload("api/auth/v1/file/uploadFile?type=" + uploadType, filesMap).subscribe(new BaseActivity.NetObserver<Progress>() {
            @Override
            public void onNext(Progress response) {
                super.onNext(response);
                if (response.getBodyStr() != null) {
                    uploadId=response.getFileId();
                }
            }

            @Override
            public boolean showLoading() {
                return false;
            }
        });
    }

    public void upload(String filePath,String uploadType,OnUploadCompleted onUploadCompleted) {
        if (filePath.equals("")) return;
        HashMap<String, Object> filesMap = new HashMap<>();
        File file = new File(filePath);
        filesMap.put("file", file);
        UserOAuth.getInstance().upload("api/auth/v1/file/uploadFile?type=" + uploadType, filesMap).subscribe(new BaseActivity.NetObserver<Progress>() {
            @Override
            public void onNext(Progress response) {
                super.onNext(response);
                if (response.getBodyStr() != null) {
                    uploadId=response.getFileId();
                    onUploadCompleted.onCompleted(UploadImg.this);
                }
            }

            @Override
            public boolean showLoading() {
                return false;
            }
        });
    }

    public static interface OnUploadCompleted{
        void onCompleted(UploadImg uploadImg);
    }
}
