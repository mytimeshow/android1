package cn.czyugang.tcg.client.modules.groupon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.czyugang.tcg.client.R;

/**
 * Created by Administrator on 2017/12/28 0028.
 */

public class PageFragment extends Fragment {
    private TextView text;
    public static final String ARG_PAGE = "ARG_PAGE";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        text=view.findViewById(R.id.product_descript);
        text.setText("this is descript");
        return view;
    }

}
