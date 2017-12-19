package cn.czyugang.tcg.client.modules.inform;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.InformApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.entity.Inform;
import cn.czyugang.tcg.client.entity.InformColumn;
import cn.czyugang.tcg.client.entity.InformColumnResponse;
import cn.czyugang.tcg.client.entity.InformFollowResponse;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.utils.img.ImgView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author ruiaa
 * @date 2017/11/24
 */


public class InformColumnFragment extends BaseFragment {

    @BindView(R.id.inform_column_list)
    RecyclerView informColumnList;

    InformColumnAdapter informColumnAdapter;
    List<InformColumn> informColumns = new ArrayList<InformColumn>();

    public static InformColumnFragment newInstance() {
        InformColumnFragment fragment = new InformColumnFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_inform_column, container, false);
        ButterKnife.bind(this, rootView);

        informColumnAdapter = new InformColumnAdapter(informColumns, getActivity());

        refreshInform(true,1);

        return rootView;
    }

    public void refreshInform(boolean firstLoad,int page){
        InformApi.getInformColumn(page).subscribe(new BaseActivity.NetObserver<InformColumnResponse>() {
            @Override
            public void onNext(InformColumnResponse response) {
                super.onNext(response);
                response.parse();
                informColumns.clear();
                informColumns.addAll(response.data);
                informColumnAdapter.notifyDataSetChanged();
                if (firstLoad){
                    informColumnList.setLayoutManager(new LinearLayoutManager(getActivity()));
                    informColumnList.setAdapter(informColumnAdapter);
                }

            }
        });
    }

    @Override
    public String getLabel() {
        return "资讯栏目";
    }

    static class InformColumnAdapter extends RecyclerView.Adapter<InformColumnAdapter.Holder> {
        private List<InformColumn> list;
        private Activity activity;

        public InformColumnAdapter(List<InformColumn> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_inform_column, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            InformColumn data = list.get(position);
            holder.columnName.setText(data.name);
            holder.columnContent.setText(data.description);
            holder.columnFollowNum.setText(String.valueOf(data.followNum));
            if (data.isFollow) {
                holder.columnIsFollow.setText("已关注");
                holder.columnIsFollow.setBackgroundResource(R.drawable.bg_rect_cir_grey_ccc);
            } else {
                holder.columnIsFollow.setText("+关注");
                holder.columnIsFollow.setBackgroundResource(R.drawable.bg_rect_cir_red);
            }

            holder.itemView.setOnClickListener(v -> {
                InformColumnMsgActivity.startInformOrderMsgActivity();
            });
            holder.columnIsFollowFrame.setOnClickListener(v -> {
                if (!data.isFollow) {
                    InformApi.toFollowColumn(data.id).subscribe(
                            new Observer<Response>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(Response response) {
                                    switch (response.getCode()) {
                                        case 200:
                                            holder.columnIsFollow.setText("已关注");
                                            holder.columnIsFollow.setBackgroundResource(R.drawable.bg_rect_cir_grey_ccc);

                                    }
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {

                                }
                            }
                    );
                } else {
                    InformApi.toUnFollowColumn(data.id).subscribe(
                            new Observer<Response>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(Response response) {
                                    switch (response.getCode()) {
                                        case 200:
                                            holder.columnIsFollow.setText("+关注");
                                            holder.columnIsFollow.setBackgroundResource(R.drawable.bg_rect_cir_red);

                                    }
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {

                                }
                            }
                    );
                }
                // holder.columnIsFollow.setText(data.isFollow?"+关注":"已关注");
                //holder.columnIsFollow.setBackgroundResource(data.isFollow ? R.drawable.bg_rect_cir_red : R.drawable.bg_rect_cir_grey_ccc);
                data.isFollow = (data.isFollow ? false : true);

            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        class Holder extends RecyclerView.ViewHolder {
            ImgView columnImg;
            TextView columnName;
            TextView columnContent;
            TextView columnFollowNum;
            TextView columnIsFollow;
            FrameLayout columnIsFollowFrame;

            public Holder(View itemView) {
                super(itemView);
                columnImg = itemView.findViewById(R.id.inform_column_img);
                columnName = itemView.findViewById(R.id.inform_column_name);
                columnContent = itemView.findViewById(R.id.inform_column_content);
                columnFollowNum = itemView.findViewById(R.id.inform_column_follownum);
                columnIsFollow = itemView.findViewById(R.id.inform_column_isfollow);
                columnIsFollowFrame= itemView.findViewById(R.id.inform_column_isfollow_frame);

            }
        }
    }
}
