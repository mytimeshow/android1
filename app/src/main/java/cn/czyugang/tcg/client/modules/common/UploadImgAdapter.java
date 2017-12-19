package cn.czyugang.tcg.client.modules.common;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Progress;
import cn.czyugang.tcg.client.utils.img.ImgView;

/**
 * @author ruiaa
 * @date 2017/11/30
 */
public class UploadImgAdapter extends RecyclerView.Adapter<UploadImgAdapter.Holder> {

    private List<Item> list = new ArrayList<>();
    private Activity activity;
    private Item addImg = new Item();
    private String uploadType ="COMMENT";
    //ATTRIBUTE（属性图片）、LABEL（标签图片）、PRODUCT_TYPE（产品类型图片）、STORE_TYPE（店铺类型图片）、
    //PROTOCOL（协议文件）、COMMENT（评论图片）、FACE（头像图片）、CONTRACT（合同）、STORE_PHOTO（店铺图片）、
    //GOODS_PHOTO（商品图片）、CERTIFICATE_PHOTO（证件图片）、INFO_VIDEO(资讯视频)、INFO_PIC(资讯图片)

    public UploadImgAdapter(Activity activity) {
        this.activity = activity;
        list.add(addImg);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(activity).inflate(
                R.layout.item_upload_img, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Item data = list.get(position);

        if (data == addImg) {
            holder.delete.setVisibility(View.GONE);
            holder.imgView.drawableId(data.addImgId);
            holder.imgView.setOnClickListener(v -> {
                openSelector();
            });
        } else {
            holder.delete.setVisibility(View.VISIBLE);
            holder.delete.setOnClickListener(v -> {
                int p = list.indexOf(data);
                list.remove(data);
                notifyItemRemoved(p);
            });
            holder.imgView.imgFile(data.path);
            holder.imgView.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return list.size() > 5 ? 5 : list.size();
    }

    private void openSelector() {
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
                Item item=new Item(selectList.get(0).getCompressPath());
                item.upload();
                list.add(list.indexOf(addImg), item);
                notifyDataSetChanged();
            }
        }
        return true;
    }

    public String getUploadImgIds(){
        StringBuilder builder=new StringBuilder();
        for(Item item:list){
            builder.append(item.uploadId);
            builder.append(",");
        }
        if (builder.length()>0) builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }

    class Holder extends RecyclerView.ViewHolder {
        ImgView imgView;
        View delete;

        public Holder(View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.item_upload_img);
            delete = itemView.findViewById(R.id.item_upload_delete);
        }
    }

    class Item {
        public int addImgId = R.drawable.upload_img_select;
        public String path = "";
        public String uploadId = "";

        public Item() {
        }

        public Item(String path) {
            this.path = path;
        }

        private void upload() {
            HashMap<String, Object> filesMap = new HashMap<>();
            filesMap.put("file", new File(path));


            UserOAuth.getInstance().upload("api/auth/v1/file/uploadFile?"+uploadType, filesMap).subscribe(new BaseActivity.NetObserver<Progress>() {
                @Override
                public void onNext(Progress response) {
                    super.onNext(response);

                }
            });
        }
    }
}
