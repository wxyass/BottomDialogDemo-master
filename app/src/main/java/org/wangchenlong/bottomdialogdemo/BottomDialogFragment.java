package org.wangchenlong.bottomdialogdemo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 底部的DialogFragment
 * <p/>
 * Created by wangchenlong on 16/6/20.
 */
public class BottomDialogFragment extends DialogFragment {

    // 礼物类别
    @BindView(R.id.regards_ll_first_container) LinearLayout mLlFirstContainer;
    @BindView(R.id.regards_tv_100_coins) TextView mTv100Coins;
    @BindView(R.id.regards_iv_100_coins) ImageView mIv100Coins;

    @BindView(R.id.regards_ll_second_container) LinearLayout mLlSecondContainer;
    @BindView(R.id.regards_tv_2_yuan) TextView mTv2Yuan;
    @BindView(R.id.regards_iv_2_yuan) ImageView mIv2Yuan;

    @BindView(R.id.regards_ll_third_container) LinearLayout mLlThirdContainer;
    @BindView(R.id.regards_tv_8_yuan) TextView mTv8Yuan;
    @BindView(R.id.regards_iv_8_yuan) ImageView mIv8Yuan;

    @BindView(R.id.regards_ll_forth_container) LinearLayout mLlForthContainer;
    @BindView(R.id.regards_tv_12_yuan) TextView mTv12Yuan;
    @BindView(R.id.regards_iv_12_yuan) ImageView mIv12Yuan;

    @BindView(R.id.regards_tv_send) TextView mTvSend; // 发送按钮
    @BindView(R.id.regards_tv_coin_count) TextView mTvCoinCount;

    private ArrayList<LinearLayout> mLayouts; // 礼物类别框架
    private ArrayList<TextView> mTvTypes; // 文字类型
    private ArrayList<ImageView> mIvTypes; // 图像类型
    private int mType = 0; // 当前类型 0:送100金币  1:送200金币  2:送800  3送1200金币
    private int mCoinCount = 2310; // 当前金币数

    @NonNull @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        // 在设置Content前, 设置Dialog的窗口属性(Window),不含标题,即Window.FEATURE_NO_TITLE
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置Dialog布局
        dialog.setContentView(R.layout.fragment_bottom);
        // 外部点击取消
        dialog.setCanceledOnTouchOutside(true);

        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 重置Dialog的窗口布局,中心为紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度为屏幕宽度
        window.setAttributes(lp);

        // 注册ButterKnife,若没集成可注掉
        ButterKnife.bind(this, dialog); // Dialog即View

        initClickTypes();

        return dialog;
    }

    /**
     * 初始化点击类型
     */
    private void initClickTypes() {
        initViewArray(); // 初始化控件组
        initLayout(); // 初始化布局

        // 送出按钮设置监听
        mTvSend.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // 默认弹出toast为金币不足
                String typeStr = "金币不足";
                // 根据选择的类型,若金币足够则提示相对应内容
                switch (mType) {
                    case 0:
                        if (countCoins(100))
                            typeStr = "一字之师";
                        break;
                    case 1:
                        if (countCoins(200))
                            typeStr = "妙语连珠";
                        break;
                    case 2:
                        if (countCoins(800))
                            typeStr = "学识丰富";
                        break;
                    case 3:
                        if (countCoins(1200))
                            typeStr = "博学多才";
                        break;
                    default:
                        break;
                }
                Toast.makeText(getContext(), typeStr, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化布局
     */
    private void initLayout() {
        // 选择默认类型mType=0:送出100金币
        chooseRegardsType(mType);

        for (int i = 0; i < mLayouts.size(); i++) {
            final int tmp = i;
            LinearLayout ll = mLayouts.get(i);
            // 对每个LinearLayout做监听
            ll.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    mType = tmp;
                    chooseRegardsType(mType);
                }
            });
        }
    }

    /**
     * 消费金币
     *
     * @param count 金币数
     * @return 是否消费
     */
    private boolean countCoins(int count) {
        // (原有金币-消费金币)>0
        int end = mCoinCount - count;
        if (end > 0) {
            mCoinCount = end;
            mTvCoinCount.setText(String.valueOf("您共有金币" + end));
            return true;
        } else {
            return false;
        }
    }

    /**
     * 初始化类型数组, 文字和图片
     */
    private void initViewArray() {
        mLayouts = new ArrayList<>();
        mTvTypes = new ArrayList<>();
        mIvTypes = new ArrayList<>();

        // 礼物类别框架
        mLayouts.add(mLlFirstContainer);
        mLayouts.add(mLlSecondContainer);
        mLayouts.add(mLlThirdContainer);
        mLayouts.add(mLlForthContainer);

        // 文字类型
        mTvTypes.add(mTv100Coins);
        mTvTypes.add(mTv2Yuan);
        mTvTypes.add(mTv8Yuan);
        mTvTypes.add(mTv12Yuan);

        // 图像类型
        mIvTypes.add(mIv100Coins);
        mIvTypes.add(mIv2Yuan);
        mIvTypes.add(mIv8Yuan);
        mIvTypes.add(mIv12Yuan);
    }

    /**
     * 选择类型
     *
     * @param type 类型
     */
    private void chooseRegardsType(int type) {
        int size = mTvTypes.size();
        for (int i = 0; i < size; ++i) {
            if (i != type) {
                // 显示选中标记
                mTvTypes.get(i).setEnabled(true);
                mIvTypes.get(i).setEnabled(true);
            } else {
                // 去掉选中标记
                mTvTypes.get(i).setEnabled(false);
                mIvTypes.get(i).setEnabled(false);
            }
        }
    }
}