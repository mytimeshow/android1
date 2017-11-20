package cn.czyugang.tcg.client.modules.set.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.modules.set.contract.ModifyNicknameContract;
import cn.czyugang.tcg.client.modules.set.presenter.ModifyNicknamePresenter;

/**
 * Created by wuzihong on 2017/10/4.
 * 修改昵称
 */

public class ModifyNicknameActivity extends BaseActivity implements ModifyNicknameContract.View {
    public static final String KEY_NICKNAME = "nickname";
    @BindView(R.id.et_name)
    EditText et_name;

    private ModifyNicknameContract.Presenter mPresenter;

    private String mNickname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_nickname);
        ButterKnife.bind(this);
        mNickname = getIntent().getStringExtra(KEY_NICKNAME);
        mPresenter = new ModifyNicknamePresenter(this);
        initView();
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    private void initView() {
        et_name.setText(mNickname);
    }

    @OnClick({R.id.iv_back, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_confirm:
                mPresenter.modifyNickname(et_name.getText().toString());
                break;
        }
    }
}
