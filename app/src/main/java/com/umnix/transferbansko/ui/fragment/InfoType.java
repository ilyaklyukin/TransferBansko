package com.umnix.transferbansko.ui.fragment;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.umnix.transferbansko.R;

public enum InfoType {
    INFO(R.string.info_text, "Information"),
    PRICE(R.string.prices_text, "Prices"),
    CONTACTS(R.string.contacts_text, "Contacts"),
    TERMS(R.string.terms_text, "Terms & Conditions");

    private int stringRes;
    private String title;

    InfoType(@StringRes int stringRes, String title) {
        this.stringRes = stringRes;
        this.title = title;
    }

    public String getContent(@NonNull Context context) {
        return context.getString(stringRes);
    }

    public String getTitle() {
        return title;
    }
}
