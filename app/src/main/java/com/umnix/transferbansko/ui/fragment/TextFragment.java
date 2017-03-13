package com.umnix.transferbansko.ui.fragment;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umnix.transferbansko.R;
import com.umnix.transferbansko.TransferApplication;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TextFragment extends BaseFragment {

    private static final String EXTRA_INFO_TYPE_ID = "extra.info.type.id";

    @BindView(R.id.info)
    TextView infoView;

    public static TextFragment newInstance(InfoType infoType) {
        TextFragment fragment = new TextFragment();

        Bundle args = new Bundle();
        args.putString(EXTRA_INFO_TYPE_ID, infoType.name());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_info, container, false);

        TransferApplication.getComponent().inject(this);
        unbinder = ButterKnife.bind(this, view);

        String infoTypeName = getArguments().getString(EXTRA_INFO_TYPE_ID);
        InfoType infoType = InfoType.valueOf(infoTypeName);

        if (infoType != null) {
            String content = infoType.getContent(getActivity());
            infoView.setText(Html.fromHtml(content, new ImageGetter(), null));
        }

        animateFadeIn(view);

        return view;
    }

    private class ImageGetter implements Html.ImageGetter {

        public Drawable getDrawable(String source) {
            int id;

            id = getResources().getIdentifier(source, "drawable", getActivity().getPackageName());

            if (id == 0) {
                // the drawable resource wasn't found in our package, maybe it is a stock android drawable?
                id = getResources().getIdentifier(source, "drawable", "android");
            }

            if (id == 0) {
                // prevent a crash if the resource still can't be found
                return null;
            }
            else {
                Drawable d = getResources().getDrawable(id);
                d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
                return d;
            }
        }

    }
}
